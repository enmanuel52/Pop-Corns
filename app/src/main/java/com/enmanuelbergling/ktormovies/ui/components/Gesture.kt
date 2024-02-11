package com.enmanuelbergling.ktormovies.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
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

    val state = remember {
        AnchoredDraggableState(
            initialValue = DragState.Closed,
            anchors = anchors,
            positionalThreshold = { distance -> distance * 0.6f },
            velocityThreshold = { with(density) { 80.dp.toPx() } },
            animationSpec = spring()
        )
    }

    Box (modifier){
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