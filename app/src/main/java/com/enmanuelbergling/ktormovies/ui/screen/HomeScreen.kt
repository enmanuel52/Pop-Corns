package com.enmanuelbergling.ktormovies.ui.screen

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.enmanuelbergling.ktormovies.R
import com.enmanuelbergling.ktormovies.domain.model.Movie
import com.enmanuelbergling.ktormovies.ui.core.dimen
import com.enmanuelbergling.ktormovies.ui.screen.detail.BASE_IMAGE_URL
import com.enmanuelbergling.ktormovies.ui.screen.home.HomeVM
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(onDetails: (id: Int) -> Unit) {

    val viewModel = koinViewModel<HomeVM>()
    val topRatedMovies = viewModel.topRatedMovies.collectAsLazyPagingItems()

    Column {
        MoviesList(movies = topRatedMovies, onDetails = onDetails)

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
fun MoviesList(
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
        MoviesShimmerList()
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
            error = painterResource(id = R.drawable.pop_corn_and_cinema),
            placeholder = painterResource(id = R.drawable.pop_corn_and_cinema)
        )
    }
}

@Composable
fun MoviesShimmerList(modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        modifier = modifier.fillMaxWidth(),
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
            .clip(MaterialTheme.shapes.medium)
    )
}