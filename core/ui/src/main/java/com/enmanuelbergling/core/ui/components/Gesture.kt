package com.enmanuelbergling.core.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Favorite
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

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
    visible: Boolean,
    modifier: Modifier = Modifier,
    onDismissFromStartToEnd: (() -> Unit)? = null,
    onDismissFromEndToStart: (() -> Unit)? = null,
    containerColorDismissFromStart: Color = Color.Transparent,
    containerColorDismissFromEnd: Color = MaterialTheme.colorScheme.errorContainer,
    shape: CornerBasedShape = CircleShape,
    backgroundIcon: @Composable (SwipeToDismissBoxValue) -> Unit = {
        if (it == SwipeToDismissBoxValue.EndToStart)
            Icon(Icons.Rounded.Delete, contentDescription = "delete")
    },
    spaceBetween: Dp = 2.dp,
    content: @Composable RowScope.() -> Unit,
) {
    val swipeToDismissState = rememberSwipeToDismissBoxState(
        initialValue = SwipeToDismissBoxValue.Settled,
        positionalThreshold = { distance: Float -> distance * 0.4f }
    )

    var swipeToDismissOffset by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(swipeToDismissState) {
        runCatching {
            snapshotFlow { swipeToDismissState.requireOffset() }.collect {
                swipeToDismissOffset = it
            }
        }
    }
    LaunchedEffect(visible) {
        if (visible) runCatching {
            swipeToDismissState.snapTo(SwipeToDismissBoxValue.Settled)
        }
    }

    AnimatedVisibility(
        visible,
        modifier = modifier,
        exit = shrinkVertically(),
        enter = expandVertically() + fadeIn(),
    ) {
        SwipeToDismissBox(
            state = swipeToDismissState,
            backgroundContent = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .drawBehind {
                            val topLeftX =
                                if (swipeToDismissOffset < 0) size.width + spaceBetween.toPx() + swipeToDismissOffset
                                else 0f
                            val offset = Offset(x = topLeftX.coerceIn(0f, size.width), y = 0f)
                            val size = if (swipeToDismissOffset < 0) size.offsetSize(offset)
                            else Size(swipeToDismissOffset - spaceBetween.toPx(), size.height)
                            drawRoundRect(
                                color = when (swipeToDismissState.dismissDirection) {
                                    SwipeToDismissBoxValue.StartToEnd -> containerColorDismissFromStart
                                    SwipeToDismissBoxValue.EndToStart -> containerColorDismissFromEnd
                                    SwipeToDismissBoxValue.Settled -> Color.Transparent
                                },
                                topLeft = offset,
                                size = size,
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
                    SwipeToDismissBoxValue.StartToEnd -> onDismissFromStartToEnd?.invoke()
                    SwipeToDismissBoxValue.EndToStart -> onDismissFromEndToStart?.invoke()
                    SwipeToDismissBoxValue.Settled -> Unit
                }
            },
            content = content
        )
    }
}

private fun Size.offsetSize(offset: Offset): Size =
    Size(this.width - offset.x, this.height - offset.y)

private fun swipeToDismissAlignment(swipeToDismissState: SwipeToDismissBoxState): Alignment =
    when (swipeToDismissState.dismissDirection) {
        SwipeToDismissBoxValue.StartToEnd -> Alignment.CenterStart
        SwipeToDismissBoxValue.EndToStart -> Alignment.CenterEnd
        SwipeToDismissBoxValue.Settled -> Alignment.CenterEnd
    }

private const val MAX_ROTATION_DEGREES = 18f

/**
 * A Tinder-like, 2D draggable swipe-to-dismiss container.
 *
 * The [content] can be dragged freely in both axis as far as the user wants. While dragging, the
 * card rotates on the Z axis proportionally to its horizontal offset. The sign of the rotation
 * depends on where the user grabbed the card vertically: when swiping from start to end the
 * rotation is positive if the card is held closer to the top and negative otherwise; when swiping
 * from end to start the behaviour is mirrored.
 *
 * A directional stamp shows up on the card's far top corner depending on the swipe direction:
 * [startToEndIcon] on the top start while swiping towards the end and [endToStartIcon] on the top
 * end while swiping towards the start; the further the card is dragged the more opaque the stamp
 * becomes.
 *
 * @param visible whether the card is shown. It is wrapped in an [AnimatedVisibility] so toggling it
 * animates the whole content in and out.
 * @param onDismissFromStartToEnd invoked once the card is flung past the threshold towards the end,
 * `null` disables that direction (and its stamp).
 * @param onDismissFromEndToStart invoked once the card is flung past the threshold towards the
 * start, `null` disables that direction (and its stamp).
 * @param dismissThreshold fraction of the card width the user must drag past to trigger a dismiss.
 * @param startToEndIcon stamp displayed on the top start corner while swiping towards the end.
 * @param endToStartIcon stamp displayed on the top end corner while swiping towards the start.
 */
@Composable
fun TinderSwipeToDismissContainer(
    visible: Boolean,
    modifier: Modifier = Modifier,
    onDismissFromStartToEnd: (() -> Unit)? = null,
    onDismissFromEndToStart: (() -> Unit)? = null,
    dismissThreshold: Float = 0.4f,
    startToEndIcon: @Composable () -> Unit = { FavoriteIcon() },
    endToStartIcon: @Composable () -> Unit = { IgnoreIcon() },
    content: @Composable () -> Unit,
) {
    val windowInfo = LocalWindowInfo.current
    val scope = rememberCoroutineScope()
    val offset = remember { Animatable(Offset.Zero, Offset.VectorConverter) }
    var cardSize by remember { mutableStateOf(IntSize.Zero) }
    var grabbedFromTop by remember { mutableStateOf(true) }

    LaunchedEffect(visible) {
        if (visible) offset.animateTo(Offset.Zero)
    }

    AnimatedVisibility(
        visible = visible,
        modifier = modifier,
        exit = shrinkVertically(),
        enter = EnterTransition.None,
    ) {
        // Translation, rotation and the corner stamps live here so they move and rotate together
        // with the content.
        Box(
            modifier = Modifier
                .onSizeChanged { cardSize = it }
                .graphicsLayer {
                    translationX = offset.value.x
                    translationY = offset.value.y
                    rotationZ = if (cardSize.width == 0) 0f
                    else (offset.value.x / cardSize.width).coerceIn(-1f, 1f) *
                            MAX_ROTATION_DEGREES * if (grabbedFromTop) 1f else -1f
                }
                .pointerInput(Unit) {
                    awaitEachGesture {
                        // onDragStarted
                        val down = awaitFirstDown(requireUnconsumed = false)
                        grabbedFromTop = down.position.y < cardSize.height / 2f
                        var horizontallyDragged: Boolean? = null

                        // onDrag
                        do {
                            val event = awaitPointerEvent()
                            val change = event.changes.firstOrNull { it.id == down.id } ?: break
                            val posChange = change.positionChange()

                            if (horizontallyDragged == null && posChange != Offset.Zero) {
                                horizontallyDragged = posChange.x.absoluteValue > posChange.y.absoluteValue
                            }

                            if (horizontallyDragged == true) {
                                change.consume()
                                scope.launch { offset.snapTo(offset.value + posChange) }
                            }
                            // horizontallyDragged == false → don't consume, forwards to parent
                        } while (event.changes.any { it.pressed })

                        // onDragEnd
                        if (horizontallyDragged == true) {
                            val x = offset.value.x
                            val threshold = cardSize.width * dismissThreshold
                            scope.launch {
                                when {
                                    onDismissFromStartToEnd != null && x > threshold -> {
                                        offset.animateTo(
                                            Offset(
                                                windowInfo.containerSize.width * 2f,
                                                offset.value.y
                                            )
                                        )
                                        onDismissFromStartToEnd()
                                    }

                                    onDismissFromEndToStart != null && x < -threshold -> {
                                        offset.animateTo(
                                            Offset(
                                                -windowInfo.containerSize.width * 2f,
                                                offset.value.y
                                            )
                                        )
                                        onDismissFromEndToStart()
                                    }

                                    else -> offset.animateTo(Offset.Zero)
                                }
                            }
                        }
                    }
                }
        ) {
            content()

            if (onDismissFromStartToEnd != null) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .graphicsLayer {
                            alpha = if (cardSize.width == 0) 0f
                            else (offset.value.x / (cardSize.width * dismissThreshold))
                                .coerceIn(0f, 1f)
                        }
                ) { startToEndIcon() }
            }

            if (onDismissFromEndToStart != null) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .graphicsLayer {
                            alpha = if (cardSize.width == 0) 0f
                            else (-offset.value.x / (cardSize.width * dismissThreshold))
                                .coerceIn(0f, 1f)
                        }
                ) { endToStartIcon() }
            }
        }
    }
}

@Composable
private fun FavoriteIcon() {
    Icon(
        imageVector = Icons.Rounded.Favorite,
        contentDescription = "like",
        modifier = Modifier
            .padding(8.dp)
            .size(48.dp),
        tint = Color.Red
    )
}

@Composable
private fun IgnoreIcon() {
    Icon(
        imageVector = Icons.Rounded.Clear,
        contentDescription = "ignore",
        modifier = Modifier
            .padding(8.dp)
            .size(48.dp),
        tint = Color.White
    )
}

