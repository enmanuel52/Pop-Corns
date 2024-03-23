package com.enmanuelbergling.ktormovies.ui

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.enmanuelbergling.core.ui.R
import com.mxalbert.sharedelements.MaterialContainerTransformSpec
import com.mxalbert.sharedelements.SharedElement
import com.mxalbert.sharedelements.SharedMaterialContainer
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.RouteBuilder
import moe.tlaster.precompose.navigation.path

@Composable
fun ListItems(onItemDetails: (Int) -> Unit) {
    LazyColumn {
        items(8) {
            ListItem(index = it) { onItemDetails(it) }
        }
    }
}

@Composable
fun ListItem(index: Int, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SharedMaterialContainer(
            key = index,
            screenKey = ITEMS_SCREEN,
            color = Color.Transparent,
        ) {
            Image(
                painter = painterResource(id = R.drawable.mr_bean),
                contentDescription = "item image",
                Modifier
                    .size(58.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        SharedElement(key = "Item $index", screenKey = ITEMS_SCREEN) {
            Text(text = "Item $index", Modifier.padding(16.dp))
        }
    }
}

@Composable
fun ItemDetail(index: Int) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        SharedMaterialContainer(
            key = index,
            screenKey = ITEM_DETAILS_SCREEN,
            color = Color.Transparent,
        ) {
            Image(
                painter = painterResource(id = R.drawable.mr_bean),
                contentDescription = "item image",
                Modifier
                    .width(200.dp)
                    .aspectRatio(1f)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        SharedElement(key = "Item $index", screenKey = ITEM_DETAILS_SCREEN) {
            Text(
                text = "Item $index",
                style = MaterialTheme.typography.titleLarge,
            )
        }
    }
}

const val ITEMS_SCREEN = "items_screen"
const val ITEM_DETAILS_SCREEN = "items_details_screen"
const val ITEM_GRAPH = "item_graph"

const val INDEX_ARG = "index_arg"

fun Navigator.toItemDetails(index: Int) {
    navigate("/$ITEM_DETAILS_SCREEN/$index")
}

fun RouteBuilder.sharedItemsGraph(onDetails: (Int) -> Unit) {
    group(ITEM_GRAPH, ITEMS_SCREEN) {
        scene(ITEMS_SCREEN) {
            ListItems(onDetails)
        }

        scene("/$ITEM_DETAILS_SCREEN/{$INDEX_ARG}") {
            val index = it.path(INDEX_ARG, 0)!!
            ItemDetail(index = index)
        }
    }
}