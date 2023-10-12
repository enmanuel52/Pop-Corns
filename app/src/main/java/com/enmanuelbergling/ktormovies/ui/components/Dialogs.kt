package com.enmanuelbergling.ktormovies.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable


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