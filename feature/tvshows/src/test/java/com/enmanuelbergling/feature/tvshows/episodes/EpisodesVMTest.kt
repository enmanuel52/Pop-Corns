package com.enmanuelbergling.feature.tvshows.episodes

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
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.core.parameter.parametersOf
import org.koin.test.KoinTest
import org.koin.test.get

@OptIn(ExperimentalCoroutinesApi::class)
class EpisodesVMTest : KoinTest {

    private val testDispatcher = StandardTestDispatcher()

    @JvmField
    @RegisterExtension
    val koinExtension = KoinExtension(tvShowsModule)

    @JvmField
    @RegisterExtension
    val mainCoroutineExtension = MainCoroutineExtension(testDispatcher)

    private lateinit var viewModel: EpisodesVM
    private val tvShowId = 1
    private val seasonNumber = 1

    @BeforeEach
    fun setUp() {
        viewModel = koinExtension.get<EpisodesVM> { parametersOf(tvShowId, seasonNumber) }
    }

    @Test
    fun `loadPage successfully updates state with season episodes`() = runTest {
        backgroundScope.launch { viewModel.uiState.collect() }
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.uiState).isEqualTo(SimplerUi.Idle)
        assertThat(viewModel.uiState.value.seasonDetails).isNotNull()
    }

    @Test
    fun `loadPage sets error when season details fail`() = runTest {
        val exception = NetworkException.ReadTimeOutException()
        koinExtension.replaceDependencies {
            single<TvRemoteDS> {
                FakeTvRemoteDS().apply {
                    throwError(TvRemoteDsFunction.GetSeasonDetails to exception)
                }
            }
        }
        viewModel = koinExtension.get { parametersOf(tvShowId, seasonNumber) }

        backgroundScope.launch { viewModel.uiState.collect() }
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.uiState).isEqualTo(SimplerUi.Error(exception.messageResource))
        assertThat(viewModel.uiState.value.seasonDetails).isNull()
    }

    @Test
    fun `OnEpisodeLongClick expands episode and collapses on second click`() = runTest {
        backgroundScope.launch { viewModel.uiState.collect() }
        advanceUntilIdle()

        val episodeId = viewModel.uiState.value.seasonDetails!!.episodes.first().id
        assertThat(viewModel.uiState.value.expandedEpisodeId).isNull()

        viewModel.onAction(EpisodesAction.OnEpisodeLongClick(episodeId))
        runCurrent()
        assertThat(viewModel.uiState.value.expandedEpisodeId).isEqualTo(episodeId)

        viewModel.onAction(EpisodesAction.OnEpisodeLongClick(episodeId))
        runCurrent()
        assertThat(viewModel.uiState.value.expandedEpisodeId).isNull()
    }

    @Test
    fun `OnEpisodeLongClick collapses previous and expands new episode`() = runTest {
        backgroundScope.launch { viewModel.uiState.collect() }
        advanceUntilIdle()

        val episodes = viewModel.uiState.value.seasonDetails!!.episodes
        val firstId = episodes[0].id
        val secondId = episodes[1].id

        viewModel.onAction(EpisodesAction.OnEpisodeLongClick(firstId))
        runCurrent()
        assertThat(viewModel.uiState.value.expandedEpisodeId).isEqualTo(firstId)

        viewModel.onAction(EpisodesAction.OnEpisodeLongClick(secondId))
        runCurrent()
        assertThat(viewModel.uiState.value.expandedEpisodeId).isEqualTo(secondId)
    }
}
