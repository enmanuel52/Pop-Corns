package com.enmanuelbergling.ktormovies.ui.screen.movie.home

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridScope
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.enmanuelbergling.ktormovies.R
import com.enmanuelbergling.ktormovies.domain.BASE_IMAGE_URL
import com.enmanuelbergling.ktormovies.domain.model.Movie
import com.enmanuelbergling.ktormovies.ui.core.LocalTopAppScrollBehaviour
import com.enmanuelbergling.ktormovies.ui.core.dimen
import com.valentinilk.shimmer.shimmer
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoviesScreen(onDetails: (id: Int) -> Unit) {

    val viewModel = koinViewModel<MoviesVM>()
    val topRatedMovies = viewModel.topRatedMovies.collectAsLazyPagingItems()

    val scrollBehaviour = LocalTopAppScrollBehaviour.current!!

    Column{
        MoviesGrid(
            movies = topRatedMovies,
            onDetails = onDetails,
            modifier = Modifier.nestedScroll(scrollBehaviour.nestedScrollConnection)
        )

        if (topRatedMovies.itemCount != 0 && topRatedMovies.loadState.append == LoadState.Loading) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = MaterialTheme.dimen.superSmall)
            )
        }
    }
}

@Composable
fun MoviesGrid(
    movies: LazyPagingItems<Movie>,
    modifier: Modifier = Modifier,
    onDetails: (id: Int) -> Unit
) {


    LazyVerticalStaggeredGrid(
        modifier = modifier.fillMaxWidth(),
        columns = StaggeredGridCells.Adaptive(150.dp),
        contentPadding = PaddingValues(MaterialTheme.dimen.verySmall),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.small),
        verticalItemSpacing = MaterialTheme.dimen.small,
        userScrollEnabled = movies.itemCount > 0
    ) {
        forYouText()

        filters()

        items(movies.itemCount) { index ->
            movies[index]?.let { movie ->
                MovieItem(
                    imageUrl = BASE_IMAGE_URL + movie.posterPath,
                    onCLick = { onDetails(movie.id) }
                )
            }
        }
    }

    if (movies.itemCount == 0 && movies.loadState.refresh == LoadState.Loading) {
        MoviesShimmerGrid()
    }
}

private fun LazyStaggeredGridScope.forYouText() {
    item(span = StaggeredGridItemSpan.FullLine) {
        Text(
            text = "For you",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = MaterialTheme.dimen.small)
        )
    }
}

val genderFilters = listOf("Action", "Thriller", "Comedy", "Drama", "Romantic", "Psycho")

@OptIn(ExperimentalMaterial3Api::class)
fun LazyStaggeredGridScope.filters() {
    item(span = StaggeredGridItemSpan.FullLine) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.medium),
            contentPadding = PaddingValues(horizontal = MaterialTheme.dimen.small)
        ) {
            items(genderFilters) { gender ->
                FilterChip(
                    selected = false,
                    onClick = { /*TODO*/ },
                    label = {
                        Text(
                            text = gender,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(MaterialTheme.dimen.small),
                        )
                    },
                    shape = MaterialTheme.shapes.small,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieItem(
    imageUrl: String,
    modifier: Modifier = Modifier,
    onCLick: () -> Unit,
) {
    Card(
        onCLick,
        modifier.animateContentSize()
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = "movie image",
            error = painterResource(id = R.drawable.pop_corn_and_cinema_poster),
            placeholder = painterResource(id = R.drawable.pop_corn_and_cinema_poster)
        )
    }
}

@Composable
fun MoviesShimmerGrid(modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        modifier = modifier
            .fillMaxWidth()
            .shimmer(),
        columns = GridCells.Adaptive(150.dp),
        contentPadding = PaddingValues(MaterialTheme.dimen.verySmall),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.small),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.small),
        userScrollEnabled = false
    ) {
        items(8) {
            MovieShimmerItem()
        }
    }
}

@Composable
fun MovieShimmerItem(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .aspectRatio(.7f)
            .background(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.shapes.medium)
    )
}