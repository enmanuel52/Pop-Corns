package com.enmanuelbergling.feature.tvshows.di

import com.enmanuelbergling.feature.tvshows.episodedetails.EpisodeDetailsVM
import com.enmanuelbergling.feature.tvshows.episodes.EpisodesVM
import com.enmanuelbergling.feature.tvshows.favorite.FavoriteTvShowsVM
import com.enmanuelbergling.feature.tvshows.home.TvShowsVM
import com.enmanuelbergling.feature.tvshows.home.model.AiringTodayTvShowsChainHandler
import com.enmanuelbergling.feature.tvshows.home.model.OnTheAirTvShowsChainHandler
import com.enmanuelbergling.feature.tvshows.home.model.PopularTvShowsChainHandler
import com.enmanuelbergling.feature.tvshows.home.model.TvShowsChain
import com.enmanuelbergling.feature.tvshows.home.model.TopRatedTvShowsChainHandler
import com.enmanuelbergling.feature.tvshows.list.viewmodel.AiringTodayTvShowsVM
import com.enmanuelbergling.feature.tvshows.list.viewmodel.OnTheAirTvShowsVM
import com.enmanuelbergling.feature.tvshows.list.viewmodel.PopularTvShowsVM
import com.enmanuelbergling.feature.tvshows.list.viewmodel.TopRatedTvShowsVM
import com.enmanuelbergling.feature.tvshows.paging.usecase.GetFilteredTvShowsUC
import com.enmanuelbergling.feature.tvshows.paging.usecase.GetPaginatedFavoriteTvShowsUC
import com.enmanuelbergling.feature.tvshows.paging.usecase.GetSectionTvShowsUC
import com.enmanuelbergling.feature.tvshows.seasons.SeasonsVM
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val tvShowsScreenModule = module {
    singleOf(::PopularTvShowsChainHandler)
    singleOf(::TopRatedTvShowsChainHandler)
    singleOf(::OnTheAirTvShowsChainHandler)
    singleOf(::AiringTodayTvShowsChainHandler)
    singleOf(::TvShowsChain)
}

private val pagingModule = module {
    singleOf(::GetFilteredTvShowsUC)
    singleOf(::GetSectionTvShowsUC)
    singleOf(::GetPaginatedFavoriteTvShowsUC)
}

val tvShowsModule = module {
    includes(pagingModule)
    includes(tvShowsScreenModule)

    viewModelOf(::TvShowsVM)

    viewModelOf(::PopularTvShowsVM)
    viewModelOf(::TopRatedTvShowsVM)
    viewModelOf(::OnTheAirTvShowsVM)
    viewModelOf(::AiringTodayTvShowsVM)

    viewModelOf(::FavoriteTvShowsVM)

    viewModelOf(::SeasonsVM)
    viewModelOf(::EpisodesVM)
    viewModelOf(::EpisodeDetailsVM)
}
