package com.enmanuelbergling.feature.watchlists.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.BookmarkBorder
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.enmanuelbergling.core.common.android_util.isDynamicShortcutActive
import com.enmanuelbergling.core.model.core.SimplerUi
import com.enmanuelbergling.core.model.movie.Movie
import com.enmanuelbergling.core.ui.R
import com.enmanuelbergling.core.ui.components.DeleteMovieConfirmationDialog
import com.enmanuelbergling.core.ui.components.HandleUiState
import com.enmanuelbergling.core.ui.components.NewerDragListItem
import com.enmanuelbergling.core.ui.components.PullToRefreshContainer
import com.enmanuelbergling.core.ui.components.common.MovieLandCard
import com.enmanuelbergling.core.ui.components.common.MovieLandCardPlaceholder
import com.enmanuelbergling.core.ui.core.dimen
import com.enmanuelbergling.core.ui.core.isRefreshing
import com.enmanuelbergling.core.ui.core.shimmerIf
import com.enmanuelbergling.core.ui.model.WatchlistShortcut
import com.enmanuelbergling.core.ui.util.watchlistShortcutId
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

    WatchListDetailsScreen(
        listName = listName,
        movies = movies,
        uiState = uiState,
        isPinned = isPinned,
        onDeleteMovie = viewModel::deleteMovieFromList,
        onMovieDetails = onMovieDetails,
        onBack = onBack,
        onIdle = viewModel::onIdle,
        onAddShortcut = {
            onAddShortcut(WatchlistShortcut(listId, listName))
            onPinned(true)
        },
        onDeleteShortcut = {
            onDeleteShortcut(listId)
            onPinned(false)
        }
    )
}

private const val NO_MOVIE = -1

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchListDetailsScreen(
    listName: String,
    movies: LazyPagingItems<Movie>,
    uiState: SimplerUi,
    isPinned: Boolean,
    onDeleteMovie: (Int) -> Unit,
    onMovieDetails: (movieId: Int) -> Unit,
    onBack: () -> Unit,
    onIdle: () -> Unit,
    onAddShortcut: () -> Unit,
    onDeleteShortcut: () -> Unit,
) {
    HandleUiState(uiState = uiState, onIdle = onIdle, movies::refresh)

    val scrollBehaviour = TopAppBarDefaults.enterAlwaysScrollBehavior()

    var pickedMovie by rememberSaveable {
        mutableIntStateOf(NO_MOVIE)
    }

    if (pickedMovie != NO_MOVIE) {
        DeleteMovieConfirmationDialog(
            onDismiss = { pickedMovie = NO_MOVIE },
            onDelete = {
                onDeleteMovie(pickedMovie)
                pickedMovie = NO_MOVIE
            })
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = listName) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBackIosNew,
                            contentDescription = stringResource(id = R.string.back_icon)
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (isPinned) onDeleteShortcut()
                            else onAddShortcut()
                        }
                    ) {
                        Icon(
                            imageVector = if (isPinned) Icons.Rounded.Bookmark else Icons.Rounded.BookmarkBorder,
                            contentDescription = "bookmark icon"
                        )
                    }
                },
                scrollBehavior = scrollBehaviour
            )
        }
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
                contentPadding = PaddingValues(MaterialTheme.dimen.small),
                modifier = Modifier
                    .shimmerIf { movies.isRefreshing },
                scrollBehavior = scrollBehaviour
            ) {
                if (movies.isRefreshing) {
                    items(12) {
                        MovieLandCardPlaceholder(Modifier.fillMaxWidth())
                    }
                } else {
                    items(movies.itemCount) { index ->
                        val movie = movies[index]
                        movie?.let {
                            val bottomWith by remember {
                                mutableStateOf((-80).dp)
                            }

                            NewerDragListItem(
                                bottomContentWidth = with(LocalDensity.current) { bottomWith.toPx() },
                                bottomContent = {
                                    Box(Modifier.align(Alignment.CenterEnd)) {

                                        IconButton(
                                            onClick = { pickedMovie = it.id },
                                            modifier = Modifier
                                                .padding(horizontal = MaterialTheme.dimen.small)

                                        ) {
                                            Icon(
                                                imageVector = Icons.Rounded.Delete,
                                                contentDescription = stringResource(R.string.delete_icon)
                                            )
                                        }
                                    }
                                },
                                modifier = Modifier.background(
                                    MaterialTheme.colorScheme.surfaceVariant,
                                    CardDefaults.elevatedShape
                                ),
                            ) {

                                MovieLandCard(
                                    movie = movie,
                                    modifier = Modifier
                                        .fillMaxWidth()
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
