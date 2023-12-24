package com.enmanuelbergling.ktormovies.ui.screen.actor.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIos
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.enmanuelbergling.ktormovies.R
import com.enmanuelbergling.ktormovies.domain.BASE_IMAGE_URL
import com.enmanuelbergling.ktormovies.domain.model.actor.Actor
import com.enmanuelbergling.ktormovies.ui.core.dimen
import com.enmanuelbergling.ktormovies.ui.screen.actor.home.model.TopBarSearch
import com.enmanuelbergling.ktormovies.ui.screen.movie.home.MoviesShimmerGrid
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActorsScreen(onDetails: (id: Int) -> Unit) {
    val viewModel = koinViewModel<ActorsVM>()

    val actors = viewModel.actors.collectAsLazyPagingItems()

    val searchState by viewModel.searchState.collectAsStateWithLifecycle()

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = {
                AnimatedContent(
                    targetState = searchState.active,
                    label = "top bar animation",
                    transitionSpec = {
                        slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Start,
                            spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMedium)
                        ) togetherWith
                                slideOutOfContainer(
                                    AnimatedContentTransitionScope.SlideDirection.Start,
                                    spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMedium)
                                )
                    }) { active ->
                    if (active) {
                        TextField(value = searchState.text, onValueChange = { newText ->
                            viewModel.onSearch(
                                searchState.copy(text = newText)
                            )
                        })
                    } else {
                        Text(text = "Actores")
                    }
                }
            }, scrollBehavior = scrollBehavior, actions = {
                if (!searchState.active) {
                    IconButton(onClick = { viewModel.onSearch(searchState.copy(active = true)) }) {
                        Icon(imageVector = Icons.Rounded.Search, contentDescription = "search icon")
                    }
                }
            }, navigationIcon = {
                if (searchState.active) {
                    IconButton(onClick = { viewModel.onSearch(TopBarSearch()) }) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBackIos,
                            contentDescription = "back icon"
                        )
                    }
                }
            })
        },

        ) { paddingValues ->
        ActorsGrid(
            actors = actors,
            onDetails = onDetails,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
fun ActorsGrid(
    actors: LazyPagingItems<Actor>,
    modifier: Modifier = Modifier,
    onDetails: (id: Int) -> Unit,
) {
    LazyVerticalStaggeredGrid(
        modifier = modifier.fillMaxWidth(),
        columns = StaggeredGridCells.Adaptive(110.dp),
        contentPadding = PaddingValues(MaterialTheme.dimen.verySmall),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.small),
        verticalItemSpacing = MaterialTheme.dimen.small,
        userScrollEnabled = actors.itemCount > 0
    ) {
        items(actors.itemCount) { index ->
            actors[index]?.let { actor ->
                ActorItem(
                    imageUrl = BASE_IMAGE_URL + actor.profilePath,
                    name = actor.originalName,
                    onCLick = { onDetails(actor.id) }
                )
            }
        }
    }

    if (actors.itemCount == 0 && actors.loadState.refresh == LoadState.Loading) {
        MoviesShimmerGrid(modifier, GridCells.Adaptive(110.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActorItem(
    imageUrl: String,
    name: String,
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
            error = painterResource(id = R.drawable.mr_bean),
            placeholder = painterResource(id = R.drawable.mr_bean)
        )
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