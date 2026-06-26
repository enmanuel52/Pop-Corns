package com.enmanuelbergling.feature.movies.details

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import com.enmanuelbergling.core.domain.datasource.remote.MovieRemoteDS
import com.enmanuelbergling.core.model.core.NetworkException
import com.enmanuelbergling.core.model.core.SimplerUi
import com.enmanuelbergling.core.testing.datasource.remote.FakeMovieRemoteDS
import com.enmanuelbergling.core.testing.datasource.remote.MovieRemoteDsFunction
import com.enmanuelbergling.core.testing.extension.KoinExtension
import com.enmanuelbergling.core.testing.extension.MainCoroutineExtension
import com.enmanuelbergling.core.ui.components.messageResource
import com.enmanuelbergling.feature.movies.di.moviesModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.core.parameter.parametersOf
import org.koin.test.KoinTest
import org.koin.test.get

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MainCoroutineExtension::class)
class MovieDetailsVMTest : KoinTest {

    @JvmField
    @RegisterExtension
    val koinExtension = KoinExtension(moviesModule)

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
}
