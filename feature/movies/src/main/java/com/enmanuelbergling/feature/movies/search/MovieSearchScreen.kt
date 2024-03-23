package com.enmanuelbergling.feature.movies.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIos
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.VerticalAlignTop
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.paging.compose.collectAsLazyPagingItems
import com.enmanuelbergling.core.ui.R
import com.enmanuelbergling.core.ui.components.FromDirection
import com.enmanuelbergling.core.ui.components.LinearLoading
import com.enmanuelbergling.core.ui.components.ShowUpFrom
import com.enmanuelbergling.core.ui.components.common.MovieLandCard
import com.enmanuelbergling.core.ui.core.dimen
import com.enmanuelbergling.core.ui.core.isAppending
import com.enmanuelbergling.core.ui.core.isRefreshing
import kotlinx.coroutines.launch
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.koin.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieSearchScreen(
    onMovieDetails: (movieId: Int) -> Unit,
    onBack: () -> Unit,
) {
    val viewModel = koinViewModel<MovieSearchVM>()

    val query by viewModel.queryState.collectAsStateWithLifecycle()
    val movies = viewModel.movies.collectAsLazyPagingItems()

    val keyboardController = LocalSoftwareKeyboardController.current

    SearchBar(
        query = query,
        onQueryChange = viewModel::onQueryChange,
        onSearch = {
            keyboardController?.hide()
        },
        active = true,
        onActiveChange = {},
        modifier = Modifier.fillMaxSize(),
        leadingIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBackIos,
                    contentDescription = stringResource(id = R.string.back_icon)
                )
            }
        },
        trailingIcon = {
            if (query.isNotBlank()) {
                IconButton(onClick = { viewModel.onQueryChange("") }) {
                    Icon(
                        imageVector = Icons.Rounded.Clear,
                        contentDescription = stringResource(R.string.clear_string_icon)
                    )
                }
            }
        },
        placeholder = {
            Text(text = "Search for a movie")
        },
        colors = SearchBarDefaults.colors(containerColor = MaterialTheme.colorScheme.background)
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
                items(movies.itemCount) {
                    movies[it]?.let { movie ->
                        MovieLandCard(movie = movie, Modifier.fillMaxWidth()) {
                            onMovieDetails(movie.id)
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
                        contentDescription = stringResource(id = R.string.to_start_icon)
                    )
                }
            }
        }
    }
}