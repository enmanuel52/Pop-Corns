package com.enmanuelbergling.core.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import kotlin.math.roundToInt

/**
 * @param rotation in degrees
 * */
fun Modifier.listItemWindAnimation(
    isScrollingForward: Boolean,
    orientation: Orientation = Orientation.Vertical,
    rotation: Float = 15f,
    durationMillis: Int = 400,
): Modifier = composed {
    val cameraAnimatable = remember { Animatable(initialValue = 7.0f) }
    val scaleAnimatable = remember { Animatable(initialValue = 0.8f) }
    val rotateAnimatable = remember(isScrollingForward) {
        Animatable(initialValue = if (isScrollingForward) rotation else -rotation)
    }

    // Observe changes to scrollDirection and update rotateAnimatable accordingly
    LaunchedEffect(isScrollingForward) {
        // Animate from 0 to either 60 or -60
        rotateAnimatable.animateTo(
            if (isScrollingForward) rotation else -rotation,
            animationSpec = tween(
                durationMillis = 100,
                easing = CubicBezierEasing(0f, 0.5f, 0.5f, 1f)
            )
        )
        // Animate from either 60 or -60 to 0
        rotateAnimatable.animateTo(
            targetValue = 0f,
            animationSpec = tween(
                durationMillis = durationMillis,
                easing = CubicBezierEasing(0f, 0.5f, 0.5f, 1f)
            )
        )
    }

    // Other animations (camera and scale) remain the same
    LaunchedEffect(Unit) {
        cameraAnimatable.animateTo(
            8.0f,
            animationSpec = tween(
                durationMillis = durationMillis.times(1.4f).roundToInt(),
                easing = CubicBezierEasing(0f, 0.5f, 0.5f, 1f)
            )
        )
    }

    LaunchedEffect(Unit) {
        scaleAnimatable.animateTo(
            1f,
            animationSpec = tween(
                durationMillis = durationMillis.times(1.4f).roundToInt(),
                easing = CubicBezierEasing(0f, 0.5f, 0.5f, 1f)
            )
        )
    }

    graphicsLayer {
        scaleX = scaleAnimatable.value
        scaleY = scaleAnimatable.value
        cameraDistance = cameraAnimatable.value
        when (orientation) {
            Orientation.Vertical -> {
                rotationX = -rotateAnimatable.value
            }

            Orientation.Horizontal -> {
                rotationY = rotateAnimatable.value
            }
        }
    }
}