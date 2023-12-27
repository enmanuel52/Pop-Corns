package com.enmanuelbergling.ktormovies

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.enmanuelbergling.ktormovies.navigation.CtiNavHost
import com.enmanuelbergling.ktormovies.navigation.DrawerDestination
import com.enmanuelbergling.ktormovies.navigation.TopDestination
import com.enmanuelbergling.ktormovies.ui.core.dimen
import com.enmanuelbergling.ktormovies.util.TAG
import kotlinx.coroutines.launch

@Composable
fun CornsTimeApp(
    state: CornsTimeAppState = rememberCtiAppState(),
) {
    val scope = rememberCoroutineScope()

    val snackbarHostState = remember { SnackbarHostState() }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    ModalNavigationDrawer(drawerContent = {
        if (state.isTopDestination) {
            DrawerContent(state::navigateToDrawerDestination, state.currentRoute)
        }
    }, gesturesEnabled = state.isTopDestination, drawerState = drawerState) {
        Scaffold(
            bottomBar = {
                if (state.shouldShowMainBottomNav) {
                    CornBottomNav(
                        currentRoute = state.currentRoute,
                        onDestination = {
                            state.navigateToTopDestination(it)
                            scope.launch {
                                drawerState.close()
                            }
                        }
                    )
                }
            },
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { paddingValues ->
            Box(
                Modifier.padding(paddingValues)
            ) {
                CtiNavHost(state)
                if (state.isTopDestination) {
                    FilledTonalIconButton(
                        onClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(MaterialTheme.dimen.verySmall)
                    ) {
                        Icon(imageVector = Icons.Rounded.Menu, contentDescription = "drawer icon")
                    }
                }
            }
        }
    }

    LaunchedEffect(key1 = state.isOnline, block = {
        Log.d(TAG, "isOnline: ${state.isOnline}")
        if (!state.isOnline) {
            snackbarHostState.showSnackbar(
                "You're offline",
                duration = SnackbarDuration.Indefinite,
                withDismissAction = true
            )
        } else {
            snackbarHostState.currentSnackbarData?.dismiss()
        }
    })
}

@Composable
fun DrawerContent(onDrawerDestination: (DrawerDestination) -> Unit, currentRoute: String?) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(.4f)
            .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.large)
    ) {
        DrawerDestination.values().forEach { destination ->
            NavigationDrawerItem(
                label = { Text(text = destination.toString()) },
                selected = currentRoute in destination.routes,
                onClick = { onDrawerDestination(destination) },
                icon = {
                    Icon(
                        imageVector = if (currentRoute in destination.routes) destination.icon else destination.unselectedIcon,
                        contentDescription = "nav icon"
                    )
                })
        }
    }
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