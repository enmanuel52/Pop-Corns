package com.enmanuelbergling.ktormovies.di

import android.content.Context
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

fun androidModules(context: Context) = module {
    localModule(context)
}