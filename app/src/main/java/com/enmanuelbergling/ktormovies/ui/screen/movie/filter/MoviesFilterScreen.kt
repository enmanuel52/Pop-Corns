package com.enmanuelbergling.ktormovies.ui.screen.movie.filter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.VerticalAlignTop
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.enmanuelbergling.ktormovies.R
import com.enmanuelbergling.ktormovies.domain.model.movie.Genre
import com.enmanuelbergling.ktormovies.domain.model.movie.Movie
import com.enmanuelbergling.ktormovies.domain.model.movie.MovieFilter
import com.enmanuelbergling.ktormovies.domain.model.movie.SortCriteria
import com.enmanuelbergling.ktormovies.ui.components.FromDirection
import com.enmanuelbergling.ktormovies.ui.components.LinearLoading
import com.enmanuelbergling.ktormovies.ui.components.ShowUpFrom
import com.enmanuelbergling.ktormovies.ui.core.dimen
import com.enmanuelbergling.ktormovies.ui.core.isAppending
import com.enmanuelbergling.ktormovies.ui.core.isRefreshing
import com.enmanuelbergling.ktormovies.ui.screen.movie.filter.model.MovieFilterEvent
import com.enmanuelbergling.ktormovies.ui.screen.watchlist.components.MovieLandCard
import kotlinx.coroutines.launch
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.koin.koinViewModel

@Composable
fun MoviesFilterRoute(
    onBack: () -> Unit,
    onMovie: (Int) -> Unit,
) {
    val viewModel: MoviesFilterVM = koinViewModel()

    val filterState by viewModel.filterState.collectAsStateWithLifecycle()
    val availableGenres by viewModel.genresState.collectAsStateWithLifecycle()
    val movies = viewModel.movies.collectAsLazyPagingItems()

    MoviesFilterScreen(
        onBack = onBack,
        movies = movies,
        availableGenres = availableGenres,
        filter = filterState,
        onFilter = viewModel::onEvent,
        onMovie = onMovie
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MoviesFilterScreen(
    onBack: () -> Unit,
    movies: LazyPagingItems<Movie>,
    availableGenres: List<Genre>,
    filter: MovieFilter,
    onFilter: (MovieFilterEvent) -> Unit,
    onMovie: (Int) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.advanced_movie_filter)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBackIosNew,
                            contentDescription = stringResource(id = R.string.back_icon)
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->
        Column(
            Modifier
                .padding(paddingValues)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .fillMaxSize(),
        ) {
            if (movies.isRefreshing) {
                LinearLoading()
            }

            val lazyListState = rememberLazyListState()

            val start by remember {
                derivedStateOf {
                    lazyListState.firstVisibleItemIndex == 0
                }
            }

            val scope = rememberCoroutineScope()

            Box {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.small),
                    contentPadding = PaddingValues(MaterialTheme.dimen.verySmall),
                    state = lazyListState
                ) {
                    item {
                        Column {
                            Column {
                                Text(
                                    text = stringResource(R.string.order_by),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold
                                )

                                Spacer(modifier = Modifier.height(MaterialTheme.dimen.small))

                                LazyRow(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.small)) {
                                    items(SortCriteria.entries) {
                                        FilterChip(
                                            selected = it == filter.sortBy,
                                            onClick = {
                                                onFilter(
                                                    MovieFilterEvent.PickOrderCriteria(
                                                        it
                                                    )
                                                )
                                            },
                                            label = { Text(text = stringResource(id = it.label)) },
                                            leadingIcon = {
                                                if (it == filter.sortBy) {
                                                    Icon(
                                                        imageVector = Icons.Rounded.Check,
                                                        contentDescription = stringResource(R.string.filter_checked_icon)
                                                    )
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    item {
                        ShowUpFrom(
                            visible = availableGenres.isNotEmpty(),
                            fromDirection = FromDirection.Top
                        ) {
                            Column {
                                Text(
                                    text = stringResource(R.string.genres),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold
                                )

                                Spacer(modifier = Modifier.height(MaterialTheme.dimen.small))

                                LazyHorizontalStaggeredGrid(
                                    rows = StaggeredGridCells.Fixed(2),
                                    horizontalItemSpacing = MaterialTheme.dimen.small,
                                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.small),
                                    modifier = Modifier.heightIn(
                                        max = FilterChipDefaults.Height.times(
                                            2
                                        ).plus(MaterialTheme.dimen.small)
                                    )
                                ) {
                                    items(availableGenres) {
                                        FilterChip(
                                            selected = it in filter.genres,
                                            onClick = { onFilter(MovieFilterEvent.PickGenre(it)) },
                                            label = { Text(text = it.name) },
                                            leadingIcon = {
                                                if (it in filter.genres) {
                                                    Icon(
                                                        imageVector = Icons.Rounded.Check,
                                                        contentDescription = stringResource(id = R.string.filter_checked_icon)
                                                    )
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    items(movies) {
                        it?.let { movie ->
                            MovieLandCard(movie = movie, Modifier.fillMaxWidth()) {
                                onMovie(movie.id)
                            }
                        }
                    }

                    item {
                        if (movies.isAppending) {
                            LinearLoading()
                        }
                    }
                }

                ShowUpFrom(
                    !start,
                    FromDirection.Bottom,
                    modifier = Modifier.align(Alignment.BottomCenter)
                ) {
                    SmallFloatingActionButton(
                        onClick = {
                            scope.launch {
                                lazyListState.animateScrollToItem(0)
                            }
                        },
                        modifier = Modifier.padding(bottom = MaterialTheme.dimen.small),
                        shape = CircleShape
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.VerticalAlignTop,
                            contentDescription = stringResource(R.string.to_start_icon)
                        )
                    }
                }
            }
        }
    }
}