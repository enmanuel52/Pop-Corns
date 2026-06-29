package com.enmanuelbergling.feature.tvshows.home.model

data class TvShowsChain(
    val popularHandler: PopularTvShowsChainHandler,
    val topRatedHandler: TopRatedTvShowsChainHandler,
    val onTheAirHandler: OnTheAirTvShowsChainHandler,
    val airingTodayHandler: AiringTodayTvShowsChainHandler,
)
