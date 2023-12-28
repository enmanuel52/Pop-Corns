package com.enmanuelbergling.ktormovies.ui.screen.movie.details

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.enmanuelbergling.ktormovies.R
import com.enmanuelbergling.ktormovies.domain.BASE_IMAGE_URL
import com.enmanuelbergling.ktormovies.domain.model.core.SimplerUi
import com.enmanuelbergling.ktormovies.ui.components.HandleUiState
import com.enmanuelbergling.ktormovies.ui.components.RatingStars
import com.enmanuelbergling.ktormovies.ui.core.dimen
import com.enmanuelbergling.ktormovies.ui.screen.movie.components.ActorCard
import com.enmanuelbergling.ktormovies.ui.screen.movie.components.ActorsRowPlaceholder
import com.enmanuelbergling.ktormovies.ui.screen.movie.details.model.MovieDetailsUiData
import com.enmanuelbergling.ktormovies.ui.screen.movie.details.model.PersonUiItem
import com.enmanuelbergling.ktormovies.ui.screen.movie.details.model.toPersonUi
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun MovieDetailsScreen(id: Int, onActor: (actorId: Int) -> Unit, onBack: () -> Unit) {

    val viewModel = koinViewModel<MovieDetailsVM> { parametersOf(id) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val uiData by viewModel.uiDataState.collectAsStateWithLifecycle()

    MovieDetailsScreen(
        uiData = uiData,
        uiState = uiState,
        onActor = onActor,
        onBack = onBack,
        onRetry = viewModel::loadPage
    )
}

@Composable
private fun MovieDetailsScreen(
    uiData: MovieDetailsUiData,
    uiState: SimplerUi,
    onActor: (actorId: Int) -> Unit,
    onBack: () -> Unit,
    onRetry: () -> Unit,
) {

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    val (details, creditsState) = uiData

    HandleUiState(
        uiState = uiState,
        snackState = snackbarHostState,
        onRetry,
        getFocus = details != null
    )

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { paddingValues ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.small),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            details?.let {
                detailsImage(backdropUrl = BASE_IMAGE_URL + details.backdropPath)

                information(
                    details.title,
                    details.releaseDate.substring(0..3),
                    details.voteAverage.toFloat(),
                    details.formattedGenres,
                    details.duration
                )

                overview(details.overview)

                persons(
                    title = "Cast",
                    persons = creditsState?.cast.orEmpty().map { it.toPersonUi() }.distinct(),
                    isLoading = uiState == SimplerUi.Loading && creditsState == null,
                    onActor = onActor
                )

                persons(
                    title = "Crew",
                    persons = creditsState?.crew.orEmpty().map { it.toPersonUi() }.distinct(),
                    isLoading = uiState == SimplerUi.Loading && creditsState == null,
                    onActor = onActor
                )
            }

        }
    }

}

private fun LazyListScope.persons(
    title: String,
    persons: List<PersonUiItem>,
    isLoading: Boolean = false,
    onActor: (personId: Int) -> Unit,
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
                        ActorCard(
                            imageUrl = person.imageUrl,
                            name = person.name,
                            modifier = Modifier.width(110.dp)
                        ) {
                            onActor(person.id)
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
            contentDescription = "poster image",
            placeholder = painterResource(
                id = R.drawable.pop_corn_and_cinema_backdrop
            ),
            error = painterResource(
                id = R.drawable.pop_corn_and_cinema_backdrop
            ),
            contentScale = ContentScale.Crop,
            modifier = Modifier.animateContentSize(),
        )
    }
}
