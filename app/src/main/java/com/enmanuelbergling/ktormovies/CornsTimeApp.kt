package com.enmanuelbergling.ktormovies

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.ViewStream
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.enmanuelbergling.ktormovies.navigation.CtiNavHost
import com.enmanuelbergling.ktormovies.navigation.TopDestination
import com.enmanuelbergling.ktormovies.ui.core.LocalTopAppScrollBehaviour
import com.enmanuelbergling.ktormovies.ui.core.dimen
import com.enmanuelbergling.ktormovies.util.TAG

@Composable
fun CornsTimeApp(
    state: CornsTimeAppState = rememberCtiAppState()
) {
    var selectedTabIndex by remember {
        mutableIntStateOf(0)
    }

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            if (state.shouldShowMainTopAppBar) {
                MainTopAppBar()
            }
        },
        bottomBar = {
            if (state.shouldShowMainTopAppBar) {
                CornBottomNav(
                    currentRoute = state.currentRoute,
                    onDestination = state::navigateToTopDestination
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            Modifier.padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.superSmall)
        ) {
            CtiNavHost(state)
        }
    }

    LaunchedEffect(key1 = state.isOnline, block = {
        Log.d(TAG, "isOnline: ${state.isOnline}")
        if (!state.isOnline) {
            snackbarHostState.showSnackbar(
                "You're offline",
                actionLabel = "Dismiss",
                true,
                duration = SnackbarDuration.Indefinite
            )
        } else {
            snackbarHostState.currentSnackbarData?.dismiss()
        }
    })
}

@Composable
fun CornBottomNav(currentRoute: String?, onDestination: (TopDestination) -> Unit) {
    NavigationBar {
        TopDestination.values().forEach { cinemaContent ->
            val selected = currentRoute == cinemaContent.route

            NavigationBarItem(
                selected = selected,
                onClick = { onDestination(cinemaContent) },
                icon = {
                    Icon(
                        imageVector = if (selected) cinemaContent.icon
                        else cinemaContent.unselectedIcon,
                        contentDescription = "nav bar icon"
                    )
                },
                label = { Text(text = cinemaContent.name) }
            )
        }

    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun MainTopAppBar() {

    val scrollBehaviour = LocalTopAppScrollBehaviour.current!!

    TopAppBar(
        title = { }, actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Rounded.Search, contentDescription = "search icon")
            }
        },
        navigationIcon = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Rounded.ViewStream,
                    contentDescription = "menu icon"
                )
            }
        },
        scrollBehavior = scrollBehaviour,
        colors = topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
        )
    )
}