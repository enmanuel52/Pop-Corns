package com.enmanuelbergling.ktormovies.ui.components

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.enmanuelbergling.ktormovies.domain.model.core.SimplerUi


@Composable
fun UiStateHandler(uiState: SimplerUi, onDismissDialog: () -> Unit) {
    when (uiState) {
        is SimplerUi.Error -> {
            DefaultErrorDialog(
                onDismissDialog,
                uiState.message.ifBlank { "An error just happen, please check your connection and try again ;)" }
            )
        }

        SimplerUi.Idle -> {}
        SimplerUi.Loading -> {
            LoadingDialog()
        }

        SimplerUi.Success -> {}
    }
}

@Composable
fun HandleDetailsUiState(
    uiState: SimplerUi,
    snackState: SnackbarHostState,
    onRetry: () -> Unit,
    isDetailLoaded: Boolean,
) {
    when (uiState) {
        is SimplerUi.Error -> {
            LaunchedEffect(key1 = Unit) {
                val snackResult = snackState.showSnackbar(
                    message = uiState.message, actionLabel = "Retry",
                    withDismissAction = true,
                    duration = SnackbarDuration.Indefinite
                )
                when (snackResult) {
                    SnackbarResult.Dismissed -> snackState.currentSnackbarData?.dismiss()
                    SnackbarResult.ActionPerformed -> onRetry()
                }
            }
        }

        SimplerUi.Idle, SimplerUi.Success -> {}
        SimplerUi.Loading -> if (!isDetailLoaded) {
            LoadingDialog()
        }
    }
}