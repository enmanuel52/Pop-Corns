package com.enmanuelbergling.ktormovies.di

import com.enmanuelbergling.ktormovies.data.source.preferences.datastore.PreferencesDSImpl
import com.enmanuelbergling.ktormovies.data.source.preferences.domain.PreferencesDS
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val localModule = module {
    single<PreferencesDS>{PreferencesDSImpl(androidContext())}
}