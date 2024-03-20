package com.enmanuelbergling.core.ui.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ModeNight
import androidx.compose.material.icons.rounded.WbSunny
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.vector.ImageVector
import com.enmanuelbergling.core.model.core.NetworkException
import com.enmanuelbergling.core.model.settings.DarkTheme
import com.enmanuelbergling.core.ui.R

val DarkTheme.icon: ImageVector
    @Composable
    @ReadOnlyComposable
    get() = when (this) {
        DarkTheme.No -> Icons.Rounded.WbSunny
        DarkTheme.Yes -> Icons.Rounded.ModeNight
        DarkTheme.System -> if (isSystemInDarkTheme()) Icons.Rounded.ModeNight else Icons.Rounded.WbSunny
    }

val NetworkException.messageResource: Int
    get() = when(this){
        NetworkException.AuthorizationException -> R.string.user_unauthorized_message
        NetworkException.DefaultException -> R.string.default_net_exception_message
        NetworkException.ReadTimeOutException -> R.string.net_time_out_exception_message
    }