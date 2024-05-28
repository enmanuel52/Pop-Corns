package com.enmanuelbergling.feature.settings.model

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Forest
import androidx.compose.material.icons.rounded.NightsStay
import androidx.compose.material.icons.rounded.Volcano
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.enmanuelbergling.core.ui.R

data class SettingItem(
    val icon: ImageVector,
    val iconContainerColor: Color,
    @StringRes val label: Int,
) {
    companion object {
        val DarkMode = SettingItem(
            icon = Icons.Rounded.NightsStay,
            iconContainerColor = Color(0xFFBFE9FF),
            label = R.string.dark_mode
        )

        val DynamicMode = SettingItem(
            icon = Icons.Rounded.Forest,
            iconContainerColor = Color(0xFFBFFFC0),
            label = R.string.dynamic_mode
        )
    }
}
