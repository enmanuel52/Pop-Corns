package com.enmanuelbergling.feature.movies.home.model

data class MoviesChain(
    val upcomingHandler: UpcomingMoviesChainHandler,
    val topRatedHandler: TopRatedMoviesChainHandler,
    val nowPlayingHandler: NowPlayingMoviesChainHandler,
    val popularHandler: PopularMoviesChainHandler,
)
