package com.enmanuelbergling.feature.actor.details

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridScope
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.enmanuelbergling.core.common.util.BASE_POSTER_IMAGE_URL
import com.enmanuelbergling.core.model.actor.KnownMovie
import com.enmanuelbergling.core.ui.R
import com.enmanuelbergling.core.ui.components.HandleUiState
import com.enmanuelbergling.core.ui.components.RatingStars
import com.enmanuelbergling.core.ui.components.common.MovieCard
import com.enmanuelbergling.core.ui.components.common.MovieCardPlaceholder
import com.enmanuelbergling.core.ui.core.BoundsTransition
import com.enmanuelbergling.core.ui.core.LocalSharedTransitionScope
import com.enmanuelbergling.core.ui.core.ObserveAsEvents
import com.enmanuelbergling.core.ui.core.dimen
import com.enmanuelbergling.core.ui.core.shimmerIf
import com.valentinilk.shimmer.shimmer
import dev.chrisbanes.haze.HazeEffectScope
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeEffect
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun AnimatedVisibilityScope.ActorDetailsRoot(
    id: Int,
    imagePath: String?,
    name: String,
    onMovie: (movieId: Int) -> Unit,
    onBack: () -> Unit,
) {

    val viewModel = koinViewModel<ActorDetailsVM> { parametersOf(id) }
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.uiEvents) { event ->
        when (event) {
            ActorDetailsEvent.NavigateBack -> onBack()
            is ActorDetailsEvent.NavigateToMovie -> onMovie(event.movieId)
        }
    }

    ActorDetailsScreen(
        imagePath = imagePath,
        name = name,
        state = state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AnimatedVisibilityScope.ActorDetailsScreen(
    imagePath: String?,
    name: String,
    state: ActorDetailsState,
    onAction: (ActorDetailsAction) -> Unit,
) {

    val snackBarHostState = remember {
        SnackbarHostState()
    }

    HandleUiState(
        uiState = state.uiState,
        snackState = snackBarHostState,
        onRetry = { onAction(ActorDetailsAction.OnRetry) },
        getFocus = false
    )

    val hazeState = remember { HazeState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(R.string.details)) },
                navigationIcon = {
                    IconButton(
                        onClick = { onAction(ActorDetailsAction.OnBack) },
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBackIos,
                            contentDescription = stringResource(R.string.back_icon)
                        )
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
        }
    ) { paddingValues ->
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Adaptive(120.dp),
            verticalItemSpacing = MaterialTheme.dimen.small,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.small),
            modifier = Modifier
                .haze(hazeState)
                .fillMaxSize(),
            contentPadding = paddingValues
        ) {
            item(span = StaggeredGridItemSpan.FullLine) {
                DetailsHeader(
                    imagePath = imagePath,
                    name = name,
                    popularity = state.details?.popularity
                )
            }

            state.details?.let {
                if (state.details.biography.isNotBlank()) {
                    about(biography = state.details.biography)
                }

                knownMovies(
                    knownMovies = state.knownMovies,
                    onMovie = { onAction(ActorDetailsAction.OnMovieClick(it)) }
                )
            } ?: run {
                item(span = StaggeredGridItemSpan.FullLine) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(MaterialTheme.dimen.large),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}

private fun LazyStaggeredGridScope.knownMovies(
    knownMovies: List<KnownMovie>,
    onMovie: (movieId: Int) -> Unit,
) {
    item(span = StaggeredGridItemSpan.FullLine) {
        Text(
            text = "Known movies",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
    }

    items(knownMovies) { movie ->
        MovieCard(
            title = movie.title,
            imageUrl = movie.posterPath.orEmpty(),
            rating = movie.voteAverage.div(2)
        ) { onMovie(movie.id) }
    }
    if (knownMovies.isEmpty()) {
        items(5) {
            MovieCardPlaceholder(
                modifier = Modifier
                    .shimmer()
            )
        }
    }
}

private fun LazyStaggeredGridScope.about(
    biography: String,
) {
    item(span = StaggeredGridItemSpan.FullLine) {
        Column(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.mediumSmall)
        ) {
            Text(
                text = stringResource(R.string.about),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            var isBiographyExpanded by remember {
                mutableStateOf(false)
            }

            Text(
                text = biography,
                maxLines = if (isBiographyExpanded) Int.MAX_VALUE else 3,
                modifier = Modifier
                    .clickable { isBiographyExpanded = !isBiographyExpanded }
                    .animateContentSize(
                        spring(Spring.DampingRatioLowBouncy, Spring.StiffnessLow)
                    ),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Light,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/**
 * @param popularity in percent max 100
 * */
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun AnimatedVisibilityScope.DetailsHeader(
    imagePath: String?,
    name: String,
    popularity: Double?,
) {
    Row(
        Modifier
            .heightIn(max = 250.dp)
            .fillMaxWidth()
    ) {
        val sharedTransitionScope = LocalSharedTransitionScope.current
        if (sharedTransitionScope != null) {
            with(sharedTransitionScope) {

                AsyncImage(
                    model = BASE_POSTER_IMAGE_URL + imagePath,
                    contentDescription = stringResource(R.string.movie_image),
                    error = painterResource(id = R.drawable.mr_bean),
                    placeholder = painterResource(id = R.drawable.mr_bean),
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(all = MaterialTheme.dimen.verySmall)
                        .sharedElement(
                            rememberSharedContentState(key = imagePath.orEmpty()),
                            animatedVisibilityScope = this@DetailsHeader,
                            boundsTransform = BoundsTransition
                        )
                        .clip(MaterialTheme.shapes.medium)
                )
            }
        } else {
            AsyncImage(
                model = BASE_POSTER_IMAGE_URL + imagePath,
                contentDescription = stringResource(R.string.movie_image),
                error = painterResource(id = R.drawable.mr_bean),
                placeholder = painterResource(id = R.drawable.mr_bean),
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(all = MaterialTheme.dimen.verySmall)
                    .clip(MaterialTheme.shapes.medium)
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                MaterialTheme.dimen.mediumSmall,
                Alignment.CenterVertically
            ),
        ) {
            if (sharedTransitionScope != null) {
                with(sharedTransitionScope) {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .sharedBounds(
                                rememberSharedContentState(key = name),
                                animatedVisibilityScope = this@DetailsHeader,
                                boundsTransform = BoundsTransition
                            )
                    )
                }
            } else {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                )
            }

            RatingStars(
                value = popularity?.div(100)?.times(5)?.toFloat() ?: 0f,
                size = 30.dp, spaceBetween = 3.dp,
                modifier = Modifier.shimmerIf { popularity == null }
            )
        }
    }
}
