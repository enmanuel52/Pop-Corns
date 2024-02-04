package com.enmanuelbergling.ktormovies.ui.screen.login.navigation

import com.enmanuelbergling.ktormovies.ui.screen.login.LoginRoute
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.RouteBuilder
import moe.tlaster.precompose.navigation.transition.NavTransition

const val LOGIN_GRAPH_ROUTE = "/login_graph_route"
private const val LOGIN_ROUTE = "login_route"

fun Navigator.navigateToLoginScreen(navOptions: NavOptions? = null) {
    navigate(LOGIN_GRAPH_ROUTE, navOptions)
}

fun RouteBuilder.loginRoute(onLoginSucceed: () -> Unit) {
    group(LOGIN_GRAPH_ROUTE, "/$LOGIN_ROUTE") {
        scene(
            "/$LOGIN_ROUTE", navTransition = NavTransition()
        ) {
            LoginRoute(onLoginSucceed)
        }
    }
}