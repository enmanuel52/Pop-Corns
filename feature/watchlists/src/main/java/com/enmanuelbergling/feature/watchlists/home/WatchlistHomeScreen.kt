package com.enmanuelbergling.feature.watchlists.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.enmanuelbergling.core.model.movie.Movie
import com.enmanuelbergling.core.ui.R
import com.enmanuelbergling.core.ui.components.HandleUiState
import com.enmanuelbergling.core.ui.components.NewerDragListItem
import com.enmanuelbergling.core.ui.components.PullToRefreshContainer
import com.enmanuelbergling.core.ui.components.common.MovieLandCard
import com.enmanuelbergling.core.ui.components.common.MovieLandCardPlaceholder
import com.enmanuelbergling.core.ui.core.dimen
import com.enmanuelbergling.core.ui.core.isRefreshing
import com.enmanuelbergling.core.ui.core.shimmerIf
import com.enmanuelbergling.feature.watchlists.components.RemoveFromWatchlistDialog
import org.koin.androidx.compose.koinViewModel

@Composable
fun WatchlistHomeRoute(
    onMovieDetails: (movieId: Int) -> Unit,
    onOpenDrawer: () -> Unit,
) {
    val viewModel = koinViewModel<WatchlistHomeVM>()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val watchlist = viewModel.watchlist.collectAsLazyPagingItems()

    HandleUiState(
        uiState = uiState, onIdle = viewModel::onIdle
    ) {
        watchlist.refresh()
        viewModel.onIdle()
    }

    WatchlistHomeScreen(
        watchlist = watchlist,
        onMovieDetails = onMovieDetails,
        onRemoveFromWatchlist = viewModel::removeFromWatchlist,
        onOpenDrawer = onOpenDrawer
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WatchlistHomeScreen(
    watchlist: LazyPagingItems<Movie>,
    onMovieDetails: (movieId: Int) -> Unit,
    onRemoveFromWatchlist: (movieId: Int) -> Unit,
    onOpenDrawer: () -> Unit,
) {
    val snackbarHostState = remember {
        SnackbarHostState()
    }

    var pickedMovie by rememberSaveable {
        mutableIntStateOf(-1)
    }

    if (pickedMovie != -1) {
        RemoveFromWatchlistDialog(
            onDismiss = { pickedMovie = -1 },
            onConfirm = {
                onRemoveFromWatchlist(pickedMovie)
                pickedMovie = -1
            }
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.watchlist)) },
                navigationIcon = {
                    IconButton(onClick = onOpenDrawer) {
                        Icon(
                            painter = painterResource(R.drawable.bars_bottom_left),
                            contentDescription = "Sandwich menu icon"
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        contentWindowInsets = WindowInsets.statusBars
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            PullToRefreshContainer(
                refreshing = false,
                onRefresh = watchlist::refresh,
                modifier = Modifier
                    .shimmerIf { watchlist.isRefreshing }
                    .padding(horizontal = MaterialTheme.dimen.verySmall),
                contentPadding = WindowInsets.navigationBars.asPaddingValues(),
            ) {
                if (watchlist.isRefreshing) {
                    items(12) {
                        MovieLandCardPlaceholder(Modifier.fillMaxWidth())
                    }
                } else {
                    items(watchlist.itemCount) { index ->
                        val movie = watchlist[index]
                        movie?.let {
                            NewerDragListItem(
                                bottomContentWidth = with(LocalDensity.current) { 80.dp.toPx() },
                                bottomContent = {
                                    Box(Modifier.align(Alignment.CenterEnd)) {
                                        IconButton(
                                            onClick = { pickedMovie = movie.id },
                                            modifier = Modifier.padding(horizontal = MaterialTheme.dimen.small)
                                        ) {
                                            Icon(
                                                painter = painterResource(R.drawable.trash),
                                                contentDescription = stringResource(id = R.string.delete_icon)
                                            )
                                        }
                                    }
                                },
                                modifier = Modifier.background(
                                    MaterialTheme.colorScheme.surfaceVariant, CardDefaults.elevatedShape
                                ),
                            ) {
                                MovieLandCard(
                                    movie = movie,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    onMovieDetails(movie.id)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}