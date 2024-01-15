package com.enmanuelbergling.ktormovies

import com.enmanuelbergling.ktormovies.domain.model.settings.DarkTheme
import com.enmanuelbergling.ktormovies.domain.usecase.settings.GetDarkThemeUC
import com.enmanuelbergling.ktormovies.domain.usecase.settings.SetDarkThemeUC
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

class CornTimeVM(
//    getDarkThemeUC: GetDarkThemeUC,
//    private val setDarkThemeUC: SetDarkThemeUC,
) : ViewModel() {

    val darkTheme = /*getDarkThemeUC()*/flow<DarkTheme> {  }

    fun setDarkTheme(darkTheme: DarkTheme) = viewModelScope.launch {
//        setDarkThemeUC(darkTheme)
    }
}