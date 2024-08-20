package com.enmanuelbergling.core.ui.components.walkthrough.components

import androidx.annotation.IntRange
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.abs

/**
 * Working onto [HorizontalPager] under the hood
 * @param state of pager
 * @param boxAngle around the rotation is performed, the bigger the value the louder the effect
 * @param reverse to define whether you are in or out of the cube
 * @param pageContent the content of the page, you must use this modifier in its page to ensure
 * the instagram effect
 * */
@Composable
fun InstagramPager(
    state: PagerState,
    modifier: Modifier = Modifier,
    @IntRange(10, 90) boxAngle: Int = 30,
    reverse: Boolean = false,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    pageSize: PageSize = PageSize.Fill,
    pageSpacing: Dp = 0.dp,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    userScrollEnabled: Boolean = true,
    pageContent: @Composable (index: Int, pageModifier: Modifier) -> Unit,
) {

    HorizontalPager(
        state = state,
        modifier,
        verticalAlignment = verticalAlignment,
        contentPadding = contentPadding,
        pageSize = pageSize,
        pageSpacing = pageSpacing,
        userScrollEnabled = userScrollEnabled,
    ) { pageIndex ->

        val degreesIn by remember(state.currentPageOffsetFraction) {
            derivedStateOf {
                if (pageIndex == state.currentPage) {
                    state.currentPageOffsetFraction * boxAngle
                } else if (pageIndex > state.currentPage) {
                    -boxAngle + abs(state.currentPageOffsetFraction * boxAngle)
                } else {
                    boxAngle - abs(state.currentPageOffsetFraction * boxAngle)
                }
            }
        }

        //alike instagram
        val degreesOut by remember(state.currentPageOffsetFraction) {
            derivedStateOf {
                if (pageIndex == state.currentPage) {
                    -state.currentPageOffsetFraction * boxAngle
                } else if (pageIndex < state.currentPage) {
                    -boxAngle + abs(state.currentPageOffsetFraction * boxAngle)
                } else {
                    boxAngle - abs(state.currentPageOffsetFraction * boxAngle)
                }
            }
        }

        val transformOriginX by remember(key1 = state.currentPage) {
            derivedStateOf {
                if (pageIndex == state.currentPage) {
                    if (state.currentPageOffsetFraction > 0f) {
                        1f
                    } else {
                        0f
                    }
                } else if (pageIndex < state.currentPage) {
                    1f
                } else {
                    0f
                }
            }
        }

        pageContent(
            pageIndex,
            Modifier.graphicsLayer {
                rotationY = if (reverse) degreesIn else degreesOut
                transformOrigin = TransformOrigin(
                    transformOriginX,
                    .5f
                )
            }
        )
    }
}