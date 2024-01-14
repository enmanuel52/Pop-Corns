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
import com.enmanuelbergling.ktormovies.navigation.DrawerDestination
import com.enmanuelbergling.ktormovies.navigation.PreCtiNavHost
import com.enmanuelbergling.ktormovies.navigation.TopDestination
import com.enmanuelbergling.ktormovies.ui.components.icon
import com.enmanuelbergling.ktormovies.ui.core.dimen
import com.enmanuelbergling.ktormovies.util.TAG
import kotlinx.coroutines.launch

@Composable
fun CornsTimeApp(
    state: PreComposeAppState = rememberPreCtiAppState(),
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
                darkTheme = state.darkTheme,
                onDarkTheme = onDarkTheme,
                isSelected = { it.any { route -> state.matchRoute(route = route) } }
            )
        }
    }, gesturesEnabled = state.isTopDestination, drawerState = drawerState) {
        Scaffold(
            bottomBar = {
                if (state.shouldShowMainBottomNav) {
                    CornBottomNav(
                        onDestination = state::navigateToTopDestination,
                        isSelected = { route -> state.matchRoute(route) }
                    )
                }
            },
            snackbarHost = { SnackbarHost(snackBarHostState) }
        ) { paddingValues ->
            Box(
                Modifier.padding(paddingValues)
            ) {
                PreCtiNavHost(state)
                if (state.isTopDestination) {
                    FilledTonalIconButton(
                        onClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.CenterStart)
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
    darkTheme: DarkTheme,
    onDarkTheme: (DarkTheme) -> Unit,
    isSelected: @Composable (List<String>) -> Boolean,
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
                selected = isSelected(destination.routes),
                onClick = { onDrawerDestination(destination) },
                icon = {
                    Icon(
                        imageVector = if (isSelected(destination.routes)) destination.icon else destination.unselectedIcon,
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
fun CornBottomNav(
    onDestination: (TopDestination) -> Unit,
    isSelected: @Composable (String) -> Boolean,
) {
    NavigationBar {
        TopDestination.entries.forEach { cinemaContent ->

            NavigationBarItem(
                selected = isSelected(cinemaContent.route),
                onClick = { onDestination(cinemaContent) },
                icon = {
                    Icon(
                        imageVector = if (isSelected(cinemaContent.route)) cinemaContent.icon
                        else cinemaContent.unselectedIcon,
                        contentDescription = "nav bar icon"
                    )
                },
                label = { Text(text = cinemaContent.name) }
            )
        }

    }
}