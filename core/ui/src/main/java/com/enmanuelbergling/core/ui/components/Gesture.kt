package com.enmanuelbergling.core.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.enmanuelbergling.core.ui.core.dimen
import kotlinx.coroutines.delay

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeToDismissContainer(
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    animationDuration: Int = 600,
    shape: Shape = RectangleShape,
    content: @Composable RowScope.() -> Unit,
) {
    var isOut by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = isOut) {
        if (isOut) {
            delay(animationDuration.toLong())
            onDelete()
        }
    }

    AnimatedVisibility(
        visible = !isOut,
        exit = shrinkVertically(
            tween(animationDuration),
            Alignment.Top
        ),
        modifier = Modifier
            .clip(shape)
            .then(modifier)
    ) {
        val swipeToDismissState = rememberSwipeToDismissBoxState(
            initialValue = SwipeToDismissBoxValue.Settled,
            confirmValueChange = {
                if (it == SwipeToDismissBoxValue.EndToStart) {
                    isOut = true

                    true
                } else false
            },
            positionalThreshold = { distance: Float -> distance * 0.4f },
        )

        SwipeToDismissBox(
            state = swipeToDismissState,
            backgroundContent = { DismissBackground() },
            enableDismissFromEndToStart = enabled,
            enableDismissFromStartToEnd = false,
            content = content
        )
    }
}

@Composable
private fun DismissBackground() {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.errorContainer)
            .padding(end = MaterialTheme.dimen.mediumSmall),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Delete", style = MaterialTheme.typography.titleSmall)

        Spacer(modifier = Modifier.width(MaterialTheme.dimen.mediumSmall))

        Icon(
            imageVector = Icons.Rounded.Delete,
            contentDescription = "delete icon",
            tint = MaterialTheme.colorScheme.onErrorContainer
        )
    }
}