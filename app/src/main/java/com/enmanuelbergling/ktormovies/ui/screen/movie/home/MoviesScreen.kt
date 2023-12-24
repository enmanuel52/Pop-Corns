package com.enmanuelbergling.ktormovies.ui.screen.movie.home

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridScope
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DirectionsRun
import androidx.compose.material.icons.rounded.Upcoming
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.enmanuelbergling.ktormovies.R
import com.enmanuelbergling.ktormovies.domain.BASE_IMAGE_URL
import com.enmanuelbergling.ktormovies.domain.model.movie.Movie
import com.enmanuelbergling.ktormovies.ui.core.LocalTopAppScrollBehaviour
import com.enmanuelbergling.ktormovies.ui.core.dimen
import com.valentinilk.shimmer.shimmer
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoviesScreen(onDetails: (id: Int) -> Unit) {

    val viewModel = koinViewModel<MoviesVM>()
    val topRatedMovies = viewModel.topRatedMovies.collectAsLazyPagingItems()

    val upcomingMovies by viewModel.upcomingState.collectAsStateWithLifecycle()
    val nowPlayingMovies by viewModel.nowPlayingState.collectAsStateWithLifecycle()

    val scrollBehaviour = LocalTopAppScrollBehaviour.current!!

    var selectedGenreIndex by remember {
        mutableIntStateOf(0)
    }

    Scaffold { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            ScrollableTabRow(
                selectedTabIndex = selectedGenreIndex,
                modifier = Modifier.padding(vertical = MaterialTheme.dimen.verySmall),

                ) {
                genreFilters.forEachIndexed { index, genre ->
                    Tab(
                        selected = index == selectedGenreIndex,
                        onClick = { selectedGenreIndex = index },
                        text = {
                            Text(
                                text = genre
                            )
                        })
                }
            }

            MoviesGrid(
                movies = topRatedMovies,
                nowPlaying = nowPlayingMovies,
                upcoming = upcomingMovies,
                onDetails = onDetails,
                modifier = Modifier.nestedScroll(scrollBehaviour.nestedScrollConnection)
            )

            if (topRatedMovies.itemCount != 0 && topRatedMovies.loadState.append == LoadState.Loading) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = MaterialTheme.dimen.superSmall)
                )
            }
        }
    }
}

enum class HeaderMovie(val title: String, val icon: ImageVector) {
    Upcoming("Upcoming", Icons.Rounded.Upcoming),
    NowPlaying("Now Playing", Icons.Rounded.DirectionsRun);
}

@Composable
fun MoviesGrid(
    movies: LazyPagingItems<Movie>,
    nowPlaying: List<Movie>,
    upcoming: List<Movie>,
    modifier: Modifier = Modifier,
    onDetails: (id: Int) -> Unit,
) {


    LazyVerticalStaggeredGrid(
        modifier = modifier.fillMaxWidth(),
        columns = StaggeredGridCells.Adaptive(150.dp),
        contentPadding = PaddingValues(MaterialTheme.dimen.verySmall),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.small),
        verticalItemSpacing = MaterialTheme.dimen.small,
        userScrollEnabled = movies.itemCount > 0
    ) {
        headersMovies(upcoming, nowPlaying, onDetails)

        forYouText()

        items(movies.itemCount) { index ->
            movies[index]?.let { movie ->
                MovieItem(
                    imageUrl = BASE_IMAGE_URL + movie.posterPath,
                    onCLick = { onDetails(movie.id) }
                )
            }
        }
    }

    if (movies.itemCount == 0 && movies.loadState.refresh == LoadState.Loading) {
        MoviesShimmerGrid()
    }
}

private fun LazyStaggeredGridScope.headersMovies(
    upcoming: List<Movie>,
    nowPlaying: List<Movie>,
    onDetails: (id: Int) -> Unit,
) {
    item(span = StaggeredGridItemSpan.FullLine) {
        var selectedHeader by remember {
            mutableStateOf(HeaderMovie.NowPlaying)
        }

        Column(
            modifier = Modifier
                .padding(horizontal = MaterialTheme.dimen.small)
        ) {
            HeaderFilter(selectedHeader) { selectedHeader = it }

            Spacer(modifier = Modifier.height(MaterialTheme.dimen.small))

            HeaderMovies(selectedHeader, upcoming, nowPlaying, onDetails)

        }
    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun HeaderMovies(
    selectedHeader: HeaderMovie,
    upcoming: List<Movie>,
    nowPlaying: List<Movie>,
    onDetails: (id: Int) -> Unit,
) {
    val pagerState = rememberPagerState { 10 }

    HorizontalPager(
        state = pagerState,
        userScrollEnabled = when (selectedHeader) {
            HeaderMovie.Upcoming -> upcoming.isNotEmpty()
            HeaderMovie.NowPlaying -> nowPlaying.isNotEmpty()
        },
        pageSpacing = MaterialTheme.dimen.verySmall
    ) { pageIndex ->

        when (selectedHeader) {
            HeaderMovie.Upcoming -> if (upcoming.isEmpty()) {
                HeaderMoviePlaceholder(Modifier.shimmer())
            } else {
                HeaderMovie(movie = upcoming[pageIndex], onDetails)
            }

            HeaderMovie.NowPlaying -> if (nowPlaying.isEmpty()) {
                HeaderMoviePlaceholder(Modifier.shimmer())
            } else {
                HeaderMovie(movie = nowPlaying[pageIndex], onDetails)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeaderFilter(filter: HeaderMovie, onFilter: (HeaderMovie) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        HeaderMovie.values().forEach {
            FilterChip(
                selected = it == filter,
                onClick = { onFilter(it) },
                label = {
                    Text(
                        text = it.title,
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = it.icon,
                        contentDescription = "header switch icon"
                    )
                },
                shape = MaterialTheme.shapes.small,
            )
        }
    }
}

@Preview
@Composable
fun HeaderMoviePlaceholder(modifier: Modifier = Modifier) {
    Column(modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.6f)
                .background(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.shapes.medium)
        )

        Spacer(modifier = Modifier.height(MaterialTheme.dimen.small))

        Box(
            modifier = Modifier
                .align(CenterHorizontally)
                .fillMaxWidth(.7f)
                .height(16.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant, RectangleShape)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeaderMovie(movie: Movie, onClick: (id: Int) -> Unit) {
    OutlinedCard(onClick = { onClick(movie.id) }) {
        AsyncImage(
            model = BASE_IMAGE_URL + movie.backdropPath,
            contentDescription = "header image",
            placeholder = painterResource(
                id = R.drawable.pop_corn_and_cinema_backdrop
            ),
            error = painterResource(
                id = R.drawable.pop_corn_and_cinema_backdrop
            ),
            modifier = Modifier.clip(MaterialTheme.shapes.medium)
        )

        Spacer(modifier = Modifier.height(MaterialTheme.dimen.small))

        Text(
            text = movie.title,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}

private fun LazyStaggeredGridScope.forYouText() {
    item(span = StaggeredGridItemSpan.FullLine) {
        Text(
            text = "For you",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = MaterialTheme.dimen.small)
        )
    }
}

val genreFilters = listOf("Action", "Thriller", "Comedy", "Drama", "Romantic", "Psycho")


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieItem(
    imageUrl: String,
    modifier: Modifier = Modifier,
    onCLick: () -> Unit,
) {
    Card(
        onCLick,
        modifier.animateContentSize()
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = "movie image",
            error = painterResource(id = R.drawable.pop_corn_and_cinema_poster),
            placeholder = painterResource(id = R.drawable.pop_corn_and_cinema_poster)
        )
    }
}

@Composable
fun MoviesShimmerGrid(
    modifier: Modifier = Modifier,
    columns: GridCells = GridCells.Adaptive(150.dp)
) {
    LazyVerticalGrid(
        modifier = modifier
            .fillMaxWidth()
            .shimmer(),
        columns = columns,
        contentPadding = PaddingValues(MaterialTheme.dimen.verySmall),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.small),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.small),
        userScrollEnabled = false
    ) {
        items(50) {
            MovieShimmerItem()
        }
    }
}

@Composable
fun MovieShimmerItem(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .aspectRatio(.7f)
            .background(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.shapes.medium)
    )
}