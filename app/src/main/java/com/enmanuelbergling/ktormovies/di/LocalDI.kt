package com.enmanuelbergling.ktormovies.di

import android.content.Context
import com.enmanuelbergling.ktormovies.data.source.preferences.datastore.AuthPreferenceDSImpl
import com.enmanuelbergling.ktormovies.data.source.preferences.datastore.PreferencesDSImpl
import com.enmanuelbergling.ktormovies.data.source.preferences.datastore.UserPreferenceDSImpl
import com.enmanuelbergling.core.domain.datasource.preferences.AuthPreferenceDS
import com.enmanuelbergling.core.domain.datasource.preferences.PreferencesDS
import com.enmanuelbergling.core.domain.datasource.preferences.UserPreferenceDS
import org.koin.dsl.module

fun localModule(context: Context) = module {
    single<PreferencesDS> { PreferencesDSImpl(context) }
    single<AuthPreferenceDS> { AuthPreferenceDSImpl(context) }
    single<UserPreferenceDS> { UserPreferenceDSImpl(context) }
}