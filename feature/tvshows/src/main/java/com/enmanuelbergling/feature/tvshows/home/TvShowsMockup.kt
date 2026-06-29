package com.enmanuelbergling.feature.tvshows.home

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.enmanuelbergling.core.common.util.BASE_BACKDROP_IMAGE_URL
import com.enmanuelbergling.core.common.util.BASE_POSTER_IMAGE_URL
import com.enmanuelbergling.core.model.tv.TvShow
import com.enmanuelbergling.core.ui.R
import com.enmanuelbergling.core.ui.components.common.MovieCard
import com.enmanuelbergling.core.ui.core.dimen
import com.enmanuelbergling.core.ui.theme.CornTimeTheme
import com.enmanuelbergling.core.ui.theme.Gold

/**
 * Senior Designer Mockup for TvShows Screen
 *
 * Focus: Sections below the header with high visual impact and clear hierarchy.
 */

@Composable
fun TvShowsMockupContent(
    topRated: List<TvShow>,
    onTheAir: List<TvShow>,
    airingToday: List<TvShow>,
    onDetails: (id: Int) -> Unit,
    onMore: (String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = WindowInsets.navigationBars.asPaddingValues(),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.large)
    ) {
        // 1. Featured Spotlight Section (Top Rated)
        // Instead of a row, we use a more impactful grid-like presentation for the best rated
        item {
            Column(modifier = Modifier.padding(horizontal = MaterialTheme.dimen.small)) {
                SectionHeaderMockup(
                    title = stringResource(R.string.top_rated),
                    onMore = { onMore("Top Rated") }
                )
                Spacer(modifier = Modifier.height(MaterialTheme.dimen.small))

                SpotlightGrid(topRated.take(4), onDetails)
            }
        }

        // 2. High Engagement Section (On The Air)
        // Uses landscape cards to emphasize the "current" feeling and provide visual variety
        item {
            Column {
                SectionHeaderMockup(
                    title = stringResource(R.string.on_the_air),
                    modifier = Modifier.padding(horizontal = MaterialTheme.dimen.small),
                    onMore = { onMore("On The Air") }
                )
                Spacer(modifier = Modifier.height(MaterialTheme.dimen.small))

                LandscapeTvShowsRow(onTheAir, onDetails)
            }
        }

        // 3. Discover Section (Airing Today)
        // Standard poster cards but with a slightly different padding/size treatment
        item {
            Column {
                SectionHeaderMockup(
                    title = stringResource(R.string.airing_today),
                    modifier = Modifier.padding(horizontal = MaterialTheme.dimen.small),
                    onMore = { onMore("Airing Today") }
                )
                Spacer(modifier = Modifier.height(MaterialTheme.dimen.small))

                DiscoveryTvShowsRow(airingToday, onDetails)
            }
        }
    }
}

@Composable
private fun SpotlightGrid(
    tvShows: List<TvShow>,
    onDetails: (id: Int) -> Unit,
) {
    // Bento-style grid: 1 large card, 3 smaller ones or 2x2
    Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.small)) {
        tvShows.chunked(2).forEach { rowItems ->
            Row(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.small)) {
                rowItems.forEach { show ->
                    SpotlightCard(
                        tvShow = show,
                        modifier = Modifier.weight(1f),
                        onClick = { onDetails(show.id) }
                    )
                }
                if (rowItems.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SpotlightCard(
    tvShow: TvShow,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    ElevatedCard(
        onClick = onClick,
        modifier = modifier.aspectRatio(1.2f),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = BASE_BACKDROP_IMAGE_URL + tvShow.backdropPath,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                placeholder = painterResource(R.drawable.pop_corn_and_cinema_backdrop)
            )

            // Gradient overlay for legibility
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)),
                            startY = 100f
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(MaterialTheme.dimen.small)
            ) {
                Text(
                    text = tvShow.name,
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Rounded.Star,
                        contentDescription = null,
                        tint = Gold,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = String.format("%.1f", tvShow.voteAverage),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}

@Composable
private fun LandscapeTvShowsRow(
    tvShows: List<TvShow>,
    onDetails: (id: Int) -> Unit,
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = MaterialTheme.dimen.small),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.small)
    ) {
        items(tvShows) { show ->
            LandscapeTvShowsCard(show) { onDetails(show.id) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LandscapeTvShowsCard(
    tvShow: TvShow,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.width(240.dp),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceContainer
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(MaterialTheme.dimen.verySmall)
        ) {
            AsyncImage(
                model = BASE_POSTER_IMAGE_URL + tvShow.posterPath,
                contentDescription = null,
                modifier = Modifier
                    .width(60.dp)
                    .aspectRatio(0.7f)
                    .clip(MaterialTheme.shapes.small),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.pop_corn_and_cinema_poster)
            )

            Column(
                modifier = Modifier
                    .padding(horizontal = MaterialTheme.dimen.small)
                    .weight(1f)
            ) {
                Text(
                    text = tvShow.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = tvShow.firstAirYear,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Rounded.Star,
                        contentDescription = null,
                        tint = Gold,
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = " ${tvShow.voteAverage}",
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}

@Composable
private fun DiscoveryTvShowsRow(
    tvShows: List<TvShow>,
    onDetails: (id: Int) -> Unit,
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = MaterialTheme.dimen.small),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.mediumSmall)
    ) {
        items(tvShows) { show ->
            // Reusing existing MovieCard but with discovery context
            MovieCard(
                imageUrl = show.posterPath.orEmpty(),
                title = show.name,
                rating = show.voteAverage,
                modifier = Modifier.width(140.dp),
                onClick = { onDetails(show.id) }
            )
        }
    }
}

@Composable
private fun SectionHeaderMockup(
    title: String,
    modifier: Modifier = Modifier,
    onMore: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )
            // Subtle indicator line
            Box(
                modifier = Modifier
                    .width(24.dp)
                    .height(3.dp)
                    .background(
                        MaterialTheme.colorScheme.tertiary,
                        MaterialTheme.shapes.small
                    )
            )
        }
        IconButton(onClick = onMore) {
            Icon(
                painter = painterResource(R.drawable.double_right),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Preview
@Composable
private fun TvShowsMockupPreview() {
    val dummyTvShow = TvShow(
        id = 1,
        name = "The Last of Us",
        originalName = "The Last of Us",
        overview = "Twenty years after modern civilization has been destroyed...",
        posterPath = "/u3bZgnocS9pZ9B6BB96U6Z6H6vA.jpg",
        backdropPath = "/uDgy6hyPdZ2UnpaHhiZohqlS3xV.jpg",
        genreIds = listOf(18, 10759),
        originalLanguage = "en",
        popularity = 100.0,
        firstAirDate = "2023-01-15",
        voteAverage = 8.7,
        voteCount = 4000
    )
    val dummyList = List(10) { dummyTvShow.copy(id = it, name = "TvShows $it") }

    CornTimeTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            TvShowsMockupContent(
                topRated = dummyList,
                onTheAir = dummyList,
                airingToday = dummyList,
                onDetails = {},
                onMore = {}
            )
        }
    }
}
