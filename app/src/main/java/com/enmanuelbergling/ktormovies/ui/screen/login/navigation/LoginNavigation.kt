package com.enmanuelbergling.ktormovies.ui.screen.login.navigation

import com.enmanuelbergling.ktormovies.ui.screen.login.LoginRoute
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.RouteBuilder
import moe.tlaster.precompose.navigation.transition.NavTransition

const val LOGIN_ROUTE = "login_route"

fun Navigator.navigateToLoginScreen(navOptions: NavOptions? = null) {
    navigate("/$LOGIN_ROUTE", navOptions)
}

fun RouteBuilder.loginScreen(onLoginSucceed: () -> Unit) {
    scene(
        "/$LOGIN_ROUTE", navTransition = NavTransition()
    ) {
        LoginRoute(onLoginSucceed)
    }
}