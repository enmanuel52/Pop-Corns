package com.enmanuelbergling.feature.series.seasons

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import assertk.assertions.isTrue
import com.enmanuelbergling.core.domain.datasource.remote.TvRemoteDS
import com.enmanuelbergling.core.model.core.NetworkException
import com.enmanuelbergling.core.model.core.SimplerUi
import com.enmanuelbergling.core.testing.datasource.remote.FakeTvRemoteDS
import com.enmanuelbergling.core.testing.datasource.remote.TvRemoteDsFunction
import com.enmanuelbergling.core.testing.extension.KoinExtension
import com.enmanuelbergling.core.testing.extension.MainCoroutineExtension
import com.enmanuelbergling.core.ui.components.messageResource
import com.enmanuelbergling.feature.series.di.seriesModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.core.parameter.parametersOf
import org.koin.test.KoinTest
import org.koin.test.get

@OptIn(ExperimentalCoroutinesApi::class)
class SeasonsVMTest : KoinTest {

    private val testDispatcher = StandardTestDispatcher()

    @JvmField
    @RegisterExtension
    val koinExtension = KoinExtension(seriesModule)

    @JvmField
    @RegisterExtension
    val mainCoroutineExtension = MainCoroutineExtension(testDispatcher)

    private lateinit var viewModel: SeasonsVM
    private val seriesId = 1

    @BeforeEach
    fun setUp() {
        viewModel = koinExtension.get<SeasonsVM> { parametersOf(seriesId) }
    }

    @Test
    fun `loadPage successfully updates state with details and account states`() = runTest {
        backgroundScope.launch { viewModel.uiState.collect() }
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.uiState).isEqualTo(SimplerUi.Idle)
        assertThat(viewModel.uiState.value.details).isNotNull()
        assertThat(viewModel.uiState.value.accountStates).isNotNull()
    }

    @Test
    fun `loadPage sets error and leaves details null when details fail`() = runTest {
        val exception = NetworkException.ReadTimeOutException()
        koinExtension.replaceDependencies {
            single<TvRemoteDS> {
                FakeTvRemoteDS().apply {
                    throwError(TvRemoteDsFunction.GetTvDetails to exception)
                }
            }
        }
        viewModel = koinExtension.get { parametersOf(seriesId) }

        backgroundScope.launch { viewModel.uiState.collect() }
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.uiState).isEqualTo(SimplerUi.Error(exception.messageResource))
        assertThat(viewModel.uiState.value.details).isNull()
    }

    @Test
    fun `loadPage keeps details and stays idle when account states fail`() = runTest {
        koinExtension.replaceDependencies {
            single<TvRemoteDS> {
                FakeTvRemoteDS().apply {
                    throwError(TvRemoteDsFunction.GetTvAccountStates to NetworkException.AuthorizationException())
                }
            }
        }
        viewModel = koinExtension.get { parametersOf(seriesId) }

        backgroundScope.launch { viewModel.uiState.collect() }
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.uiState).isEqualTo(SimplerUi.Idle)
        assertThat(viewModel.uiState.value.details).isNotNull()
        assertThat(viewModel.uiState.value.accountStates).isNull()
    }

    @Test
    fun `OnWatchlistClick toggles watchlist and updates loading flag`() = runTest {
        backgroundScope.launch { viewModel.uiState.collect() }
        advanceUntilIdle()
        assertThat(viewModel.uiState.value.accountStates?.watchlist).isEqualTo(false)

        viewModel.uiState.test {
            awaitItem() // current

            viewModel.onAction(SeasonsAction.OnWatchlistClick)
            runCurrent()

            assertThat(awaitItem().isWatchlistLoading).isTrue()

            advanceUntilIdle()
            val finalItem = awaitItem()
            assertThat(finalItem.isWatchlistLoading).isFalse()
            assertThat(finalItem.accountStates?.watchlist).isEqualTo(true)
        }
    }

    @Test
    fun `OnWatchlistClick fails sets error`() = runTest {
        koinExtension.replaceDependencies {
            single<TvRemoteDS> {
                FakeTvRemoteDS().apply {
                    throwError(TvRemoteDsFunction.AddTvToAccountWatchlist to NetworkException.AuthorizationException())
                }
            }
        }
        viewModel = koinExtension.get { parametersOf(seriesId) }

        backgroundScope.launch { viewModel.uiState.collect() }
        advanceUntilIdle()

        viewModel.onAction(SeasonsAction.OnWatchlistClick)
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.isWatchlistLoading).isFalse()
        assertThat(viewModel.uiState.value.uiState).isEqualTo(
            SimplerUi.Error(NetworkException.AuthorizationException().messageResource)
        )
    }

    @Test
    fun `OnFavoriteClick toggles favorite and updates loading flag`() = runTest {
        backgroundScope.launch { viewModel.uiState.collect() }
        advanceUntilIdle()
        assertThat(viewModel.uiState.value.accountStates?.favorite).isEqualTo(false)

        viewModel.uiState.test {
            awaitItem()

            viewModel.onAction(SeasonsAction.OnFavoriteClick)
            runCurrent()

            assertThat(awaitItem().isFavoriteLoading).isTrue()

            advanceUntilIdle()
            val finalItem = awaitItem()
            assertThat(finalItem.isFavoriteLoading).isFalse()
            assertThat(finalItem.accountStates?.favorite).isEqualTo(true)
        }
    }

    @Test
    fun `OnFavoriteClick fails sets error`() = runTest {
        koinExtension.replaceDependencies {
            single<TvRemoteDS> {
                FakeTvRemoteDS().apply {
                    throwError(TvRemoteDsFunction.AddTvToFavorites to NetworkException.AuthorizationException())
                }
            }
        }
        viewModel = koinExtension.get { parametersOf(seriesId) }

        backgroundScope.launch { viewModel.uiState.collect() }
        advanceUntilIdle()

        viewModel.onAction(SeasonsAction.OnFavoriteClick)
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.isFavoriteLoading).isFalse()
        assertThat(viewModel.uiState.value.uiState).isEqualTo(
            SimplerUi.Error(NetworkException.AuthorizationException().messageResource)
        )
    }

    @Test
    fun `OnSeasonLongClick expands season and collapses on second click`() = runTest {
        backgroundScope.launch { viewModel.uiState.collect() }
        advanceUntilIdle()

        val seasonId = viewModel.uiState.value.details!!.seasons.first().id
        assertThat(viewModel.uiState.value.expandedSeasonId).isNull()

        viewModel.onAction(SeasonsAction.OnSeasonLongClick(seasonId))
        runCurrent()
        assertThat(viewModel.uiState.value.expandedSeasonId).isEqualTo(seasonId)

        viewModel.onAction(SeasonsAction.OnSeasonLongClick(seasonId))
        runCurrent()
        assertThat(viewModel.uiState.value.expandedSeasonId).isNull()
    }

    @Test
    fun `OnSeasonLongClick collapses previous and expands new season`() = runTest {
        backgroundScope.launch { viewModel.uiState.collect() }
        advanceUntilIdle()

        val seasons = viewModel.uiState.value.details!!.seasons
        val firstId = seasons[0].id
        val secondId = seasons[1].id

        viewModel.onAction(SeasonsAction.OnSeasonLongClick(firstId))
        runCurrent()
        assertThat(viewModel.uiState.value.expandedSeasonId).isEqualTo(firstId)

        viewModel.onAction(SeasonsAction.OnSeasonLongClick(secondId))
        runCurrent()
        assertThat(viewModel.uiState.value.expandedSeasonId).isEqualTo(secondId)
    }
}
