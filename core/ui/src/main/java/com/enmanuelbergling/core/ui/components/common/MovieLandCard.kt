package com.enmanuelbergling.core.ui.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
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
import com.enmanuelbergling.core.model.movie.Movie
import com.enmanuelbergling.core.ui.R
import com.enmanuelbergling.core.ui.components.RatingStars
import com.enmanuelbergling.core.ui.core.dimen
import com.enmanuelbergling.core.ui.theme.CornTimeTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieLandCard(
    movie: Movie,
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
                model = BASE_BACKDROP_IMAGE_URL + movie.backdropPath,
                contentDescription = "movie image",
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
                    text = movie.title + "(${movie.releaseYear})",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                )

                RatingStars(
                    value = movie.voteAverage.div(2).toFloat(),
                    spaceBetween = 1.dp,
                    surfaceVariant = LocalContentColor.current,
                )
            }
        }
    }
}

@Preview
@Composable
fun MovieLandCardPlaceholder(
    modifier: Modifier = Modifier,
) {
    ElevatedCard(
        modifier = modifier
            .heightIn(max = 90.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(
                        horizontal = MaterialTheme.dimen.mediumSmall,
                        vertical = MaterialTheme.dimen.small
                    )
                    .aspectRatio(1.5f)
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant,
                        MaterialTheme.shapes.medium
                    )
            )

            Spacer(modifier = Modifier.width(MaterialTheme.dimen.mediumSmall))

            Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceEvenly) {
                Box(
                    modifier = Modifier
                        .height(MaterialTheme.dimen.medium)
                        .fillMaxWidth(.75f)
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant,
                            shape = MaterialTheme.shapes.small
                        )
                )

                RatingStars(value = 0f, spaceBetween = 3.dp)
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun MovieLandCardPrev() {
    CornTimeTheme {
        MovieLandCard(
            movie = GODFATHER_2,
            modifier = Modifier.padding(20.dp)
        ) {}
    }
}

val GODFATHER_2 = Movie(
    adult = false,
    backdropPath = "/uIk2g2bRkNwNywKZIhC5oIU94Kh.jpg",
    genreIds = listOf(),
    id = 0,
    originalLanguage = "English",
    originalTitle = "The Godfather 2",
    overview = "This is a mafia movie, 2nd part",
    popularity = 9.0,
    posterPath = "godfather2_path",
    releaseDate = "2004",
    title = "The Godfather 2",
    video = false,
    voteAverage = 7.0,
    voteCount = 2673
)