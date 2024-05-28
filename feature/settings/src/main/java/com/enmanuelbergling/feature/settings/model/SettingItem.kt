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
    @StringRes val label: Int,
) {
    companion object {
        val DarkMode = SettingItem(
            icon = Icons.Rounded.NightsStay,
            label = R.string.dark_mode
        )

        val DynamicMode = SettingItem(
            icon = Icons.Rounded.Forest,
            label = R.string.dynamic_mode
        )
    }
}
