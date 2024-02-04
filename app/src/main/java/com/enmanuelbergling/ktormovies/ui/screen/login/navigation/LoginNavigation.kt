package com.enmanuelbergling.ktormovies.ui.screen.login.navigation

import com.enmanuelbergling.ktormovies.ui.screen.login.LoginRoute
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.RouteBuilder

const val LOGIN_ROUTE = "/login_route"

fun Navigator.navigateToLoginScreen(navOptions: NavOptions? = null) {
    navigate(
        route = LOGIN_ROUTE, options = navOptions
    )
}

fun RouteBuilder.loginScreen(onLoginSucceed: () -> Unit) {
    scene(
        LOGIN_ROUTE
    ) {
        LoginRoute(onLoginSucceed)
    }
}