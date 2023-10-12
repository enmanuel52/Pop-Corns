package com.enmanuelbergling.ktormovies.ui.screen.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIos
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.enmanuelbergling.ktormovies.R
import com.enmanuelbergling.ktormovies.domain.model.MovieDetails
import com.enmanuelbergling.ktormovies.ui.components.DefaultErrorDialog
import com.enmanuelbergling.ktormovies.ui.components.RatingStars
import com.enmanuelbergling.ktormovies.ui.core.dimen
import com.enmanuelbergling.ktormovies.ui.screen.detail.BASE_IMAGE_URL
import org.koin.androidx.compose.koinViewModel

@Composable
fun DetailsScreen(id: Int, onBack: () -> Unit) {

    val viewModel = koinViewModel<DetailsVM>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val details by viewModel.detailsState.collectAsStateWithLifecycle()

    UiStateHandler(uiState = uiState, viewModel::hideErrorDialog)

    details?.let { DetailsScreen(it, onBack) }
}

@Composable
fun DetailsScreen(details: MovieDetails, onBack: () -> Unit) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.small)) {

        detailsImage(backdropUrl = BASE_IMAGE_URL + details.backdropPath, onBack = onBack)

        information(
            details.title,
            details.releaseDate.substring(0..3),
            details.voteAverage.toFloat(),
            details.formattedGenres,
            details.duration
        )

        overview(details.overview)
    }
}

private fun LazyListScope.overview(overview: String) {
    item {
        Text(
            text = overview,
            modifier = Modifier.padding(horizontal = MaterialTheme.dimen.small),
            style = MaterialTheme.typography.bodyMedium
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
        Column {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.dimen.small),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    //getting year
                    text = "$title ($year)",
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                //it comes 10 for server
                RatingStars(value = rating.div(2))
            }

            Text(
                text = "$genres - $duration",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Light
            )
        }
    }
}

private fun LazyListScope.detailsImage(
    backdropUrl: String?,
    onBack: () -> Unit
) {
    item {
        Box {
            AsyncImage(
                model = backdropUrl,
                contentDescription = "poster image",
                placeholder = painterResource(
                    id = R.drawable.pop_corn_and_cinema
                ),
                error = painterResource(
                    id = R.drawable.pop_corn_and_cinema
                ),
//                modifier = Modifier.aspectRatio(.8f),
                contentScale = ContentScale.Crop,
            )

            FilledTonalIconButton(
                onClick = onBack,
                modifier = Modifier
                    .padding(MaterialTheme.dimen.small)
                    .align(Alignment.TopStart)
            ) {
                Icon(imageVector = Icons.Rounded.ArrowBackIos, contentDescription = "back icon")
            }

            FilledTonalIconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .padding(MaterialTheme.dimen.small)
                    .align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = Icons.Rounded.FavoriteBorder,
                    contentDescription = "favorite"
                )
            }
        }
    }
}

@Composable
private fun UiStateHandler(uiState: DetailsUi, onDismissDialog: () -> Unit) {
    when (uiState) {
        is DetailsUi.Error -> {
            DefaultErrorDialog(
                onDismissDialog,
                uiState.message.ifBlank { "An error just happen, please check your connection and try again ;)" }
            )
        }

        DetailsUi.Idle -> {}
        DetailsUi.Loading -> {
            Dialog(onDismissRequest = { }) {
                CircularProgressIndicator()
            }
        }

        DetailsUi.Success -> {}
    }
}