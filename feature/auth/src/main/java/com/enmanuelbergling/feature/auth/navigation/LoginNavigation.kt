package com.enmanuelbergling.feature.auth.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.enmanuelbergling.feature.auth.LoginRoute
import kotlinx.serialization.Serializable

@Serializable
data object LoginDestination

fun NavHostController.navigateToLoginScreen(navOptions: NavOptions? = null) {
    navigate(LoginDestination, navOptions)
}

fun NavGraphBuilder.loginScreen(onLoginSucceed: () -> Unit, onBack: ()->Unit) {
    composable<LoginDestination> {
        LoginRoute(onLoginSucceed,onBack)
    }
}