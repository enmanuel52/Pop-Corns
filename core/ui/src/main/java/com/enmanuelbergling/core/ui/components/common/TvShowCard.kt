package com.enmanuelbergling.core.ui.components.common

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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.enmanuelbergling.core.common.util.BASE_BACKDROP_IMAGE_URL
import com.enmanuelbergling.core.common.util.BASE_POSTER_IMAGE_URL
import com.enmanuelbergling.core.ui.R
import com.enmanuelbergling.core.ui.components.RatingStars
import com.enmanuelbergling.core.ui.core.dimen
import com.enmanuelbergling.core.ui.theme.CornTimeTheme

/**
 * @param rating between 1 and 5 showed as yellow stars
 * */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TvShowCard(
    imageUrl: String,
    title: String,
    rating: Double,
    modifier: Modifier = Modifier,
    titleLines: Int = 1,
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
                ),
        ) {
            AsyncImage(
                model = BASE_POSTER_IMAGE_URL + imageUrl,
                contentDescription = "movie image",
                error = painterResource(id = R.drawable.pop_corn_and_cinema_poster),
                placeholder = painterResource(id = R.drawable.pop_corn_and_cinema_poster),
                modifier = Modifier.aspectRatio(.65f),
                contentScale = ContentScale.Crop
            )
        }
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = titleLines,
            overflow = TextOverflow.Ellipsis
        )
        RatingStars(value = rating.div(2).toFloat(), size = 16.dp, spaceBetween = 1.dp)
    }
}

@Preview
@Composable
fun TvShowCardPrev() {
    CornTimeTheme {
        TvShowCard(imageUrl = "", title = "Joker", rating = 3.5) {}
    }
}

@Preview
@Composable
fun TvShowCardPlaceholder(modifier: Modifier = Modifier) {
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

@Composable
fun HeaderTvShowCard(
    imageUrl: String,
    title: String,
    rating: Double,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Column(modifier) {
        ElevatedCard(onClick = onClick) {
            AsyncImage(
                model = BASE_BACKDROP_IMAGE_URL + imageUrl,
                contentDescription = "header image",
                placeholder = painterResource(
                    id = R.drawable.pop_corn_and_cinema_backdrop
                ),
                error = painterResource(
                    id = R.drawable.pop_corn_and_cinema_backdrop
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.78f),
                contentScale = ContentScale.FillWidth
            )
        }

        Spacer(modifier = Modifier.height(MaterialTheme.dimen.small))

        HeaderTvShowInfo(
            title = title,
            rating = rating,
            modifier = Modifier.padding(MaterialTheme.dimen.small)
        )
    }
}

@Composable
fun HeaderTvShowInfo(
    title: String,
    rating: Double,
    modifier: Modifier = Modifier,
) {
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        HeaderTvShowTitle(title = title, modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.width(MaterialTheme.dimen.verySmall))
        RatingStars(value = rating.div(2).toFloat(), spaceBetween = 1.dp)
    }
}

@Composable
fun HeaderTvShowTitle(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.SemiBold,
        maxLines = 1,
        modifier = modifier,
        overflow = TextOverflow.Ellipsis
    )
}

@Preview
@Composable
fun HeaderTvShowCardPrev() {
    HeaderTvShowCard(
        imageUrl = "", title = "Joker", rating = 3.5
    ) {}
}

@Preview
@Composable
fun HeaderTvShowPlaceholder(modifier: Modifier = Modifier) {
    Column(modifier) {
        Box(
            modifier = Modifier
                .aspectRatio(1.6f)
                .background(MaterialTheme.colorScheme.surfaceVariant, CardDefaults.elevatedShape)
        )

        Spacer(modifier = Modifier.height(MaterialTheme.dimen.small))

        HeaderTvShowInfoPlaceholder()
    }
}

@Composable
private fun HeaderTvShowInfoPlaceholder() {
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