package com.enmanuelbergling.core.ui.components

import androidx.compose.animation.core.spring
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

enum class DragState {
    Open, Closed
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NewerDragListItem(
    bottomContentWidth: Float,
    bottomContent: @Composable BoxScope.() -> Unit,
    modifier: Modifier = Modifier,
    topContent: @Composable () -> Unit,
) {

    val anchors = DraggableAnchors {
        DragState.Open at bottomContentWidth
        DragState.Closed at 0f
    }

    val density = LocalDensity.current
    val decayAnimationSpec = rememberSplineBasedDecay<Float>()

    val state = remember {
        AnchoredDraggableState(
            initialValue = DragState.Closed,
            positionalThreshold = { distance: Float -> distance * 0.6f },
            velocityThreshold = { with(density) { 80.dp.toPx() } },
            decayAnimationSpec = decayAnimationSpec,
            snapAnimationSpec = spring(),
            anchors = anchors
        )
    }

    Box(modifier) {
        bottomContent()

        Box(modifier = Modifier
            .graphicsLayer {
                translationX = state.requireOffset()
            }
            .anchoredDraggable(state, Orientation.Horizontal)
        ) {
            topContent()
        }
    }
}