package com.enmanuelbergling.ktormovies.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enmanuelbergling.core.domain.usecase.settings.GetDarkThemeUC
import com.enmanuelbergling.core.domain.usecase.settings.SetDarkThemeUC
import com.enmanuelbergling.core.domain.usecase.user.GetSavedUserUC
import com.enmanuelbergling.core.domain.usecase.user.LogoutUC
import com.enmanuelbergling.core.model.settings.DarkTheme
import com.enmanuelbergling.core.model.user.UserDetails
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CornTimeVM(
    getDarkThemeUC: GetDarkThemeUC,
    private val setDarkThemeUC: SetDarkThemeUC,
    private val getSavedUserUC: GetSavedUserUC,
    private val logoutUC: LogoutUC,
) : ViewModel() {

    val darkTheme = getDarkThemeUC()

    val userDetails = getSavedUserUC().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        UserDetails()
    )

    fun setDarkTheme(darkTheme: DarkTheme) = viewModelScope.launch {
        setDarkThemeUC(darkTheme)
    }

    fun logout() = viewModelScope.launch {
        logoutUC()
    }
}