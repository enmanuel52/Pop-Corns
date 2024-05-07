@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.enmanuelbergling.ktormovies.ui

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.enmanuelbergling.core.ui.R
import com.enmanuelbergling.core.ui.core.BoundsTransition
import com.enmanuelbergling.core.ui.core.LocalSharedTransitionScope

@Composable
fun AnimatedContentScope.ListItems(onItemDetails: (Int) -> Unit) {
    LazyColumn {
        items(8) {
            ListItem(index = it) { onItemDetails(it) }
        }
    }
}

@Composable
fun AnimatedContentScope.ListItem(index: Int, onClick: () -> Unit) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick() }
        .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically) {
        with(LocalSharedTransitionScope.current!!) {
            Image(
                painter = painterResource(id = R.drawable.mr_bean),
                contentDescription = "item image",
                Modifier
                    .sharedElement(
                        state = rememberSharedContentState(key = index),
                        animatedVisibilityScope = this@ListItem,
                        boundsTransform = BoundsTransition
                    )
                    .size(58.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        Text(text = "Item $index", Modifier.padding(16.dp))
    }
}

@Composable
fun AnimatedContentScope.ItemDetail(index: Int) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        with(LocalSharedTransitionScope.current!!) {
            Image(
                painter = painterResource(id = R.drawable.mr_bean),
                contentDescription = "item image",
                Modifier
                    .sharedElement(
                        state = rememberSharedContentState(key = index),
                        animatedVisibilityScope = this@ItemDetail,
                        boundsTransform = BoundsTransition
                    )
                    .width(200.dp)
                    .aspectRatio(1f)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }
        Text(
            text = "Item $index",
            style = MaterialTheme.typography.titleLarge,
        )

    }
}

fun NavHostController.onSharedItemsDetails(id: Int){
    navigate("details/$id")
}

fun NavGraphBuilder.sharedItemsGraph(onDetails: (Int) -> Unit) {
    navigation("home", "graph") {
        composable("home") {
            ListItems(onDetails)
        }

        composable("details/{id}", arguments = listOf(
            navArgument("id") { type = NavType.IntType }
        )) {
            val id = it.arguments?.getInt("id")!!
            ItemDetail(
                index = id
            )
        }
    }
}