package com.enmanuelbergling.feature.movies.details

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.enmanuelbergling.core.common.util.BASE_BACKDROP_IMAGE_URL
import com.enmanuelbergling.core.model.core.SimplerUi
import com.enmanuelbergling.core.ui.R
import com.enmanuelbergling.core.ui.components.HandleUiState
import com.enmanuelbergling.core.ui.components.RatingStars
import com.enmanuelbergling.core.ui.components.common.ActorCard
import com.enmanuelbergling.core.ui.components.common.ActorsRowPlaceholder
import com.enmanuelbergling.core.ui.core.ObserveAsEvents
import com.enmanuelbergling.core.ui.core.dimen
import com.enmanuelbergling.core.ui.navigation.ActorDetailNavAction
import com.enmanuelbergling.feature.movies.details.model.PersonUiItem
import com.enmanuelbergling.feature.movies.details.model.toPersonUi
import dev.chrisbanes.haze.HazeEffectScope
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeEffect
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun AnimatedContentScope.MovieDetailsScreen(
    id: Int,
    onActor: (ActorDetailNavAction) -> Unit,
    onBack: () -> Unit,
) {

    val viewModel = koinViewModel<MovieDetailsVM> { parametersOf(id) }

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.uiEvents) { event ->
        when (event) {
            MovieDetailsEvent.NavigateBack -> onBack()
            is MovieDetailsEvent.NavigateToActor -> onActor(event.action)
        }
    }

    MovieDetailsScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun AnimatedContentScope.MovieDetailsScreen(
    state: MovieDetailsState,
    onAction: (MovieDetailsAction) -> Unit,
) {

    val details = state.details
    val creditsState = state.credits

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    val context = LocalContext.current

    HandleUiState(
        uiState = state.uiState,
        snackState = snackbarHostState,
        onRetry = { onAction(MovieDetailsAction.OnRetry) },
        getFocus = details == null
    )

    val hazeState = remember { HazeState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.details),
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onAction(MovieDetailsAction.OnBack) }) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBackIosNew,
                            contentDescription = "back icon"
                        )
                    }
                },
                actions = {
                    if (state.isWatchlistLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(horizontal = MaterialTheme.dimen.small)
                                .size(24.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        state.accountStates?.let {
                            IconButton(onClick = { onAction(MovieDetailsAction.OnWatchlistClick) }) {
                                Icon(
                                    painter = painterResource(
                                        if (it.watchlist) R.drawable.bookmark_solid
                                        else R.drawable.bookmark_outline
                                    ),
                                    contentDescription = stringResource(R.string.watchlist),
                                    tint = if (it.watchlist) MaterialTheme.colorScheme.primary
                                    else LocalContentColor.current
                                )
                            }
                        }
                    }
                },
                modifier = Modifier
                    .hazeEffect(
                        hazeState,
                        block = fun HazeEffectScope.() {
                            blurRadius = 16.dp
                        })
                    .fillMaxWidth(),
                // Need to make app bar transparent to see the content behind
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    Color.Transparent,
                    scrolledContainerColor = Color.Transparent
                ),
            )
        },
        contentWindowInsets = WindowInsets.statusBars,
    ) { paddingValues ->

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.small),
            modifier = Modifier
                .haze(hazeState)
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = WindowInsets.navigationBars.asPaddingValues(),
        ) {
            details?.let {
                detailsImage(backdropUrl = BASE_BACKDROP_IMAGE_URL + details.backdropPath)

                information(
                    details.title,
                    details.releaseYear,
                    details.voteAverage.toFloat(),
                    details.formattedGenres,
                    details.duration
                )

                overview(details.overview)

                persons(
                    title = context.getString(R.string.cast),
                    persons = creditsState?.cast.orEmpty().map { it.toPersonUi() }.distinct(),
                    isLoading = state.uiState == SimplerUi.Loading && creditsState == null,
                    animatedContentScope = this@MovieDetailsScreen,
                    onActor = { onAction(MovieDetailsAction.OnActorClick(it)) }
                )

                persons(
                    title = context.getString(R.string.crew),
                    persons = creditsState?.crew.orEmpty().map { it.toPersonUi() }.distinct(),
                    isLoading = state.uiState == SimplerUi.Loading && creditsState == null,
                    animatedContentScope = this@MovieDetailsScreen,
                    onActor = { onAction(MovieDetailsAction.OnActorClick(it)) }
                )
            }

        }
    }
}

private fun LazyListScope.persons(
    title: String,
    persons: List<PersonUiItem>,
    isLoading: Boolean = false,
    animatedContentScope: AnimatedContentScope,
    onActor: (ActorDetailNavAction) -> Unit,
) {
    item {
        Column(Modifier.padding(all = MaterialTheme.dimen.small)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(start = MaterialTheme.dimen.small)
            )

            Spacer(modifier = Modifier.height(MaterialTheme.dimen.small))

            if (isLoading && persons.isEmpty()) {
                ActorsRowPlaceholder()
            } else {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.small)) {
                    items(persons) { person ->

                        with(animatedContentScope) {
                            ActorCard(
                                imageUrl = person.imageUrl,
                                name = person.name,
                                modifier = Modifier
                                    .width(110.dp)
                            ) {
                                onActor(
                                    ActorDetailNavAction(
                                        person.id,
                                        person.imageUrl,
                                        person.name
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun LazyListScope.overview(overview: String) {
    item {
        var expanded by remember {
            mutableStateOf(false)
        }

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
                    .animateContentSize(
                        spring(Spring.DampingRatioLowBouncy, Spring.StiffnessLow)
                    ),
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
                    //getting year
                    text = "$title ($year)",
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.SemiBold
                )

                //it comes 10 for server
                RatingStars(value = rating.div(2))
            }

            Text(
                text = "$genres - $duration",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Light
            )
        }
    }
}

private fun LazyListScope.detailsImage(
    backdropUrl: String?,
) {
    item {
        AsyncImage(
            model = backdropUrl,
            contentDescription = stringResource(R.string.poster_image),
            placeholder = painterResource(
                id = R.drawable.pop_corn_and_cinema_backdrop
            ),
            error = painterResource(
                id = R.drawable.pop_corn_and_cinema_backdrop
            ),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .animateContentSize()
                .fillMaxWidth(),
        )
    }
}
