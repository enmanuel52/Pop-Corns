package com.enmanuelbergling.ktormovies.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.pullrefresh.PullRefreshIndicator
import androidx.compose.material3.pullrefresh.pullRefresh
import androidx.compose.material3.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PullToRefreshContainer(
    refreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    content: LazyListScope.() -> Unit,
) {

    val pullRefreshState =
        rememberPullRefreshState(
            refreshing = refreshing,
            onRefresh = onRefresh,
        )

    Box(
        modifier = modifier.pullRefresh(pullRefreshState)
    ) {

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment,
            contentPadding = contentPadding,
            content = content,
        )

        PullRefreshIndicator(
            refreshing = refreshing,
            state = pullRefreshState,
            scale = true,
            modifier = Modifier
                .align(Alignment.TopCenter)
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