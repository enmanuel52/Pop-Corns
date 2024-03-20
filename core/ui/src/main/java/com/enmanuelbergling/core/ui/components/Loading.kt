package com.enmanuelbergling.core.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import com.enmanuelbergling.core.ui.core.dimen

@Composable
fun LinearLoading(modifier: Modifier = Modifier) {
    LinearProgressIndicator(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = MaterialTheme.dimen.superSmall),
        strokeCap = StrokeCap.Round
    )
}