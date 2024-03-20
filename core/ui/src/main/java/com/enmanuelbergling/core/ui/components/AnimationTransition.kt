package com.enmanuelbergling.core.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Customized slide animation
 * @param visible decide when to show
 * @param fromDirection where slideIn comes from
 * */
@Composable
fun ShowUpFrom(
    visible: Boolean,
    fromDirection: FromDirection,
    modifier: Modifier = Modifier,
    content: @Composable AnimatedVisibilityScope.() -> Unit,
) {
    AnimatedVisibility(
        visible = visible,
        modifier = modifier,
        enter = fromDirection.enterSlideTransition(),
        exit = fromDirection.exitSlideTransition() + fadeOut(),
        content = content,
    )
}

@Composable
private fun FromDirection.enterSlideTransition() =
    when (this) {
        FromDirection.Top -> {
            slideInVertically(
                spring(
                    Spring.DampingRatioLowBouncy,
                    Spring.StiffnessLow
                )
            ) { -it }
        }

        FromDirection.End -> {
            slideInHorizontally(
                spring(
                    Spring.DampingRatioLowBouncy,
                    Spring.StiffnessLow
                )
            ) { it }
        }

        FromDirection.Bottom -> {
            slideInVertically(
                spring(
                    Spring.DampingRatioLowBouncy,
                    Spring.StiffnessLow
                )
            ) { it }
        }

        FromDirection.Start -> {
            slideInHorizontally(
                spring(
                    Spring.DampingRatioLowBouncy,
                    Spring.StiffnessLow
                )
            ) { -it }
        }
    }

@Composable
private fun FromDirection.exitSlideTransition() =
    when (this) {
        FromDirection.Top -> {
            slideOutVertically(
                spring(
                    Spring.DampingRatioLowBouncy,
                    Spring.StiffnessLow
                )
            ) { -it }
        }

        FromDirection.End -> {
            slideOutHorizontally(
                spring(
                    Spring.DampingRatioLowBouncy,
                    Spring.StiffnessLow
                )
            ) { it }
        }

        FromDirection.Bottom -> {
            slideOutVertically(
                spring(
                    Spring.DampingRatioLowBouncy,
                    Spring.StiffnessLow
                )
            ) { it }
        }

        FromDirection.Start -> {
            slideOutHorizontally(
                spring(
                    Spring.DampingRatioLowBouncy,
                    Spring.StiffnessLow
                )
            ) { -it }
        }
    }

enum class FromDirection {
    Top, End, Bottom, Start
}