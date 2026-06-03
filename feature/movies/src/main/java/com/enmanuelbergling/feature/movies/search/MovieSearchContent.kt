package com.enmanuelbergling.feature.movies.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.VerticalAlignTop
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.enmanuelbergling.core.domain.datasource.preferences.StringQuery
import com.enmanuelbergling.core.model.movie.Movie
import com.enmanuelbergling.core.ui.R
import com.enmanuelbergling.core.ui.components.FromDirection
import com.enmanuelbergling.core.ui.components.LinearLoading
import com.enmanuelbergling.core.ui.components.ShowUpFrom
import com.enmanuelbergling.core.ui.components.SwipeToDismissContainer
import com.enmanuelbergling.core.ui.components.common.MovieLandCard
import com.enmanuelbergling.core.ui.core.dimen
import com.enmanuelbergling.core.ui.core.isAppending
import com.enmanuelbergling.core.ui.core.isRefreshing
import com.enmanuelbergling.core.ui.theme.DimensionTokens
import com.enmanuelbergling.feature.movies.home.model.SuggestionEvent
import kotlinx.coroutines.launch
import java.util.UUID


@Composable
internal fun ExpandedSearchBarContent(
    movies: LazyPagingItems<Movie>,
    searchSuggestions: List<StringQuery>,
    onSuggestionEvent: (SuggestionEvent) -> Unit,
    textFieldState: TextFieldState,
    onMovieDetails: (Int) -> Unit
) {
    if (movies.isRefreshing && textFieldState.text.isNotBlank()) {
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
            verticalArrangement = Arrangement.spacedBy(
                if (textFieldState.text.isEmpty()) MaterialTheme.dimen.superSmall else MaterialTheme.dimen.small
            ),
            contentPadding = WindowInsets.navigationBars.asPaddingValues(),
            state = lazyListState,
            modifier = Modifier
                .padding(horizontal = MaterialTheme.dimen.verySmall)
        ) {
            if (textFieldState.text.isBlank()) {

                if (searchSuggestions.isNotEmpty()) {
                    items(searchSuggestions, key = { UUID.randomUUID() }) { query ->
                        SearchSuggestionUi(
                            query = query,
                            modifier = Modifier
                                .fillMaxWidth()
                                .animateItem(),
                            onDelete = {
                                onSuggestionEvent(
                                    SuggestionEvent.Delete(
                                        query
                                    )
                                )
                            }) {
                            textFieldState.setTextAndPlaceCursorAtEnd(query)
                        }
                    }

                    item {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                            TextButton(
                                onClick = { onSuggestionEvent(SuggestionEvent.Clear) },
                                colors = ButtonDefaults.textButtonColors(
                                    contentColor = MaterialTheme.colorScheme.error
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Delete,
                                    contentDescription = "delete icon",
                                    Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(MaterialTheme.dimen.verySmall))
                                Text(text = stringResource(id = R.string.clear).uppercase())
                            }
                        }
                    }
                }
            } else {

                items(movies.itemCount) {
                    movies[it]?.let { movie ->
                        MovieLandCard(movie = movie, Modifier.fillMaxWidth()) {
                            onMovieDetails(movie.id)
                        }
                    }
                }

                if (movies.isAppending) {
                    item {
                        LinearLoading()
                    }
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


@Composable
fun SearchSuggestionUi(
    query: String,
    modifier: Modifier = Modifier,
    onDelete: () -> Unit = {},
    onClick: () -> Unit,
) {
    SwipeToDismissContainer(
        onDelete = onDelete,
        modifier = Modifier
            .height(DimensionTokens.TopAppBarHeight)
            .clickable { onClick() } then modifier,
        shape = MaterialTheme.shapes.small,
    ) {

        Row(
            Modifier
                .fillMaxSize()
                .background(
                    MaterialTheme.colorScheme.background,
                    shape = MaterialTheme.shapes.small
                )
        ) {

            Icon(
                imageVector = Icons.Rounded.History,
                contentDescription = "history icon",
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(horizontal = MaterialTheme.dimen.small)
            )

            Text(
                text = query,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically),
            )
        }

    }
}

@Preview
@Composable
private fun SearchSuggestionUiPrev() {
    SearchSuggestionUi(
        "The shawtank redemtion", modifier = Modifier
            .fillMaxWidth()
    ) {}
}