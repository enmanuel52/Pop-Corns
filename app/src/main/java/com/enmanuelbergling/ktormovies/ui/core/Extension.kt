package com.enmanuelbergling.ktormovies.ui.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.enmanuelbergling.ktormovies.ui.theme.Dimen
import com.valentinilk.shimmer.shimmer

typealias Material3 = androidx.compose.material3.MaterialTheme

val Material3.dimen: Dimen
    @Composable
    @ReadOnlyComposable
    get() = LocalDimen.current

@Stable
fun Modifier.shimmerIf(condition: () -> Boolean) = if (condition()) this.then(shimmer()) else this

val LazyPagingItems<*>.isRefreshing: Boolean
    get() = loadState.refresh == LoadState.Loading && itemCount == 0