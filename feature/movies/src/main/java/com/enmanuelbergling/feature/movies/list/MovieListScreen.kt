package com.enmanuelbergling.feature.movies.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.enmanuelbergling.core.model.movie.Movie
import com.enmanuelbergling.core.ui.R
import com.enmanuelbergling.core.ui.components.HandlerPagingUiState
import com.enmanuelbergling.core.ui.components.common.MovieCard
import com.enmanuelbergling.core.ui.components.common.MovieCardPlaceholder
import com.enmanuelbergling.core.ui.components.listItemWindAnimation
import com.enmanuelbergling.core.ui.core.dimen
import com.enmanuelbergling.core.ui.core.isRefreshing
import com.enmanuelbergling.core.ui.core.isScrollingForward
import com.enmanuelbergling.core.ui.core.items
import com.enmanuelbergling.core.ui.core.shimmerIf
import com.enmanuelbergling.feature.movies.list.viewmodel.NowPlayingMoviesVM
import com.enmanuelbergling.feature.movies.list.viewmodel.PopularMoviesVM
import com.enmanuelbergling.feature.movies.list.viewmodel.TopRatedMoviesVM
import com.enmanuelbergling.feature.movies.list.viewmodel.UpcomingMoviesVM
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

@Composable
fun PopularMoviesScreen(onMovie: (movieId: Int) -> Unit, onBack: () -> Unit) {
    val viewModel = koinViewModel<PopularMoviesVM>()

    val movies = viewModel.movies.collectAsLazyPagingItems()
    MovieListScreen(
        title = "Popular Movies",
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
                    Icon(
                        imageVector = Icons.Rounded.ArrowBackIosNew,
                        contentDescription = stringResource(
                            id = R.string.back_icon
                        )
                    )
                }
            }, scrollBehavior = scrollBehavior)
        },
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) {
        val listState = rememberLazyGridState()

        Box(
            Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            LazyVerticalGrid(
                modifier = Modifier
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
                    .shimmerIf { movies.isRefreshing },
                state = listState,
                columns = GridCells.Adaptive(150.dp),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.small),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.small),
                userScrollEnabled = !movies.isRefreshing,
                contentPadding = PaddingValues(MaterialTheme.dimen.small)
            ) {
                items(movies, key = { movie -> movie.id }) { movie ->
                    movie?.let {
                        MovieCard(
                            imageUrl = movie.posterPath,
                            title = movie.title,
                            rating = movie.voteAverage,
                            titleLines = 1,
                            modifier = Modifier
                                .widthIn(max = 200.dp)
                                .listItemWindAnimation(
                                    isScrollingForward = listState.isScrollingForward(),
                                    rotation = 4f,
                                    durationMillis = 200
                                )
                        ) {
                            onMovie(movie.id)
                        }
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
}