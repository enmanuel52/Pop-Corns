package com.enmanuelbergling.ktormovies.ui.screen.actor.details.di

import com.enmanuelbergling.ktormovies.ui.screen.actor.details.model.ActorDetailsChainHandler
import com.enmanuelbergling.ktormovies.ui.screen.actor.details.model.ActorKnownMoviesChainHandler
import com.enmanuelbergling.ktormovies.ui.screen.actor.details.model.ActorDetailsChainStart
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val actorDetailsModule = module {
    singleOf(::ActorDetailsChainHandler)
    singleOf(::ActorKnownMoviesChainHandler)
    singleOf(::ActorDetailsChainStart)
}