package com.enmanuelbergling.feature.movies.home.di

import com.enmanuelbergling.feature.movies.home.model.MoviesChainStart
import com.enmanuelbergling.feature.movies.home.model.NowPlayingMoviesChainHandler
import com.enmanuelbergling.feature.movies.home.model.PopularMoviesChainHandler
import com.enmanuelbergling.feature.movies.home.model.TopRatedMoviesChainHandler
import com.enmanuelbergling.feature.movies.home.model.UpcomingMoviesChainHandler
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val movieModule = module {
    singleOf(::MoviesChainStart)
    singleOf(::UpcomingMoviesChainHandler)
    singleOf(::TopRatedMoviesChainHandler)
    singleOf(::NowPlayingMoviesChainHandler)
    singleOf(::PopularMoviesChainHandler)
}