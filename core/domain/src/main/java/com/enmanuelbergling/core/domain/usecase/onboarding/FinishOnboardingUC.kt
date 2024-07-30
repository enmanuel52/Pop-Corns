package com.enmanuelbergling.core.domain.usecase.onboarding

import com.enmanuelbergling.core.domain.datasource.preferences.OnboardingPreferenceDS

class FinishOnboardingUC(
    private val preferenceDS: OnboardingPreferenceDS,
) {
    suspend operator fun invoke() = preferenceDS.finishOnboarding()
}