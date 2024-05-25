package com.enmanuelbergling.feature.series.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.enmanuelbergling.core.ui.components.backgroundGradient

@Composable
fun SeriesScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .backgroundGradient()
    ) {
        Text(text = "Series Screen", modifier = Modifier.align(Alignment.Center))
    }
}