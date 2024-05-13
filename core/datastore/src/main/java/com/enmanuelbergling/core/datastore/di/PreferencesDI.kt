package com.enmanuelbergling.core.datastore.di

import android.content.Context
import com.enmanuelbergling.core.datastore.AuthPreferenceDSImpl
import com.enmanuelbergling.core.datastore.PreferencesDSImpl
import com.enmanuelbergling.core.datastore.UserPreferenceDSImpl
import com.enmanuelbergling.core.domain.datasource.preferences.AuthPreferenceDS
import com.enmanuelbergling.core.domain.datasource.preferences.PreferencesDS
import com.enmanuelbergling.core.domain.datasource.preferences.UserPreferenceDS
import org.koin.dsl.module

fun preferencesModule(context: Context) = module {
    single<PreferencesDS> { PreferencesDSImpl(context) }

    single<AuthPreferenceDS> { AuthPreferenceDSImpl(context) }

    single<UserPreferenceDS> { UserPreferenceDSImpl(context) }
}