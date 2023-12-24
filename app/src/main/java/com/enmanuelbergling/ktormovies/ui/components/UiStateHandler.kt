package com.enmanuelbergling.ktormovies.ui.components

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog
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
            Dialog(onDismissRequest = { }) {
                CircularProgressIndicator()
            }
        }

        SimplerUi.Success -> {}
    }
}