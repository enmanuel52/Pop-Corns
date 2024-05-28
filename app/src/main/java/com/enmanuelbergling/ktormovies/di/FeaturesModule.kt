package com.enmanuelbergling.ktormovies.di

import com.enmanuelbergling.feature.actor.di.actorsModule
import com.enmanuelbergling.feature.auth.di.loginModule
import com.enmanuelbergling.feature.movies.di.moviesModule
import com.enmanuelbergling.feature.settings.di.settingsModule
import com.enmanuelbergling.feature.watchlists.di.watchlistModule
import com.enmanuelbergling.ktormovies.ui.CornTimeVM
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val featuresModule = module {
    includes(
        listOf(
            moviesModule,
            actorsModule,
            watchlistModule,
            settingsModule
        ) + loginModule
    )

    viewModelOf(::CornTimeVM)
}