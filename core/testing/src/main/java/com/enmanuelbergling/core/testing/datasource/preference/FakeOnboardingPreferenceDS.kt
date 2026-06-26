package com.enmanuelbergling.core.testing.datasource.preference

import com.enmanuelbergling.core.domain.datasource.preferences.OnboardingPreferenceDS
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FakeOnboardingPreferenceDS: OnboardingPreferenceDS {
    private val _isOnboarding = MutableStateFlow(false)

    override fun isOnboarding() = _isOnboarding.asStateFlow()

    override suspend fun finishOnboarding() {
        _isOnboarding.update { false }
    }
}