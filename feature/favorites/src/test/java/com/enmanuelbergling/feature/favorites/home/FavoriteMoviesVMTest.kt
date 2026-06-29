package com.enmanuelbergling.feature.favorites.home

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.doesNotContain
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import com.enmanuelbergling.core.domain.datasource.remote.UserRemoteDS
import com.enmanuelbergling.core.model.core.NetworkException
import com.enmanuelbergling.core.testing.datasource.remote.FakeUserRemoteDS
import com.enmanuelbergling.core.testing.datasource.remote.UserRemoteDsFunction
import com.enmanuelbergling.core.testing.extension.KoinExtension
import com.enmanuelbergling.core.testing.extension.MainCoroutineExtension
import com.enmanuelbergling.feature.favorites.di.favoritesModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.test.KoinTest
import org.koin.test.get

@OptIn(ExperimentalCoroutinesApi::class)
class FavoriteMoviesVMTest : KoinTest {

    private val testDispatcher = StandardTestDispatcher()

    @JvmField
    @RegisterExtension
    val koinExtension = KoinExtension(favoritesModule)

    @JvmField
    @RegisterExtension
    val mainCoroutineExtension = MainCoroutineExtension(testDispatcher)

    private lateinit var viewModel: FavoriteMoviesVM
    private val movieId = 111

    @BeforeEach
    fun setUp() {
        viewModel = koinExtension.get()
    }

    @Test
    fun `when OnRemoveMovie is called, movie is marked deleted and undo side effect is emitted`() =
        runTest {
            viewModel.sideEffectChannel.test {
                // When
                viewModel.onEvent(FavoriteMoviesEvent.OnRemoveMovie(movieId))
                advanceUntilIdle()

                // Then
                assertThat(viewModel.uiState.value.deletedMovieIds).contains(movieId)
                assertThat(awaitItem())
                    .isEqualTo(FavoriteMoviesSideEffect.UndoRemoveMovie(movieId))
            }
        }

    @Test
    fun `when RemoveMovie succeeds, no error side effect is emitted`() = runTest {
        viewModel.sideEffectChannel.test {
            // When
            viewModel.onEvent(FavoriteMoviesEvent.RemoveMovie(movieId))
            advanceUntilIdle()

            // Then
            expectNoEvents()
        }
    }

    @Test
    fun `when RemoveMovie fails, RemoveMovieError side effect is emitted`() = runTest {
        // Given
        koinExtension.replaceDependencies {
            single<UserRemoteDS> {
                FakeUserRemoteDS().apply {
                    throwError(UserRemoteDsFunction.RemoveMovieFromFavorites to NetworkException.AuthorizationException())
                }
            }
        }
        viewModel = koinExtension.get()

        viewModel.sideEffectChannel.test {
            // When
            viewModel.onEvent(FavoriteMoviesEvent.RemoveMovie(movieId))
            advanceUntilIdle()

            // Then
            assertThat(awaitItem())
                .isEqualTo(FavoriteMoviesSideEffect.RemoveMovieError(movieId))
        }
    }

    @Test
    fun `when UndoRemove is called, last deleted movie is restored`() = runTest {
        // Given a deleted movie
        viewModel.onEvent(FavoriteMoviesEvent.OnRemoveMovie(movieId))
        advanceUntilIdle()
        assertThat(viewModel.uiState.value.deletedMovieIds).contains(movieId)

        // When
        viewModel.onEvent(FavoriteMoviesEvent.UndoRemove)
        advanceUntilIdle()

        // Then
        assertThat(viewModel.uiState.value.deletedMovieIds).doesNotContain(movieId)
    }

    @Test
    fun `when UndoRemove is called with empty deleted list, nothing happens`() = runTest {
        // When
        viewModel.onEvent(FavoriteMoviesEvent.UndoRemove)
        advanceUntilIdle()

        // Then
        assertThat(viewModel.uiState.value.deletedMovieIds).isEmpty()
    }

    @Test
    fun `when OnRemoveMovieErrorDismissed is called, movie is removed from deleted list`() = runTest {
        // Given a deleted movie
        viewModel.onEvent(FavoriteMoviesEvent.OnRemoveMovie(movieId))
        advanceUntilIdle()

        // When
        viewModel.onEvent(FavoriteMoviesEvent.OnRemoveMovieErrorDismissed(movieId))
        advanceUntilIdle()

        // Then
        assertThat(viewModel.uiState.value.deletedMovieIds).doesNotContain(movieId)
    }

    @Test
    fun `when NavigateToDetails is called, navigate side effect is emitted`() = runTest {
        viewModel.sideEffectChannel.test {
            // When
            viewModel.onEvent(FavoriteMoviesEvent.NavigateToDetails(movieId))
            advanceUntilIdle()

            // Then
            assertThat(awaitItem())
                .isEqualTo(FavoriteMoviesSideEffect.NavigateToDetails(movieId))
        }
    }
}
