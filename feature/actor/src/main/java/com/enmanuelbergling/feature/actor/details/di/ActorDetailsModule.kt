package com.enmanuelbergling.feature.actor.details.di

import com.enmanuelbergling.feature.actor.details.model.ActorDetailsChainHandler
import com.enmanuelbergling.feature.actor.details.model.ActorKnownMoviesChainHandler
import com.enmanuelbergling.feature.actor.details.model.ActorDetailsChainStart
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val actorDetailsModule = module {
    singleOf(::ActorDetailsChainHandler)
    singleOf(::ActorKnownMoviesChainHandler)
    singleOf(::ActorDetailsChainStart)
}