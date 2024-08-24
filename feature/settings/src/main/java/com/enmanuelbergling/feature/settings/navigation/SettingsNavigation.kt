package com.enmanuelbergling.feature.settings.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.navigation
import com.enmanuelbergling.core.ui.components.topComposable
import com.enmanuelbergling.feature.settings.home.SettingsRoute
import kotlinx.serialization.Serializable


@Serializable
data object SettingsGraphDestination

@Serializable
data object SettingsDestination

fun NavHostController.navigateToSettingsGraph(navOptions: NavOptions?=null) {
    navigate(SettingsGraphDestination, navOptions)
}

fun NavGraphBuilder.settingsGraph(onBack: ()->Unit, onLogin: ()->Unit) {
    navigation<SettingsGraphDestination>(startDestination = SettingsDestination) {
        topComposable<SettingsDestination> {
            SettingsRoute(onBack, onLogin)
        }
    }
}