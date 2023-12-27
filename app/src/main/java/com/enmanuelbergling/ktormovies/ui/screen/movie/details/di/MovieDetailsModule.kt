package com.enmanuelbergling.ktormovies.ui.screen.movie.details.di

import com.enmanuelbergling.ktormovies.ui.screen.movie.details.model.CreditsChainHandler
import com.enmanuelbergling.ktormovies.ui.screen.movie.details.model.DetailsChainHandler
import com.enmanuelbergling.ktormovies.ui.screen.movie.details.model.MovieDetailsChainHandler
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val movieDetailsModule = module {
    singleOf(::DetailsChainHandler)
    singleOf(::MovieDetailsChainHandler)
    singleOf(::CreditsChainHandler)
}