package com.enmanuelbergling.feature.settings.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
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
        composable<SettingsDestination> {
            SettingsRoute(onBack, onLogin)
        }
    }
}