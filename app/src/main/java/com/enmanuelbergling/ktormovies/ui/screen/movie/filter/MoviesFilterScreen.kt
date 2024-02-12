package com.enmanuelbergling.ktormovies.ui.screen.movie.filter

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.enmanuelbergling.ktormovies.domain.model.movie.Genre
import com.enmanuelbergling.ktormovies.domain.model.movie.Movie
import com.enmanuelbergling.ktormovies.domain.model.movie.MovieFilter
import com.enmanuelbergling.ktormovies.domain.model.movie.SortCriteria
import com.enmanuelbergling.ktormovies.ui.core.dimen
import com.enmanuelbergling.ktormovies.ui.screen.movie.filter.model.MovieFilterEvent
import com.enmanuelbergling.ktormovies.ui.screen.watchlist.components.MovieLandCard
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
                title = { Text(text = "Advanced Movie Filter") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBackIosNew,
                            contentDescription = "back icon"
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->
        LazyColumn(
            Modifier
                .padding(paddingValues)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.small),
            contentPadding = PaddingValues(MaterialTheme.dimen.verySmall)
        ) {
            item {
                Column {
                    Column {
                        Text(
                            text = "Order By:",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(MaterialTheme.dimen.small))

                        LazyRow(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.small)) {
                            items(SortCriteria.entries) {
                                FilterChip(
                                    selected = it == filter.sortBy,
                                    onClick = { onFilter(MovieFilterEvent.PickOrderCriteria(it)) },
                                    label = { Text(text = "$it") })
                            }
                        }
                    }
                }
            }

            item {
                Column {
                    Column {
                        Text(
                            text = "Genres:",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(MaterialTheme.dimen.small))

                        LazyHorizontalStaggeredGrid(
                            rows = StaggeredGridCells.Fixed(2),
                            horizontalItemSpacing = MaterialTheme.dimen.small,
                            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.small),
                            modifier = Modifier.heightIn(max = FilterChipDefaults.Height.times(3))
                        ) {
                            items(availableGenres) {
                                FilterChip(
                                    selected = it in filter.genres,
                                    onClick = { onFilter(MovieFilterEvent.PickGenre(it)) },
                                    label = { Text(text = it.name) })
                            }
                        }
                    }
                }
            }

            items(movies, key = { it.id }) {
                it?.let { movie ->
                    MovieLandCard(movie = movie, Modifier.fillMaxWidth()) {
                        onMovie(movie.id)
                    }
                }
            }
        }
    }
}