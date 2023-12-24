package com.enmanuelbergling.ktormovies.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.enmanuelbergling.ktormovies.ui.theme.Gold
import com.gowtham.ratingbar.RatingBar

@Composable
fun RatingStars(
    value: Float,
    modifier: Modifier = Modifier,
    size: Dp = 20.dp,
    spaceBetween: Dp = 2.dp,
) {
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
        size = size,
        spaceBetween = spaceBetween
    )
}