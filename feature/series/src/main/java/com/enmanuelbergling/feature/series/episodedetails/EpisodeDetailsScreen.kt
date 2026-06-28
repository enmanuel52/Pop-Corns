package com.enmanuelbergling.feature.series.episodedetails

import androidx.annotation.StringRes
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
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
import com.enmanuelbergling.core.common.util.BASE_POSTER_IMAGE_URL
import com.enmanuelbergling.core.model.tv.EpisodeDetails
import com.enmanuelbergling.core.ui.components.HandleUiState
import com.enmanuelbergling.core.ui.components.RatingStars
import com.enmanuelbergling.core.ui.core.ObserveAsEvents
import com.enmanuelbergling.core.ui.core.dimen
import com.enmanuelbergling.core.ui.navigation.ActorDetailNavAction
import com.enmanuelbergling.feature.series.R
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import com.enmanuelbergling.core.ui.R as CoreR

@Composable
fun EpisodeDetailsScreen(
    seriesId: Int,
    seasonNumber: Int,
    episodeNumber: Int,
    onActor: (ActorDetailNavAction) -> Unit,
    onBack: () -> Unit,
) {
    val viewModel = koinViewModel<EpisodeDetailsVM> {
        parametersOf(seriesId, seasonNumber, episodeNumber)
    }
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.uiEvents) { event ->
        when (event) {
            EpisodeDetailsUiEvent.NavigateBack -> onBack()
            is EpisodeDetailsUiEvent.NavigateToActor -> onActor(event.action)
        }
    }

    EpisodeDetailsScreen(state = state, onAction = viewModel::onAction)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EpisodeDetailsScreen(
    state: EpisodeDetailsState,
    onAction: (EpisodeDetailsAction) -> Unit,
) {
    val details = state.details

    val snackbarHostState = remember { SnackbarHostState() }

    HandleUiState(
        uiState = state.uiState,
        snackState = snackbarHostState,
        onRetry = { onAction(EpisodeDetailsAction.OnRetry) },
        getFocus = details == null
    )

    val topBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                scrollBehavior = topBarScrollBehavior,
                title = { Text(text = stringResource(R.string.episode_details)) },
                navigationIcon = {
                    IconButton(onClick = { onAction(EpisodeDetailsAction.OnBack) }) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBackIosNew,
                            contentDescription = stringResource(CoreR.string.back_icon)
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            )
        },
        contentWindowInsets = WindowInsets.statusBars,
    ) { paddingValues ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.small),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .nestedScroll(topBarScrollBehavior.nestedScrollConnection),
            contentPadding = WindowInsets.navigationBars.asPaddingValues(),
        ) {
            details?.let {
                stillImage(BASE_BACKDROP_IMAGE_URL + details.stillPath.orEmpty())

                information(
                    title = details.name,
                    year = details.airYear,
                    rating = details.voteAverage.toFloat(),
                    duration = details.duration,
                )

                overview(details.overview)

                persons(
                    title = CoreR.string.cast,
                    persons = details.guestStars.map { cast ->
                        PersonUi(cast.id, cast.profilePath, cast.name)
                    }.distinct(),
                    onActor = { onAction(EpisodeDetailsAction.OnActorClick(it)) }
                )

                persons(
                    title = CoreR.string.crew,
                    persons = details.crew.map { crew ->
                        PersonUi(crew.id, crew.profilePath, crew.name)
                    }.distinct(),
                    onActor = { onAction(EpisodeDetailsAction.OnActorClick(it)) }
                )
            }
        }
    }
}

private data class PersonUi(val id: Int, val imageUrl: String?, val name: String)

private fun LazyListScope.persons(
    @StringRes title: Int,
    persons: List<PersonUi>,
    onActor: (ActorDetailNavAction) -> Unit,
) {
    if (persons.isEmpty()) return

    item {
        Column(Modifier.padding(all = MaterialTheme.dimen.small)) {
            Text(
                text = stringResource(title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(start = MaterialTheme.dimen.small)
            )

            Spacer(modifier = Modifier.height(MaterialTheme.dimen.small))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.small)) {
                items(persons) { person ->
                    PersonCard(
                        imageUrl = person.imageUrl,
                        name = person.name,
                        modifier = Modifier.width(110.dp)
                    ) {
                        onActor(ActorDetailNavAction(person.id, person.imageUrl, person.name))
                    }
                }
            }
        }
    }
}

@Composable
private fun PersonCard(
    imageUrl: String?,
    name: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Column(modifier) {
        ElevatedCard(onClick = onClick) {
            AsyncImage(
                model = BASE_POSTER_IMAGE_URL + imageUrl.orEmpty(),
                contentDescription = "person image",
                error = painterResource(id = CoreR.drawable.mr_bean),
                placeholder = painterResource(id = CoreR.drawable.mr_bean),
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(.65f),
                contentScale = ContentScale.Crop
            )
        }

        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

private fun LazyListScope.overview(overview: String) {
    if (overview.isBlank()) return

    item {
        var expanded by remember { mutableStateOf(false) }

        Column(Modifier.padding(all = MaterialTheme.dimen.small)) {
            Text(
                text = "Overview",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = overview,
                modifier = Modifier
                    .clickable { expanded = !expanded }
                    .animateContentSize(spring(Spring.DampingRatioLowBouncy, Spring.StiffnessLow)),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = if (expanded) Int.MAX_VALUE else 3,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

private fun LazyListScope.information(
    title: String,
    year: String,
    rating: Float,
    duration: String,
) {
    item {
        Column(Modifier.padding(MaterialTheme.dimen.small)) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.dimen.small),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (year.isBlank()) title else "$title ($year)",
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.SemiBold
                )

                RatingStars(value = rating.div(2))
            }

            Text(
                text = duration,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Light
            )
        }
    }
}

private fun LazyListScope.stillImage(backdropUrl: String?) {
    item {
        AsyncImage(
            model = backdropUrl,
            contentDescription = stringResource(CoreR.string.poster_image),
            placeholder = painterResource(id = CoreR.drawable.pop_corn_and_cinema_backdrop),
            error = painterResource(id = CoreR.drawable.pop_corn_and_cinema_backdrop),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .animateContentSize()
                .fillMaxWidth(),
        )
    }
}
