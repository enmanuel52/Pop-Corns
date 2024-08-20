package com.enmanuelbergling.core.ui.components.walkthrough.model

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp

/**
 * To define how the indicator will look like
 * */
sealed interface IndicatorStyle {

    data class Step(
        val spaceBetween: Dp = DimenTokens.IndicatorSize,
        val stepSize: Dp = DimenTokens.IndicatorSize,
        val shape: Shape = CircleShape,
    ) : IndicatorStyle

    data class Shift(
        val spaceBetween: Dp = DimenTokens.IndicatorSize,
        val stepSize: Dp = DimenTokens.IndicatorSize,
        val shape: Shape = CircleShape,
    ) : IndicatorStyle

}