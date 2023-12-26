package com.enmanuelbergling.ktormovies.ui.screen.movie.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.enmanuelbergling.ktormovies.domain.model.MovieSection
import com.enmanuelbergling.ktormovies.domain.model.movie.Movie
import com.enmanuelbergling.ktormovies.ui.core.dimen
import com.enmanuelbergling.ktormovies.ui.screen.movie.components.HeaderMovieCard
import com.enmanuelbergling.ktormovies.ui.screen.movie.components.HeaderMoviePlaceholder
import com.enmanuelbergling.ktormovies.ui.screen.movie.components.MovieCard
import com.enmanuelbergling.ktormovies.ui.screen.movie.components.MovieCardPlaceholder
import com.valentinilk.shimmer.shimmer
import org.koin.androidx.compose.koinViewModel

@Composable
fun MoviesScreen(onDetails: (id: Int) -> Unit, onMore: (MovieSection) -> Unit) {

    val viewModel = koinViewModel<MoviesVM>()

    val topRatedMovies by viewModel.topRatedState.collectAsStateWithLifecycle()
    val upcomingMovies by viewModel.upcomingState.collectAsStateWithLifecycle()
    val nowPlayingMovies by viewModel.nowPlayingState.collectAsStateWithLifecycle()

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
                upcoming = upcomingMovies,
                topRated = topRatedMovies,
                nowPlaying = nowPlayingMovies,
                onDetails = onDetails,
                onMore = onMore
            )
        }
    }
}

@Composable
fun MoviesGrid(
    upcoming: List<Movie>,
    topRated: List<Movie>,
    nowPlaying: List<Movie>,
    modifier: Modifier = Modifier,
    onDetails: (id: Int) -> Unit,
    onMore: (MovieSection) -> Unit,
) {


    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(MaterialTheme.dimen.verySmall),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.small)
    ) {
        headersMovies(upcoming, onDetails) { onMore(MovieSection.Upcoming) }

        forYouText()

        moviesSection("Top Rated", topRated, onDetails) { onMore(MovieSection.TopRated) }

        moviesSection("Now playing", nowPlaying, onDetails) { onMore(MovieSection.NowPlaying) }
    }
}


fun LazyListScope.moviesSection(
    title: String,
    movies: List<Movie>,
    onDetails: (id: Int) -> Unit,
    onMore: () -> Unit,
) {
    item {
        if (movies.isEmpty()) {
            Row(Modifier.shimmer()) {
                repeat(5) {
                    MovieCardPlaceholder(modifier = Modifier.padding(start = MaterialTheme.dimen.small))
                }
            }
        } else {
            Column {

                SectionHeader(title = title, onMore = onMore)

                Spacer(modifier = Modifier.height(MaterialTheme.dimen.small))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.small)) {
                    items(movies) { movie ->
                        MovieCard(
                            imageUrl = movie.posterPath,
                            title = movie.title,
                            rating = movie.voteAverage,
                            modifier = Modifier
                                .widthIn(max = 200.dp)
                        ) {
                            onDetails(movie.id)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
private fun LazyListScope.headersMovies(
    upcoming: List<Movie>,
    onDetails: (id: Int) -> Unit,
    onMore: () -> Unit,
) {

    item {

        if (upcoming.isEmpty()) {
            HeaderMoviePlaceholder(Modifier.shimmer())
        } else {
            val pagerState = rememberPagerState { upcoming.count() }
            Column {
                SectionHeader(title = "Upcoming", onMore = onMore)

                Spacer(modifier = Modifier.height(MaterialTheme.dimen.small))

                HorizontalPager(state = pagerState) { page ->
                    val movie = upcoming.getOrNull(page)
                    movie?.let {
                        HeaderMovieCard(
                            movie.backdropPath, movie.title, movie.voteAverage.div(2)
                        ) {
                            onDetails(movie.id)
                        }
                    }
                }
            }
        }

    }
}

@Composable
private fun SectionHeader(title: String, modifier: Modifier = Modifier, onMore: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = "more",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .clip(MaterialTheme.shapes.small)
                .clickable { onMore() }
        )
    }
}

private fun LazyListScope.forYouText() {
    item {
        Text(
            text = "For you",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = MaterialTheme.dimen.small)
        )
    }
}

val genreFilters = listOf("All", "Action", "Thriller", "Comedy", "Drama", "Romantic", "Psycho")