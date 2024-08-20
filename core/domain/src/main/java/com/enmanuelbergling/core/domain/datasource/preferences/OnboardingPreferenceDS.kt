package com.enmanuelbergling.core.domain.datasource.preferences

import kotlinx.coroutines.flow.Flow

interface OnboardingPreferenceDS {
    fun isOnboarding(): Flow<Boolean>

    suspend fun finishOnboarding()
}