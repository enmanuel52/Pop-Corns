package com.enmanuelbergling.core.domain.usecase.onboarding

import com.enmanuelbergling.core.domain.datasource.preferences.OnboardingPreferenceDS

class IsOnboardingUC(
    private val preferenceDS: OnboardingPreferenceDS,
) {
    operator fun invoke() = preferenceDS.isOnboarding()
}