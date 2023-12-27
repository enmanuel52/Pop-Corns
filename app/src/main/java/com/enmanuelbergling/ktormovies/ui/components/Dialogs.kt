package com.enmanuelbergling.ktormovies.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.window.Dialog
import com.enmanuelbergling.ktormovies.ui.core.dimen


@Composable
fun DefaultErrorDialog(
    onDismissDialog: () -> Unit,
    message: String,
) {
    AlertDialog(
        onDismissRequest = onDismissDialog,
        confirmButton = {
            TextButton(onClick = onDismissDialog) {
                Text(text = "Aceptar")
            }
        },
        text = {
            Text(text = message)
        },
        title = { Text(text = "Error") },
        icon = {
            Icon(
                imageVector = Icons.Rounded.Error,
                contentDescription = "error icon"
            )
        }
    )
}

@Composable
fun LoadingDialog() {
    Dialog(onDismissRequest = { }) {
        Surface(
            shape = AlertDialogDefaults.shape,
            tonalElevation = AlertDialogDefaults.TonalElevation,
            color = AlertDialogDefaults.containerColor,
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(MaterialTheme.dimen.medium),
                strokeCap = StrokeCap.Round
            )
        }
    }
}