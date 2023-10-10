package com.enmanuelbergling.ktormovies.di

import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

val appModule = module {
    loadKoinModules(
        listOf(
            remoteModule,
            ucModule,
            vmModule,
        )
    )
}