package com.enmanuelbergling.ktormovies.ui.screen.actor.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIos
import androidx.compose.material.icons.rounded.Search
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.enmanuelbergling.ktormovies.domain.model.actor.Actor
import com.enmanuelbergling.ktormovies.ui.components.listItemWindAnimation
import com.enmanuelbergling.ktormovies.ui.core.dimen
import com.enmanuelbergling.ktormovies.ui.core.isScrollingUp
import com.enmanuelbergling.ktormovies.ui.core.shimmerIf
import com.enmanuelbergling.ktormovies.ui.screen.actor.home.model.TopBarSearch
import com.enmanuelbergling.ktormovies.ui.screen.movie.components.ActorCard
import com.enmanuelbergling.ktormovies.ui.screen.movie.components.ActorPlaceHolder
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActorsScreen(onDetails: (id: Int) -> Unit) {
    val viewModel = koinViewModel<ActorsVM>()

    val actors = viewModel.actors.collectAsLazyPagingItems()

    val searchState by viewModel.searchState.collectAsStateWithLifecycle()

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

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
                        TextField(
                            value = searchState.text,
                            onValueChange = { newText ->
                                viewModel.onSearch(
                                    searchState.copy(text = newText)
                                )
                            }, leadingIcon = {
                                IconButton(onClick = { viewModel.onSearch(TopBarSearch()) }) {
                                    Icon(
                                        imageVector = Icons.Rounded.ArrowBackIos,
                                        contentDescription = "back icon"
                                    )
                                }
                            }
                        )
                    } else {
                        Text(text = "Actors")
                    }
                }
            }, scrollBehavior = scrollBehavior, actions = {
                if (!searchState.active) {
                    IconButton(onClick = { viewModel.onSearch(searchState.copy(active = true)) }) {
                        Icon(imageVector = Icons.Rounded.Search, contentDescription = "search icon")
                    }
                }
            })
        },

        ) { paddingValues ->
        ActorsGrid(
            actors = actors,
            onDetails = onDetails,
            modifier = Modifier
                .padding(paddingValues)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        )
    }
}

@Composable
fun ActorsGrid(
    actors: LazyPagingItems<Actor>,
    modifier: Modifier = Modifier,
    onDetails: (id: Int) -> Unit,
) {
    val listState = rememberLazyStaggeredGridState()

    LazyVerticalStaggeredGrid(
        modifier = modifier
            .fillMaxWidth()
            .shimmerIf { actors.itemCount <= 0 }
            .listItemWindAnimation(isScrollingUp = listState.isScrollingUp()),
        state = listState,
        columns = StaggeredGridCells.Adaptive(110.dp),
        contentPadding = PaddingValues(MaterialTheme.dimen.verySmall),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.small),
        verticalItemSpacing = MaterialTheme.dimen.small,
        userScrollEnabled = actors.itemCount > 0
    ) {
        items(actors.itemCount) { index ->
            actors[index]?.let { actor ->
                ActorCard(
                    imageUrl = actor.profilePath,
                    name = actor.originalName,
                    onCLick = { onDetails(actor.id) }
                )
            }
        }
        if (actors.itemCount == 0 && actors.loadState.refresh == LoadState.Loading) {
            items(50) { ActorPlaceHolder() }
        }
    }

}