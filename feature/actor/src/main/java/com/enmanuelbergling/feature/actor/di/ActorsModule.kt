package com.enmanuelbergling.feature.actor.di

import com.enmanuelbergling.core.model.actor.Actor
import com.enmanuelbergling.feature.actor.details.model.ActorDetailsChainHandler
import com.enmanuelbergling.feature.actor.details.model.ActorKnownMoviesChainHandler
import com.enmanuelbergling.feature.actor.details.model.ActorDetailsChainStart
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import com.enmanuelbergling.feature.actor.details.ActorDetailsVM
import com.enmanuelbergling.feature.actor.home.ActorsVM
import org.koin.core.module.dsl.factoryOf
import org.koin.core.qualifier.named

internal val actorDetailsScreenModule = module {
    singleOf(::ActorDetailsChainHandler)
    singleOf(::ActorKnownMoviesChainHandler)
    singleOf(::ActorDetailsChainStart)
}

val actorsModule = module {
    includes(actorDetailsScreenModule)

    factory { ActorsVM(get(qualifier = named<Actor>())) }
    factoryOf(::ActorDetailsVM)
}