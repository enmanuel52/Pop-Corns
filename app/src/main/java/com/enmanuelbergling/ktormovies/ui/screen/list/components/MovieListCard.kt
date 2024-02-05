package com.enmanuelbergling.ktormovies.ui.screen.list.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.enmanuelbergling.ktormovies.ui.core.dimen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieListCard(
    name: String,
    description: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    ElevatedCard(onClick = onClick, modifier = modifier) {
        Column(Modifier.padding(MaterialTheme.dimen.small)) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Light
            )
        }
    }
}