package com.enmanuelbergling.core.ui.components

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.IntSize
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlin.math.roundToInt

const val DurationMillis = 300

val TopDestinationEnterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition? =
    {
        slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start)
    }
val TopDestinationExitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition? =
    {
        slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End)
    }

/**
 * Wrapper for top destination screens with defined animations
 * */
inline fun <reified T : Any> NavGraphBuilder.topComposable(
    noinline content: @Composable() (AnimatedContentScope.(NavBackStackEntry) -> Unit),
) {
    composable<T>(
        content = content
    )
}