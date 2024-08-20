package com.enmanuelbergling.core.ui.components.walkthrough.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.Dp
import com.enmanuelbergling.core.ui.components.walkthrough.model.DimenTokens
import com.enmanuelbergling.core.ui.components.walkthrough.model.IndicatorColors
import com.enmanuelbergling.core.ui.components.walkthrough.model.IndicatorDefaults
import com.enmanuelbergling.core.ui.components.walkthrough.model.IndicatorStyle

/**
 * @param pagerState is required to handle the exact state
 * @param colors for dots
 * */
@Composable
fun StepIndicator(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    spaceBetween: Dp = DimenTokens.IndicatorSize,
    stepSize: Dp = DimenTokens.IndicatorSize,
    shape: Shape = CircleShape,
    colors: IndicatorColors = IndicatorDefaults.colors(),
) {
    Row(
        modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spaceBetween)
    ) {
        (0 until pagerState.pageCount).forEach { currentPage ->
            val scaleAnimation by animateFloatAsState(
                targetValue = pagerState.getPageProgress(currentPage).plus(1f),
                label = "width animation",
            )

            val color by animateColorAsState(
                targetValue = lerp(
                    start = colors.inactiveIndicatorColor,
                    stop = colors.activeIndicatorColor,
                    fraction = pagerState.getPageProgress(currentPage)
                ), label = "color animation"
            )

            Box(modifier = Modifier
                .size(stepSize)
                .graphicsLayer {
                    scaleX = scaleAnimation
                    scaleY = scaleAnimation
                }
                .clip(shape)
                .drawBehind {
                    drawRect(color)
                })

        }
    }
}

@Composable
fun ShiftIndicator(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    spaceBetween: Dp = DimenTokens.IndicatorSize,
    stepSize: Dp = DimenTokens.IndicatorSize,
    shape: Shape = CircleShape,
    colors: IndicatorColors = IndicatorDefaults.colors(),
) {
    Row(
        modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spaceBetween)
    ) {
        (0 until pagerState.pageCount).forEach { currentPage ->
            val widthAnimation by animateDpAsState(
                targetValue = stepSize * pagerState.getPageProgress(currentPage).times(2).plus(1f),
                label = "width animation"
            )

            val color by animateColorAsState(
                targetValue = lerp(
                    start = colors.inactiveIndicatorColor,
                    stop = colors.activeIndicatorColor,
                    fraction = pagerState.getPageProgress(currentPage)
                ), label = "color animation"
            )

            Box(modifier = Modifier
                .height(stepSize)
                .width(widthAnimation)
                .clip(shape)
                .drawBehind {
                    drawRect(color)
                })
        }
    }
}

@Composable
internal fun Indicator(
    indicatorStyle: IndicatorStyle,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    colors: IndicatorColors,
) {
    when (indicatorStyle) {
        is IndicatorStyle.Step -> {
            StepIndicator(
                pagerState = pagerState,
                modifier = modifier.padding(DimenTokens.LessLarge),
                spaceBetween = indicatorStyle.spaceBetween,
                stepSize = indicatorStyle.stepSize,
                shape = indicatorStyle.shape,
                colors = colors,
            )
        }

        is IndicatorStyle.Shift -> {
            ShiftIndicator(
                pagerState,
                modifier = modifier.padding(DimenTokens.LessLarge),
                spaceBetween = indicatorStyle.spaceBetween,
                stepSize = indicatorStyle.stepSize,
                shape = indicatorStyle.shape,
                colors = colors,
            )
        }
    }
}