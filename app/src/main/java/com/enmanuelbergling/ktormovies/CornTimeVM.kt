package com.enmanuelbergling.ktormovies

import com.enmanuelbergling.ktormovies.domain.model.settings.DarkTheme
import com.enmanuelbergling.ktormovies.domain.model.user.UserDetails
import com.enmanuelbergling.ktormovies.domain.usecase.settings.GetDarkThemeUC
import com.enmanuelbergling.ktormovies.domain.usecase.settings.SetDarkThemeUC
import com.enmanuelbergling.ktormovies.domain.usecase.user.GetSavedUserUC
import com.enmanuelbergling.ktormovies.domain.usecase.user.UserLogoutUC
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

class CornTimeVM(
    getDarkThemeUC: GetDarkThemeUC,
    private val setDarkThemeUC: SetDarkThemeUC,
    private val getSavedUserUC: GetSavedUserUC,
    private val userLogoutUC: UserLogoutUC,
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
        userLogoutUC()
    }
}