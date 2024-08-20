package com.enmanuelbergling.core.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.enmanuelbergling.core.domain.datasource.preferences.OnboardingPreferenceDS
import kotlinx.coroutines.flow.map

class OnboardingPreferenceDSImpl(private val context: Context) : OnboardingPreferenceDS {
    private val Context.dataStore by preferencesDataStore("onboarding")

    private object Keys {
        val ONBOARDING = booleanPreferencesKey("onboarding")
    }

    override fun isOnboarding() = context.dataStore.data.map { it[Keys.ONBOARDING] ?: true }

    override suspend fun finishOnboarding() {
        context.dataStore.edit {
            it[Keys.ONBOARDING] = false
        }
    }
}