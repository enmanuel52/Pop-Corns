package com.enmanuelbergling.feature.movies.home

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.doesNotContain
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEmpty
import com.enmanuelbergling.core.domain.datasource.remote.MovieRemoteDS
import com.enmanuelbergling.core.model.core.NetworkException
import com.enmanuelbergling.core.model.core.SimplerUi
import com.enmanuelbergling.core.testing.datasource.remote.FakeMovieRemoteDS
import com.enmanuelbergling.core.testing.datasource.remote.MovieRemoteDsFunction
import com.enmanuelbergling.core.testing.extension.KoinExtension
import com.enmanuelbergling.core.testing.extension.MainCoroutineExtension
import com.enmanuelbergling.core.ui.components.messageResource
import com.enmanuelbergling.feature.movies.di.moviesModule
import com.enmanuelbergling.feature.movies.home.model.SuggestionEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.core.parameter.parametersOf
import org.koin.test.inject

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MainCoroutineExtension::class)
class MoviesVMTest {
    @JvmField
    @RegisterExtension
    val koinExtension = KoinExtension(moviesModule)

    lateinit var moviesVM: MoviesVM

    @BeforeEach
    fun setup() {
        moviesVM = koinExtension.inject<MoviesVM> { parametersOf(false) }.value
    }

    @Test
    fun `save movie lists when are successfully loaded`() = runTest {
        // Given
        // Initial State

        // When
        moviesVM.loadUi()
        advanceUntilIdle()

        // Then
        assertThat(moviesVM.uiState.value).isEqualTo(SimplerUi.Idle)
        assertThat(moviesVM.uiDataState.value.upcoming).isNotEmpty()
        assertThat(moviesVM.uiDataState.value.popular).isNotEmpty()
        assertThat(moviesVM.uiDataState.value.topRated).isNotEmpty()
        assertThat(moviesVM.uiDataState.value.nowPlaying).isNotEmpty()
    }

    @Test
    fun `movie lists remain empty when one of them fails and uiState is error`() = runTest {
        //Given
        val readTimeOutException = NetworkException.ReadTimeOutException
        val fakeMovieRemoteDS = FakeMovieRemoteDS().apply {
            throwError(MovieRemoteDsFunction.GetUpcomingMovies to readTimeOutException)
        }

        koinExtension.replaceDependencies {
            single<MovieRemoteDS> { fakeMovieRemoteDS }
        }
        moviesVM = koinExtension.inject<MoviesVM> { parametersOf(false) }.value

        // When
        backgroundScope.launch {
            moviesVM.uiState.collect()
        }
        // this is already called when the uiState is collected
//        moviesVM.loadUi()
        advanceUntilIdle()

        // Then

        assertThat(moviesVM.uiState.value).isEqualTo(SimplerUi.Error(readTimeOutException.messageResource))
        assertThat(moviesVM.uiDataState.value.upcoming).isEmpty()
        assertThat(moviesVM.uiDataState.value.popular).isEmpty()
        assertThat(moviesVM.uiDataState.value.topRated).isEmpty()
        assertThat(moviesVM.uiDataState.value.nowPlaying).isEmpty()
    }

    @Test
    fun `when onSuggestionEvent Add is called, suggestion is added to ui state`() = runTest {
        // Given
        val query = "Avengers"

        // When
        moviesVM.onSuggestionEvent(SuggestionEvent.Add(query))
        advanceUntilIdle()

        // Then
        assertThat(moviesVM.uiDataState.value.searchSuggestions).contains(query)
    }

    @Test
    fun `when onSuggestionEvent Clear is called, suggestions are cleared from ui state`() = runTest {
        // Given
        moviesVM.onSuggestionEvent(SuggestionEvent.Add("Avengers"))
        advanceUntilIdle()
        assertThat(moviesVM.uiDataState.value.searchSuggestions).isNotEmpty()

        // When
        moviesVM.onSuggestionEvent(SuggestionEvent.Clear)
        advanceUntilIdle()

        // Then
        assertThat(moviesVM.uiDataState.value.searchSuggestions).isEmpty()
    }

    @Test
    fun `when onSuggestionEvent Delete is called, suggestion is first added to deleted list then removed`() = runTest {
        // Given
        val query = "Avengers"
        moviesVM.onSuggestionEvent(SuggestionEvent.Add(query))
        advanceUntilIdle()

        // When
        moviesVM.onSuggestionEvent(SuggestionEvent.Delete(query))
        advanceUntilIdle()

        // Then
        assertThat(moviesVM.uiDataState.value.searchSuggestions).doesNotContain(query)
    }

    @AfterEach
    fun tearDown() {
    }
}
