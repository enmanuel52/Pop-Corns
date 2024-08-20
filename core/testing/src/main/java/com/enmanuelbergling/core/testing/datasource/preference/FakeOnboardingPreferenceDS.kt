package com.enmanuelbergling.core.testing.datasource.preference

import com.enmanuelbergling.core.domain.datasource.preferences.OnboardingPreferenceDS
import kotlinx.coroutines.flow.flow

class FakeOnboardingPreferenceDS: OnboardingPreferenceDS {
    override fun isOnboarding() = flow { emit(false) }

    override suspend fun finishOnboarding() =Unit
}