package com.enmanuelbergling.ktormovies.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enmanuelbergling.core.domain.usecase.onboarding.FinishOnboardingUC
import com.enmanuelbergling.core.domain.usecase.onboarding.IsOnboardingUC
import com.enmanuelbergling.core.domain.usecase.settings.GetDarkThemeUC
import com.enmanuelbergling.core.domain.usecase.settings.GetDynamicColorUC
import com.enmanuelbergling.core.domain.usecase.user.GetSavedUserUC
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CornTimeVM(
    getDarkThemeUC: GetDarkThemeUC,
    getDynamicColorUC: GetDynamicColorUC,
    private val getSavedUserUC: GetSavedUserUC,
    isOnboardingUC: IsOnboardingUC,
    private val finishOnboardingUC: FinishOnboardingUC,
) : ViewModel() {

    val darkTheme = getDarkThemeUC()

    val dynamicColor = getDynamicColorUC()

    val userDetails = getSavedUserUC()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            null,
        )

    val isOnboarding = isOnboardingUC()

    fun finishOnboarding() = viewModelScope.launch {
        finishOnboardingUC()
    }
}