package com.enmanuelbergling.feature.series.di

import com.enmanuelbergling.feature.series.episodes.EpisodesVM
import com.enmanuelbergling.feature.series.favorite.FavoriteSeriesVM
import com.enmanuelbergling.feature.series.home.SeriesVM
import com.enmanuelbergling.feature.series.home.model.AiringTodaySeriesChainHandler
import com.enmanuelbergling.feature.series.home.model.OnTheAirSeriesChainHandler
import com.enmanuelbergling.feature.series.home.model.PopularSeriesChainHandler
import com.enmanuelbergling.feature.series.home.model.SeriesChain
import com.enmanuelbergling.feature.series.home.model.TopRatedSeriesChainHandler
import com.enmanuelbergling.feature.series.list.viewmodel.AiringTodaySeriesVM
import com.enmanuelbergling.feature.series.list.viewmodel.OnTheAirSeriesVM
import com.enmanuelbergling.feature.series.list.viewmodel.PopularSeriesVM
import com.enmanuelbergling.feature.series.list.viewmodel.TopRatedSeriesVM
import com.enmanuelbergling.feature.series.paging.usecase.GetFilteredSeriesUC
import com.enmanuelbergling.feature.series.paging.usecase.GetPaginatedFavoriteSeriesUC
import com.enmanuelbergling.feature.series.paging.usecase.GetPaginatedWatchlistSeriesUC
import com.enmanuelbergling.feature.series.paging.usecase.GetSectionSeriesUC
import com.enmanuelbergling.feature.series.seasons.SeasonsVM
import com.enmanuelbergling.feature.series.watchlist.WatchlistSeriesVM
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val seriesScreenModule = module {
    singleOf(::PopularSeriesChainHandler)
    singleOf(::TopRatedSeriesChainHandler)
    singleOf(::OnTheAirSeriesChainHandler)
    singleOf(::AiringTodaySeriesChainHandler)
    singleOf(::SeriesChain)
}

private val pagingModule = module {
    singleOf(::GetFilteredSeriesUC)
    singleOf(::GetSectionSeriesUC)
    singleOf(::GetPaginatedFavoriteSeriesUC)
    singleOf(::GetPaginatedWatchlistSeriesUC)
}

val seriesModule = module {
    includes(pagingModule)
    includes(seriesScreenModule)

    viewModelOf(::SeriesVM)

    viewModelOf(::PopularSeriesVM)
    viewModelOf(::TopRatedSeriesVM)
    viewModelOf(::OnTheAirSeriesVM)
    viewModelOf(::AiringTodaySeriesVM)

    viewModelOf(::FavoriteSeriesVM)
    viewModelOf(::WatchlistSeriesVM)

    viewModelOf(::SeasonsVM)
    viewModelOf(::EpisodesVM)
}
