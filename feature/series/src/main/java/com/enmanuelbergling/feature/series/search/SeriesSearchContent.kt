package com.enmanuelbergling.feature.series.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.VerticalAlignTop
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import coil.compose.AsyncImage
import com.enmanuelbergling.core.common.util.BASE_BACKDROP_IMAGE_URL
import com.enmanuelbergling.core.domain.datasource.preferences.StringQuery
import com.enmanuelbergling.core.model.tv.TvShow
import com.enmanuelbergling.core.ui.R
import com.enmanuelbergling.core.ui.components.FromDirection
import com.enmanuelbergling.core.ui.components.RatingStars
import com.enmanuelbergling.core.ui.components.ShowUpFrom
import com.enmanuelbergling.core.ui.components.SwipeToDismissContainer
import com.enmanuelbergling.core.ui.core.dimen
import com.enmanuelbergling.core.ui.core.isAppending
import com.enmanuelbergling.core.ui.core.isRefreshing
import com.enmanuelbergling.feature.series.home.model.SuggestionEvent
import kotlinx.coroutines.launch
import java.util.UUID


@Composable
internal fun ExpandedSearchBarContent(
    series: LazyPagingItems<TvShow>,
    searchSuggestions: List<StringQuery>,
    searchSuggestionsDeleted: List<StringQuery>,
    onSuggestionEvent: (SuggestionEvent) -> Unit,
    textFieldState: TextFieldState,
    onSeriesDetails: (Int) -> Unit,
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
        if (series.isRefreshing && textFieldState.text.isNotBlank())
            LoadingIndicator()
        else HorizontalDivider()

        LazyColumn(
            contentPadding = WindowInsets.navigationBars.asPaddingValues()
                    + PaddingValues(top = MaterialTheme.dimen.small),
            state = lazyListState,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.small),
        ) {
            items(suggestions, key = { UUID.randomUUID() }) { query ->
                SearchSuggestionUi(
                    visible = query !in searchSuggestionsDeleted,
                    query = query,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MaterialTheme.dimen.mediumSmall)
                        .animateItem(),
                    onDelete = {
                        onSuggestionEvent(SuggestionEvent.Delete(query))
                    },
                    shape = MaterialTheme.shapes.medium
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

            if (series.itemCount > 0) {
                items(series.itemCount) {
                    series[it]?.let { tvShow ->
                        TvShowLandCard(tvShow = tvShow, Modifier.fillMaxWidth()) {
                            onSeriesDetails(tvShow.id)
                        }
                    }
                }

                if (series.isAppending) item {
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
private fun TvShowLandCard(
    tvShow: TvShow,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        modifier = modifier.heightIn(max = 90.dp)
    ) {
        Row(
            Modifier.padding(MaterialTheme.dimen.small),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = BASE_BACKDROP_IMAGE_URL + tvShow.backdropPath,
                contentDescription = "series image",
                error = painterResource(id = R.drawable.pop_corn_and_cinema_poster),
                placeholder = painterResource(id = R.drawable.pop_corn_and_cinema_poster),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(
                        horizontal = MaterialTheme.dimen.small,
                        vertical = MaterialTheme.dimen.verySmall
                    )
                    .aspectRatio(1.5f)
                    .clip(MaterialTheme.shapes.medium)
            )

            Column {
                Text(
                    text = "${tvShow.name} (${tvShow.firstAirYear})",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                )

                RatingStars(value = tvShow.voteAverage.div(2).toFloat(), spaceBetween = 1.dp)
            }
        }
    }
}

@Composable
private fun SearchSuggestionUi(
    visible: Boolean,
    query: String,
    modifier: Modifier = Modifier,
    onDelete: () -> Unit = {},
    shape: CornerBasedShape = MaterialTheme.shapes.medium,
    onClick: () -> Unit,
) {
    SwipeToDismissContainer(
        visible = visible,
        onDismissFromEndToStart = onDelete,
        modifier = modifier
            .clickable { onClick() },
        shape = shape,
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
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}
