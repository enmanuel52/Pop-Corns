package com.enmanuelbergling.feature.watchlists.components

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.enmanuelbergling.core.ui.R
import com.enmanuelbergling.core.ui.components.CtiContentDialog
import com.enmanuelbergling.core.ui.core.dimen
import com.enmanuelbergling.core.ui.design.CtiTextField
import com.enmanuelbergling.feature.watchlists.model.CreateListEvent
import com.enmanuelbergling.feature.watchlists.model.CreateListForm

@Composable
internal fun CreateListDialog(
    form: CreateListForm,
    onEvent: (CreateListEvent) -> Unit,
) {
    CtiContentDialog(
        onDismiss = { onEvent(CreateListEvent.ToggleVisibility) },
        title = { Text("New List") },
        confirmButton = {
            TextButton(onClick = { onEvent(CreateListEvent.Submit) }) {
                Text(text = "Create")
            }
        },
        verticalSpacing = MaterialTheme.dimen.medium
    ) {
        item {
            CtiTextField(
                text = form.name,
                onTextChange = { onEvent(CreateListEvent.Name(it)) },
                hint = "List name*",
                leadingIcon = {
                    Icon(painter = painterResource(R.drawable.info_circle), null)
                },
                errorText = form.nameError,
            )
        }

        item {
            CtiTextField(
                text = form.description,
                onTextChange = { onEvent(CreateListEvent.Description(it)) },
                hint = "List description*",
                leadingIcon = {
                    Icon(painter = painterResource(R.drawable.info_circle), null)
                },
                errorText = form.descriptionError
            )
        }
    }
}