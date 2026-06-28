package com.enmanuelbergling.feature.watchlists.series

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
class WatchlistSeriesVMTest {
    @JvmField
    @RegisterExtension
    val koinExtension = KoinExtension(watchlistModule)

    private fun viewModel(): WatchlistSeriesVM =
        koinExtension.inject<WatchlistSeriesVM>().value

    @Test
    fun `watchlist emits paged shows`() = runTest {
        assertThat(viewModel().watchlist.asSnapshot()).isNotEmpty()
    }

    @Test
    fun `OnRemoveSeries emits UndoRemoveSeries side effect`() = runTest {
        val vm = viewModel()
        vm.sideEffectChannel.test {
            vm.onEvent(WatchlistSeriesEvent.OnRemoveSeries(1))
            assertThat(awaitItem()).isInstanceOf(WatchlistSeriesSideEffect.UndoRemoveSeries::class)
        }
    }

    @Test
    fun `RemoveSeries emits error side effect when removal fails`() = runTest {
        koinExtension.replaceDependencies {
            single<TvRemoteDS> {
                FakeTvRemoteDS().apply {
                    throwError(TvRemoteDsFunction.RemoveTvFromAccountWatchlist to NetworkException.AuthorizationException)
                }
            }
        }
        val vm = viewModel()
        vm.sideEffectChannel.test {
            vm.onEvent(WatchlistSeriesEvent.RemoveSeries(1))
            assertThat(awaitItem()).isInstanceOf(WatchlistSeriesSideEffect.RemoveSeriesError::class)
        }
    }

    @Test
    fun `OnAddToFavorites emits UndoAddToFavoritesSeries side effect`() = runTest {
        val vm = viewModel()
        vm.sideEffectChannel.test {
            vm.onEvent(WatchlistSeriesEvent.OnAddToFavorites(1))
            assertThat(awaitItem()).isInstanceOf(WatchlistSeriesSideEffect.UndoAddToFavoritesSeries::class)
        }
    }

    @Test
    fun `AddToFavorites emits error side effect when adding to favorites fails`() = runTest {
        koinExtension.replaceDependencies {
            single<TvRemoteDS> {
                FakeTvRemoteDS().apply {
                    throwError(TvRemoteDsFunction.AddTvToFavorites to NetworkException.AuthorizationException)
                }
            }
        }
        val vm = viewModel()
        vm.sideEffectChannel.test {
            vm.onEvent(WatchlistSeriesEvent.AddToFavorites(1))
            assertThat(awaitItem()).isInstanceOf(WatchlistSeriesSideEffect.AddToFavoritesError::class)
        }
    }
}
