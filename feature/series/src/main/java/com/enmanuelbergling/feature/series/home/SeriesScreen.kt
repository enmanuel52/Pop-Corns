package com.enmanuelbergling.feature.series.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun SeriesScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(text = "Series Screen", modifier = Modifier.align(Alignment.Center))
    }
}