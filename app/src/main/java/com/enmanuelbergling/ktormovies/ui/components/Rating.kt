package com.enmanuelbergling.ktormovies.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.enmanuelbergling.ktormovies.ui.theme.Gold
import com.gowtham.ratingbar.RatingBar

@Composable
fun RatingStars(value: Float, modifier: Modifier = Modifier) {
    RatingBar(
        value = value,
        onValueChange = {},
        onRatingChanged = {},
        painterEmpty = rememberVectorPainter(
            image = Icons.Rounded.Star, color = MaterialTheme.colorScheme.surfaceVariant
        ),
        painterFilled = rememberVectorPainter(
            image = Icons.Rounded.Star, color = Gold
        ),
        modifier = modifier,
        size = 20.dp,
        spaceBetween = 2.dp
    )
}