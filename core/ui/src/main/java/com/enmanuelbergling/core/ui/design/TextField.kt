package com.enmanuelbergling.core.ui.design

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.VisualTransformation

/**
 * @param errorText when isn't null turn on isError
 * */
@Composable
fun CtiTextField(
    text: String,
    onTextChange: (String) -> Unit,
    hint: String,
    leadingIcon: ImageVector,
    modifier: Modifier = Modifier,
    errorText: String? = null,
    trailingIcon: @Composable () -> Unit = {},
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    TextField(
        value = text,
        onValueChange = onTextChange,
        modifier = modifier,
        label = {
            Text(
                text = hint
            )
        },
        isError = errorText != null,
        supportingText = {
            errorText?.let {
                Text(
                    text = it
                )
            }
        },
        leadingIcon = {
            Icon(imageVector = leadingIcon, contentDescription = "$hint icon")
        },
        trailingIcon = trailingIcon,
        visualTransformation = visualTransformation
    )
}