package com.enmanuelbergling.core.ui.components.walkthrough.model

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color

@Stable
data class WalkThroughColors(
    val containerColor: Color,
    val buttonContainerColor: Color,
    val buttonContentColor: Color,
    val activeIndicatorColor: Color,
    val inactiveIndicatorColor: Color,
) {
    internal fun indicator() = IndicatorColors(
        activeIndicatorColor = activeIndicatorColor,
        inactiveIndicatorColor = inactiveIndicatorColor,
    )

}

object WalkThroughDefaults {

    @Composable
    fun colors(
        containerColor: Color = MaterialTheme.colorScheme.background,
        buttonContainerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
        buttonContentColor: Color = MaterialTheme.colorScheme.background,
        activeIndicatorColor: Color = MaterialTheme.colorScheme.primary,
        inactiveIndicatorColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    ) = WalkThroughColors(
        containerColor = containerColor,
        buttonContainerColor = buttonContainerColor,
        buttonContentColor = buttonContentColor,
        activeIndicatorColor = activeIndicatorColor,
        inactiveIndicatorColor = inactiveIndicatorColor,
    )
}

@Stable
data class IndicatorColors(
    val activeIndicatorColor: Color,
    val inactiveIndicatorColor: Color,
)

object IndicatorDefaults {

    @Composable
    fun colors(
        activeIndicatorColor: Color = MaterialTheme.colorScheme.primary,
        inactiveIndicatorColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    ) = IndicatorColors(
        activeIndicatorColor = activeIndicatorColor,
        inactiveIndicatorColor = inactiveIndicatorColor,
    )
}