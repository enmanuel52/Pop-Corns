package com.enmanuelbergling.feature.tvshows.home

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
import com.enmanuelbergling.feature.tvshows.di.tvShowsModule
import com.enmanuelbergling.feature.tvshows.home.model.SuggestionEvent
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
class TvShowsVMTest {
    @JvmField
    @RegisterExtension
    val koinExtension = KoinExtension(tvShowsModule)

    lateinit var tvShowVM: TvShowsVM

    val userPreferenceDS: UserPreferenceDS by koinExtension.inject()

    @org.junit.jupiter.api.BeforeEach
    fun setup() {
        tvShowVM = koinExtension.inject<TvShowsVM> { parametersOf(false) }.value
    }

    @Test
    fun `isLoggedIn updates when user is saved or cleared`() = runTest {
        tvShowVM.isLoggedIn.test {
            assertThat(awaitItem()).isEqualTo(false)

            userPreferenceDS.updateUser(UserDetails(id = 1, username = "test"))
            assertThat(awaitItem()).isEqualTo(true)

            userPreferenceDS.clear()
            assertThat(awaitItem()).isEqualTo(false)
        }
    }

    @Test
    fun `save tvShows lists when are successfully loaded`() = runTest {
        // When
        backgroundScope.launch {
            tvShowVM.uiState.collect()
        }
        advanceUntilIdle()

        // Then
        assertThat(tvShowVM.uiState.value).isEqualTo(SimplerUi.Idle)
        assertThat(tvShowVM.uiDataState.value.popular).isNotEmpty()
        assertThat(tvShowVM.uiDataState.value.topRated).isNotEmpty()
        assertThat(tvShowVM.uiDataState.value.onTheAir).isNotEmpty()
        assertThat(tvShowVM.uiDataState.value.airingToday).isNotEmpty()
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
        tvShowVM = koinExtension.inject<TvShowsVM> { parametersOf(false) }.value

        // When
        backgroundScope.launch {
            tvShowVM.uiState.collect()
        }
        advanceUntilIdle()

        // Then
        assertThat(tvShowVM.uiState.value).isEqualTo(SimplerUi.Error(readTimeOutException.messageResource))
        assertThat(tvShowVM.uiDataState.value.popular).isEmpty()
        assertThat(tvShowVM.uiDataState.value.topRated).isEmpty()
        assertThat(tvShowVM.uiDataState.value.onTheAir).isEmpty()
        assertThat(tvShowVM.uiDataState.value.airingToday).isEmpty()
    }

    @Test
    fun `tvShows lists are partially saved when the third chain handler fails`() = runTest {
        // Given
        val readTimeOutException = NetworkException.ReadTimeOutException()
        val fakeTvRemoteDS = FakeTvRemoteDS().apply {
            throwError(TvRemoteDsFunction.GetOnTheAirTv to readTimeOutException)
        }

        koinExtension.replaceDependencies {
            single<TvRemoteDS> { fakeTvRemoteDS }
        }
        tvShowVM = koinExtension.inject<TvShowsVM> { parametersOf(false) }.value

        // When
        backgroundScope.launch {
            tvShowVM.uiState.collect()
        }
        tvShowVM.loadUi()
        advanceUntilIdle()

        // Then
        assertThat(tvShowVM.uiState.value).isEqualTo(SimplerUi.Error(readTimeOutException.messageResource))
        assertThat(tvShowVM.uiDataState.value.popular).isNotEmpty()
        assertThat(tvShowVM.uiDataState.value.topRated).isNotEmpty()
        assertThat(tvShowVM.uiDataState.value.onTheAir).isEmpty()
        assertThat(tvShowVM.uiDataState.value.airingToday).isEmpty()
    }

    @Test
    fun `when onSuggestionEvent Add is called, suggestion is added to ui state`() = runTest {
        // Given
        val query = "Breaking Bad"
        backgroundScope.launch {
            tvShowVM.uiDataState.collect()
        }

        // When
        tvShowVM.onSuggestionEvent(SuggestionEvent.Add(query))
        advanceUntilIdle()

        // Then
        assertThat(tvShowVM.uiDataState.value.searchSuggestions).contains(query)
    }

    @Test
    fun `when onSuggestionEvent Clear is called, suggestions are cleared from ui state`() = runTest {
        // Given
        backgroundScope.launch {
            tvShowVM.uiDataState.collect()
        }
        tvShowVM.onSuggestionEvent(SuggestionEvent.Add("Breaking Bad"))
        advanceUntilIdle()
        assertThat(tvShowVM.uiDataState.value.searchSuggestions).isNotEmpty()

        // When
        tvShowVM.onSuggestionEvent(SuggestionEvent.Clear)
        advanceUntilIdle()

        // Then
        assertThat(tvShowVM.uiDataState.value.searchSuggestions).isEmpty()
    }

    @Test
    fun `when onSuggestionEvent Delete is called, suggestion is first added to deleted list then removed`() =
        runTest {
            // Given
            val query = "Breaking Bad"
            backgroundScope.launch {
                tvShowVM.uiDataState.collect()
            }
            tvShowVM.onSuggestionEvent(SuggestionEvent.Add(query))
            advanceUntilIdle()

            // When
            tvShowVM.onSuggestionEvent(SuggestionEvent.Delete(query))
            runCurrent()

            // Then - Immediate state (animation starting)
            assertThat(tvShowVM.uiDataState.value.searchSuggestionsDeleted).contains(query)

            // When - Wait for animation
            advanceTimeBy(200.milliseconds)
            runCurrent()

            // Then - Final state
            assertThat(tvShowVM.uiDataState.value.searchSuggestions).doesNotContain(query)
            assertThat(tvShowVM.uiDataState.value.searchSuggestionsDeleted).isEmpty()
        }
}
