package com.enmanuelbergling.core.ui.components.walkthrough.model

import androidx.annotation.IntRange

/**
 * Scroll animation
 * */
sealed interface WalkScrollStyle {
    data object Normal : WalkScrollStyle

    /**
     *  @param boxAngle around the rotation in performed, the bigger the value the louder the effect
     *  @param reverse to define whether you are in or out of the cube
     *  */
    data class Instagram(
        @IntRange(10, 90) val boxAngle: Int = 30,
        val reverse: Boolean = false,
    ): WalkScrollStyle
}