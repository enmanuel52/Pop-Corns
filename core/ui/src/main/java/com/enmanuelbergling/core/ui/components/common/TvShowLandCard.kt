package com.enmanuelbergling.core.ui.components.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.enmanuelbergling.core.common.util.BASE_BACKDROP_IMAGE_URL
import com.enmanuelbergling.core.model.tv.TvShow
import com.enmanuelbergling.core.ui.R
import com.enmanuelbergling.core.ui.components.RatingStars
import com.enmanuelbergling.core.ui.core.dimen
import com.enmanuelbergling.core.ui.theme.CornTimeTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TvShowLandCard(
    tvShow: TvShow,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .heightIn(max = 90.dp)
    ) {
        Row(
            Modifier.padding(MaterialTheme.dimen.small),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = BASE_BACKDROP_IMAGE_URL + tvShow.backdropPath,
                contentDescription = "series image",
                error = painterResource(id = R.drawable.pop_corn_and_cinema_poster),
                placeholder = painterResource(id = R.drawable.pop_corn_and_cinema_poster),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(
                        horizontal = MaterialTheme.dimen.small,
                        vertical = MaterialTheme.dimen.verySmall
                    )
                    .aspectRatio(1.5f)
                    .clip(shape)
            )

            Column {
                Text(
                    text = tvShow.name + "(${tvShow.firstAirYear})",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                )

                RatingStars(
                    value = tvShow.voteAverage.div(2).toFloat(),
                    spaceBetween = 1.dp,
                    surfaceVariant = LocalContentColor.current,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TvShowLandCardPrev() {
    CornTimeTheme {
        TvShowLandCard(
            tvShow = TvShow(
                id = 0,
                name = "Breaking Bad",
                originalName = "Breaking Bad",
                overview = "A chemistry teacher turns to crime.",
                posterPath = null,
                backdropPath = null,
                genreIds = listOf(),
                originalLanguage = "English",
                popularity = 9.0,
                firstAirDate = "2008-01-20",
                voteAverage = 8.9,
                voteCount = 12000,
            ),
            modifier = Modifier.padding(20.dp)
        ) {}
    }
}
