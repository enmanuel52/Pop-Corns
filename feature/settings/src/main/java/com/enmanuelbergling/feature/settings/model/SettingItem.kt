package com.enmanuelbergling.feature.settings.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Forest
import androidx.compose.material.icons.rounded.NightsStay
import androidx.compose.ui.graphics.vector.ImageVector
import com.enmanuelbergling.core.ui.R

internal data class SettingItem(
   @DrawableRes val iconRes: Int,
    @StringRes val label: Int,
) {
    companion object {
        val DarkMode = SettingItem(
           R.drawable.moon,
            label = R.string.dark_mode
        )

        val DynamicColor = SettingItem(
            iconRes = R.drawable.paint_brush,
            label = R.string.dynamic_color
        )
    }
}
