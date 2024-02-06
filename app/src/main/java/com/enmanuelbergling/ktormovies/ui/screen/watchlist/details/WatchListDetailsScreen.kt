package com.enmanuelbergling.ktormovies.ui.screen.watchlist.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.enmanuelbergling.ktormovies.domain.model.core.SimplerUi
import com.enmanuelbergling.ktormovies.domain.model.movie.Movie
import com.enmanuelbergling.ktormovies.ui.components.HandleUiState
import com.enmanuelbergling.ktormovies.ui.components.PullToRefreshContainer
import com.enmanuelbergling.ktormovies.ui.core.dimen
import com.enmanuelbergling.ktormovies.ui.core.isRefreshing
import com.enmanuelbergling.ktormovies.ui.core.shimmerIf
import com.enmanuelbergling.ktormovies.ui.screen.watchlist.components.MovieLandCard
import com.enmanuelbergling.ktormovies.ui.screen.watchlist.components.MovieLandCardPlaceholder
import kotlinx.coroutines.Job
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.koin.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun WatchListDetailsRoute(
    listId: Int,
    listName: String,
    onMovieDetails: (movieId: Int) -> Unit,
    onBack: () -> Unit,
) {

    val viewModel = koinViewModel<WatchListDetailsVM> { parametersOf(listId) }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val movies = viewModel.movies.collectAsLazyPagingItems()

    WatchListDetailsScreen(
        listName = listName,
        movies = movies,
        uiState = uiState,
        onDeleteMovie = viewModel::deleteMovieFromList,
        onMovieDetails = onMovieDetails,
        onBack = onBack,
        onIdle = viewModel::onIdle,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchListDetailsScreen(
    listName: String,
    movies: LazyPagingItems<Movie>,
    uiState: SimplerUi,
    onDeleteMovie: (Int) -> Job,
    onMovieDetails: (movieId: Int) -> Unit,
    onBack: () -> Unit,
    onIdle: () -> Unit,
) {
    HandleUiState(uiState = uiState, onIdle = onIdle, movies::refresh)

    val scrollBehaviour = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text(text = listName) }, navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBackIosNew,
                        contentDescription = "back icon"
                    )
                }
            }, scrollBehavior = scrollBehaviour)
        }
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .shimmerIf { movies.isRefreshing }
        ) {
            PullToRefreshContainer(
                refreshing = false, onRefresh = { movies.refresh() },
                verticalArrangement = Arrangement.spacedBy(
                    MaterialTheme.dimen.small,
                ),
                contentPadding = PaddingValues(MaterialTheme.dimen.small),
                modifier = Modifier
                    .nestedScroll(scrollBehaviour.nestedScrollConnection)
                    .fillMaxSize()
            ) {
                if (movies.isRefreshing) {
                    items(12) {
                        MovieLandCardPlaceholder(Modifier.fillMaxWidth())
                    }
                } else {
                    items(movies) { movie ->
                        movie?.let {
                            MovieLandCard(movie = movie, modifier = Modifier.fillMaxWidth()) {
                                onMovieDetails(movie.id)
                            }
                        }
                    }
                }
            }
        }
    }
}
