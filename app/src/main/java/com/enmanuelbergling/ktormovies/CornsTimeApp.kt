package com.enmanuelbergling.ktormovies

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.ViewStream
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
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
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            Modifier.padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.superSmall)
        ) {
            if (state.shouldShowMainTopAppBar) {
                DestinationTabs(selectedTabIndex) { index ->
                    selectedTabIndex = index
                    state.navigateToTopDestination(TopDestination.values()[index])
                }
            }

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
private fun DestinationTabs(selectedTabIndex: Int, onTab: (index: Int) -> Unit) {
    ScrollableTabRow(
        selectedTabIndex = selectedTabIndex,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = MaterialTheme.dimen.small),
        divider = {},
    ) {
        TopDestination.values().forEach { cinemaContent ->
            Tab(
                selected = cinemaContent.ordinal == selectedTabIndex,
                onClick = { onTab(cinemaContent.ordinal) },
            ) {
                Icon(
                    imageVector = cinemaContent.icon,
                    contentDescription = "tab icon",
                )
                Text(
                    text = cinemaContent.toString(),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
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