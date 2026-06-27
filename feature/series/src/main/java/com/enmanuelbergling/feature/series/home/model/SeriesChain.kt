package com.enmanuelbergling.feature.series.home.model

data class SeriesChain(
    val popularHandler: PopularSeriesChainHandler,
    val topRatedHandler: TopRatedSeriesChainHandler,
    val onTheAirHandler: OnTheAirSeriesChainHandler,
    val airingTodayHandler: AiringTodaySeriesChainHandler,
)
