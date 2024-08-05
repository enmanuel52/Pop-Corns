@file:OptIn(ExperimentalFoundationApi::class)

package com.enmanuelbergling.core.ui.components.walkthrough.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import com.enmanuelbergling.core.ui.components.walkthrough.model.DimenTokens
import com.enmanuelbergling.core.ui.components.walkthrough.model.IndicatorColors
import com.enmanuelbergling.core.ui.components.walkthrough.model.IndicatorDefaults
import com.enmanuelbergling.core.ui.components.walkthrough.model.IndicatorStyle

/**
 * @param pageIndex starts from 0
 * @param colors for dots
 * */
@Composable
fun StepIndicator(
    pageIndex: Int,
    pageCount: Int,
    modifier: Modifier = Modifier,
    spaceBetween: Dp = DimenTokens.VerySmall,
    stepSize: Dp = DimenTokens.Small,
    colors: IndicatorColors = IndicatorDefaults.colors(),
) {
    Row(
        modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spaceBetween)
    ) {
        (0 until pageCount).forEach { currentPage ->
            val activeTransition =
                updateTransition(
                    targetState = pageIndex == currentPage,
                    label = "active transition animation"
                )

            val sizeAnimation by activeTransition.animateDp(
                label = "size animation",
                transitionSpec = { springAnimation() }
            ) {
                if (it) stepSize.times(1.25f)
                else stepSize
            }

            val colorAnimation by activeTransition.animateColor(
                label = "color animation",
            ) { active ->
                if (active) colors.activeIndicatorColor
                else colors.inactiveIndicatorColor
            }

            Canvas(modifier = Modifier.size(sizeAnimation)) {
                drawCircle(colorAnimation)
            }
        }
    }
}

@Composable
fun ShiftIndicator(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    spaceBetween: Dp = DimenTokens.VerySmall,
    stepSize: Dp = DimenTokens.Small,
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
                targetValue =
                stepSize * pagerState.getPageProgress(currentPage).plus(1f),
                label = "width animation"
            )

            Box(
                modifier = Modifier
                    .height(stepSize)
                    .width(widthAnimation)
                    .background(colors.activeIndicatorColor, shape)
            )
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
        IndicatorStyle.Step -> {
            StepIndicator(
                pageIndex = pagerState.currentPage,
                pageCount = pagerState.pageCount,
                stepSize = DimenTokens.IndicatorSize,
                modifier = modifier.padding(DimenTokens.LessLarge),
                colors = colors
            )
        }

        IndicatorStyle.Shift -> {
            ShiftIndicator(
                pagerState,
                stepSize = DimenTokens.IndicatorSize,
                modifier = modifier.padding(DimenTokens.LessLarge),
                colors = colors
            )
        }
    }
}