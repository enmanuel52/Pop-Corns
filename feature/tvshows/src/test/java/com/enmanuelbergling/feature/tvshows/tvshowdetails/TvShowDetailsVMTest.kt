package com.enmanuelbergling.feature.tvshows.tvshowdetails

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
import com.enmanuelbergling.feature.tvshows.di.tvShowsModule
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
class TvShowDetailsVMTest : KoinTest {

    private val testDispatcher = StandardTestDispatcher()

    @JvmField
    @RegisterExtension
    val koinExtension = KoinExtension(tvShowsModule)

    @JvmField
    @RegisterExtension
    val mainCoroutineExtension = MainCoroutineExtension(testDispatcher)

    private lateinit var viewModel: TvShowDetailsVM
    private val tvShowId = 1

    @BeforeEach
    fun setUp() {
        viewModel = koinExtension.get<TvShowDetailsVM> { parametersOf(tvShowId) }
    }

    @Test
    fun `loadPage successfully updates state with details, credits and account states`() = runTest {
        backgroundScope.launch { viewModel.uiState.collect() }
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.uiState).isEqualTo(SimplerUi.Idle)
        assertThat(viewModel.uiState.value.details).isNotNull()
        assertThat(viewModel.uiState.value.credits).isNotNull()
        assertThat(viewModel.uiState.value.accountStates).isNotNull()
    }

    @Test
    fun `loadPage partially saves state when the account states handler fails`() = runTest {
        val exception = NetworkException.ReadTimeOutException()
        koinExtension.replaceDependencies {
            single<TvRemoteDS> {
                FakeTvRemoteDS().apply {
                    throwError(TvRemoteDsFunction.GetTvAccountStates to exception)
                }
            }
        }
        viewModel = koinExtension.get { parametersOf(tvShowId) }

        backgroundScope.launch { viewModel.uiState.collect() }
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.uiState).isEqualTo(SimplerUi.Error(exception.messageResource))
        assertThat(viewModel.uiState.value.details).isNotNull()
        assertThat(viewModel.uiState.value.credits).isNotNull()
        assertThat(viewModel.uiState.value.accountStates).isNull()
    }

    @Test
    fun `OnWatchlistClick toggles watchlist and updates loading flag`() = runTest {
        backgroundScope.launch { viewModel.uiState.collect() }
        advanceUntilIdle()
        assertThat(viewModel.uiState.value.accountStates?.watchlist).isEqualTo(false)

        viewModel.uiState.test {
            awaitItem() // current

            viewModel.onAction(TvShowDetailsAction.OnWatchlistClick)
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
        viewModel = koinExtension.get { parametersOf(tvShowId) }

        backgroundScope.launch { viewModel.uiState.collect() }
        advanceUntilIdle()

        viewModel.onAction(TvShowDetailsAction.OnWatchlistClick)
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

            viewModel.onAction(TvShowDetailsAction.OnFavoriteClick)
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
        viewModel = koinExtension.get { parametersOf(tvShowId) }

        backgroundScope.launch { viewModel.uiState.collect() }
        advanceUntilIdle()

        viewModel.onAction(TvShowDetailsAction.OnFavoriteClick)
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.isFavoriteLoading).isFalse()
        assertThat(viewModel.uiState.value.uiState).isEqualTo(
            SimplerUi.Error(NetworkException.AuthorizationException().messageResource)
        )
    }
}
