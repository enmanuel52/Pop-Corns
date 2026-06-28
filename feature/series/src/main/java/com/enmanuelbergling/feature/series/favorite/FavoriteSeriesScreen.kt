package com.enmanuelbergling.feature.series.favorite

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.enmanuelbergling.core.model.tv.TvShow
import com.enmanuelbergling.core.ui.components.TinderSwipeToDismissContainer
import com.enmanuelbergling.core.ui.components.common.MovieCard
import com.enmanuelbergling.core.ui.components.common.MovieCardPlaceholder
import com.enmanuelbergling.core.ui.core.ObserveAsEvents
import com.enmanuelbergling.core.ui.core.dimen
import com.enmanuelbergling.core.ui.core.isRefreshing
import com.enmanuelbergling.core.ui.core.items
import com.enmanuelbergling.core.ui.core.shimmerIf
import com.enmanuelbergling.feature.series.R
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun FavoriteSeriesRoute(
    onSeriesDetails: (seriesId: Int) -> Unit,
    onBack: () -> Unit,
) {
    val viewModel = koinViewModel<FavoriteSeriesVM>()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val favorites = viewModel.favorites.collectAsLazyPagingItems()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val removeSeriesErrorMessage =
        stringResource(R.string.the_series_could_not_be_removed_from_favorites)
    val seriesRemovedMessage = stringResource(R.string.series_removed_from_favorites)
    val undoMessage = stringResource(com.enmanuelbergling.core.ui.R.string.undo)
    val retryMessage = stringResource(com.enmanuelbergling.core.ui.R.string.retry)

    ObserveAsEvents(viewModel.sideEffectChannel) {
        when (it) {
            is FavoriteSeriesSideEffect.NavigateToDetails -> onSeriesDetails(it.seriesId)
            is FavoriteSeriesSideEffect.UndoRemoveSeries -> scope.launch {
                val result = snackbarHostState.showSnackbar(
                    message = seriesRemovedMessage,
                    actionLabel = undoMessage,
                    duration = SnackbarDuration.Short,
                )
                when (result) {
                    SnackbarResult.Dismissed -> viewModel.onEvent(FavoriteSeriesEvent.RemoveSeries(it.seriesId))
                    SnackbarResult.ActionPerformed -> viewModel.onEvent(FavoriteSeriesEvent.UndoRemove)
                }
            }

            is FavoriteSeriesSideEffect.RemoveSeriesError -> scope.launch {
                val result = snackbarHostState.showSnackbar(
                    message = removeSeriesErrorMessage,
                    actionLabel = retryMessage,
                    duration = SnackbarDuration.Indefinite,
                    withDismissAction = true,
                )
                when (result) {
                    SnackbarResult.Dismissed -> viewModel.onEvent(
                        FavoriteSeriesEvent.OnRemoveSeriesErrorDismissed(it.seriesId)
                    )

                    SnackbarResult.ActionPerformed -> viewModel.onEvent(
                        FavoriteSeriesEvent.RemoveSeries(it.seriesId)
                    )
                }
            }
        }
    }

    FavoriteSeriesScreen(
        favorites = favorites,
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onBack = onBack,
        onEvent = viewModel::onEvent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FavoriteSeriesScreen(
    favorites: LazyPagingItems<TvShow>,
    uiState: FavoriteSeriesState,
    snackbarHostState: SnackbarHostState,
    onBack: () -> Unit,
    onEvent: (FavoriteSeriesEvent) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(R.string.favorite_series)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBackIosNew,
                            contentDescription = stringResource(com.enmanuelbergling.core.ui.R.string.back_icon)
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { paddingValues ->
        val listState = rememberLazyGridState()

        Box(
            Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
                    .shimmerIf { favorites.isRefreshing },
                state = listState,
                columns = GridCells.Adaptive(150.dp),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.small),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.small),
                userScrollEnabled = !favorites.isRefreshing,
                contentPadding = PaddingValues(MaterialTheme.dimen.small)
            ) {
                if (favorites.isRefreshing) {
                    items(50) {
                        MovieCardPlaceholder()
                    }
                } else {
                    items(favorites, key = { tvShow -> tvShow.id }) { tvShow ->
                        tvShow?.let {
                            TinderSwipeToDismissContainer(
                                visible = tvShow.id !in uiState.deletedSeriesIds,
                                onDismissFromStartToEnd = {
                                    onEvent(FavoriteSeriesEvent.NavigateToDetails(tvShow.id))
                                },
                                onDismissFromEndToStart = {
                                    onEvent(FavoriteSeriesEvent.OnRemoveSeries(tvShow.id))
                                },
                                endToStartIcon = { TrashIcon() },
                                modifier = Modifier.animateItem()
                            ) {
                                MovieCard(
                                    imageUrl = tvShow.posterPath.orEmpty(),
                                    title = tvShow.name,
                                    rating = tvShow.voteAverage,
                                    titleLines = 1,
                                ) {
                                    onEvent(FavoriteSeriesEvent.NavigateToDetails(tvShow.id))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TrashIcon() {
    Icon(
        painterResource(com.enmanuelbergling.core.ui.R.drawable.trash_filled),
        contentDescription = "remove",
        modifier = Modifier
            .padding(8.dp)
            .size(48.dp),
        tint = MaterialTheme.colorScheme.error,
    )
}
