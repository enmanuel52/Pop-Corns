package com.enmanuelbergling.ktormovies.ui.screen.movie.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
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
import com.enmanuelbergling.ktormovies.R
import com.enmanuelbergling.ktormovies.domain.BASE_IMAGE_URL
import com.enmanuelbergling.ktormovies.ui.core.dimen
import com.valentinilk.shimmer.shimmer

@Preview
@Composable
fun ActorPlaceHolder(modifier: Modifier = Modifier) {
    Column(
        modifier.widthIn(min = 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            Modifier
                .aspectRatio(.65f)
                .background(
                    MaterialTheme.colorScheme.surfaceVariant,
                    CardDefaults.elevatedShape
                )
        )

        Spacer(modifier = Modifier.height(MaterialTheme.dimen.small))

        Box(
            modifier = modifier
                .fillMaxWidth(.7f)
                .height(MaterialTheme.dimen.lessLarge)
                .background(
                    MaterialTheme.colorScheme.surfaceVariant,
                    MaterialTheme.shapes.small
                )
        )
    }
}

@Composable
fun ActorsRowPlaceholder(modifier: Modifier = Modifier) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.small),
        modifier = modifier.shimmer()
    ) {
        items(50) { ActorPlaceHolder(modifier = Modifier.width(110.dp)) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActorCard(
    imageUrl: String?,
    name: String,
    modifier: Modifier = Modifier,
    onCLick: () -> Unit,
) {
    Column(modifier) {
        ElevatedCard(
            onCLick,
            Modifier.animateContentSize()
        ) {
            AsyncImage(
                model = BASE_IMAGE_URL + imageUrl,
                contentDescription = "movie image",
                error = painterResource(id = R.drawable.mr_bean),
                placeholder = painterResource(id = R.drawable.mr_bean),
                modifier = Modifier.aspectRatio(.65f),
                contentScale = ContentScale.Crop
            )
        }
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(MaterialTheme.dimen.small),
            maxLines = 2,
            minLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}