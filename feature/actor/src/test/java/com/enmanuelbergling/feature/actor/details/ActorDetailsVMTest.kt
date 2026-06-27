package com.enmanuelbergling.feature.actor.details

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isNotEmpty
import com.enmanuelbergling.core.domain.datasource.remote.ActorRemoteDS
import com.enmanuelbergling.core.model.core.NetworkException
import com.enmanuelbergling.core.model.core.SimplerUi
import com.enmanuelbergling.core.testing.datasource.remote.ActorRemoteDsFunction
import com.enmanuelbergling.core.testing.datasource.remote.FakeActorRemoteDS
import com.enmanuelbergling.core.testing.extension.KoinExtension
import com.enmanuelbergling.core.testing.extension.MainCoroutineExtension
import com.enmanuelbergling.core.ui.components.messageResource
import com.enmanuelbergling.feature.actor.di.actorsModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.core.parameter.parametersOf
import org.koin.test.KoinTest
import org.koin.test.get

@OptIn(ExperimentalCoroutinesApi::class)
class ActorDetailsVMTest : KoinTest {

    private val testDispatcher = StandardTestDispatcher()

    @JvmField
    @RegisterExtension
    val koinExtension = KoinExtension(actorsModule)

    @JvmField
    @RegisterExtension
    val mainCoroutineExtension = MainCoroutineExtension(testDispatcher)

    private val actorId = 42

    @Test
    fun `loadPage successfully updates state with details and known movies`() = runTest {
        // Given
        val viewModel = koinExtension.get<ActorDetailsVM> { parametersOf(actorId) }

        // When
        advanceUntilIdle() // init { loadPage() }

        // Then
        assertThat(viewModel.uiState.value.uiState).isEqualTo(SimplerUi.Idle)
        assertThat(viewModel.uiState.value.details).isNotNull()
        assertThat(viewModel.uiState.value.knownMovies).isNotEmpty()
    }

    @Test
    fun `loadPage sets error state when details fail`() = runTest {
        // Given
        koinExtension.replaceDependencies {
            single<ActorRemoteDS> {
                FakeActorRemoteDS().apply {
                    throwError(ActorRemoteDsFunction.GetActorDetails to NetworkException.ReadTimeOutException)
                }
            }
        }
        val viewModel = koinExtension.get<ActorDetailsVM> { parametersOf(actorId) }

        // When
        advanceUntilIdle()

        // Then
        assertThat(viewModel.uiState.value.uiState)
            .isEqualTo(SimplerUi.Error(NetworkException.DefaultException.messageResource))
    }

    @Test
    fun `loadPage sets error state when known movies fail`() = runTest {
        // Given
        koinExtension.replaceDependencies {
            single<ActorRemoteDS> {
                FakeActorRemoteDS().apply {
                    throwError(ActorRemoteDsFunction.GetMoviesByActor to NetworkException.ReadTimeOutException)
                }
            }
        }
        val viewModel = koinExtension.get<ActorDetailsVM> { parametersOf(actorId) }

        // When
        advanceUntilIdle()

        // Then
        assertThat(viewModel.uiState.value.uiState)
            .isEqualTo(SimplerUi.Error(NetworkException.DefaultException.messageResource))
    }

    @Test
    fun `when OnRetry is called, page is reloaded`() = runTest {
        // Given
        val viewModel = koinExtension.get<ActorDetailsVM> { parametersOf(actorId) }
        advanceUntilIdle()

        // When
        viewModel.onAction(ActorDetailsAction.OnRetry)
        advanceUntilIdle()

        // Then
        assertThat(viewModel.uiState.value.uiState).isEqualTo(SimplerUi.Idle)
        assertThat(viewModel.uiState.value.details).isNotNull()
        assertThat(viewModel.uiState.value.knownMovies).isNotEmpty()
    }

    @Test
    fun `when OnBack is called, NavigateBack event is emitted`() = runTest {
        // Given
        val viewModel = koinExtension.get<ActorDetailsVM> { parametersOf(actorId) }
        advanceUntilIdle()

        viewModel.uiEvents.test {
            // When
            viewModel.onAction(ActorDetailsAction.OnBack)
            advanceUntilIdle()

            // Then
            assertThat(awaitItem()).isEqualTo(ActorDetailsEvent.NavigateBack)
        }
    }

    @Test
    fun `when OnMovieClick is called, NavigateToMovie event is emitted`() = runTest {
        // Given
        val movieId = 7
        val viewModel = koinExtension.get<ActorDetailsVM> { parametersOf(actorId) }
        advanceUntilIdle()

        viewModel.uiEvents.test {
            // When
            viewModel.onAction(ActorDetailsAction.OnMovieClick(movieId))
            advanceUntilIdle()

            // Then
            assertThat(awaitItem()).isEqualTo(ActorDetailsEvent.NavigateToMovie(movieId))
        }
    }
}
