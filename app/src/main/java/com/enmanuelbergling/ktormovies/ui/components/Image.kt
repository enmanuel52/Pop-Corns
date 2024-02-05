package com.enmanuelbergling.ktormovies.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.RenderVectorGroup
import androidx.compose.ui.graphics.vector.VectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.enmanuelbergling.ktormovies.R
import com.enmanuelbergling.ktormovies.domain.BASE_IMAGE_URL
import com.enmanuelbergling.ktormovies.ui.core.dimen

/**
 * Crete a [VectorPainter] with the given [ImageVector] and [Color]*/
@Composable
fun rememberVectorPainter(image: ImageVector, color: Color): VectorPainter =
    androidx.compose.ui.graphics.vector.rememberVectorPainter(
        defaultWidth = image.defaultWidth,
        defaultHeight = image.defaultHeight,
        viewportWidth = image.viewportWidth,
        viewportHeight = image.viewportHeight,
        name = image.name,
        tintColor = color,
        tintBlendMode = image.tintBlendMode,
        autoMirror = image.autoMirror,
        content = { _, _ -> RenderVectorGroup(group = image.root) }
    )

@Composable
fun UserImage(avatarPath: String, modifier: Modifier =Modifier) {
    AsyncImage(
        model = "$BASE_IMAGE_URL$avatarPath",
        contentDescription = "user image",
        placeholder = painterResource(
            id = R.drawable.mr_bean
        ),
        error = painterResource(
            id = R.drawable.mr_bean
        ),
        modifier = modifier
            .size(MaterialTheme.dimen.almostGiant)
            .clip(CircleShape),
        contentScale = ContentScale.Crop
    )
}