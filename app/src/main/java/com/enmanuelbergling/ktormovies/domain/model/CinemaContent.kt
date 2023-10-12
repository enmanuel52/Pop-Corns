package com.enmanuelbergling.ktormovies.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material.icons.rounded.Tv
import androidx.compose.ui.graphics.vector.ImageVector

enum class CinemaContent(val icon: ImageVector) {
    Movie(Icons.Rounded.Movie), Series(Icons.Rounded.Tv)
}