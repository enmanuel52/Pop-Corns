package com.enmanuelbergling.feature.movies.details

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import assertk.assertions.isTrue
import assertk.assertions.isFalse
import com.enmanuelbergling.core.domain.datasource.remote.MovieRemoteDS
import com.enmanuelbergling.core.domain.datasource.remote.UserRemoteDS
import com.enmanuelbergling.core.model.core.NetworkException
import com.enmanuelbergling.core.model.core.SimplerUi
import com.enmanuelbergling.core.testing.datasource.remote.FakeMovieRemoteDS
import com.enmanuelbergling.core.testing.datasource.remote.MovieRemoteDsFunction
import com.enmanuelbergling.core.testing.datasource.remote.UserRemoteDsFunction
import com.enmanuelbergling.core.testing.extension.KoinExtension
import com.enmanuelbergling.core.testing.extension.MainCoroutineExtension
import com.enmanuelbergling.core.ui.components.messageResource
import com.enmanuelbergling.feature.movies.di.moviesModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.core.parameter.parametersOf
import org.koin.test.KoinTest
import org.koin.test.get

@OptIn(ExperimentalCoroutinesApi::class)
class MovieDetailsVMTest : KoinTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    @JvmField
    @RegisterExtension
    val koinExtension = KoinExtension(moviesModule)

    @JvmField
    @RegisterExtension
    val mainCoroutineExtension = MainCoroutineExtension(testDispatcher)

    private lateinit var viewModel: MovieDetailsVM
    private val movieId = 123

    @BeforeEach
    fun setUp() {
        viewModel = koinExtension.get<MovieDetailsVM> { parametersOf(movieId) }
    }

    @Test
    fun `loadPage successfully updates state with details, credits and account states`() = runTest {
        // When
        backgroundScope.launch {
            viewModel.uiState.collect()
        }
        advanceUntilIdle()

        // Then
        assertThat(viewModel.uiState.value.uiState).isEqualTo(SimplerUi.Idle)
        assertThat(viewModel.uiState.value.details).isNotNull()
        assertThat(viewModel.uiState.value.credits).isNotNull()
        assertThat(viewModel.uiState.value.accountStates).isNotNull()
    }

    @Test
    fun `loadPage partially saves state when the third chain handler fails`() = runTest {
        // Given
        val exception = NetworkException.ReadTimeOutException
        val fakeMovieRemoteDS = FakeMovieRemoteDS().apply {
            throwError(MovieRemoteDsFunction.GetMovieAccountStates to exception)
        }

        koinExtension.replaceDependencies {
            single<MovieRemoteDS> { fakeMovieRemoteDS }
        }
        viewModel = koinExtension.get<MovieDetailsVM> { parametersOf(movieId) }

        // When
        backgroundScope.launch {
            viewModel.uiState.collect()
        }
        advanceUntilIdle()

        // Then
        assertThat(viewModel.uiState.value.uiState).isEqualTo(SimplerUi.Error(exception.messageResource))
        assertThat(viewModel.uiState.value.details).isNotNull()
        assertThat(viewModel.uiState.value.credits).isNotNull()
        assertThat(viewModel.uiState.value.accountStates).isNull()
    }

    @Test
    fun `when onAction OnWatchlistClick is called, isWatchlistLoading is updated and watchlist status toggles`() =
        runTest {
            // Given
            backgroundScope.launch {
                viewModel.uiState.collect()
            }
            advanceUntilIdle() // Load page
            assertThat(viewModel.uiState.value.accountStates?.watchlist).isEqualTo(false)

            viewModel.uiState.test {
                val initial = awaitItem() // Idle state

                // When
                viewModel.onAction(MovieDetailsAction.OnWatchlistClick)

                // Then
                assertThat(awaitItem().isWatchlistLoading).isTrue()
                
                val finalItem = awaitItem()
                assertThat(finalItem.isWatchlistLoading).isFalse()
                assertThat(finalItem.accountStates?.watchlist).isEqualTo(true)
            }
        }

    @Test
    fun `when onAction OnWatchlistClick fails, uiState is error`() = runTest {
        // Given
        val exception = NetworkException.AuthorizationException
        koinExtension.replaceDependencies {
            single<UserRemoteDS> {
                com.enmanuelbergling.core.testing.datasource.remote.FakeUserRemoteDS().apply {
                    throwError(UserRemoteDsFunction.AddMovieToAccountWatchlist to exception)
                }
            }
        }
        viewModel = koinExtension.get { parametersOf(movieId) }

        backgroundScope.launch {
            viewModel.uiState.collect()
        }
        advanceUntilIdle() // Load page

        // When
        viewModel.onAction(MovieDetailsAction.OnWatchlistClick)
        advanceUntilIdle()

        // Then
        assertThat(viewModel.uiState.value.isWatchlistLoading).isFalse()
        assertThat(viewModel.uiState.value.uiState).isEqualTo(SimplerUi.Error(exception.messageResource))
    }

    @Test
    fun `when onAction OnFavoriteClick is called, isFavoriteLoading is updated and favorite status toggles`() =
        runTest {
            // Given
            backgroundScope.launch {
                viewModel.uiState.collect()
            }
            advanceUntilIdle() // Load page
            assertThat(viewModel.uiState.value.accountStates?.favorite).isEqualTo(false)

            viewModel.uiState.test {
                val initial = awaitItem() // Idle state

                // When
                viewModel.onAction(MovieDetailsAction.OnFavoriteClick)

                // Then
                assertThat(awaitItem().isFavoriteLoading).isTrue()
                
                val finalItem = awaitItem()
                assertThat(finalItem.isFavoriteLoading).isFalse()
                assertThat(finalItem.accountStates?.favorite).isEqualTo(true)
            }
        }

    @Test
    fun `when onAction OnFavoriteClick fails, uiState is error`() = runTest {
        // Given
        val exception = NetworkException.AuthorizationException
        koinExtension.replaceDependencies {
            single<UserRemoteDS> {
                com.enmanuelbergling.core.testing.datasource.remote.FakeUserRemoteDS().apply {
                    throwError(UserRemoteDsFunction.AddMovieToFavorites to exception)
                }
            }
        }
        viewModel = koinExtension.get { parametersOf(movieId) }

        backgroundScope.launch {
            viewModel.uiState.collect()
        }
        advanceUntilIdle() // Load page

        // When
        viewModel.onAction(MovieDetailsAction.OnFavoriteClick)
        advanceUntilIdle()

        // Then
        assertThat(viewModel.uiState.value.isFavoriteLoading).isFalse()
        assertThat(viewModel.uiState.value.uiState).isEqualTo(SimplerUi.Error(exception.messageResource))
    }
}
