package com.enmanuelbergling.ktormovies.ui.screen.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.Mood
import androidx.compose.material.icons.rounded.Tv
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.enmanuelbergling.ktormovies.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun DetailsScreen(id: Int) {

    val viewModel = koinViewModel<DetailsVM>()

    val movie by viewModel.movieDetailsState.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(modifier = Modifier.fillMaxSize()) {

        Box(modifier = Modifier.align(Alignment.Center)) {

            UiStateHandler(
                uiState = uiState,
                onDialogDismiss = viewModel::dismissDialog
            )

            movie?.let {
                OutlinedCard {
                    AsyncImage(
                        model = BASE_IMAGE_URL + it.posterPath,
                        contentDescription = "poster image",
                        error = painterResource(
                            id = R.drawable.ic_launcher_foreground
                        ),
                        modifier = Modifier.width(200.dp),
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = it.title,
                        style = MaterialTheme.typography.titleLarge,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 3,
                        textAlign = TextAlign.Center
                    )
                }
            } ?: run {
                if (uiState is DetailsUi.Idle) {
                    ExtendedFloatingActionButton(
                        onClick = { viewModel.getDetails(id) }, icon = {
                            Icon(
                                imageVector = Icons.Rounded.Tv,
                                contentDescription = "get movie"
                            )
                        }, text = { Text(text = "Load movie") }
                    )
                }
            }

        }
    }

}

@Composable
fun UiStateHandler(uiState: DetailsUi, onDialogDismiss: () -> Unit) {
    when (uiState) {
        is DetailsUi.Error -> {
            AlertDialog(
                onDismissRequest = onDialogDismiss,
                confirmButton = {
                    TextButton(onClick = onDialogDismiss) {
                        Text(text = "Accept")
                    }
                },
                title = { Text(text = "Error") },
                text = { Text(text = uiState.exception.message.orEmpty()) },
                icon = {
                    Icon(
                        imageVector = Icons.Rounded.Error,
                        contentDescription = "error icon",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            )
        }

        DetailsUi.Idle -> {}
        DetailsUi.Loading -> {
            CircularProgressIndicator(strokeWidth = 6.dp, modifier = Modifier.size(50.dp))
        }

        DetailsUi.Success -> {
            AlertDialog(
                onDismissRequest = onDialogDismiss,
                confirmButton = {
                    TextButton(onClick = onDialogDismiss) {
                        Text(text = "Accept")
                    }
                },
                title = { Text(text = "Success") },
                text = { Text(text = "The request was successfully done") },
                icon = {
                    Icon(
                        imageVector = Icons.Rounded.Mood,
                        contentDescription = "error icon",
                    )
                }
            )
        }
    }
}

const val BASE_IMAGE_URL = "https://image.tmdb.org/t/p/original"
