package com.enmanuelbergling.ktormovies.di

import com.enmanuelbergling.feature.actor.di.actorsModule
import com.enmanuelbergling.feature.auth.di.loginModule
import com.enmanuelbergling.feature.movies.di.moviesModule
import com.enmanuelbergling.feature.watchlists.di.watchlistModule
import com.enmanuelbergling.ktormovies.CornTimeVM
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val uiModule = module {
    includes(
        moviesModule,
        actorsModule,
        watchlistModule,
        loginModule
    )

    factoryOf(::CornTimeVM)
}