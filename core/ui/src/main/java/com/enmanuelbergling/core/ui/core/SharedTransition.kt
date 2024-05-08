@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.enmanuelbergling.core.ui.core

import androidx.compose.animation.BoundsTransform
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf

val LocalSharedTransitionScope: ProvidableCompositionLocal<SharedTransitionScope?> = compositionLocalOf { null }

val BoundsTransition = BoundsTransform { _, _ -> spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow) }

