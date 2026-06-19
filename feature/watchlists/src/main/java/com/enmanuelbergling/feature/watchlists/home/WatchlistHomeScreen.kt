package com.enmanuelbergling.feature.watchlists.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.enmanuelbergling.core.model.core.SimplerUi
import com.enmanuelbergling.core.model.movie.Movie
import com.enmanuelbergling.core.ui.R
import com.enmanuelbergling.core.ui.components.LoadingDialog
import com.enmanuelbergling.core.ui.components.PullToRefreshContainer
import com.enmanuelbergling.core.ui.components.SwipeToDismissContainer
import com.enmanuelbergling.core.ui.components.common.MovieLandCard
import com.enmanuelbergling.core.ui.components.common.MovieLandCardPlaceholder
import com.enmanuelbergling.core.ui.core.ObserveAsEvents
import com.enmanuelbergling.core.ui.core.dimen
import com.enmanuelbergling.core.ui.core.isRefreshing
import com.enmanuelbergling.core.ui.core.shimmerIf
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun WatchlistHomeRoute(
    onMovieDetails: (movieId: Int) -> Unit,
    onNavigateToLists: () -> Unit,
    onOpenDrawer: () -> Unit,
) {
    val viewModel = koinViewModel<WatchlistHomeVM>()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val watchlist = viewModel.watchlist.collectAsLazyPagingItems()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val deleteMovieErrorMessage =
        stringResource(com.enmanuelbergling.feature.watchlists.R.string.the_movie_couldn_t_be_deleted_from_the_watchlist)
    val movieDeletedMessage =
        stringResource(com.enmanuelbergling.feature.watchlists.R.string.movie_removed_from_watchlist)
    val undoMessage = stringResource(R.string.undo)
    val retryMessage = stringResource(R.string.retry)
    ObserveAsEvents(viewModel.sideEffectChannel) {
        when (it) {
            is WatchlistHomeSideEffect.NavigateToDetails -> onMovieDetails(it.movieId)
            is WatchlistHomeSideEffect.UndoDeleteMovie -> scope.launch {
                val result = snackbarHostState.showSnackbar(
                    message = movieDeletedMessage,
                    actionLabel = undoMessage,
                    duration = SnackbarDuration.Short,
                )
                when (result) {
                    SnackbarResult.Dismissed -> viewModel.onEvent(WatchlistHomeEvent.DeleteMovie(it.movieId))
                    SnackbarResult.ActionPerformed -> viewModel.onEvent(WatchlistHomeEvent.UndoDelete)
                }
            }

            is WatchlistHomeSideEffect.DeleteMovieError -> scope.launch {

                val result = snackbarHostState.showSnackbar(
                    message = deleteMovieErrorMessage,
                    actionLabel = retryMessage,
                    duration = SnackbarDuration.Indefinite,
                    withDismissAction = true,
                )
                when (result) {
                    SnackbarResult.Dismissed -> viewModel.onEvent(
                        WatchlistHomeEvent.OnDeleteMovieErrorDismissed(it.movieId)
                    )

                    SnackbarResult.ActionPerformed -> viewModel.onEvent(
                        WatchlistHomeEvent.DeleteMovie(it.movieId)
                    )
                }
            }

            WatchlistHomeSideEffect.NavigateToLists -> onNavigateToLists()
            WatchlistHomeSideEffect.OpenDrawer -> onOpenDrawer()
        }
    }

    when (uiState.uiState) {
        is SimplerUi.Error, SimplerUi.Idle -> Unit
        SimplerUi.Success -> watchlist.refresh()
        SimplerUi.Loading -> LoadingDialog()
    }

    WatchlistHomeScreen(
        watchlist = watchlist,
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onEvent = viewModel::onEvent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WatchlistHomeScreen(
    watchlist: LazyPagingItems<Movie>,
    uiState: WatchlistHomeState,
    snackbarHostState: SnackbarHostState,
    onEvent: (WatchlistHomeEvent) -> Unit,
) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.watchlist)) },
                navigationIcon = {
                    IconButton(onClick = { onEvent(WatchlistHomeEvent.OpenDrawer) }) {
                        Icon(
                            painter = painterResource(R.drawable.bars_bottom_left),
                            contentDescription = "Sandwich menu icon"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { onEvent(WatchlistHomeEvent.NavigateToLists) }) {
                        Icon(
                            painter = painterResource(R.drawable.paint_brush),
                            contentDescription = "Customization icon"
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
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
                            SwipeToDismissContainer(
                                visible = movie.id !in uiState.deletedMovieIds,
                                onDismissFromEndToStart = {
                                    onEvent(WatchlistHomeEvent.OnDeleteMovie(movie.id))
                                }
                            ) {
                                MovieLandCard(
                                    movie = movie,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(MaterialTheme.colorScheme.surface),
                                ) {
                                    onEvent(WatchlistHomeEvent.NavigateToDetails(movie.id))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
