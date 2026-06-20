package com.enmanuelbergling.feature.favorites.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.enmanuelbergling.core.model.movie.Movie
import com.enmanuelbergling.core.ui.R
import com.enmanuelbergling.core.ui.components.TinderSwipeToDismissContainer
import com.enmanuelbergling.core.ui.components.common.MovieCard
import com.enmanuelbergling.core.ui.components.common.MovieCardPlaceholder
import com.enmanuelbergling.core.ui.core.ObserveAsEvents
import com.enmanuelbergling.core.ui.core.dimen
import com.enmanuelbergling.core.ui.core.isRefreshing
import com.enmanuelbergling.core.ui.core.items
import com.enmanuelbergling.core.ui.core.shimmerIf
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun FavoriteMoviesRoute(
    onMovieDetails: (movieId: Int) -> Unit,
    onBack: () -> Unit,
) {
    val viewModel = koinViewModel<FavoriteMoviesVM>()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val favorites = viewModel.favorites.collectAsLazyPagingItems()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val movieRemovedMessage =
        stringResource(com.enmanuelbergling.feature.favorites.R.string.movie_removed_from_favorites)
    val undoMessage = stringResource(R.string.undo)

    ObserveAsEvents(viewModel.sideEffectChannel) {
        when (it) {
            is FavoriteMoviesSideEffect.NavigateToDetails -> onMovieDetails(it.movieId)
            is FavoriteMoviesSideEffect.UndoRemoveMovie -> scope.launch {
                val result = snackbarHostState.showSnackbar(
                    message = movieRemovedMessage,
                    actionLabel = undoMessage,
                    duration = SnackbarDuration.Short,
                )
                if (result == SnackbarResult.ActionPerformed) {
                    viewModel.onEvent(FavoriteMoviesEvent.UndoRemove)
                }
            }
        }
    }

    FavoriteMoviesScreen(
        favorites = favorites,
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onBack = onBack,
        onEvent = viewModel::onEvent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FavoriteMoviesScreen(
    favorites: LazyPagingItems<Movie>,
    uiState: FavoriteMoviesState,
    snackbarHostState: SnackbarHostState,
    onBack: () -> Unit,
    onEvent: (FavoriteMoviesEvent) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(com.enmanuelbergling.feature.favorites.R.string.favorites)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBackIosNew,
                            contentDescription = stringResource(R.string.back_icon)
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { paddingValues ->
        val listState = rememberLazyGridState()

        Box(
            Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            LazyVerticalGrid(
                modifier = Modifier
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
                    .shimmerIf { favorites.isRefreshing },
                state = listState,
                columns = GridCells.Adaptive(150.dp),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.small),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.small),
                userScrollEnabled = !favorites.isRefreshing,
                contentPadding = PaddingValues(MaterialTheme.dimen.small)
            ) {
                if (favorites.isRefreshing) {
                    items(50) {
                        MovieCardPlaceholder()
                    }
                } else {
                    items(favorites, key = { movie -> movie.id }) { movie ->
                        movie?.let {
                            TinderSwipeToDismissContainer(
                                visible = movie.id !in uiState.deletedMovieIds,
                                onDismissFromStartToEnd = {
                                    onEvent(FavoriteMoviesEvent.OnRemoveMovie(movie.id))
                                },
                                onDismissFromEndToStart = {
                                    onEvent(FavoriteMoviesEvent.OnRemoveMovie(movie.id))
                                },
                            ) {
                                MovieCard(
                                    imageUrl = movie.posterPath,
                                    title = movie.title,
                                    rating = movie.voteAverage,
                                    titleLines = 1,
                                ) {
                                    onEvent(FavoriteMoviesEvent.NavigateToDetails(movie.id))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
