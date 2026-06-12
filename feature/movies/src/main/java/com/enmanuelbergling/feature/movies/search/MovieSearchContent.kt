package com.enmanuelbergling.feature.movies.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.VerticalAlignTop
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.enmanuelbergling.core.domain.datasource.preferences.StringQuery
import com.enmanuelbergling.core.model.movie.Movie
import com.enmanuelbergling.core.ui.R
import com.enmanuelbergling.core.ui.components.FromDirection
import com.enmanuelbergling.core.ui.components.ShowUpFrom
import com.enmanuelbergling.core.ui.components.SwipeToDismissContainer
import com.enmanuelbergling.core.ui.components.common.MovieLandCard
import com.enmanuelbergling.core.ui.core.dimen
import com.enmanuelbergling.core.ui.core.isAppending
import com.enmanuelbergling.core.ui.core.isRefreshing
import com.enmanuelbergling.feature.movies.home.model.SuggestionEvent
import kotlinx.coroutines.launch
import java.util.UUID


@Composable
internal fun ExpandedSearchBarContent(
    movies: LazyPagingItems<Movie>,
    searchSuggestions: List<StringQuery>,
    onSuggestionEvent: (SuggestionEvent) -> Unit,
    textFieldState: TextFieldState,
    onMovieDetails: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val lazyListState = rememberLazyListState()

    val start by remember {
        derivedStateOf {
            lazyListState.firstVisibleItemIndex == 0
        }
    }
    val suggestions = searchSuggestions.filter {
        it.contains(textFieldState.text) &&
                !it.contentEquals(textFieldState.text)
    }

    val keyboardController = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()

    Box(modifier) {
        if (movies.isRefreshing && textFieldState.text.isNotBlank())
            LoadingIndicator()
        else HorizontalDivider()

        LazyColumn(
            contentPadding = WindowInsets.navigationBars.asPaddingValues()
                    + PaddingValues(top = MaterialTheme.dimen.small),
            state = lazyListState,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.small),
        ) {
            if (suggestions.isNotEmpty()) stickyHeader {
                Text(
                    stringResource(R.string.suggestions),
                    style = MaterialTheme.typography.titleMedium,
                )
            }

            items(suggestions, key = { UUID.randomUUID() }) { query ->
                SearchSuggestionUi(
                    query = query,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MaterialTheme.dimen.mediumSmall)
                        .animateItem(),
                    onDelete = {
                        onSuggestionEvent(SuggestionEvent.Delete(query))
                    },
                    shape = CircleShape
                ) {
                    scope.launch { keyboardController?.hide() }
                    textFieldState.setTextAndPlaceCursorAtEnd(query)
                }
            }

            if (suggestions.isNotEmpty()) item {
                TextButton(
                    onClick = { onSuggestionEvent(SuggestionEvent.Clear) },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    ),
                ) {
                    Text(text = stringResource(id = R.string.clear).uppercase())
                }
            }



            if (movies.itemCount > 0) {
                stickyHeader {
                    Text(
                        stringResource(R.string.movies),
                        style = MaterialTheme.typography.titleMedium,
                    )
                }

                items(movies.itemCount) {
                    movies[it]?.let { movie ->
                        MovieLandCard(movie = movie, Modifier.fillMaxWidth()) {
                            onMovieDetails(movie.id)
                        }
                    }
                }

                if (movies.isAppending) item {
                    LoadingIndicator()
                }

            }
        }

        ShowUpFrom(
            visible = !start,
            fromDirection = FromDirection.Bottom,
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

@Composable
private fun LoadingIndicator() {
    LinearWavyProgressIndicator(
        modifier = Modifier.fillMaxWidth(),
        waveSpeed = MaterialTheme.dimen.medium,
    )
}


@Composable
fun SearchSuggestionUi(
    query: String,
    modifier: Modifier = Modifier,
    onDelete: () -> Unit = {},
    shape: Shape = MaterialTheme.shapes.medium,
    onClick: () -> Unit,
) {
    SwipeToDismissContainer(
        onDelete = onDelete,
        modifier = modifier
            .clip(shape)
            .clickable { onClick() },
    ) {
        Card(shape = shape) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(MaterialTheme.dimen.medium)
            ) {
                Icon(
                    imageVector = Icons.Rounded.History,
                    contentDescription = "history icon",
                    modifier = Modifier.size(20.dp)
                )

                Spacer(Modifier.width(MaterialTheme.dimen.medium))

                Text(
                    text = query,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .weight(1f),
                )
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun SearchSuggestionUiPrev() {
    SearchSuggestionUi(
        "The shawtank redemtion", modifier = Modifier
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.large
    ) {}
}