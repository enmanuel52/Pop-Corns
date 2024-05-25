package com.enmanuelbergling.core.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush

@Composable
fun Modifier.backgroundGradient() = run {
    val colors = listOf(
        MaterialTheme.colorScheme.primaryContainer,
        MaterialTheme.colorScheme.secondaryContainer,
    )

    then(
//        Modifier.drawBehind {
//            drawRect(
//                Brush.verticalGradient(
//                    colors
//                )
//            )
//        }
        Modifier
    )
}