package com.enmanuelbergling.feature.series.watchlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.enmanuelbergling.core.model.tv.TvShow
import com.enmanuelbergling.core.ui.components.OnceLottieAnimation
import com.enmanuelbergling.core.ui.components.PullToRefreshContainer
import com.enmanuelbergling.core.ui.components.SwipeToDismissContainer
import com.enmanuelbergling.core.ui.components.common.MovieLandCardPlaceholder
import com.enmanuelbergling.core.ui.components.common.TvShowLandCard
import com.enmanuelbergling.core.ui.core.ObserveAsEvents
import com.enmanuelbergling.core.ui.core.dimen
import com.enmanuelbergling.core.ui.core.isRefreshing
import com.enmanuelbergling.core.ui.core.shimmerIf
import com.enmanuelbergling.feature.series.R
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun WatchlistSeriesRoute(
    onSeriesDetails: (seriesId: Int) -> Unit,
    onBack: () -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(R.string.watchlist_series)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBackIosNew,
                            contentDescription = stringResource(com.enmanuelbergling.core.ui.R.string.back_icon)
                        )
                    }
                },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { paddingValues ->
        WatchlistSeriesContent(
            snackbarHostState = snackbarHostState,
            onSeriesDetails = onSeriesDetails,
            contentPadding = WindowInsets.navigationBars.asPaddingValues(),
            modifier = Modifier.padding(paddingValues),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun WatchlistSeriesContent(
    snackbarHostState: SnackbarHostState,
    onSeriesDetails: (seriesId: Int) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = WindowInsets.navigationBars.asPaddingValues(),
) {
    val viewModel = koinViewModel<WatchlistSeriesVM>()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val watchlist = viewModel.watchlist.collectAsLazyPagingItems()
    val scope = rememberCoroutineScope()

    val removeSeriesErrorMessage =
        stringResource(R.string.the_series_could_not_be_removed_from_watchlist)
    val seriesRemovedMessage = stringResource(R.string.series_removed_from_watchlist)
    val seriesAddedToFavoritesMessage = stringResource(R.string.series_added_to_favorites)
    val addToFavoritesErrorMessage =
        stringResource(R.string.series_could_not_be_added_to_favorites)
    val undoMessage = stringResource(com.enmanuelbergling.core.ui.R.string.undo)
    val retryMessage = stringResource(com.enmanuelbergling.core.ui.R.string.retry)

    ObserveAsEvents(viewModel.sideEffectChannel) {
        when (it) {
            is WatchlistSeriesSideEffect.NavigateToDetails -> onSeriesDetails(it.seriesId)
            is WatchlistSeriesSideEffect.UndoRemoveSeries -> scope.launch {
                val result = snackbarHostState.showSnackbar(
                    message = seriesRemovedMessage,
                    actionLabel = undoMessage,
                    duration = SnackbarDuration.Short,
                )
                when (result) {
                    SnackbarResult.Dismissed -> viewModel.onEvent(WatchlistSeriesEvent.RemoveSeries(it.seriesId))
                    SnackbarResult.ActionPerformed -> viewModel.onEvent(WatchlistSeriesEvent.UndoRemove)
                }
            }

            is WatchlistSeriesSideEffect.RemoveSeriesError -> scope.launch {
                val result = snackbarHostState.showSnackbar(
                    message = removeSeriesErrorMessage,
                    actionLabel = retryMessage,
                    duration = SnackbarDuration.Indefinite,
                    withDismissAction = true,
                )
                when (result) {
                    SnackbarResult.Dismissed -> viewModel.onEvent(
                        WatchlistSeriesEvent.OnRemoveSeriesErrorDismissed(it.seriesId)
                    )

                    SnackbarResult.ActionPerformed -> viewModel.onEvent(
                        WatchlistSeriesEvent.RemoveSeries(it.seriesId)
                    )
                }
            }

            is WatchlistSeriesSideEffect.UndoAddToFavoritesSeries -> scope.launch {
                val result = snackbarHostState.showSnackbar(
                    message = seriesAddedToFavoritesMessage,
                    actionLabel = undoMessage,
                    duration = SnackbarDuration.Short,
                )
                when (result) {
                    SnackbarResult.Dismissed -> viewModel.onEvent(WatchlistSeriesEvent.AddToFavorites(it.seriesId))
                    SnackbarResult.ActionPerformed -> viewModel.onEvent(WatchlistSeriesEvent.UndoAddToFavorites)
                }
            }

            is WatchlistSeriesSideEffect.AddToFavoritesError -> scope.launch {
                val result = snackbarHostState.showSnackbar(
                    message = addToFavoritesErrorMessage,
                    actionLabel = retryMessage,
                    duration = SnackbarDuration.Indefinite,
                    withDismissAction = true,
                )
                when (result) {
                    SnackbarResult.Dismissed -> viewModel.onEvent(
                        WatchlistSeriesEvent.OnAddToFavoritesErrorDismissed(it.seriesId)
                    )

                    SnackbarResult.ActionPerformed -> viewModel.onEvent(
                        WatchlistSeriesEvent.AddToFavorites(it.seriesId)
                    )
                }
            }
        }
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        PullToRefreshContainer(
            refreshing = false,
            onRefresh = watchlist::refresh,
            modifier = Modifier
                .shimmerIf { watchlist.isRefreshing }
                .padding(horizontal = MaterialTheme.dimen.verySmall),
            contentPadding = contentPadding,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.small)
        ) {
            if (watchlist.isRefreshing) {
                items(12) {
                    MovieLandCardPlaceholder(Modifier.fillMaxWidth())
                }
            } else {
                items(watchlist.itemCount) { index ->
                    val tvShow = watchlist[index]
                    tvShow?.let {
                        SeriesWatchlistItem(
                            tvShow = tvShow,
                            removed = tvShow.id in uiState.removedItems,
                            onEvent = viewModel::onEvent,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SeriesWatchlistItem(
    tvShow: TvShow,
    removed: Boolean,
    onEvent: (WatchlistSeriesEvent) -> Unit,
) {
    SwipeToDismissContainer(
        visible = !removed,
        onDismissFromStartToEnd = {
            onEvent(WatchlistSeriesEvent.OnAddToFavorites(tvShow.id))
        },
        onDismissFromEndToStart = {
            onEvent(WatchlistSeriesEvent.OnRemoveSeries(tvShow.id))
        },
        containerColorDismissFromStart = MaterialTheme.colorScheme.primaryContainer.copy(alpha = .3f),
        containerColorDismissFromEnd = MaterialTheme.colorScheme.errorContainer.copy(alpha = .3f),
        backgroundIcon = { direction ->
            when (direction) {
                SwipeToDismissBoxValue.StartToEnd -> OnceLottieAnimation(
                    resId = com.enmanuelbergling.core.ui.R.raw.add_to_favorite,
                    modifier = Modifier
                        .size(48.dp)
                        .scale(1.5f),
                )

                SwipeToDismissBoxValue.EndToStart -> OnceLottieAnimation(
                    resId = com.enmanuelbergling.core.ui.R.raw.delete,
                    modifier = Modifier
                        .size(48.dp)
                        .scale(2.2f),
                )

                SwipeToDismissBoxValue.Settled -> Unit
            }
        },
    ) {
        TvShowLandCard(
            tvShow = tvShow,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface),
        ) {
            onEvent(WatchlistSeriesEvent.NavigateToDetails(tvShow.id))
        }
    }
}
