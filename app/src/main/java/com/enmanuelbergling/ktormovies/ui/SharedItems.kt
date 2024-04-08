@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.enmanuelbergling.ktormovies.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.BoundsTransform
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.enmanuelbergling.core.ui.R
import moe.tlaster.precompose.navigation.BackHandler
import moe.tlaster.precompose.navigation.RouteBuilder

private val boundsTransition = BoundsTransform { _, _ -> spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow) }


@Composable
fun SharedTransitionScope.ListItems(visible: Boolean, onItemDetails: (Int) -> Unit) {
    LazyColumn {
        items(8) {
            ListItem(visible = visible, index = it) { onItemDetails(it) }
        }
    }
}

@Composable
fun SharedTransitionScope.ListItem(visible: Boolean, index: Int, onClick: () -> Unit) {
    AnimatedVisibility(visible = visible, enter = fadeIn()) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.mr_bean),
                contentDescription = "item image",
                Modifier
                    .sharedElement(
                        state = rememberSharedContentState(key = index),
                        animatedVisibilityScope = this@AnimatedVisibility,
                        boundsTransform = boundsTransition
                    )
                    .size(58.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Text(text = "Item $index", Modifier.padding(16.dp))
        }
    }
}

@Composable
fun SharedTransitionScope.ItemDetail(visible: Boolean, index: Int) {
    AnimatedVisibility(visible = visible, enter = fadeIn()) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(id = R.drawable.mr_bean),
                contentDescription = "item image",
                Modifier
                    .sharedElement(
                        state = rememberSharedContentState(key = index),
                        animatedVisibilityScope = this@AnimatedVisibility,
                        boundsTransform = boundsTransition
                    )
                    .width(200.dp)
                    .aspectRatio(1f)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Text(
                text = "Item $index",
                style = MaterialTheme.typography.titleLarge,
            )

        }
    }
}

const val ITEMS_AND_DETAIL_SCREEN = "items_screen"
const val ITEM_GRAPH = "item_graph"

fun RouteBuilder.sharedItemsGraph() {
    group(ITEM_GRAPH, ITEMS_AND_DETAIL_SCREEN) {
        scene(ITEMS_AND_DETAIL_SCREEN) {
            val selectedItem = rememberSaveable {
                mutableIntStateOf(-1)
            }

            SharedTransitionLayout {

                ListItems(visible = selectedItem.intValue == -1) { index ->
                    selectedItem.intValue = index
                }

                ItemDetail(visible = selectedItem.intValue != -1, index = selectedItem.intValue)
            }

            BackHandler(enabled = selectedItem.intValue != -1) {
                selectedItem.intValue = -1
            }

        }
    }
}