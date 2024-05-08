package com.enmanuelbergling.feature.actor.home

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.enmanuelbergling.core.model.actor.Actor
import com.enmanuelbergling.core.ui.R
import com.enmanuelbergling.core.ui.components.common.ActorCard
import com.enmanuelbergling.core.ui.components.common.ActorPlaceHolder
import com.enmanuelbergling.core.ui.core.BoundsTransition
import com.enmanuelbergling.core.ui.core.LocalSharedTransitionScope
import com.enmanuelbergling.core.ui.core.dimen
import com.enmanuelbergling.core.ui.core.items
import com.enmanuelbergling.core.ui.core.shimmerIf
import com.enmanuelbergling.core.ui.navigation.ActorDetailNavAction
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimatedVisibilityScope.ActorsScreen(
    onDetails: (ActorDetailNavAction) -> Unit,
    onBack: () -> Unit,
) {
    val viewModel = koinViewModel<ActorsVM>()

    val actors = viewModel.actors.collectAsLazyPagingItems()

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = {
                Text(text = stringResource(R.string.actors))
            }, navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBackIos,
                        contentDescription = stringResource(id = R.string.back_icon)
                    )
                }
            }, scrollBehavior = scrollBehavior)
        },

        ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            ActorsGrid(
                actors = actors,
                onDetails = onDetails,
                modifier = Modifier
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
            )
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AnimatedVisibilityScope.ActorsGrid(
    actors: LazyPagingItems<Actor>,
    modifier: Modifier = Modifier,
    onDetails: (ActorDetailNavAction) -> Unit,
) {
    val listState = rememberLazyStaggeredGridState()

    LazyVerticalStaggeredGrid(
        modifier = modifier
            .fillMaxWidth()
            .shimmerIf { actors.itemCount <= 0 },
        state = listState,
        columns = StaggeredGridCells.Adaptive(110.dp),
        contentPadding = PaddingValues(MaterialTheme.dimen.verySmall),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.small),
        verticalItemSpacing = MaterialTheme.dimen.small,
        userScrollEnabled = actors.itemCount > 0
    ) {
        items(actors) { actor ->
            actor?.let {
                with(LocalSharedTransitionScope.current!!) {

                    ActorCard(
                        imageUrl = actor.profilePath,
                        name = actor.originalName,
                        onCLick = {
                            onDetails(
                                ActorDetailNavAction(
                                    id = actor.id,
                                    imageUrl = actor.profilePath ?: "error",
                                    name = actor.originalName
                                )
                            )
                        },
                        modifier = Modifier
                            .sharedElement(
                                state = rememberSharedContentState(key = actor.profilePath.orEmpty()),
                                animatedVisibilityScope = this@ActorsGrid,
                                boundsTransform = BoundsTransition
                            )
                    )

                }
            }
        }
        if (actors.itemCount == 0 && actors.loadState.refresh == LoadState.Loading) {
            items(50) { ActorPlaceHolder() }
        }
    }

}