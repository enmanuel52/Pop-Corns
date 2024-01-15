package com.enmanuelbergling.ktormovies.di

import android.content.Context
import com.enmanuelbergling.ktormovies.data.source.preferences.datastore.PreferencesDSImpl
import com.enmanuelbergling.ktormovies.data.source.preferences.domain.PreferencesDS
import org.koin.dsl.module

fun localModule(context: Context) = module {
    single<PreferencesDS> { PreferencesDSImpl(context) }
}