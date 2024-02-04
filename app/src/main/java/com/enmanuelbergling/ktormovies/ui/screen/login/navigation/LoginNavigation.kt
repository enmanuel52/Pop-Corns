package com.enmanuelbergling.ktormovies.ui.screen.login.navigation

import com.enmanuelbergling.ktormovies.navigation.TopDestination
import com.enmanuelbergling.ktormovies.ui.screen.login.LoginRoute
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.PopUpTo
import moe.tlaster.precompose.navigation.RouteBuilder

const val LOGIN_ROUTE = "login_route"

fun Navigator.navigateToLoginScreen() {
    navigate(
        "/$LOGIN_ROUTE", options = NavOptions(
            popUpTo = PopUpTo(
                route = "/${TopDestination.Movie.route}",
                inclusive = true
            )
        )
    )
}

fun RouteBuilder.loginScreen(onLoginSucceed: () -> Unit) {
    scene(
        "/$LOGIN_ROUTE"
    ) {
        LoginRoute(onLoginSucceed)
    }
}