package com.enmanuelbergling.ktormovies

import android.app.Application
import com.enmanuelbergling.ktormovies.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class CornsTimeApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@CornsTimeApplication)
            modules(appModule)
        }
    }
}