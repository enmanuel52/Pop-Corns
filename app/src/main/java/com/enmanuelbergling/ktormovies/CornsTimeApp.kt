package com.enmanuelbergling.ktormovies

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.enmanuelbergling.ktormovies.domain.model.settings.DarkTheme
import com.enmanuelbergling.ktormovies.navigation.CtiNavHost
import com.enmanuelbergling.ktormovies.navigation.DrawerDestination
import com.enmanuelbergling.ktormovies.navigation.TopDestination
import com.enmanuelbergling.ktormovies.ui.components.icon
import com.enmanuelbergling.ktormovies.ui.core.dimen
import com.enmanuelbergling.ktormovies.util.TAG
import kotlinx.coroutines.launch

@Composable
fun CornsTimeApp(
    state: CornsTimeAppState = rememberCtiAppState(),
    onDarkTheme: (DarkTheme) -> Unit,
) {
    val scope = rememberCoroutineScope()

    val snackBarHostState = remember { SnackbarHostState() }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    ModalNavigationDrawer(drawerContent = {
        if (state.isTopDestination) {
            DrawerContent(
                onDrawerDestination = { drawerDestination ->
                    scope.launch {
                        drawerState.close()
                        state.navigateToDrawerDestination(drawerDestination)
                    }
                },
                currentRoute = state.currentRoute,
                darkTheme = state.darkTheme,
                onDarkTheme = onDarkTheme
            )
        }
    }, gesturesEnabled = state.isTopDestination, drawerState = drawerState) {
        Scaffold(
            bottomBar = {
                if (state.shouldShowMainBottomNav) {
                    CornBottomNav(
                        currentRoute = state.currentRoute,
                        onDestination = state::navigateToTopDestination
                    )
                }
            },
            snackbarHost = { SnackbarHost(snackBarHostState) }
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
            snackBarHostState.showSnackbar(
                "You're offline",
                duration = SnackbarDuration.Indefinite,
                withDismissAction = true
            )
        } else {
            snackBarHostState.currentSnackbarData?.dismiss()
        }
    })
}

@Composable
fun DrawerContent(
    onDrawerDestination: (DrawerDestination) -> Unit,
    currentRoute: String?,
    darkTheme: DarkTheme,
    onDarkTheme: (DarkTheme) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(.6f)
            .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.large)
    ) {
        DarkThemeDropDown(
            darkTheme, onDarkTheme, modifier = Modifier.align(Alignment.End)
        )
        DrawerDestination.entries.forEach { destination ->
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
private fun DarkThemeDropDown(
    darkTheme: DarkTheme,
    onDarkTheme: (DarkTheme) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isMenuExpanded by remember {
        mutableStateOf(false)
    }

    Box(modifier = modifier) {
        IconButton(onClick = { isMenuExpanded = true }) {
            AnimatedDarkThemeIcon(darkTheme)

            DropdownMenu(expanded = isMenuExpanded, onDismissRequest = { isMenuExpanded = false }) {
                DarkTheme.entries.forEach {
                    DropdownMenuItem(
                        text = { Text(text = it.label) },
                        onClick = {
                            onDarkTheme(it)
                            isMenuExpanded = false
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = it.icon,
                                contentDescription = it.label
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun AnimatedDarkThemeIcon(darkTheme: DarkTheme) {
    AnimatedContent(darkTheme, label = "dark theme animation", transitionSpec = {
        slideIntoContainer(
            AnimatedContentTransitionScope.SlideDirection.Up,
            animationSpec = spring(Spring.DampingRatioHighBouncy, Spring.StiffnessLow)
        ) togetherWith (fadeOut() + slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End))
    }) { theme ->
        Icon(theme.icon, theme.label)
    }
}

@Composable
fun CornBottomNav(currentRoute: String?, onDestination: (TopDestination) -> Unit) {
    NavigationBar {
        TopDestination.entries.forEach { cinemaContent ->
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