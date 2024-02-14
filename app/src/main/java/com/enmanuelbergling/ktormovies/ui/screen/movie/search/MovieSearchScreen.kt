package com.enmanuelbergling.ktormovies.ui.screen.movie.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIos
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.enmanuelbergling.ktormovies.ui.core.dimen
import com.enmanuelbergling.ktormovies.ui.core.isAppending
import com.enmanuelbergling.ktormovies.ui.core.isRefreshing
import com.enmanuelbergling.ktormovies.ui.screen.watchlist.components.MovieLandCard
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
                    contentDescription = "back icon"
                )
            }
        },
        trailingIcon = {
            if (query.isNotBlank()) {
                IconButton(onClick = { viewModel.onQueryChange("") }) {
                    Icon(
                        imageVector = Icons.Rounded.Clear,
                        contentDescription = "clear string icon"
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
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = MaterialTheme.dimen.verySmall),
                strokeCap = StrokeCap.Round
            )
        }
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.small),
            contentPadding = PaddingValues(MaterialTheme.dimen.verySmall)
        ) {
            items(movies) {
                it?.let { movie ->
                    MovieLandCard(movie = movie, Modifier.fillMaxWidth()) {
                        onMovieDetails(movie.id)
                    }
                }
            }

            item {
                if (movies.isAppending) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(MaterialTheme.dimen.small)
                                .align(Alignment.Center)
                        )
                    }
                }
            }
        }
    }
}