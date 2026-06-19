package com.enmanuelbergling.feature.watchlists.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.enmanuelbergling.core.common.android_util.isDynamicShortcutActive
import com.enmanuelbergling.core.model.movie.Movie
import com.enmanuelbergling.core.ui.R
import com.enmanuelbergling.core.ui.components.HandleUiState
import com.enmanuelbergling.core.ui.components.PullToRefreshContainer
import com.enmanuelbergling.core.ui.components.SwipeToDismissContainer
import com.enmanuelbergling.core.ui.components.common.MovieLandCard
import com.enmanuelbergling.core.ui.components.common.MovieLandCardPlaceholder
import com.enmanuelbergling.core.ui.core.ObserveAsEvents
import com.enmanuelbergling.core.ui.core.dimen
import com.enmanuelbergling.core.ui.core.isRefreshing
import com.enmanuelbergling.core.ui.core.shimmerIf
import com.enmanuelbergling.core.ui.model.WatchlistShortcut
import com.enmanuelbergling.core.ui.util.watchlistShortcutId
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun WatchListDetailsRoute(
    listId: Int,
    listName: String,
    onMovieDetails: (movieId: Int) -> Unit,
    onAddShortcut: (WatchlistShortcut) -> Unit,
    onDeleteShortcut: (watchlistId: Int) -> Unit,
    onBack: () -> Unit,
) {

    val viewModel = koinViewModel<WatchListDetailsVM> { parametersOf(listId) }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val movies = viewModel.movies.collectAsLazyPagingItems()

    val context = LocalContext.current

    val (isPinned, onPinned) = rememberSaveable {
        mutableStateOf(
            context.isDynamicShortcutActive(watchlistShortcutId(listId))
        )
    }

    val snackBarState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val deleteMovieErrorMessage =
        stringResource(com.enmanuelbergling.feature.watchlists.R.string.the_movie_could_not_be_deleted_from_the_watchlist)
    val movieDeletedMessage =
        stringResource(com.enmanuelbergling.feature.watchlists.R.string.movie_removed_from_watchlist)
    val undoMessage = stringResource(R.string.undo)
    val retryMessage = stringResource(R.string.retry)

    ObserveAsEvents(viewModel.sideEffectChannel) {
        when (it) {
            WatchlistDetailsSideEffect.NavigateBack -> onBack()
            is WatchlistDetailsSideEffect.NavigateToDetails -> onMovieDetails(it.movieId)
            WatchlistDetailsSideEffect.OnAddShortcut -> {
                onAddShortcut(WatchlistShortcut(listId, listName))
                onPinned(true)
            }

            WatchlistDetailsSideEffect.OnDeleteShortCut -> {
                onDeleteShortcut(listId)
                onPinned(false)
            }

            is WatchlistDetailsSideEffect.UndoDeleteMovie -> scope.launch {
                val result = snackBarState.showSnackbar(
                    message = movieDeletedMessage,
                    actionLabel = undoMessage,
                    duration = SnackbarDuration.Short,
                )
                when (result) {
                    SnackbarResult.Dismissed -> viewModel.onEvent(
                        WatchlistDetailsEvent.DeleteMovie(
                            it.movieId
                        )
                    )

                    SnackbarResult.ActionPerformed -> viewModel.onEvent(WatchlistDetailsEvent.UndoDelete)
                }
            }

            is WatchlistDetailsSideEffect.DeleteMovieError -> scope.launch {
                val result = snackBarState.showSnackbar(
                    message = deleteMovieErrorMessage,
                    actionLabel = retryMessage,
                    duration = SnackbarDuration.Indefinite,
                    withDismissAction = true
                )
                when (result) {
                    SnackbarResult.Dismissed -> viewModel.onEvent(
                        WatchlistDetailsEvent.OnDeleteMovieErrorDismissed(it.movieId)
                    )

                    SnackbarResult.ActionPerformed -> viewModel.onEvent(
                        WatchlistDetailsEvent.DeleteMovie(it.movieId)
                    )
                }
            }
        }
    }

    WatchListDetailsScreen(
        listName = listName,
        movies = movies,
        uiState = uiState,
        isPinned = isPinned,
        snackbarHostState = snackBarState,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun WatchListDetailsScreen(
    listName: String,
    movies: LazyPagingItems<Movie>,
    isPinned: Boolean,
    snackbarHostState: SnackbarHostState,
    uiState: WatchlistDetailsState,
    onEvent: (WatchlistDetailsEvent) -> Unit,
) {
    HandleUiState(
        uiState = uiState.uiState,
        onIdle = { onEvent(WatchlistDetailsEvent.DismissDialog) },
        onSuccess = movies::refresh
    )

    val scrollBehaviour = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = listName) },
                navigationIcon = {
                    IconButton(onClick = { onEvent(WatchlistDetailsEvent.NavigateBack) }) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBackIosNew,
                            contentDescription = stringResource(id = R.string.back_icon)
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (isPinned) {
                                onEvent(WatchlistDetailsEvent.OnDeleteShortCut)
                            } else {
                                onEvent(WatchlistDetailsEvent.OnAddShortcut)
                            }
                        }
                    ) {
                        Icon(
                            painter = painterResource(if (isPinned) R.drawable.bookmark_solid else R.drawable.bookmark_outline),
                            contentDescription = "bookmark icon"
                        )
                    }
                },
                scrollBehavior = scrollBehaviour
            )
        },
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            PullToRefreshContainer(
                refreshing = false, onRefresh = { movies.refresh() },
                verticalArrangement = Arrangement.spacedBy(
                    MaterialTheme.dimen.small,
                ),
                contentPadding = WindowInsets.navigationBars.asPaddingValues(),
                modifier = Modifier
                    .shimmerIf { movies.isRefreshing }
                    .padding(horizontal = MaterialTheme.dimen.verySmall),
                scrollBehavior = scrollBehaviour
            ) {
                if (movies.isRefreshing) {
                    items(12) {
                        val outline = MaterialTheme.colorScheme.outline
                        MovieLandCardPlaceholder(Modifier.fillMaxWidth().drawBehind{
                            drawLine(
                                color = outline,
                                start = Offset(0f, size.height),
                                end = Offset(size.width, size.height),
                                strokeWidth = 1.dp.toPx()
                            )
                        })
                    }
                } else {
                    items(movies.itemCount) { index ->
                        val movie = movies[index]
                        movie?.let {
                            SwipeToDismissContainer(
                                visible = movie.id !in uiState.deletedMovieIds,
                                onDismissFromEndToStart = {
                                    onEvent(WatchlistDetailsEvent.OnDeleteMovie(movie.id))
                                },
                            ) {
                                MovieLandCard(
                                    movie = movie,
                                    shape = MaterialTheme.shapes.medium,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(MaterialTheme.colorScheme.surface),
                                ) {
                                    onEvent(WatchlistDetailsEvent.NavigateToDetails(movie.id))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
