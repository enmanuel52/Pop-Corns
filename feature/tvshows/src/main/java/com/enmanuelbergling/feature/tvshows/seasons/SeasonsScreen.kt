package com.enmanuelbergling.feature.tvshows.seasons

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.enmanuelbergling.core.common.util.BASE_POSTER_IMAGE_URL
import com.enmanuelbergling.core.model.tv.Season
import com.enmanuelbergling.core.ui.components.HandleUiState
import com.enmanuelbergling.core.ui.core.ObserveAsEvents
import com.enmanuelbergling.core.ui.core.dimen
import com.enmanuelbergling.feature.tvshows.R
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import com.enmanuelbergling.core.ui.R as CoreR

@Composable
fun SeasonsScreen(
    tvShowId: Int,
    onSeason: (seasonNumber: Int) -> Unit,
    onBack: () -> Unit,
) {
    val viewModel = koinViewModel<SeasonsVM> { parametersOf(tvShowId) }
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.uiEvents) { event ->
        when (event) {
            SeasonsEvent.NavigateBack -> onBack()
            is SeasonsEvent.NavigateToEpisodes -> onSeason(event.seasonNumber)
        }
    }

    SeasonsScreen(state = state, onAction = viewModel::onAction)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SeasonsScreen(
    state: SeasonsState,
    onAction: (SeasonsAction) -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    HandleUiState(
        uiState = state.uiState,
        snackState = snackbarHostState,
        onRetry = { onAction(SeasonsAction.OnRetry) },
        getFocus = state.details == null
    )

    val topBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                scrollBehavior = topBarScrollBehavior,
                title = { Text(text = state.details?.name ?: stringResource(R.string.seasons)) },
                navigationIcon = {
                    IconButton(onClick = { onAction(SeasonsAction.OnBack) }) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBackIosNew,
                            contentDescription = stringResource(CoreR.string.back_icon)
                        )
                    }
                },
            )
        },
        contentWindowInsets = WindowInsets.statusBars,
    ) { paddingValues ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.small),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = MaterialTheme.dimen.small)
                .nestedScroll(topBarScrollBehavior.nestedScrollConnection),
            contentPadding = WindowInsets.navigationBars.asPaddingValues(),
        ) {
            items(state.details?.seasons.orEmpty(), key = { it.id }) { season ->
                SeasonRow(
                    season = season,
                    expanded = state.expandedSeasonId == season.id,
                    onLongClick = { onAction(SeasonsAction.OnSeasonLongClick(season.id)) },
                    onOpen = { onAction(SeasonsAction.OnSeasonClick(season.seasonNumber)) }
                )
            }
        }
    }
}

@Composable
private fun SeasonRow(
    season: Season,
    expanded: Boolean,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier,
    onOpen: () -> Unit,
) {
    Card(
        modifier = modifier
            .clip(CardDefaults.shape)
            .combinedClickable(onLongClick = onLongClick, onClick = onOpen)
    ) {
        Row(
            modifier = Modifier.padding(MaterialTheme.dimen.small),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = BASE_POSTER_IMAGE_URL + season.posterPath.orEmpty(),
                contentDescription = "season poster",
                placeholder = painterResource(CoreR.drawable.pop_corn_and_cinema_poster),
                error = painterResource(CoreR.drawable.pop_corn_and_cinema_poster),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(
                        horizontal = MaterialTheme.dimen.small,
                        vertical = MaterialTheme.dimen.verySmall
                    )
                    .height(90.dp)
                    .aspectRatio(0.65f)
                    .clip(CardDefaults.elevatedShape)
            )

            Spacer()

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = season.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = stringResource(R.string.episode_count, season.episodeCount),
                    style = MaterialTheme.typography.bodySmall,
                )

                AnimatedVisibility(expanded && season.overview.isNotBlank()) {
                    Spacer()
                    Text(
                        text = season.overview,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 4,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}

@Composable
private fun Spacer() {
    androidx.compose.foundation.layout.Spacer(
        modifier = Modifier
            .height(MaterialTheme.dimen.small)
            .width(MaterialTheme.dimen.small)
    )
}
