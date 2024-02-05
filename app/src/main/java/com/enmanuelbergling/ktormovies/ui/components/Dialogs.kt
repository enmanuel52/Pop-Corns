package com.enmanuelbergling.ktormovies.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
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

@Composable
fun CtiContentDialog(
    onDismiss: () -> Unit,
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    defaultWidth: Boolean = true,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    verticalSpacing: Dp = 0.dp,
    confirmButton: @Composable () -> Unit,
    dismissButton: @Composable (() -> Unit)? = null,
    neutralButton: @Composable (() -> Unit)? = null,
    content: LazyListScope.() -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = defaultWidth)
    ) {
        Surface(
            modifier = modifier,
            shape = AlertDialogDefaults.shape,
            color = containerColor,
            tonalElevation = AlertDialogDefaults.TonalElevation,
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.medium),
                modifier = Modifier
                    .padding(MaterialTheme.dimen.large)
            ) {
                icon?.let {
                    Icon(
                        imageVector = it,
                        contentDescription = null,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        tint = AlertDialogDefaults.iconContentColor,
                    )
                }
                Box(
                    modifier = Modifier.align(if (icon != null) Alignment.CenterHorizontally else Alignment.Start)
                ) { title() }

                val maxHeight = LocalConfiguration.current.screenHeightDp

                LazyColumn(
                    modifier = Modifier.heightIn(
                        max = maxHeight.times(60).div(100).dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(verticalSpacing)
                ) {
                    content()
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = MaterialTheme.dimen.verySmall)
                ) {
                    neutralButton?.let { it() } ?: Text(text = "")

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        dismissButton?.let { it() }
                        confirmButton()
                    }
                }
            }
        }
    }
}