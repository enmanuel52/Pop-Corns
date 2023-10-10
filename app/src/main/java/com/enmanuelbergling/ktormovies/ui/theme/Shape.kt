package com.enmanuelbergling.ktormovies.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes

val Shape = Shapes(
    medium = RoundedCornerShape(
        topStartPercent = 12,
        bottomEndPercent = 12,
        topEndPercent = 3,
        bottomStartPercent = 3,
    ),

    small = RoundedCornerShape(
        topStartPercent = 8,
        bottomEndPercent = 8,
        topEndPercent = 0,
        bottomStartPercent = 0,
    ),

    large = RoundedCornerShape(
        topStartPercent = 16,
        bottomEndPercent = 16,
        topEndPercent = 4,
        bottomStartPercent = 4,
    ),

    )