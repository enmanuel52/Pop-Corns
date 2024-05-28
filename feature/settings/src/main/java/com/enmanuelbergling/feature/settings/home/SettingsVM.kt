package com.enmanuelbergling.feature.settings.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enmanuelbergling.core.domain.usecase.settings.GetDarkThemeUC
import com.enmanuelbergling.core.domain.usecase.settings.GetDynamicThemeUC
import com.enmanuelbergling.core.domain.usecase.settings.SetDarkThemeUC
import com.enmanuelbergling.core.domain.usecase.settings.SetDynamicThemeUC
import com.enmanuelbergling.core.domain.usecase.user.GetSavedUserUC
import com.enmanuelbergling.core.domain.usecase.user.LogoutUC
import com.enmanuelbergling.feature.settings.model.SettingMenuUi
import com.enmanuelbergling.feature.settings.model.SettingUiEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsVM(
    private val setDarkThemeUC: SetDarkThemeUC,
    getDarkThemeUC: GetDarkThemeUC,
    private val logoutUC: LogoutUC,
    getSavedUserUC: GetSavedUserUC,
    getDynamicThemeUC: GetDynamicThemeUC,
    private val setDynamicThemeUC: SetDynamicThemeUC,
) : ViewModel() {
    val userState = getSavedUserUC()

    val darkThemeState = getDarkThemeUC()

    val dynamicThemeState = getDynamicThemeUC()

    private val _menuVisibleState = MutableStateFlow(SettingMenuUi())
    val menuVisibleState get() = _menuVisibleState.asStateFlow()

    fun onEvent(event: SettingUiEvent) = viewModelScope.launch {
        when (event) {
            is SettingUiEvent.DarkThemeEvent -> {
                setDarkThemeUC(event.theme)
            }

            SettingUiEvent.DarkThemeMenu -> {
                _menuVisibleState.update { it.copy(darkThemeVisible = !it.darkThemeVisible) }
            }

            is SettingUiEvent.DynamicTheme -> {
                setDynamicThemeUC(event.active)
            }

            SettingUiEvent.DynamicThemeMenu -> {
                _menuVisibleState.update { it.copy(dynamicThemeVisible = !it.dynamicThemeVisible) }
            }

            SettingUiEvent.Logout -> {
                logoutUC()
            }
        }
    }
}