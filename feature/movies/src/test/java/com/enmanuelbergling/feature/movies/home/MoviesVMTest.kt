package com.enmanuelbergling.feature.movies.home

import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEmpty
import com.enmanuelbergling.core.model.core.SimplerUi
import com.enmanuelbergling.core.testing.extension.KoinExtension
import com.enmanuelbergling.core.testing.extension.MainCoroutineExtension
import com.enmanuelbergling.feature.movies.di.moviesModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.core.parameter.parametersOf
import org.koin.test.KoinTest
import org.koin.test.inject

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MainCoroutineExtension::class)
class MoviesVMTest : KoinTest {
    @JvmField
    @RegisterExtension
    val koinExtension = KoinExtension(moviesModule)

    lateinit var moviesVM: MoviesVM

    @BeforeEach
    fun setup() {
        moviesVM = koinExtension.inject<MoviesVM> { parametersOf(false) }.value
    }

    @Test
    fun `when load ui items are properly save and state is idle`() = runTest {
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

    @AfterEach
    fun tearDown() {
    }
}
