package com.enmanuelbergling.ktormovies.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enmanuelbergling.core.domain.usecase.settings.GetDarkThemeUC
import com.enmanuelbergling.core.domain.usecase.settings.GetDynamicColorUC
import com.enmanuelbergling.core.domain.usecase.user.GetSavedUserUC
import com.enmanuelbergling.core.model.user.UserDetails
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.stateIn

class CornTimeVM(
    getDarkThemeUC: GetDarkThemeUC,
    getDynamicColorUC: GetDynamicColorUC,
    private val getSavedUserUC: GetSavedUserUC,
) : ViewModel() {

    val darkTheme = getDarkThemeUC()

    val dynamicColor = getDynamicColorUC()

    val userDetails = getSavedUserUC()
        .stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        null,
    )
}