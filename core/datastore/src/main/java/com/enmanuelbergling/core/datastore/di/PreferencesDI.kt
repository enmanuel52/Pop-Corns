package com.enmanuelbergling.core.datastore.di

import android.content.Context
import com.enmanuelbergling.core.datastore.AuthPreferenceDSImpl
import com.enmanuelbergling.core.datastore.OnboardingPreferenceDSImpl
import com.enmanuelbergling.core.datastore.SearchSuggestionDSImpl
import com.enmanuelbergling.core.datastore.SettingsPreferencesDSImpl
import com.enmanuelbergling.core.datastore.UserPreferenceDSImpl
import com.enmanuelbergling.core.domain.datasource.preferences.AuthPreferenceDS
import com.enmanuelbergling.core.domain.datasource.preferences.OnboardingPreferenceDS
import com.enmanuelbergling.core.domain.datasource.preferences.SearchSuggestionDS
import com.enmanuelbergling.core.domain.datasource.preferences.SettingsPreferencesDS
import com.enmanuelbergling.core.domain.datasource.preferences.UserPreferenceDS
import org.koin.dsl.module

fun preferencesModule(context: Context) = module {
    single<SettingsPreferencesDS> { SettingsPreferencesDSImpl(context) }

    single<AuthPreferenceDS> { AuthPreferenceDSImpl(context) }

    single<UserPreferenceDS> { UserPreferenceDSImpl(context) }

    single<SearchSuggestionDS> { SearchSuggestionDSImpl(context) }

    single<OnboardingPreferenceDS> { OnboardingPreferenceDSImpl(context) }
}