package com.enmanuelbergling.ktormovies.ui.screen.actor.details

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIos
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.enmanuelbergling.ktormovies.R
import com.enmanuelbergling.ktormovies.domain.BASE_IMAGE_URL
import com.enmanuelbergling.ktormovies.domain.TAG
import com.enmanuelbergling.ktormovies.domain.model.actor.ActorDetails
import com.enmanuelbergling.ktormovies.domain.model.actor.KnownMovie
import com.enmanuelbergling.ktormovies.ui.components.RatingStars
import com.enmanuelbergling.ktormovies.ui.components.UiStateHandler
import com.enmanuelbergling.ktormovies.ui.core.dimen
import com.enmanuelbergling.ktormovies.ui.screen.movie.components.MovieCard
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ActorDetailsScreen(id: Int, onMovie: (movieId: Int) -> Unit, onBack: () -> Unit) {

    val viewModel = koinViewModel<ActorDetailsVM> { parametersOf(id) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val details by viewModel.detailsState.collectAsStateWithLifecycle()

    val knownMovies by viewModel.knownMoviesState.collectAsStateWithLifecycle()

    UiStateHandler(uiState = uiState, onDismissDialog = viewModel::onDismissDialog)

    details?.let {
        ActorDetailsScreen(
            details = it,
            onBack = onBack,
            knownMovies = knownMovies,
            onMovie = onMovie
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ActorDetailsScreen(
    details: ActorDetails,
    knownMovies: List<KnownMovie>,
    onMovie: (movieId: Int) -> Unit,
    onBack: () -> Unit,
) {

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Details") },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBackIos,
                            contentDescription = "back icon"
                        )
                    }
                }, actions = {
                    IconButton(
                        onClick = { /*TODO*/ },
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.FavoriteBorder,
                            contentDescription = "favorite"
                        )
                    }
                }, scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->
        Column(
            Modifier
                .padding(paddingValues)
        ) {
            val configuration = LocalConfiguration.current
            val columnCount by remember(configuration) {
                derivedStateOf { configuration.screenWidthDp / 120 }
            }

            LazyVerticalGrid(
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.mediumSmall),
                modifier = Modifier
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                columns = GridCells.Adaptive(120.dp),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.small),
                contentPadding = PaddingValues(MaterialTheme.dimen.small)
            ) {

                detailsHeader(
                    imageUrl = BASE_IMAGE_URL + details.profilePath,
                    name = details.name,
                    popularity = details.popularity,
                    columnCount
                )

                about(details.biography, columnCount)

                knownMovies(knownMovies, onMovie, columnCount)
            }

        }
    }
}

private fun LazyGridScope.knownMovies(
    knownMovies: List<KnownMovie>,
    onMovie: (movieId: Int) -> Unit,
    columnCount: Int,
) {
    item(span = { GridItemSpan(columnCount) }) {
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
            rating = movie.voteAverage.div(2),
        ) { onMovie(movie.id) }
    }
}

private fun LazyGridScope.about(biography: String, columnCount: Int) {
    item(span = { GridItemSpan(columnCount) }) {
        Column(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.mediumSmall)
        ) {
            Text(
                text = "About",
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
 * @param popularity in percent max 100*/
private fun LazyGridScope.detailsHeader(
    imageUrl: String,
    name: String,
    popularity: Double,
    columnCount: Int,
) {
    item(span = { GridItemSpan(columnCount) }) {
        Row(
            Modifier
                .heightIn(max = 250.dp)
                .fillMaxWidth()
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "movie image",
                error = painterResource(id = R.drawable.mr_bean),
                placeholder = painterResource(id = R.drawable.mr_bean),
                modifier = Modifier
                    .padding(all = MaterialTheme.dimen.verySmall)
                    .clip(MaterialTheme.shapes.medium)
            )
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
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )

                LaunchedEffect(key1 = Unit) {
                    Log.d(
                        TAG,
                        "detailsHeader: $popularity, ${popularity.div(100).times(5).toFloat()}"
                    )
                }
                RatingStars(
                    value = popularity.div(100).times(5).toFloat(),
                    size = 30.dp, spaceBetween = 3.dp
                )
            }
        }
    }
}
