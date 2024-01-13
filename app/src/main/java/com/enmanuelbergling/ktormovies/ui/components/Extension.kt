package com.enmanuelbergling.ktormovies.ui.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ModeNight
import androidx.compose.material.icons.rounded.WbSunny
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.vector.ImageVector
import com.enmanuelbergling.ktormovies.domain.model.settings.DarkTheme

val DarkTheme.icon: ImageVector
    @Composable
    @ReadOnlyComposable
    get() = when (this) {
        DarkTheme.No -> Icons.Rounded.WbSunny
        DarkTheme.Yes -> Icons.Rounded.ModeNight
        DarkTheme.System -> if (isSystemInDarkTheme()) Icons.Rounded.ModeNight else Icons.Rounded.WbSunny
    }