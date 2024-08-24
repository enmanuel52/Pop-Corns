package com.enmanuelbergling.feature.movies.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FastForward
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.enmanuelbergling.core.common.util.BASE_BACKDROP_IMAGE_URL
import com.enmanuelbergling.core.model.MovieSection
import com.enmanuelbergling.core.model.core.SimplerUi
import com.enmanuelbergling.core.model.movie.Movie
import com.enmanuelbergling.core.ui.R
import com.enmanuelbergling.core.ui.components.HandleUiState
import com.enmanuelbergling.core.ui.components.common.HeaderMovieInfo
import com.enmanuelbergling.core.ui.components.common.HeaderMoviePlaceholder
import com.enmanuelbergling.core.ui.components.common.MovieCard
import com.enmanuelbergling.core.ui.components.common.MovieCardPlaceholder
import com.enmanuelbergling.core.ui.components.listItemWindAnimation
import com.enmanuelbergling.core.ui.components.walkthrough.components.InstagramPager
import com.enmanuelbergling.core.ui.components.walkthrough.components.ShiftIndicator
import com.enmanuelbergling.core.ui.core.dimen
import com.enmanuelbergling.core.ui.core.isScrollingForward
import com.valentinilk.shimmer.shimmer
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoviesScreen(
    onDetails: (id: Int) -> Unit,
    onMore: (MovieSection) -> Unit,
    onSearch: () -> Unit,
    onFilter: () -> Unit,
    onOpenDrawer: () -> Unit,
) {

    val viewModel = koinViewModel<MoviesVM>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val uiData by viewModel.uiDataState.collectAsStateWithLifecycle()
    val (upcomingMovies, topRatedMovies, nowPlayingMovies, popularMovies) = uiData

    val snackBarHostState = remember {
        SnackbarHostState()
    }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    HandleUiState(uiState, snackState = snackBarHostState, onRetry = viewModel::loadUi)

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            MoviesTopBar(
                scrollBehavior = scrollBehavior,
                onOpenDrawer = onOpenDrawer,
                onSearch = onSearch,
                onFilter = onFilter,
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
        ) {

            MoviesGrid(
                upcoming = upcomingMovies.take(5),
                topRated = topRatedMovies,
                nowPlaying = nowPlayingMovies,
                popular = popularMovies,
                onDetails = onDetails,
                onMore = onMore,
                isLoading = uiState == SimplerUi.Loading
            )
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MoviesTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    onOpenDrawer: () -> Unit,
    onSearch: () -> Unit,
    onFilter: () -> Unit,
) {
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.movies)) },
        navigationIcon = {

            IconButton(
                onClick = onOpenDrawer
            ) {
                Icon(
                    imageVector = Icons.Rounded.Menu,
                    contentDescription = stringResource(R.string.drawer_icon)
                )
            }
        },
        actions = {
            IconButton(onClick = onFilter) {
                Icon(
                    imageVector = Icons.Rounded.FilterList,
                    contentDescription = stringResource(R.string.filter_icon)
                )
            }

            IconButton(onClick = onSearch) {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = stringResource(R.string.search_icon)
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}

@Composable
fun MoviesGrid(
    upcoming: List<Movie>,
    topRated: List<Movie>,
    nowPlaying: List<Movie>,
    popular: List<Movie>,
    modifier: Modifier = Modifier,
    onDetails: (id: Int) -> Unit,
    onMore: (MovieSection) -> Unit,
    isLoading: Boolean,
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(MaterialTheme.dimen.small),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.small)
    ) {
        headersMovies(upcoming, onDetails, isLoading) { onMore(MovieSection.Upcoming) }

        forYouText()

        moviesSection(
            title = context.getString(R.string.top_rated),
            movies = topRated,
            onDetails = onDetails,
            isLoading = isLoading
        ) { onMore(MovieSection.TopRated) }

        moviesSection(
            title = context.getString(R.string.now_playing),
            movies = nowPlaying,
            onDetails = onDetails,
            isLoading = isLoading
        ) { onMore(MovieSection.NowPlaying) }

        moviesSection(
            title = context.getString(R.string.popular),
            movies = popular,
            onDetails = onDetails,
            isLoading = isLoading
        ) { onMore(MovieSection.Popular) }
    }
}


fun LazyListScope.moviesSection(
    title: String,
    movies: List<Movie>,
    onDetails: (id: Int) -> Unit,
    isLoading: Boolean,
    onMore: () -> Unit,
) {
    item {
        if (movies.isEmpty() && isLoading) {
            Row(Modifier.shimmer()) {
                repeat(5) {
                    MovieCardPlaceholder(
                        modifier = Modifier
                            .padding(start = MaterialTheme.dimen.small)
                            .width(180.dp)
                    )
                }
            }
        } else {
            Column {

                SectionHeader(
                    title = title,
                    modifier = Modifier.padding(horizontal = MaterialTheme.dimen.small),
                    onMore = onMore
                )

                Spacer(modifier = Modifier.height(MaterialTheme.dimen.small))

                val listState = rememberLazyListState()
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.small),
                    state = listState
                ) {
                    items(movies) { movie ->
                        MovieCard(
                            imageUrl = movie.posterPath,
                            title = movie.title,
                            rating = movie.voteAverage,
                            modifier = Modifier
                                .width(180.dp)
                                .listItemWindAnimation(
                                    listState.isScrollingForward(),
                                    Orientation.Horizontal
                                )
                        ) {
                            onDetails(movie.id)
                        }
                    }
                }
            }
        }
    }
}

private fun LazyListScope.headersMovies(
    upcoming: List<Movie>,
    onDetails: (id: Int) -> Unit,
    isLoading: Boolean,
    onMore: () -> Unit,
) {

    item {

        if (upcoming.isEmpty() && isLoading) {
            HeaderMoviePlaceholder(Modifier.shimmer())
        } else {
            val pagerState = rememberPagerState { upcoming.count() }
            Column {
                SectionHeader(
                    title = stringResource(R.string.upcoming),
                    modifier = Modifier.padding(horizontal = MaterialTheme.dimen.small),
                    onMore = onMore
                )

                Spacer(modifier = Modifier.height(MaterialTheme.dimen.small))

                InstagramPager(
                    state = pagerState,
                    pageSpacing = MaterialTheme.dimen.verySmall,
                    boxAngle = 20
                ) { page, pageModifier ->
                    val movie = upcoming.getOrNull(page)
                    movie?.let {
                        ElevatedCard(onClick = { onDetails(movie.id) }, modifier = pageModifier) {
                            AsyncImage(
                                model = BASE_BACKDROP_IMAGE_URL + movie.backdropPath.orEmpty(),
                                contentDescription = "header image",
                                placeholder = painterResource(
                                    id = R.drawable.pop_corn_and_cinema_backdrop
                                ),
                                error = painterResource(
                                    id = R.drawable.pop_corn_and_cinema_backdrop
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(1.78f),
                                contentScale = ContentScale.FillWidth
                            )
                        }
                    }
                }

                AnimatedContent(
                    targetState = pagerState.currentPage,
                    transitionSpec = {
                        slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up) togetherWith
                                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Down)
                    },
                    label = "movie info animation",
                    modifier = Modifier.padding(MaterialTheme.dimen.small)
                ) { page ->
                    val movie = upcoming.getOrNull(page)

                    HeaderMovieInfo(title = movie?.title.orEmpty(), rating = movie?.voteAverage ?: .0)
                }

                Spacer(modifier = Modifier.height(MaterialTheme.dimen.verySmall))

                ShiftIndicator(
                    pagerState = pagerState,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    stepSize = MaterialTheme.dimen.small,
                    spaceBetween = MaterialTheme.dimen.small
                )
            }
        }

    }
}

@Composable
private fun SectionHeader(title: String, modifier: Modifier = Modifier, onMore: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        IconButton(onClick = onMore) {
            Icon(
                imageVector = Icons.Rounded.FastForward,
                contentDescription = stringResource(R.string.more_icon)
            )
        }
    }
}

private fun LazyListScope.forYouText() {
    item {
        Text(
            text = stringResource(R.string.for_you),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = MaterialTheme.dimen.small)
        )
    }
}