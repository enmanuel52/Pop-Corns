package com.enmanuelbergling.feature.watchlists.home

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
import org.koin.test.KoinTest
import org.koin.test.get

@OptIn(ExperimentalCoroutinesApi::class)
class WatchlistHomeVMTest : KoinTest {

    private val testDispatcher = StandardTestDispatcher()

    @JvmField
    @RegisterExtension
    val koinExtension = KoinExtension(watchlistModule)

    @JvmField
    @RegisterExtension
    val mainCoroutineExtension = MainCoroutineExtension(testDispatcher)

    private lateinit var viewModel: WatchlistHomeVM
    private val movieId = 222

    @BeforeEach
    fun setUp() {
        viewModel = koinExtension.get()
    }

    // region delete

    @Test
    fun `when OnDeleteMovie is called, movie is marked deleted and undo side effect is emitted`() =
        runTest {
            viewModel.sideEffectChannel.test {
                viewModel.onEvent(WatchlistHomeEvent.OnDeleteMovie(movieId))
                advanceUntilIdle()

                assertThat(viewModel.uiState.value.deletedMovieIds).contains(movieId)
                assertThat(awaitItem())
                    .isEqualTo(WatchlistHomeSideEffect.UndoDeleteMovie(movieId))
            }
        }

    @Test
    fun `when DeleteMovie succeeds, no error side effect is emitted`() = runTest {
        viewModel.sideEffectChannel.test {
            viewModel.onEvent(WatchlistHomeEvent.DeleteMovie(movieId))
            advanceUntilIdle()

            expectNoEvents()
        }
    }

    @Test
    fun `when DeleteMovie fails, DeleteMovieError side effect is emitted`() = runTest {
        koinExtension.replaceDependencies {
            single<UserRemoteDS> {
                FakeUserRemoteDS().apply {
                    throwError(UserRemoteDsFunction.RemoveMovieFromAccountWatchlist to NetworkException.AuthorizationException)
                }
            }
        }
        viewModel = koinExtension.get()

        viewModel.sideEffectChannel.test {
            viewModel.onEvent(WatchlistHomeEvent.DeleteMovie(movieId))
            advanceUntilIdle()

            assertThat(awaitItem())
                .isEqualTo(WatchlistHomeSideEffect.DeleteMovieError(movieId))
        }
    }

    @Test
    fun `when UndoDelete is called, last deleted movie is restored`() = runTest {
        viewModel.onEvent(WatchlistHomeEvent.OnDeleteMovie(movieId))
        advanceUntilIdle()

        viewModel.onEvent(WatchlistHomeEvent.UndoDelete)
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.deletedMovieIds).doesNotContain(movieId)
    }

    @Test
    fun `when UndoDelete is called with empty list, nothing happens`() = runTest {
        viewModel.onEvent(WatchlistHomeEvent.UndoDelete)
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.deletedMovieIds).isEmpty()
    }

    @Test
    fun `when OnDeleteMovieErrorDismissed is called, movie is removed from deleted list`() = runTest {
        viewModel.onEvent(WatchlistHomeEvent.OnDeleteMovie(movieId))
        advanceUntilIdle()

        viewModel.onEvent(WatchlistHomeEvent.OnDeleteMovieErrorDismissed(movieId))
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.deletedMovieIds).doesNotContain(movieId)
    }

    // endregion

    // region favorites

    @Test
    fun `when OnAddToFavorites is called, movie is marked favorited and undo side effect is emitted`() =
        runTest {
            viewModel.sideEffectChannel.test {
                viewModel.onEvent(WatchlistHomeEvent.OnAddToFavorites(movieId))
                advanceUntilIdle()

                assertThat(viewModel.uiState.value.favoritedMovieIds).contains(movieId)
                assertThat(awaitItem())
                    .isEqualTo(WatchlistHomeSideEffect.UndoAddToFavoritesMovie(movieId))
            }
        }

    @Test
    fun `when AddToFavorites succeeds, no error side effect is emitted`() = runTest {
        viewModel.sideEffectChannel.test {
            viewModel.onEvent(WatchlistHomeEvent.AddToFavorites(movieId))
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
            viewModel = koinExtension.get()

            viewModel.sideEffectChannel.test {
                viewModel.onEvent(WatchlistHomeEvent.AddToFavorites(movieId))
                advanceUntilIdle()

                assertThat(awaitItem())
                    .isEqualTo(WatchlistHomeSideEffect.AddToFavoritesError(movieId))
            }
        }

    @Test
    fun `when AddToFavorites fails on watchlist removal, AddToFavoritesError side effect is emitted`() =
        runTest {
            koinExtension.replaceDependencies {
                single<UserRemoteDS> {
                    FakeUserRemoteDS().apply {
                        throwError(UserRemoteDsFunction.RemoveMovieFromAccountWatchlist to NetworkException.AuthorizationException)
                    }
                }
            }
            viewModel = koinExtension.get()

            viewModel.sideEffectChannel.test {
                viewModel.onEvent(WatchlistHomeEvent.AddToFavorites(movieId))
                advanceUntilIdle()

                assertThat(awaitItem())
                    .isEqualTo(WatchlistHomeSideEffect.AddToFavoritesError(movieId))
            }
        }

    @Test
    fun `when UndoAddToFavorites is called, last favorited movie is restored`() = runTest {
        viewModel.onEvent(WatchlistHomeEvent.OnAddToFavorites(movieId))
        advanceUntilIdle()

        viewModel.onEvent(WatchlistHomeEvent.UndoAddToFavorites)
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.favoritedMovieIds).doesNotContain(movieId)
    }

    @Test
    fun `when UndoAddToFavorites is called with empty list, nothing happens`() = runTest {
        viewModel.onEvent(WatchlistHomeEvent.UndoAddToFavorites)
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.favoritedMovieIds).isEmpty()
    }

    @Test
    fun `when OnAddToFavoritesErrorDismissed is called, movie is removed from favorited list`() =
        runTest {
            viewModel.onEvent(WatchlistHomeEvent.OnAddToFavorites(movieId))
            advanceUntilIdle()

            viewModel.onEvent(WatchlistHomeEvent.OnAddToFavoritesErrorDismissed(movieId))
            advanceUntilIdle()

            assertThat(viewModel.uiState.value.favoritedMovieIds).doesNotContain(movieId)
        }

    // endregion

    // region navigation

    @Test
    fun `when NavigateToDetails is called, navigate side effect is emitted`() = runTest {
        viewModel.sideEffectChannel.test {
            viewModel.onEvent(WatchlistHomeEvent.NavigateToDetails(movieId))
            advanceUntilIdle()

            assertThat(awaitItem())
                .isEqualTo(WatchlistHomeSideEffect.NavigateToDetails(movieId))
        }
    }

    @Test
    fun `when NavigateToLists is called, navigate side effect is emitted`() = runTest {
        viewModel.sideEffectChannel.test {
            viewModel.onEvent(WatchlistHomeEvent.NavigateToLists)
            advanceUntilIdle()

            assertThat(awaitItem()).isEqualTo(WatchlistHomeSideEffect.NavigateToLists)
        }
    }

    @Test
    fun `when OpenDrawer is called, open drawer side effect is emitted`() = runTest {
        viewModel.sideEffectChannel.test {
            viewModel.onEvent(WatchlistHomeEvent.OpenDrawer)
            advanceUntilIdle()

            assertThat(awaitItem()).isEqualTo(WatchlistHomeSideEffect.OpenDrawer)
        }
    }

    @Test
    fun `when DismissDialog is called, uiState is Idle`() = runTest {
        viewModel.onEvent(WatchlistHomeEvent.DismissDialog)
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.uiState).isEqualTo(SimplerUi.Idle)
    }

    // endregion
}
