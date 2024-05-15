package com.enmanuelbergling.ktormovies.di

import android.content.Context
import com.enmanuelbergling.core.datastore.di.preferencesModule
import com.enmanuelbergling.core.domain.usecase.di.ucModule
import com.enmanuelbergling.core.network.di.remoteModule
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

val appModule = module {
    loadKoinModules(
        modules = ucModule + remoteModule + featuresModule
    )
}

fun androidModules(context: Context) = module {
    loadKoinModules(
        preferencesModule(context)
    )
}