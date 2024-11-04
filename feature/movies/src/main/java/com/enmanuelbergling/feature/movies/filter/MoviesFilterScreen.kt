package com.enmanuelbergling.feature.movies.filter

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ContextualFlowRow
import androidx.compose.foundation.layout.ContextualFlowRowOverflow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRowOverflowScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.VerticalAlignTop
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.enmanuelbergling.core.model.movie.Genre
import com.enmanuelbergling.core.model.movie.Movie
import com.enmanuelbergling.core.model.movie.MovieFilter
import com.enmanuelbergling.core.model.movie.SortCriteria
import com.enmanuelbergling.core.ui.R
import com.enmanuelbergling.core.ui.components.FromDirection
import com.enmanuelbergling.core.ui.components.LinearLoading
import com.enmanuelbergling.core.ui.components.ShowUpFrom
import com.enmanuelbergling.core.ui.components.common.MovieLandCard
import com.enmanuelbergling.core.ui.core.dimen
import com.enmanuelbergling.core.ui.core.isAppending
import com.enmanuelbergling.core.ui.core.isRefreshing
import com.enmanuelbergling.feature.movies.filter.model.MovieFilterEvent
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
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
                        SectionFilterUi(
                            filter = filter,
                            onFilter = onFilter,
                        )
                    }

                    item {
                        GenresUi(
                            availableGenres = availableGenres,
                            filter = filter,
                            onFilter = onFilter
                        )
                    }

                    items(movies.itemCount) {
                        movies[it]?.let { movie ->
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

@Composable
private fun SectionFilterUi(
    filter: MovieFilter,
    modifier: Modifier = Modifier,
    onFilter: (MovieFilterEvent) -> Unit,
) {
    Column(modifier) {
        Text(
            text = stringResource(R.string.order_by),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(MaterialTheme.dimen.small))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.small)) {
            items(SortCriteria.entries) {
                ElevatedFilterChip(
                    selected = it == filter.sortBy,
                    onClick = {
                        onFilter(
                            MovieFilterEvent.PickOrderCriteria(
                                it
                            )
                        )
                    },
                    label = { Text(text = stringResource(id = it.labelResource)) },
                    shape = CircleShape,
                )
            }
        }
    }
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun GenresUi(
    availableGenres: List<Genre>,
    filter: MovieFilter,
    onFilter: (MovieFilterEvent) -> Unit,
) {
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

            var maxLines by remember {
                mutableIntStateOf(2)
            }

            ContextualFlowRow(
                itemCount = availableGenres.size,
                maxLines = maxLines,
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.small),
                overflow = ContextualFlowRowOverflow.expandOrCollapseIndicator(
                    expandIndicator = {
                        ElevatedAssistChip(
                            onClick = { maxLines += 2 },
                            label = {
                                Text(text = "${this@expandOrCollapseIndicator.itemsLeft} more")
                            },
                            colors = AssistChipDefaults.elevatedAssistChipColors(
                                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                                labelColor = MaterialTheme.colorScheme.onTertiaryContainer,
                            ), shape = CircleShape,
                        )
                    },
                    collapseIndicator = {
                        ElevatedAssistChip(
                            onClick = { maxLines = 2 },
                            label = {
                                Text(text = stringResource(id = R.string.hide))
                            },
                            colors = AssistChipDefaults.elevatedAssistChipColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer,
                                labelColor = MaterialTheme.colorScheme.onErrorContainer
                            ), shape = CircleShape
                        )
                    }
                ),
                modifier = Modifier.animateContentSize(
                    spring(Spring.DampingRatioLowBouncy, Spring.StiffnessLow)
                )
            ) { index ->
                val currentGenre = availableGenres[index]

                ElevatedFilterChip(
                    selected = currentGenre in filter.genres,
                    onClick = { onFilter(MovieFilterEvent.PickGenre(currentGenre)) },
                    label = { Text(text = currentGenre.name) },
                    shape = CircleShape,
                )
            }

        }
    }
}

val SortCriteria.labelResource: Int
    get() = when (this) {
        SortCriteria.Popularity -> R.string.popularity
        SortCriteria.VoteAverage -> R.string.vote_average
        SortCriteria.VoteCount -> R.string.vote_count
        SortCriteria.Revenue -> R.string.revenue
    }

@OptIn(ExperimentalLayoutApi::class)
val FlowRowOverflowScope.itemsLeft
    get() = totalItemCount - shownItemCount