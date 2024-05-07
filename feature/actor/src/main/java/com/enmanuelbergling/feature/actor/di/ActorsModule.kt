package com.enmanuelbergling.feature.actor.di

import com.enmanuelbergling.core.model.actor.Actor
import com.enmanuelbergling.feature.actor.details.ActorDetailsVM
import com.enmanuelbergling.feature.actor.details.model.ActorDetailsChainHandler
import com.enmanuelbergling.feature.actor.details.model.ActorDetailsChainStart
import com.enmanuelbergling.feature.actor.details.model.ActorKnownMoviesChainHandler
import com.enmanuelbergling.feature.actor.home.ActorsVM
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal val actorDetailsScreenModule = module {
    singleOf(::ActorDetailsChainHandler)
    singleOf(::ActorKnownMoviesChainHandler)
    singleOf(::ActorDetailsChainStart)
}

val actorsModule = module {
    includes(actorDetailsScreenModule)

    viewModel { ActorsVM(get(qualifier = named<Actor>())) }
    viewModelOf(::ActorDetailsVM)
}