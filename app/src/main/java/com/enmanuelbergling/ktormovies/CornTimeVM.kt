package com.enmanuelbergling.ktormovies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enmanuelbergling.ktormovies.domain.model.settings.DarkTheme
import com.enmanuelbergling.ktormovies.domain.usecase.settings.GetDarkThemeUC
import com.enmanuelbergling.ktormovies.domain.usecase.settings.SetDarkThemeUC
import kotlinx.coroutines.launch

class CornTimeVM(
    getDarkThemeUC: GetDarkThemeUC,
    private val setDarkThemeUC: SetDarkThemeUC,
) : ViewModel() {

    val darkTheme = getDarkThemeUC()

    fun setDarkTheme(darkTheme: DarkTheme) = viewModelScope.launch {
        setDarkThemeUC(darkTheme)
    }
}