package com.enmanuelbergling.ktormovies

import android.app.Application
import com.enmanuelbergling.ktormovies.di.androidModules
import com.enmanuelbergling.ktormovies.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

class CornsTimeApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin()
    }

    private fun startKoin() {
        stopKoin()
        startKoin {
            androidContext(this@CornsTimeApplication)
            modules(
                androidModules(this@CornsTimeApplication),
                appModule,
            )
        }
    }
}