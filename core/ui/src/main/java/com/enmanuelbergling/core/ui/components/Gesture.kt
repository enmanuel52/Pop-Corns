package com.enmanuelbergling.core.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeOut
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.absoluteValue
import kotlin.time.Duration.Companion.milliseconds

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

        Box(
            modifier = Modifier
                .graphicsLayer {
                    translationX = state.requireOffset()
                }
                .anchoredDraggable(state, Orientation.Horizontal)) {
            topContent()
        }
    }
}

/**
 * @param spaceBetween [content] and background
 * */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeToDismissContainer(
    modifier: Modifier = Modifier,
    onDismissFromStartToEnd: (() -> Unit)? = null,
    onDismissFromEndToStart: (() -> Unit)? = null,
    exitTransition: ExitTransition = shrinkOut() + fadeOut(),
    containerColorDismissFromStart: Color = MaterialTheme.colorScheme.primaryContainer,
    containerColorDismissFromEnd: Color = MaterialTheme.colorScheme.errorContainer,
    shape: CornerBasedShape = CircleShape,
    backgroundIcon: @Composable (SwipeToDismissBoxValue) -> Unit = {
        if (it == SwipeToDismissBoxValue.EndToStart)
            Icon(Icons.Rounded.Delete, contentDescription = "delete")
    },
    spaceBetween: Dp = 2.dp,
    content: @Composable RowScope.() -> Unit,
) {
    var onDismissedFromStart by remember {
        mutableStateOf(false)
    }
    var onDismissedFromEnd by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(onDismissedFromStart, onDismissedFromEnd) {
        delay(300.milliseconds)
        if (onDismissedFromStart) onDismissFromStartToEnd?.invoke()

        if (onDismissedFromEnd) onDismissFromEndToStart?.invoke()
    }

    AnimatedVisibility(
        visible = !onDismissedFromStart && !onDismissedFromEnd,
        exit = exitTransition,
        modifier = modifier
    ) {
        val swipeToDismissState = rememberSwipeToDismissBoxState(
            initialValue = SwipeToDismissBoxValue.Settled,
            positionalThreshold = { distance: Float -> distance * 0.4f })

        var swipeToDismissOffset by remember { mutableFloatStateOf(0f) }

        LaunchedEffect(swipeToDismissState) {
            snapshotFlow { swipeToDismissState.requireOffset() }.collect {
                swipeToDismissOffset = it
            }
        }

        SwipeToDismissBox(
            state = swipeToDismissState,
            backgroundContent = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .drawBehind {
                            val topLeftX = size.width + spaceBetween.toPx() + swipeToDismissOffset
                            drawRoundRect(
                                color = when (swipeToDismissState.dismissDirection) {
                                    SwipeToDismissBoxValue.StartToEnd -> containerColorDismissFromStart
                                    SwipeToDismissBoxValue.EndToStart -> containerColorDismissFromEnd
                                    SwipeToDismissBoxValue.Settled -> Color.Transparent
                                },
                                topLeft = Offset(x = topLeftX.coerceIn(0f, size.width), y = 0f),
                                cornerRadius = CornerRadius(
                                    x = shape.topStart.toPx(
                                        shapeSize = size,
                                        density = Density(density)
                                    )
                                )
                            )
                        },
                    contentAlignment = swipeToDismissAlignment(swipeToDismissState),
                ) {
                    Box(modifier = Modifier.graphicsLayer {
                        val width = size.width + spaceBetween.toPx()
                        if (swipeToDismissOffset.absoluteValue > width) {
                            val centerOffset =
                                if (swipeToDismissOffset < 0) +width / 2 else -width / 2
                            this.translationX = swipeToDismissOffset / 2 + centerOffset
                        }
                    }) {
                        backgroundIcon(swipeToDismissState.dismissDirection)
                    }
                }
            },
            enableDismissFromEndToStart = onDismissFromEndToStart != null,
            enableDismissFromStartToEnd = onDismissFromStartToEnd != null,
            onDismiss = {
                when (it) {
                    SwipeToDismissBoxValue.StartToEnd -> onDismissedFromStart = true
                    SwipeToDismissBoxValue.EndToStart -> onDismissedFromEnd = true
                    SwipeToDismissBoxValue.Settled -> Unit
                }
            },
            content = content
        )
    }
}

private fun swipeToDismissAlignment(swipeToDismissState: SwipeToDismissBoxState): Alignment =
    when (swipeToDismissState.dismissDirection) {
        SwipeToDismissBoxValue.StartToEnd -> Alignment.CenterStart
        SwipeToDismissBoxValue.EndToStart -> Alignment.CenterEnd
        SwipeToDismissBoxValue.Settled -> Alignment.CenterEnd
    }