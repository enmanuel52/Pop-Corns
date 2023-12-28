package com.enmanuelbergling.ktormovies.ui.screen.movie.details.di

import com.enmanuelbergling.ktormovies.ui.screen.movie.details.model.CreditsChainHandler
import com.enmanuelbergling.ktormovies.ui.screen.movie.details.model.MovieDetailsChainStart
import com.enmanuelbergling.ktormovies.ui.screen.movie.details.model.MovieDetailsChainHandler
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val movieDetailsModule = module {
    singleOf(::MovieDetailsChainStart)
    singleOf(::MovieDetailsChainHandler)
    singleOf(::CreditsChainHandler)
}