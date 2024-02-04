package com.enmanuelbergling.ktormovies.ui.screen.login.navigation

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ModeNight
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.enmanuelbergling.ktormovies.domain.TAG
import com.enmanuelbergling.ktormovies.ui.components.CtiTextField
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

        var text by remember {
            mutableStateOf("")
        }

        LoginRoute(onLoginSucceed)

        CtiTextField(
            text = text,
            onTextChange = { text = it },
            hint = "Whateve",
            leadingIcon = Icons.Rounded.ModeNight
        )
    }
}