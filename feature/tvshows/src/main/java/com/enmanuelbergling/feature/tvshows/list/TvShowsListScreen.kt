package com.enmanuelbergling.feature.tvshows.list

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
import com.enmanuelbergling.core.model.tv.TvShow
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
import com.enmanuelbergling.feature.tvshows.list.viewmodel.AiringTodayTvShowsVM
import com.enmanuelbergling.feature.tvshows.list.viewmodel.OnTheAirTvShowsVM
import com.enmanuelbergling.feature.tvshows.list.viewmodel.PopularTvShowsVM
import com.enmanuelbergling.feature.tvshows.list.viewmodel.TopRatedTvShowsVM
import org.koin.androidx.compose.koinViewModel


@Composable
fun PopularTvShowsScreen(onTvShows: (id: Int) -> Unit, onBack: () -> Unit) {
    val viewModel = koinViewModel<PopularTvShowsVM>()
    val tvShows = viewModel.tvShows.collectAsLazyPagingItems()
    TvShowsListScreen(
        title = stringResource(R.string.popular),
        onBack = onBack,
        tvShows = tvShows,
        onTvShows = onTvShows
    )
}

@Composable
fun TopRatedTvShowsScreen(onTvShows: (id: Int) -> Unit, onBack: () -> Unit) {
    val viewModel = koinViewModel<TopRatedTvShowsVM>()
    val tvShows = viewModel.tvShows.collectAsLazyPagingItems()
    TvShowsListScreen(
        title = stringResource(R.string.top_rated),
        onBack = onBack,
        tvShows = tvShows,
        onTvShows = onTvShows
    )
}

@Composable
fun OnTheAirTvShowsScreen(onTvShows: (id: Int) -> Unit, onBack: () -> Unit) {
    val viewModel = koinViewModel<OnTheAirTvShowsVM>()
    val tvShows = viewModel.tvShows.collectAsLazyPagingItems()
    TvShowsListScreen(
        title = stringResource(R.string.on_the_air),
        onBack = onBack,
        tvShows = tvShows,
        onTvShows = onTvShows
    )
}

@Composable
fun AiringTodayTvShowsScreen(onTvShows: (id: Int) -> Unit, onBack: () -> Unit) {
    val viewModel = koinViewModel<AiringTodayTvShowsVM>()
    val tvShows = viewModel.tvShows.collectAsLazyPagingItems()
    TvShowsListScreen(
        title = stringResource(R.string.airing_today),
        onBack = onBack,
        tvShows = tvShows,
        onTvShows = onTvShows
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TvShowsListScreen(
    title: String,
    onBack: () -> Unit,
    tvShows: LazyPagingItems<TvShow>,
    onTvShows: (id: Int) -> Unit,
) {
    val snackBarHostState = remember { SnackbarHostState() }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    HandlerPagingUiState(items = tvShows, snackState = snackBarHostState)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text(text = title) }, navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBackIosNew,
                        contentDescription = stringResource(id = R.string.back_icon)
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
                    .shimmerIf { tvShows.isRefreshing },
                state = listState,
                columns = GridCells.Adaptive(150.dp),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.small),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.small),
                userScrollEnabled = !tvShows.isRefreshing,
                contentPadding = PaddingValues(MaterialTheme.dimen.small)
            ) {
                items(tvShows, key = { tvShow -> tvShow.id }) { tvShow ->
                    tvShow?.let {
                        MovieCard(
                            imageUrl = tvShow.posterPath.orEmpty(),
                            title = tvShow.name,
                            rating = tvShow.voteAverage,
                            titleLines = 1,
                            modifier = Modifier
                                .widthIn(max = 200.dp)
                                .listItemWindAnimation(
                                    isScrollingForward = listState.isScrollingForward(),
                                    rotation = 4f,
                                    durationMillis = 200
                                )
                        ) {
                            onTvShows(tvShow.id)
                        }
                    }
                }
                if (tvShows.isRefreshing) {
                    items(50) {
                        MovieCardPlaceholder()
                    }
                }
            }
        }
    }
}
