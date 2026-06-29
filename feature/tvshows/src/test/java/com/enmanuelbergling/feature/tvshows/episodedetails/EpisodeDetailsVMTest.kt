package com.enmanuelbergling.feature.tvshows.episodedetails

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isNull
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
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.core.parameter.parametersOf
import org.koin.test.KoinTest
import org.koin.test.get

@OptIn(ExperimentalCoroutinesApi::class)
class EpisodeDetailsVMTest : KoinTest {

    private val testDispatcher = StandardTestDispatcher()

    @JvmField
    @RegisterExtension
    val koinExtension = KoinExtension(tvShowsModule)

    @JvmField
    @RegisterExtension
    val mainCoroutineExtension = MainCoroutineExtension(testDispatcher)

    private lateinit var viewModel: EpisodeDetailsVM
    private val tvShowId = 1
    private val seasonNumber = 1
    private val episodeNumber = 1

    @BeforeEach
    fun setUp() {
        viewModel = koinExtension.get<EpisodeDetailsVM> {
            parametersOf(tvShowId, seasonNumber, episodeNumber)
        }
    }

    @Test
    fun `loadPage successfully updates state with episode details`() = runTest {
        backgroundScope.launch { viewModel.uiState.collect() }
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.uiState).isEqualTo(SimplerUi.Idle)
        assertThat(viewModel.uiState.value.details).isNotNull()
    }

    @Test
    fun `loadPage sets error when episode details fail`() = runTest {
        val exception = NetworkException.ReadTimeOutException()
        koinExtension.replaceDependencies {
            single<TvRemoteDS> {
                FakeTvRemoteDS().apply {
                    throwError(TvRemoteDsFunction.GetEpisodeDetails to exception)
                }
            }
        }
        viewModel = koinExtension.get { parametersOf(tvShowId, seasonNumber, episodeNumber) }

        backgroundScope.launch { viewModel.uiState.collect() }
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.uiState).isEqualTo(SimplerUi.Error(exception.messageResource))
        assertThat(viewModel.uiState.value.details).isNull()
    }
}
