package com.enmanuelbergling.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.pullrefresh.PullRefreshIndicator
import com.enmanuelbergling.core.ui.components.material.pullRefresh
import androidx.compose.material3.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PullToRefreshContainer(
    refreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    scrollBehavior: TopAppBarScrollBehavior? = null,
    content: LazyListScope.() -> Unit,
) {

    val pullRefreshState =
        rememberPullRefreshState(
            refreshing = refreshing,
            onRefresh = onRefresh,
        )

    val refreshIndicatorScale by remember {
        derivedStateOf {
            pullRefreshState.progress.let {
                if (it > 2f) 1.6f
                else it - .4f
//                if (it > 1f) 1f else it
            }
        }
    }

    Box(
        modifier = modifier.pullRefresh(pullRefreshState)
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (scrollBehavior == null) Modifier else
                        Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
                ),
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment,
            contentPadding = contentPadding,
            content = content,
        )

        PullRefreshIndicator(
            refreshing = refreshing,
            state = pullRefreshState,
            scale = false,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .graphicsLayer {
                    scaleX = refreshIndicatorScale
                    scaleY = refreshIndicatorScale
                }
        )
    }
}

@Composable
fun PullToRefreshBoxContainer(
    refreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {

    val pullRefreshState =
        rememberPullRefreshState(
            refreshing = refreshing,
            onRefresh = onRefresh,
        )

    Box(
        modifier = modifier.pullRefresh(pullRefreshState)
    ) {

        content()

        PullRefreshIndicator(
            refreshing = refreshing,
            state = pullRefreshState,
            scale = true,
            modifier = Modifier
                .align(Alignment.TopCenter)
        )
    }
}