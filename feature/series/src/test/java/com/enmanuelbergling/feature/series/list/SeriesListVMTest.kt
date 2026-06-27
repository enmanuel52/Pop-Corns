package com.enmanuelbergling.feature.series.list

import androidx.paging.testing.asSnapshot
import assertk.assertThat
import assertk.assertions.isNotEmpty
import com.enmanuelbergling.core.testing.extension.KoinExtension
import com.enmanuelbergling.core.testing.extension.MainCoroutineExtension
import com.enmanuelbergling.feature.series.di.seriesModule
import com.enmanuelbergling.feature.series.list.viewmodel.AiringTodaySeriesVM
import com.enmanuelbergling.feature.series.list.viewmodel.OnTheAirSeriesVM
import com.enmanuelbergling.feature.series.list.viewmodel.PopularSeriesVM
import com.enmanuelbergling.feature.series.list.viewmodel.TopRatedSeriesVM
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.test.inject

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MainCoroutineExtension::class)
class SeriesListVMTest {
    @JvmField
    @RegisterExtension
    val koinExtension = KoinExtension(seriesModule)

    private val popularVM: PopularSeriesVM by koinExtension.inject()
    private val topRatedVM: TopRatedSeriesVM by koinExtension.inject()
    private val onTheAirVM: OnTheAirSeriesVM by koinExtension.inject()
    private val airingTodayVM: AiringTodaySeriesVM by koinExtension.inject()

    @Test
    fun `popular series VM emits paged shows`() = runTest {
        assertThat(popularVM.series.asSnapshot()).isNotEmpty()
    }

    @Test
    fun `top rated series VM emits paged shows`() = runTest {
        assertThat(topRatedVM.series.asSnapshot()).isNotEmpty()
    }

    @Test
    fun `on the air series VM emits paged shows`() = runTest {
        assertThat(onTheAirVM.series.asSnapshot()).isNotEmpty()
    }

    @Test
    fun `airing today series VM emits paged shows`() = runTest {
        assertThat(airingTodayVM.series.asSnapshot()).isNotEmpty()
    }
}
