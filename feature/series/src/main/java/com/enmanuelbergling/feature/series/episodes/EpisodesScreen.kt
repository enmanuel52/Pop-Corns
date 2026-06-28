package com.enmanuelbergling.feature.series.episodes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.enmanuelbergling.core.common.util.BASE_BACKDROP_IMAGE_URL
import com.enmanuelbergling.core.model.tv.Episode
import com.enmanuelbergling.core.ui.components.HandleUiState
import com.enmanuelbergling.core.ui.components.RatingStars
import com.enmanuelbergling.core.ui.core.ObserveAsEvents
import com.enmanuelbergling.core.ui.core.dimen
import com.enmanuelbergling.feature.series.R
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import com.enmanuelbergling.core.ui.R as CoreR

@Composable
fun EpisodesScreen(
    seriesId: Int,
    seasonNumber: Int,
    onEpisode: (episodeNumber: Int) -> Unit,
    onBack: () -> Unit,
) {
    val viewModel = koinViewModel<EpisodesVM> { parametersOf(seriesId, seasonNumber) }
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.uiEvents) { event ->
        when (event) {
            EpisodesEvent.NavigateBack -> onBack()
            is EpisodesEvent.NavigateToEpisodeDetails -> onEpisode(event.episodeNumber)
        }
    }

    EpisodesScreen(state = state, onAction = viewModel::onAction)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EpisodesScreen(
    state: EpisodesState,
    onAction: (EpisodesAction) -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    HandleUiState(
        uiState = state.uiState,
        snackState = snackbarHostState,
        onRetry = { onAction(EpisodesAction.OnRetry) },
        getFocus = state.seasonDetails == null
    )

    val topBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                scrollBehavior = topBarScrollBehavior,
                title = {
                    Text(text = state.seasonDetails?.name ?: stringResource(R.string.episodes))
                },
                navigationIcon = {
                    IconButton(onClick = { onAction(EpisodesAction.OnBack) }) {
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
            items(state.seasonDetails?.episodes.orEmpty(), key = { it.id }) { episode ->
                EpisodeRow(
                    episode = episode,
                    onOpen = { onAction(EpisodesAction.OnEpisodeClick(episode.episodeNumber)) }
                )
            }
        }
    }
}

@Composable
private fun EpisodeRow(
    episode: Episode,
    onOpen: () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(CardDefaults.shape)
            .combinedClickable(onLongClick = { expanded = !expanded }, onClick = onOpen)
    ) {
        Column(
            modifier = Modifier
                .padding(all = MaterialTheme.dimen.small),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = BASE_BACKDROP_IMAGE_URL + episode.stillPath.orEmpty(),
                    contentDescription = "episode still",
                    placeholder = painterResource(CoreR.drawable.pop_corn_and_cinema_backdrop),
                    error = painterResource(CoreR.drawable.pop_corn_and_cinema_backdrop),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(70.dp)
                        .aspectRatio(1.5f)
                        .clip(CardDefaults.shape)
                )

                Spacer(Modifier.width(MaterialTheme.dimen.small))

                Column(
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.verySmall),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "${episode.episodeNumber}. ${episode.name}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RatingStars(
                            value = episode.voteAverage.div(2).toFloat(),
                            size = 14.dp,
                            spaceBetween = 1.dp
                        )

                        Spacer(Modifier.width(MaterialTheme.dimen.small))

                        Text(
                            text = episode.duration,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Light,
                        )
                    }
                }

            }

            AnimatedVisibility(expanded && episode.overview.isNotBlank()) {
                Text(
                    text = episode.overview,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = MaterialTheme.dimen.verySmall)
                )
            }
        }

    }
}
