package com.enmanuelbergling.feature.settings.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enmanuelbergling.core.domain.usecase.settings.GetDarkThemeUC
import com.enmanuelbergling.core.domain.usecase.settings.SetDarkThemeUC
import com.enmanuelbergling.core.domain.usecase.user.GetSavedUserUC
import com.enmanuelbergling.core.domain.usecase.user.LogoutUC
import com.enmanuelbergling.core.model.settings.DarkTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsVM(
    private val setDarkThemeUC: SetDarkThemeUC,
    getDarkThemeUC: GetDarkThemeUC,
    private val logoutUC: LogoutUC,
    getSavedUserUC: GetSavedUserUC,
) : ViewModel() {
    val userState = getSavedUserUC()

    val darkThemeState = getDarkThemeUC()

    private val _shouldGoOutState = MutableStateFlow(false)
    val shouldGoOutState get() = _shouldGoOutState.asStateFlow()

    fun onDarkTheme(darkTheme: DarkTheme) = viewModelScope.launch {
        setDarkThemeUC(darkTheme)
    }

    fun onLogout() = viewModelScope.launch {
        logoutUC()
        _shouldGoOutState.update { true }
    }
}