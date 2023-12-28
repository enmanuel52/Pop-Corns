package com.enmanuelbergling.ktormovies.ui.screen.movie.home.di

import com.enmanuelbergling.ktormovies.ui.screen.movie.home.model.MoviesChainStart
import com.enmanuelbergling.ktormovies.ui.screen.movie.home.model.NowPlayingMoviesChainHandler
import com.enmanuelbergling.ktormovies.ui.screen.movie.home.model.TopRatedMoviesChainHandler
import com.enmanuelbergling.ktormovies.ui.screen.movie.home.model.UpcomingMoviesChainHandler
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val movieModule = module {
    singleOf(::MoviesChainStart)
    singleOf(::UpcomingMoviesChainHandler)
    singleOf(::TopRatedMoviesChainHandler)
    singleOf(::NowPlayingMoviesChainHandler)
}