package com.enmanuelbergling.feature.movies.details.di

import com.enmanuelbergling.feature.movies.details.model.CreditsChainHandler
import com.enmanuelbergling.feature.movies.details.model.MovieDetailsChainHandler
import com.enmanuelbergling.feature.movies.details.model.MovieDetailsChainStart
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val movieDetailsModule = module {
    singleOf(::MovieDetailsChainStart)
    singleOf(::MovieDetailsChainHandler)
    singleOf(::CreditsChainHandler)
}