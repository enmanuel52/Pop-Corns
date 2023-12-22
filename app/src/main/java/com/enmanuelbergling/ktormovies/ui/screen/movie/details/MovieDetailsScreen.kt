package com.enmanuelbergling.ktormovies.ui.screen.movie.details

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIos
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.enmanuelbergling.ktormovies.R
import com.enmanuelbergling.ktormovies.domain.BASE_IMAGE_URL
import com.enmanuelbergling.ktormovies.domain.model.MovieDetails
import com.enmanuelbergling.ktormovies.ui.components.DefaultErrorDialog
import com.enmanuelbergling.ktormovies.ui.components.RatingStars
import com.enmanuelbergling.ktormovies.ui.core.dimen
import com.enmanuelbergling.ktormovies.ui.theme.CornTimeTheme
import com.valentinilk.shimmer.shimmer
import org.koin.androidx.compose.koinViewModel

@Composable
fun MovieDetailsScreen(id: Int, onBack: () -> Unit) {

    val viewModel = koinViewModel<MovieDetailsVM>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val details by viewModel.detailsState.collectAsStateWithLifecycle()

    val creditsState by viewModel.creditsState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit, block = { viewModel.getDetails(id) })

    UiStateHandler(uiState = uiState, onDismissDialog = onBack)

    details?.let {
        MovieDetailsScreen(
            details = it,
            credits = creditsState,
            onBack = onBack
        ) { viewModel.getMovieCredits(id) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailsScreen(
    details: MovieDetails,
    credits: CreditsUiState,
    onBack: () -> Unit,
    onGetCredits: () -> Unit
) {

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    LaunchedEffect(key1 = credits) {
        if (credits is CreditsUiState.Error) {
            val result = snackbarHostState.showSnackbar(
                message = credits.message,
                actionLabel = "Retry",
                duration = SnackbarDuration.Indefinite
            )
            when (result) {
                SnackbarResult.Dismissed -> {}
                SnackbarResult.ActionPerformed -> onGetCredits()
            }
        }
    }

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
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { paddingValues ->

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.small),
            modifier = Modifier
                .padding(paddingValues)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        ) {

            detailsImage(backdropUrl = BASE_IMAGE_URL + details.backdropPath)

            information(
                details.title,
                details.releaseDate.substring(0..3),
                details.voteAverage.toFloat(),
                details.formattedGenres,
                details.duration
            )

            overview(details.overview)

            castAndCrew(credits)
        }
    }

}

private fun LazyListScope.castAndCrew(credits: CreditsUiState) {
    casts(credits)

    crew(credits)
}

private fun LazyListScope.crew(credits: CreditsUiState) {
    item {
        Column {
            Text(
                text = "Crew",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = MaterialTheme.dimen.small)
            )

            Spacer(modifier = Modifier.height(MaterialTheme.dimen.small))

            if (credits is CreditsUiState.Success) {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.small)) {
                    val crew = credits.data?.crew.orEmpty()
                    itemsIndexed(crew) { index, it ->
                        ProfileItem(
                            photo = BASE_IMAGE_URL + it.profilePath,
                            name = it.name,
                            modifier = Modifier.padding(
                                start = if (index == 0) MaterialTheme.dimen.small else 0.dp,
                                end = if (index == crew
                                        .count() - 1
                                ) MaterialTheme.dimen.small else 0.dp,
                            )
                        )
                    }
                }
            } else if (credits is CreditsUiState.Loading) {
                ProfilesShimmer()
            }
        }
    }
}

private fun LazyListScope.casts(credits: CreditsUiState) {
    item {
        Column {
            Text(
                text = "Cast",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = MaterialTheme.dimen.small)
            )

            Spacer(modifier = Modifier.height(MaterialTheme.dimen.small))

            if (credits is CreditsUiState.Success) {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.small)) {
                    val casts = credits.data?.cast.orEmpty()

                    itemsIndexed(casts) { index, cast ->
                        ProfileItem(
                            photo = BASE_IMAGE_URL + cast.profilePath,
                            name = cast.name,
                            modifier = Modifier.padding(
                                start = if (index == 0) MaterialTheme.dimen.small else 0.dp,
                                end = if (index == casts
                                        .count() - 1
                                ) MaterialTheme.dimen.small else 0.dp,
                            )
                        )
                    }
                }
            } else if (credits is CreditsUiState.Loading) {
                ProfilesShimmer()
            }
        }
    }
}

@Composable
private fun ProfilesShimmer() {
    LazyRow(
        modifier = Modifier.shimmer(),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.small)
    ) {
        items(8) {
            ProfilePlaceholder(Modifier.padding(start = if (it == 0) MaterialTheme.dimen.small else 0.dp))
        }
    }
}

@Composable
private fun ProfileItem(photo: String?, name: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.widthIn(max = 130.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AsyncImage(
            model = photo,
            contentDescription = "actor image",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.clip(MaterialTheme.shapes.medium),
            placeholder = painterResource(id = R.drawable.mr_bean),
            error = painterResource(id = R.drawable.mr_bean),
        )

        Spacer(modifier = Modifier.height(MaterialTheme.dimen.verySmall))

        Text(
            text = name,
            style = MaterialTheme.typography.labelLarge,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
fun ProfilePlaceholder(modifier: Modifier = Modifier) {
    Column(
        modifier.width(130.dp),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.verySmall)
    ) {
        Box(
            modifier = Modifier
                .size(130.dp, 160.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.shapes.medium)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth(.6f)
                .height(12.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant, RectangleShape)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth(.8f)
                .height(12.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant, RectangleShape)
        )
    }
}

@Preview
@Composable
fun ProfilePlaceholderPrev() {
    CornTimeTheme {
        ProfilePlaceholder()
    }
}

private fun LazyListScope.overview(overview: String) {
    item {
        var expanded by remember {
            mutableStateOf(false)
        }

        Text(
            text = overview,
            modifier = Modifier
                .padding(all = MaterialTheme.dimen.small)
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

private fun LazyListScope.information(
    title: String,
    year: String,
    rating: Float,
    genres: String,
    duration: String
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
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
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

@Composable
private fun UiStateHandler(uiState: MovieDetailsUi, onDismissDialog: () -> Unit) {
    when (uiState) {
        is MovieDetailsUi.Error -> {
            DefaultErrorDialog(
                onDismissDialog,
                uiState.message.ifBlank { "An error just happen, please check your connection and try again ;)" }
            )
        }

        MovieDetailsUi.Idle -> {}
        MovieDetailsUi.Loading -> {
            Dialog(onDismissRequest = { }) {
                CircularProgressIndicator()
            }
        }

        MovieDetailsUi.Success -> {}
    }
}