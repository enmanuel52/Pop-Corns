package com.enmanuelbergling.feature.watchlists.tvShows

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
import com.enmanuelbergling.feature.watchlists.R
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun WatchlistTvShowsContent(
    snackbarHostState: SnackbarHostState,
    onTvShowsDetails: (tvShowId: Int) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = WindowInsets.navigationBars.asPaddingValues(),
) {
    val viewModel = koinViewModel<WatchlistTvShowsVM>()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val watchlist = viewModel.watchlist.collectAsLazyPagingItems()
    val scope = rememberCoroutineScope()

    val removeTvShowsErrorMessage =
        stringResource(R.string.the_tv_show_could_not_be_removed_from_watchlist)
    val tvShowRemovedMessage = stringResource(R.string.tv_show_removed_from_watchlist)
    val tvShowAddedToFavoritesMessage = stringResource(R.string.tv_show_added_to_favorites)
    val addToFavoritesErrorMessage =
        stringResource(R.string.tv_show_could_not_be_added_to_favorites)
    val undoMessage = stringResource(com.enmanuelbergling.core.ui.R.string.undo)
    val retryMessage = stringResource(com.enmanuelbergling.core.ui.R.string.retry)

    ObserveAsEvents(viewModel.sideEffectChannel) {
        when (it) {
            is WatchlistTvShowsSideEffect.NavigateToDetails -> onTvShowsDetails(it.tvShowId)
            is WatchlistTvShowsSideEffect.UndoRemoveTvShows -> scope.launch {
                val result = snackbarHostState.showSnackbar(
                    message = tvShowRemovedMessage,
                    actionLabel = undoMessage,
                    duration = SnackbarDuration.Short,
                )
                when (result) {
                    SnackbarResult.Dismissed -> viewModel.onEvent(WatchlistTvShowsEvent.RemoveTvShows(it.tvShowId))
                    SnackbarResult.ActionPerformed -> viewModel.onEvent(WatchlistTvShowsEvent.UndoRemove)
                }
            }

            is WatchlistTvShowsSideEffect.RemoveTvShowsError -> scope.launch {
                val result = snackbarHostState.showSnackbar(
                    message = removeTvShowsErrorMessage,
                    actionLabel = retryMessage,
                    duration = SnackbarDuration.Indefinite,
                    withDismissAction = true,
                )
                when (result) {
                    SnackbarResult.Dismissed -> viewModel.onEvent(
                        WatchlistTvShowsEvent.OnRemoveTvShowsErrorDismissed(it.tvShowId)
                    )

                    SnackbarResult.ActionPerformed -> viewModel.onEvent(
                        WatchlistTvShowsEvent.RemoveTvShows(it.tvShowId)
                    )
                }
            }

            is WatchlistTvShowsSideEffect.UndoAddToFavoritesTvShows -> scope.launch {
                val result = snackbarHostState.showSnackbar(
                    message = tvShowAddedToFavoritesMessage,
                    actionLabel = undoMessage,
                    duration = SnackbarDuration.Short,
                )
                when (result) {
                    SnackbarResult.Dismissed -> viewModel.onEvent(WatchlistTvShowsEvent.AddToFavorites(it.tvShowId))
                    SnackbarResult.ActionPerformed -> viewModel.onEvent(WatchlistTvShowsEvent.UndoAddToFavorites)
                }
            }

            is WatchlistTvShowsSideEffect.AddToFavoritesError -> scope.launch {
                val result = snackbarHostState.showSnackbar(
                    message = addToFavoritesErrorMessage,
                    actionLabel = retryMessage,
                    duration = SnackbarDuration.Indefinite,
                    withDismissAction = true,
                )
                when (result) {
                    SnackbarResult.Dismissed -> viewModel.onEvent(
                        WatchlistTvShowsEvent.OnAddToFavoritesErrorDismissed(it.tvShowId)
                    )

                    SnackbarResult.ActionPerformed -> viewModel.onEvent(
                        WatchlistTvShowsEvent.AddToFavorites(it.tvShowId)
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
                        TvShowsWatchlistItem(
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
private fun TvShowsWatchlistItem(
    tvShow: TvShow,
    removed: Boolean,
    onEvent: (WatchlistTvShowsEvent) -> Unit,
) {
    SwipeToDismissContainer(
        visible = !removed,
        onDismissFromStartToEnd = {
            onEvent(WatchlistTvShowsEvent.OnAddToFavorites(tvShow.id))
        },
        onDismissFromEndToStart = {
            onEvent(WatchlistTvShowsEvent.OnRemoveTvShows(tvShow.id))
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
            onEvent(WatchlistTvShowsEvent.NavigateToDetails(tvShow.id))
        }
    }
}
