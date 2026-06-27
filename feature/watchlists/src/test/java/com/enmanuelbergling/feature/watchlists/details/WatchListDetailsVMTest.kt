package com.enmanuelbergling.feature.watchlists.details

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.doesNotContain
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import com.enmanuelbergling.core.domain.datasource.remote.UserRemoteDS
import com.enmanuelbergling.core.model.core.NetworkException
import com.enmanuelbergling.core.model.core.SimplerUi
import com.enmanuelbergling.core.testing.datasource.remote.FakeUserRemoteDS
import com.enmanuelbergling.core.testing.datasource.remote.UserRemoteDsFunction
import com.enmanuelbergling.core.testing.extension.KoinExtension
import com.enmanuelbergling.core.testing.extension.MainCoroutineExtension
import com.enmanuelbergling.feature.watchlists.di.watchlistModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.core.parameter.parametersOf
import org.koin.test.KoinTest
import org.koin.test.get

@OptIn(ExperimentalCoroutinesApi::class)
class WatchListDetailsVMTest : KoinTest {

    private val testDispatcher = StandardTestDispatcher()

    @JvmField
    @RegisterExtension
    val koinExtension = KoinExtension(watchlistModule)

    @JvmField
    @RegisterExtension
    val mainCoroutineExtension = MainCoroutineExtension(testDispatcher)

    private lateinit var viewModel: WatchListDetailsVM
    private val listId = 9
    private val movieId = 333

    @BeforeEach
    fun setUp() {
        viewModel = koinExtension.get { parametersOf(listId) }
    }

    // region delete

    @Test
    fun `when OnDeleteMovie is called, movie is marked deleted and undo side effect is emitted`() =
        runTest {
            viewModel.sideEffectChannel.test {
                viewModel.onEvent(WatchlistDetailsEvent.OnDeleteMovie(movieId))
                advanceUntilIdle()

                assertThat(viewModel.uiState.value.deletedMovieIds).contains(movieId)
                assertThat(awaitItem())
                    .isEqualTo(WatchlistDetailsSideEffect.UndoDeleteMovie(movieId))
            }
        }

    @Test
    fun `when DeleteMovie succeeds, no error side effect is emitted`() = runTest {
        viewModel.sideEffectChannel.test {
            viewModel.onEvent(WatchlistDetailsEvent.DeleteMovie(movieId))
            advanceUntilIdle()

            expectNoEvents()
        }
    }

    @Test
    fun `when DeleteMovie fails, DeleteMovieError side effect is emitted`() = runTest {
        koinExtension.replaceDependencies {
            single<UserRemoteDS> {
                FakeUserRemoteDS().apply {
                    throwError(UserRemoteDsFunction.DeleteMovieFromList to NetworkException.AuthorizationException)
                }
            }
        }
        viewModel = koinExtension.get { parametersOf(listId) }

        viewModel.sideEffectChannel.test {
            viewModel.onEvent(WatchlistDetailsEvent.DeleteMovie(movieId))
            advanceUntilIdle()

            assertThat(awaitItem())
                .isEqualTo(WatchlistDetailsSideEffect.DeleteMovieError(movieId))
        }
    }

    @Test
    fun `when UndoDelete is called, last deleted movie is restored`() = runTest {
        viewModel.onEvent(WatchlistDetailsEvent.OnDeleteMovie(movieId))
        advanceUntilIdle()

        viewModel.onEvent(WatchlistDetailsEvent.UndoDelete)
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.deletedMovieIds).doesNotContain(movieId)
    }

    @Test
    fun `when UndoDelete is called with empty list, nothing happens`() = runTest {
        viewModel.onEvent(WatchlistDetailsEvent.UndoDelete)
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.deletedMovieIds).isEmpty()
    }

    @Test
    fun `when OnDeleteMovieErrorDismissed is called, movie is removed from deleted list`() = runTest {
        viewModel.onEvent(WatchlistDetailsEvent.OnDeleteMovie(movieId))
        advanceUntilIdle()

        viewModel.onEvent(WatchlistDetailsEvent.OnDeleteMovieErrorDismissed(movieId))
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.deletedMovieIds).doesNotContain(movieId)
    }

    // endregion

    // region favorites

    @Test
    fun `when OnAddToFavorites is called, movie is marked favorited and undo side effect is emitted`() =
        runTest {
            viewModel.sideEffectChannel.test {
                viewModel.onEvent(WatchlistDetailsEvent.OnAddToFavorites(movieId))
                advanceUntilIdle()

                assertThat(viewModel.uiState.value.favoritedMovieIds).contains(movieId)
                assertThat(awaitItem())
                    .isEqualTo(WatchlistDetailsSideEffect.UndoAddToFavoritesMovie(movieId))
            }
        }

    @Test
    fun `when AddToFavorites succeeds, no error side effect is emitted`() = runTest {
        viewModel.sideEffectChannel.test {
            viewModel.onEvent(WatchlistDetailsEvent.AddToFavorites(movieId))
            advanceUntilIdle()

            expectNoEvents()
        }
    }

    @Test
    fun `when AddToFavorites fails on favorites, AddToFavoritesError side effect is emitted`() =
        runTest {
            koinExtension.replaceDependencies {
                single<UserRemoteDS> {
                    FakeUserRemoteDS().apply {
                        throwError(UserRemoteDsFunction.AddMovieToFavorites to NetworkException.AuthorizationException)
                    }
                }
            }
            viewModel = koinExtension.get { parametersOf(listId) }

            viewModel.sideEffectChannel.test {
                viewModel.onEvent(WatchlistDetailsEvent.AddToFavorites(movieId))
                advanceUntilIdle()

                assertThat(awaitItem())
                    .isEqualTo(WatchlistDetailsSideEffect.AddToFavoritesError(movieId))
            }
        }

    @Test
    fun `when AddToFavorites fails on list removal, AddToFavoritesError side effect is emitted`() =
        runTest {
            koinExtension.replaceDependencies {
                single<UserRemoteDS> {
                    FakeUserRemoteDS().apply {
                        throwError(UserRemoteDsFunction.DeleteMovieFromList to NetworkException.AuthorizationException)
                    }
                }
            }
            viewModel = koinExtension.get { parametersOf(listId) }

            viewModel.sideEffectChannel.test {
                viewModel.onEvent(WatchlistDetailsEvent.AddToFavorites(movieId))
                advanceUntilIdle()

                assertThat(awaitItem())
                    .isEqualTo(WatchlistDetailsSideEffect.AddToFavoritesError(movieId))
            }
        }

    @Test
    fun `when UndoAddToFavorites is called, last favorited movie is restored`() = runTest {
        viewModel.onEvent(WatchlistDetailsEvent.OnAddToFavorites(movieId))
        advanceUntilIdle()

        viewModel.onEvent(WatchlistDetailsEvent.UndoAddToFavorites)
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.favoritedMovieIds).doesNotContain(movieId)
    }

    @Test
    fun `when UndoAddToFavorites is called with empty list, nothing happens`() = runTest {
        viewModel.onEvent(WatchlistDetailsEvent.UndoAddToFavorites)
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.favoritedMovieIds).isEmpty()
    }

    @Test
    fun `when OnAddToFavoritesErrorDismissed is called, movie is removed from favorited list`() =
        runTest {
            viewModel.onEvent(WatchlistDetailsEvent.OnAddToFavorites(movieId))
            advanceUntilIdle()

            viewModel.onEvent(WatchlistDetailsEvent.OnAddToFavoritesErrorDismissed(movieId))
            advanceUntilIdle()

            assertThat(viewModel.uiState.value.favoritedMovieIds).doesNotContain(movieId)
        }

    // endregion

    // region navigation & shortcuts

    @Test
    fun `when NavigateToDetails is called, navigate side effect is emitted`() = runTest {
        viewModel.sideEffectChannel.test {
            viewModel.onEvent(WatchlistDetailsEvent.NavigateToDetails(movieId))
            advanceUntilIdle()

            assertThat(awaitItem())
                .isEqualTo(WatchlistDetailsSideEffect.NavigateToDetails(movieId))
        }
    }

    @Test
    fun `when NavigateBack is called, navigate back side effect is emitted`() = runTest {
        viewModel.sideEffectChannel.test {
            viewModel.onEvent(WatchlistDetailsEvent.NavigateBack)
            advanceUntilIdle()

            assertThat(awaitItem()).isEqualTo(WatchlistDetailsSideEffect.NavigateBack)
        }
    }

    @Test
    fun `when OnAddShortcut is called, add shortcut side effect is emitted`() = runTest {
        viewModel.sideEffectChannel.test {
            viewModel.onEvent(WatchlistDetailsEvent.OnAddShortcut)
            advanceUntilIdle()

            assertThat(awaitItem()).isEqualTo(WatchlistDetailsSideEffect.OnAddShortcut)
        }
    }

    @Test
    fun `when OnDeleteShortCut is called, delete shortcut side effect is emitted`() = runTest {
        viewModel.sideEffectChannel.test {
            viewModel.onEvent(WatchlistDetailsEvent.OnDeleteShortCut)
            advanceUntilIdle()

            assertThat(awaitItem()).isEqualTo(WatchlistDetailsSideEffect.OnDeleteShortCut)
        }
    }

    @Test
    fun `when DismissDialog is called, uiState is Idle`() = runTest {
        viewModel.onEvent(WatchlistDetailsEvent.DismissDialog)
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.uiState).isEqualTo(SimplerUi.Idle)
    }

    // endregion
}
