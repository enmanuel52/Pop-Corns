package com.enmanuelbergling.feature.series.favorite

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotEmpty
import androidx.paging.testing.asSnapshot
import com.enmanuelbergling.core.domain.datasource.remote.TvRemoteDS
import com.enmanuelbergling.core.model.core.NetworkException
import com.enmanuelbergling.core.testing.datasource.remote.FakeTvRemoteDS
import com.enmanuelbergling.core.testing.datasource.remote.TvRemoteDsFunction
import com.enmanuelbergling.core.testing.extension.KoinExtension
import com.enmanuelbergling.core.testing.extension.MainCoroutineExtension
import com.enmanuelbergling.feature.series.di.seriesModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.test.inject

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MainCoroutineExtension::class)
class FavoriteSeriesVMTest {
    @JvmField
    @RegisterExtension
    val koinExtension = KoinExtension(seriesModule)

    private fun viewModel(): FavoriteSeriesVM =
        koinExtension.inject<FavoriteSeriesVM>().value

    @Test
    fun `favorites emits paged shows`() = runTest {
        assertThat(viewModel().favorites.asSnapshot()).isNotEmpty()
    }

    @Test
    fun `OnRemoveSeries emits UndoRemoveSeries side effect`() = runTest {
        val vm = viewModel()
        vm.sideEffectChannel.test {
            vm.onEvent(FavoriteSeriesEvent.OnRemoveSeries(1))
            assertThat(awaitItem()).isInstanceOf(FavoriteSeriesSideEffect.UndoRemoveSeries::class)
        }
    }

    @Test
    fun `RemoveSeries emits error side effect when removal fails`() = runTest {
        koinExtension.replaceDependencies {
            single<TvRemoteDS> {
                FakeTvRemoteDS().apply {
                    throwError(TvRemoteDsFunction.RemoveTvFromFavorites to NetworkException.AuthorizationException())
                }
            }
        }
        val vm = viewModel()
        vm.sideEffectChannel.test {
            vm.onEvent(FavoriteSeriesEvent.RemoveSeries(1))
            assertThat(awaitItem()).isInstanceOf(FavoriteSeriesSideEffect.RemoveSeriesError::class)
        }
    }
}
