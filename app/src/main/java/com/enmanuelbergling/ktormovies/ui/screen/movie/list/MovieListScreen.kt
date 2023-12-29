package com.enmanuelbergling.ktormovies.ui.screen.movie.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIos
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.enmanuelbergling.ktormovies.domain.model.movie.Movie
import com.enmanuelbergling.ktormovies.ui.components.HandlerPagingUiState
import com.enmanuelbergling.ktormovies.ui.components.listItemWindAnimation
import com.enmanuelbergling.ktormovies.ui.core.dimen
import com.enmanuelbergling.ktormovies.ui.core.isRefreshing
import com.enmanuelbergling.ktormovies.ui.core.isScrollingForward
import com.enmanuelbergling.ktormovies.ui.core.shimmerIf
import com.enmanuelbergling.ktormovies.ui.screen.movie.components.MovieCard
import com.enmanuelbergling.ktormovies.ui.screen.movie.components.MovieCardPlaceholder
import com.enmanuelbergling.ktormovies.ui.screen.movie.list.viewmodel.NowPlayingMoviesVM
import com.enmanuelbergling.ktormovies.ui.screen.movie.list.viewmodel.TopRatedMoviesVM
import com.enmanuelbergling.ktormovies.ui.screen.movie.list.viewmodel.UpcomingMoviesVM
import org.koin.androidx.compose.koinViewModel


@Composable
fun UpcomingMoviesScreen(onMovie: (movieId: Int) -> Unit, onBack: () -> Unit) {
    val viewModel = koinViewModel<UpcomingMoviesVM>()

    val movies = viewModel.movies.collectAsLazyPagingItems()
    MovieListScreen(title = "Upcoming Movies", onBack = onBack, movies = movies, onMovie = onMovie)
}

@Composable
fun TopRatedMoviesScreen(onMovie: (movieId: Int) -> Unit, onBack: () -> Unit) {
    val viewModel = koinViewModel<TopRatedMoviesVM>()

    val movies = viewModel.movies.collectAsLazyPagingItems()
    MovieListScreen(title = "Top Rated Movies", onBack = onBack, movies = movies, onMovie = onMovie)
}

@Composable
fun NowPlayingMoviesScreen(onMovie: (movieId: Int) -> Unit, onBack: () -> Unit) {
    val viewModel = koinViewModel<NowPlayingMoviesVM>()

    val movies = viewModel.movies.collectAsLazyPagingItems()
    MovieListScreen(
        title = "Now Playing Movies",
        onBack = onBack,
        movies = movies,
        onMovie = onMovie
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MovieListScreen(
    title: String,
    onBack: () -> Unit,
    movies: LazyPagingItems<Movie>,
    onMovie: (movieId: Int) -> Unit,
) {
    val snackBarHostState = remember {
        SnackbarHostState()
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    HandlerPagingUiState(items = movies, snackState = snackBarHostState)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text(text = title) }, navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(imageVector = Icons.Rounded.ArrowBackIos, contentDescription = "back icon")
                }
            }, scrollBehavior = scrollBehavior)
        },
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) {
        val listState = rememberLazyStaggeredGridState()

        LazyVerticalStaggeredGrid(
            modifier = Modifier
                .padding(it)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .shimmerIf { movies.isRefreshing },
            state = listState,
            columns = StaggeredGridCells.Adaptive(150.dp),
            verticalItemSpacing = MaterialTheme.dimen.small,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.small),
            userScrollEnabled = !movies.isRefreshing,
            contentPadding = PaddingValues(MaterialTheme.dimen.small)
        ) {
            items(movies.itemSnapshotList.items) { movie ->
                MovieCard(
                    imageUrl = movie.posterPath,
                    title = movie.title,
                    rating = movie.voteAverage,
                    modifier = Modifier
                        .widthIn(max = 200.dp)
                        .listItemWindAnimation(isScrollingForward = listState.isScrollingForward())
                ) {
                    onMovie(movie.id)
                }
            }
            if (movies.isRefreshing) {
                items(50) {
                    MovieCardPlaceholder()
                }
            }
        }
    }
}