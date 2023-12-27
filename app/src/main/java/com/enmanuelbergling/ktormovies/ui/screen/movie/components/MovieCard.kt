package com.enmanuelbergling.ktormovies.ui.screen.movie.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.enmanuelbergling.ktormovies.R
import com.enmanuelbergling.ktormovies.domain.BASE_IMAGE_URL
import com.enmanuelbergling.ktormovies.ui.components.RatingStars
import com.enmanuelbergling.ktormovies.ui.core.dimen
import com.enmanuelbergling.ktormovies.ui.theme.CornTimeTheme
import com.valentinilk.shimmer.shimmer

/**
 * @param rating between 1 and 5 showed as yellow stars
 * */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieCard(
    imageUrl: String,
    title: String,
    rating: Double,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.small),
        modifier = modifier
    ) {
        ElevatedCard(
            onClick = onClick, modifier = Modifier
                .animateContentSize(
                    spring(Spring.DampingRatioLowBouncy, Spring.StiffnessMediumLow)
                )
        ) {
            AsyncImage(
                model = BASE_IMAGE_URL + imageUrl,
                contentDescription = "movie image",
                error = painterResource(id = R.drawable.pop_corn_and_cinema_poster),
                placeholder = painterResource(id = R.drawable.pop_corn_and_cinema_poster)
            )
        }
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        RatingStars(value = rating.toFloat(), size = 16.dp, spaceBetween = 1.dp)
    }
}

@Preview
@Composable
fun MovieCardPrev() {
    CornTimeTheme {
        MovieCard(imageUrl = "", title = "Joker", rating = 3.5) {}
    }
}

@Preview
@Composable
fun MovieCardPlaceholder(modifier: Modifier = Modifier) {
    Column(
        modifier.widthIn(max = 200.dp),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.small)
    ) {
        Box(
            Modifier
                .aspectRatio(.65f)
                .background(
                    MaterialTheme.colorScheme.surfaceVariant,
                    CardDefaults.elevatedShape
                )
        )
        Box(
            modifier = modifier
                .fillMaxWidth(.5f)
                .height(MaterialTheme.dimen.lessLarge)
                .background(
                    MaterialTheme.colorScheme.surfaceVariant,
                    MaterialTheme.shapes.small
                )
        )
        RatingStars(value = 0f)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeaderMovieCard(
    imageUrl: String,
    title: String,
    rating: Double,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Column(modifier) {
        ElevatedCard(onClick = onClick) {
            AsyncImage(
                model = BASE_IMAGE_URL + imageUrl,
                contentDescription = "header image",
                placeholder = painterResource(
                    id = R.drawable.pop_corn_and_cinema_backdrop
                ),
                error = painterResource(
                    id = R.drawable.pop_corn_and_cinema_backdrop
                )
            )
        }

        Spacer(modifier = Modifier.height(MaterialTheme.dimen.small))

        HeaderMovieInfo(
            title = title,
            rating = rating,
            modifier = Modifier.padding(MaterialTheme.dimen.small)
        )
    }
}

@Composable
private fun HeaderMovieInfo(
    title: String,
    rating: Double,
    modifier: Modifier = Modifier,
) {
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            modifier = Modifier.weight(1f),
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.width(MaterialTheme.dimen.verySmall))
        RatingStars(value = rating.toFloat(), spaceBetween = 1.dp)
    }
}

@Preview
@Composable
fun HeaderMovieCardPrev() {
    HeaderMovieCard(
        imageUrl = "", title = "Joker", rating = 3.5
    ) {}
}

@Preview
@Composable
fun HeaderMoviePlaceholder(modifier: Modifier = Modifier) {
    Column(modifier) {
        Box(
            modifier = Modifier
                .aspectRatio(1.6f)
                .background(MaterialTheme.colorScheme.surfaceVariant, CardDefaults.elevatedShape)
        )

        Spacer(modifier = Modifier.height(MaterialTheme.dimen.small))

        HeaderMovieInfoPlaceholder()
    }
}

@Composable
private fun HeaderMovieInfoPlaceholder() {
    Row {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(16.dp)
                .background(
                    MaterialTheme.colorScheme.surfaceVariant,
                    MaterialTheme.shapes.small
                )
        )
        Spacer(modifier = Modifier.width(MaterialTheme.dimen.verySmall))
        RatingStars(value = 0f)
    }
}

@Composable
fun VerticalGridPlaceholder() {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(120.dp),
        modifier = Modifier.shimmer(),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.small),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.small),
    ) {
        items(50) {
            HeaderMoviePlaceholder()
        }
    }
}