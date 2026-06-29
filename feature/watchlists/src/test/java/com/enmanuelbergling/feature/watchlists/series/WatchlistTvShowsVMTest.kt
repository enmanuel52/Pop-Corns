package com.enmanuelbergling.feature.watchlists.tvShows

import androidx.paging.testing.asSnapshot
import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotEmpty
import com.enmanuelbergling.core.domain.datasource.remote.TvRemoteDS
import com.enmanuelbergling.core.model.core.NetworkException
import com.enmanuelbergling.core.testing.datasource.remote.FakeTvRemoteDS
import com.enmanuelbergling.core.testing.datasource.remote.TvRemoteDsFunction
import com.enmanuelbergling.core.testing.extension.KoinExtension
import com.enmanuelbergling.core.testing.extension.MainCoroutineExtension
import com.enmanuelbergling.feature.watchlists.di.watchlistModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.test.inject

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MainCoroutineExtension::class)
class WatchlistTvShowsVMTest {
    @JvmField
    @RegisterExtension
    val koinExtension = KoinExtension(watchlistModule)

    private fun viewModel(): WatchlistTvShowsVM =
        koinExtension.inject<WatchlistTvShowsVM>().value

    @Test
    fun `watchlist emits paged shows`() = runTest {
        assertThat(viewModel().watchlist.asSnapshot()).isNotEmpty()
    }

    @Test
    fun `OnRemoveTvShows emits UndoRemoveTvShows side effect`() = runTest {
        val vm = viewModel()
        vm.sideEffectChannel.test {
            vm.onEvent(WatchlistTvShowsEvent.OnRemoveTvShows(1))
            assertThat(awaitItem()).isInstanceOf(WatchlistTvShowsSideEffect.UndoRemoveTvShows::class)
        }
    }

    @Test
    fun `RemoveTvShows emits error side effect when removal fails`() = runTest {
        koinExtension.replaceDependencies {
            single<TvRemoteDS> {
                FakeTvRemoteDS().apply {
                    throwError(TvRemoteDsFunction.RemoveTvFromAccountWatchlist to NetworkException.AuthorizationException())
                }
            }
        }
        val vm = viewModel()
        vm.sideEffectChannel.test {
            vm.onEvent(WatchlistTvShowsEvent.RemoveTvShows(1))
            assertThat(awaitItem()).isInstanceOf(WatchlistTvShowsSideEffect.RemoveTvShowsError::class)
        }
    }

    @Test
    fun `OnAddToFavorites emits UndoAddToFavoritesTvShows side effect`() = runTest {
        val vm = viewModel()
        vm.sideEffectChannel.test {
            vm.onEvent(WatchlistTvShowsEvent.OnAddToFavorites(1))
            assertThat(awaitItem()).isInstanceOf(WatchlistTvShowsSideEffect.UndoAddToFavoritesTvShows::class)
        }
    }

    @Test
    fun `AddToFavorites emits error side effect when adding to favorites fails`() = runTest {
        koinExtension.replaceDependencies {
            single<TvRemoteDS> {
                FakeTvRemoteDS().apply {
                    throwError(TvRemoteDsFunction.AddTvToFavorites to NetworkException.AuthorizationException())
                }
            }
        }
        val vm = viewModel()
        vm.sideEffectChannel.test {
            vm.onEvent(WatchlistTvShowsEvent.AddToFavorites(1))
            assertThat(awaitItem()).isInstanceOf(WatchlistTvShowsSideEffect.AddToFavoritesError::class)
        }
    }
}
