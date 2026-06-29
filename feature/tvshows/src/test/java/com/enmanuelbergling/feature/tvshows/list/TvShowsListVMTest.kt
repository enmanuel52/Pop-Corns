package com.enmanuelbergling.feature.tvshows.list

import androidx.paging.testing.asSnapshot
import assertk.assertThat
import assertk.assertions.isNotEmpty
import com.enmanuelbergling.core.testing.extension.KoinExtension
import com.enmanuelbergling.core.testing.extension.MainCoroutineExtension
import com.enmanuelbergling.feature.tvshows.di.tvShowsModule
import com.enmanuelbergling.feature.tvshows.list.viewmodel.AiringTodayTvShowsVM
import com.enmanuelbergling.feature.tvshows.list.viewmodel.OnTheAirTvShowsVM
import com.enmanuelbergling.feature.tvshows.list.viewmodel.PopularTvShowsVM
import com.enmanuelbergling.feature.tvshows.list.viewmodel.TopRatedTvShowsVM
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.test.inject

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MainCoroutineExtension::class)
class TvShowsListVMTest {
    @JvmField
    @RegisterExtension
    val koinExtension = KoinExtension(tvShowsModule)

    private val popularVM: PopularTvShowsVM by koinExtension.inject()
    private val topRatedVM: TopRatedTvShowsVM by koinExtension.inject()
    private val onTheAirVM: OnTheAirTvShowsVM by koinExtension.inject()
    private val airingTodayVM: AiringTodayTvShowsVM by koinExtension.inject()

    @Test
    fun `popular tvShows VM emits paged shows`() = runTest {
        assertThat(popularVM.tvShows.asSnapshot()).isNotEmpty()
    }

    @Test
    fun `top rated tvShows VM emits paged shows`() = runTest {
        assertThat(topRatedVM.tvShows.asSnapshot()).isNotEmpty()
    }

    @Test
    fun `on the air tvShows VM emits paged shows`() = runTest {
        assertThat(onTheAirVM.tvShows.asSnapshot()).isNotEmpty()
    }

    @Test
    fun `airing today tvShows VM emits paged shows`() = runTest {
        assertThat(airingTodayVM.tvShows.asSnapshot()).isNotEmpty()
    }
}
