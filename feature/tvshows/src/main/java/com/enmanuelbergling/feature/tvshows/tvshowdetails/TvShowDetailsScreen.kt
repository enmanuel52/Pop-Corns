package com.enmanuelbergling.feature.tvshows.tvshowdetails

import androidx.annotation.StringRes
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.LocalContentColor
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
import androidx.compose.ui.draw.scale
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
import com.enmanuelbergling.core.model.core.SimplerUi
import com.enmanuelbergling.core.model.tv.Season
import com.enmanuelbergling.core.ui.components.HandleUiState
import com.enmanuelbergling.core.ui.components.OnceLottieAnimation
import com.enmanuelbergling.core.ui.components.RatingStars
import com.enmanuelbergling.core.ui.core.ObserveAsEvents
import com.enmanuelbergling.core.ui.core.dimen
import com.enmanuelbergling.core.ui.navigation.ActorDetailNavAction
import com.enmanuelbergling.feature.tvshows.R
import com.enmanuelbergling.feature.tvshows.tvshowdetails.model.PersonUiItem
import com.enmanuelbergling.feature.tvshows.tvshowdetails.model.toPersonUi
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import com.enmanuelbergling.core.ui.R as CoreR

@Composable
fun TvShowDetailsScreen(
    tvShowId: Int,
    onSeason: (seasonNumber: Int) -> Unit,
    onSeeAllSeasons: () -> Unit,
    onActor: (ActorDetailNavAction) -> Unit,
    onBack: () -> Unit,
) {
    val viewModel = koinViewModel<TvShowDetailsVM> { parametersOf(tvShowId) }
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.uiEvents) { event ->
        when (event) {
            TvShowDetailsEvent.NavigateBack -> onBack()
            TvShowDetailsEvent.NavigateToSeasons -> onSeeAllSeasons()
            is TvShowDetailsEvent.NavigateToEpisodes -> onSeason(event.seasonNumber)
            is TvShowDetailsEvent.NavigateToActor -> onActor(event.action)
        }
    }

    TvShowDetailsScreen(state = state, onAction = viewModel::onAction)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun TvShowDetailsScreen(
    state: TvShowDetailsState,
    onAction: (TvShowDetailsAction) -> Unit,
) {
    val details = state.details
    val creditsState = state.credits

    val snackbarHostState = remember { SnackbarHostState() }

    HandleUiState(
        uiState = state.uiState,
        snackState = snackbarHostState,
        onRetry = { onAction(TvShowDetailsAction.OnRetry) },
        getFocus = false
    )

    val topBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                scrollBehavior = topBarScrollBehavior,
                title = { Text(text = stringResource(CoreR.string.details)) },
                navigationIcon = {
                    IconButton(onClick = { onAction(TvShowDetailsAction.OnBack) }) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBackIosNew,
                            contentDescription = stringResource(CoreR.string.back_icon)
                        )
                    }
                },
                actions = {
                    if (state.isFavoriteLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(horizontal = MaterialTheme.dimen.small)
                                .size(24.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        state.accountStates?.let {
                            IconButton(onClick = { onAction(TvShowDetailsAction.OnFavoriteClick) }) {
                                if (it.favorite) {
                                    OnceLottieAnimation(
                                        resId = CoreR.raw.add_to_favorite,
                                        modifier = Modifier
                                            .size(24.dp)
                                            .scale(3f),
                                    )
                                } else {
                                    Icon(
                                        painter = painterResource(CoreR.drawable.heart_outline),
                                        contentDescription = stringResource(CoreR.string.favorites),
                                    )
                                }
                            }
                        }
                    }

                    if (state.isWatchlistLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(horizontal = MaterialTheme.dimen.small)
                                .size(24.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        state.accountStates?.let {
                            IconButton(onClick = { onAction(TvShowDetailsAction.OnWatchlistClick) }) {
                                Icon(
                                    painter = painterResource(
                                        if (it.watchlist) CoreR.drawable.bookmark_solid
                                        else CoreR.drawable.bookmark_outline
                                    ),
                                    contentDescription = stringResource(CoreR.string.watchlist),
                                    tint = if (it.watchlist) MaterialTheme.colorScheme.primary
                                    else LocalContentColor.current
                                )
                            }
                        }
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
            if (state.uiState == SimplerUi.Loading) item {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                    LoadingIndicator(
                        modifier = Modifier.padding(MaterialTheme.dimen.medium),
                    )
                }
            }

            details?.let {
                backdropImage(BASE_BACKDROP_IMAGE_URL + details.backdropPath.orEmpty())

                information(
                    title = details.name,
                    year = details.firstAirYear,
                    rating = details.voteAverage.toFloat(),
                    genres = details.formattedGenres,
                    seasonCount = details.numberOfSeasons,
                )

                overview(details.overview)

                seasonsSection(
                    seasons = details.seasons,
                    onSeason = { onAction(TvShowDetailsAction.OnSeasonClick(it)) },
                    onSeeAll = { onAction(TvShowDetailsAction.OnSeeAllSeasons) },
                )

                persons(
                    title = CoreR.string.cast,
                    persons = creditsState?.cast.orEmpty().map { it.toPersonUi() }.distinct(),
                    onActor = { onAction(TvShowDetailsAction.OnActorClick(it)) }
                )

                persons(
                    title = CoreR.string.crew,
                    persons = creditsState?.crew.orEmpty().map { it.toPersonUi() }.distinct(),
                    onActor = { onAction(TvShowDetailsAction.OnActorClick(it)) }
                )
            }
        }
    }
}

private fun LazyListScope.seasonsSection(
    seasons: List<Season>,
    onSeason: (seasonNumber: Int) -> Unit,
    onSeeAll: () -> Unit,
) {
    if (seasons.isEmpty()) return

    item {
        Column(Modifier.padding(all = MaterialTheme.dimen.small)) {
            Text(
                text = stringResource(R.string.seasons),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(start = MaterialTheme.dimen.small)
            )

            Spacer(modifier = Modifier.height(MaterialTheme.dimen.small))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.small)) {
                items(seasons, key = { it.id }) { season ->
                    SeasonCard(
                        season = season,
                        modifier = Modifier.width(80.dp),
                        onClick = { onSeason(season.seasonNumber) }
                    )
                }
                item {
                    SeeAllCard(
                        modifier = Modifier.width(80.dp),
                        onClick = onSeeAll,
                    )
                }
            }
        }
    }
}

@Composable
private fun SeasonCard(
    season: Season,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Column(modifier) {
        ElevatedCard(onClick = onClick, shape = MaterialTheme.shapes.extraLarge) {
            AsyncImage(
                model = BASE_POSTER_IMAGE_URL + season.posterPath.orEmpty(),
                contentDescription = "season poster",
                error = painterResource(id = CoreR.drawable.pop_corn_and_cinema_poster),
                placeholder = painterResource(id = CoreR.drawable.pop_corn_and_cinema_poster),
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(.8f),
                contentScale = ContentScale.Crop
            )
        }

        Text(
            text = season.name,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            text = stringResource(R.string.episode_count, season.episodeCount),
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
        )
    }
}

@Composable
private fun SeeAllCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        ElevatedCard(onClick = onClick, shape = MaterialTheme.shapes.extraLarge) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(.8f),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                    contentDescription = stringResource(R.string.see_all),
                )
            }
        }

        Text(
            text = stringResource(R.string.see_all),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
        )
    }
}

private fun LazyListScope.persons(
    @StringRes title: Int,
    persons: List<PersonUiItem>,
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
            minLines = 2,
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
                text = stringResource(R.string.overview),
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
    genres: String,
    seasonCount: Int,
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
                    text = "$title ($year)",
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.SemiBold
                )

                RatingStars(value = rating.div(2))
            }

            Text(
                text = "$genres - ${stringResource(R.string.season_count, seasonCount)}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Light
            )
        }
    }
}

private fun LazyListScope.backdropImage(backdropUrl: String?) {
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
