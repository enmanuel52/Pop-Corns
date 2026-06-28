package com.enmanuelbergling.feature.series.home

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.doesNotContain
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEmpty
import com.enmanuelbergling.core.domain.datasource.preferences.UserPreferenceDS
import com.enmanuelbergling.core.domain.datasource.remote.TvRemoteDS
import com.enmanuelbergling.core.model.core.NetworkException
import com.enmanuelbergling.core.model.core.SimplerUi
import com.enmanuelbergling.core.model.user.UserDetails
import com.enmanuelbergling.core.testing.datasource.remote.FakeTvRemoteDS
import com.enmanuelbergling.core.testing.datasource.remote.TvRemoteDsFunction
import com.enmanuelbergling.core.testing.extension.KoinExtension
import com.enmanuelbergling.core.testing.extension.MainCoroutineExtension
import com.enmanuelbergling.core.ui.components.messageResource
import com.enmanuelbergling.feature.series.di.seriesModule
import com.enmanuelbergling.feature.series.home.model.SuggestionEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.core.parameter.parametersOf
import org.koin.test.inject
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MainCoroutineExtension::class)
class SeriesVMTest {
    @JvmField
    @RegisterExtension
    val koinExtension = KoinExtension(seriesModule)

    lateinit var seriesVM: SeriesVM

    val userPreferenceDS: UserPreferenceDS by koinExtension.inject()

    @org.junit.jupiter.api.BeforeEach
    fun setup() {
        seriesVM = koinExtension.inject<SeriesVM> { parametersOf(false) }.value
    }

    @Test
    fun `isLoggedIn updates when user is saved or cleared`() = runTest {
        seriesVM.isLoggedIn.test {
            assertThat(awaitItem()).isEqualTo(false)

            userPreferenceDS.updateUser(UserDetails(id = 1, username = "test"))
            assertThat(awaitItem()).isEqualTo(true)

            userPreferenceDS.clear()
            assertThat(awaitItem()).isEqualTo(false)
        }
    }

    @Test
    fun `save series lists when are successfully loaded`() = runTest {
        // When
        backgroundScope.launch {
            seriesVM.uiState.collect()
        }
        advanceUntilIdle()

        // Then
        assertThat(seriesVM.uiState.value).isEqualTo(SimplerUi.Idle)
        assertThat(seriesVM.uiDataState.value.popular).isNotEmpty()
        assertThat(seriesVM.uiDataState.value.topRated).isNotEmpty()
        assertThat(seriesVM.uiDataState.value.onTheAir).isNotEmpty()
        assertThat(seriesVM.uiDataState.value.airingToday).isNotEmpty()
    }

    @Test
    fun `lists left remain empty when one of them fails and uiState is error`() = runTest {
        // Given
        val readTimeOutException = NetworkException.ReadTimeOutException()
        val fakeTvRemoteDS = FakeTvRemoteDS().apply {
            throwError(TvRemoteDsFunction.GetPopularTv to readTimeOutException)
        }

        koinExtension.replaceDependencies {
            single<TvRemoteDS> { fakeTvRemoteDS }
        }
        seriesVM = koinExtension.inject<SeriesVM> { parametersOf(false) }.value

        // When
        backgroundScope.launch {
            seriesVM.uiState.collect()
        }
        advanceUntilIdle()

        // Then
        assertThat(seriesVM.uiState.value).isEqualTo(SimplerUi.Error(readTimeOutException.messageResource))
        assertThat(seriesVM.uiDataState.value.popular).isEmpty()
        assertThat(seriesVM.uiDataState.value.topRated).isEmpty()
        assertThat(seriesVM.uiDataState.value.onTheAir).isEmpty()
        assertThat(seriesVM.uiDataState.value.airingToday).isEmpty()
    }

    @Test
    fun `series lists are partially saved when the third chain handler fails`() = runTest {
        // Given
        val readTimeOutException = NetworkException.ReadTimeOutException()
        val fakeTvRemoteDS = FakeTvRemoteDS().apply {
            throwError(TvRemoteDsFunction.GetOnTheAirTv to readTimeOutException)
        }

        koinExtension.replaceDependencies {
            single<TvRemoteDS> { fakeTvRemoteDS }
        }
        seriesVM = koinExtension.inject<SeriesVM> { parametersOf(false) }.value

        // When
        backgroundScope.launch {
            seriesVM.uiState.collect()
        }
        seriesVM.loadUi()
        advanceUntilIdle()

        // Then
        assertThat(seriesVM.uiState.value).isEqualTo(SimplerUi.Error(readTimeOutException.messageResource))
        assertThat(seriesVM.uiDataState.value.popular).isNotEmpty()
        assertThat(seriesVM.uiDataState.value.topRated).isNotEmpty()
        assertThat(seriesVM.uiDataState.value.onTheAir).isEmpty()
        assertThat(seriesVM.uiDataState.value.airingToday).isEmpty()
    }

    @Test
    fun `when onSuggestionEvent Add is called, suggestion is added to ui state`() = runTest {
        // Given
        val query = "Breaking Bad"
        backgroundScope.launch {
            seriesVM.uiDataState.collect()
        }

        // When
        seriesVM.onSuggestionEvent(SuggestionEvent.Add(query))
        advanceUntilIdle()

        // Then
        assertThat(seriesVM.uiDataState.value.searchSuggestions).contains(query)
    }

    @Test
    fun `when onSuggestionEvent Clear is called, suggestions are cleared from ui state`() = runTest {
        // Given
        backgroundScope.launch {
            seriesVM.uiDataState.collect()
        }
        seriesVM.onSuggestionEvent(SuggestionEvent.Add("Breaking Bad"))
        advanceUntilIdle()
        assertThat(seriesVM.uiDataState.value.searchSuggestions).isNotEmpty()

        // When
        seriesVM.onSuggestionEvent(SuggestionEvent.Clear)
        advanceUntilIdle()

        // Then
        assertThat(seriesVM.uiDataState.value.searchSuggestions).isEmpty()
    }

    @Test
    fun `when onSuggestionEvent Delete is called, suggestion is first added to deleted list then removed`() =
        runTest {
            // Given
            val query = "Breaking Bad"
            backgroundScope.launch {
                seriesVM.uiDataState.collect()
            }
            seriesVM.onSuggestionEvent(SuggestionEvent.Add(query))
            advanceUntilIdle()

            // When
            seriesVM.onSuggestionEvent(SuggestionEvent.Delete(query))
            runCurrent()

            // Then - Immediate state (animation starting)
            assertThat(seriesVM.uiDataState.value.searchSuggestionsDeleted).contains(query)

            // When - Wait for animation
            advanceTimeBy(200.milliseconds)
            runCurrent()

            // Then - Final state
            assertThat(seriesVM.uiDataState.value.searchSuggestions).doesNotContain(query)
            assertThat(seriesVM.uiDataState.value.searchSuggestionsDeleted).isEmpty()
        }
}
