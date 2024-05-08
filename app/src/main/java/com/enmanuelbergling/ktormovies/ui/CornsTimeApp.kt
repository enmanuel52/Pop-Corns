package com.enmanuelbergling.ktormovies.ui

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.enmanuelbergling.core.common.util.TAG
import com.enmanuelbergling.core.model.settings.DarkTheme
import com.enmanuelbergling.core.model.user.UserDetails
import com.enmanuelbergling.core.ui.components.UserImage
import com.enmanuelbergling.core.ui.components.icon
import com.enmanuelbergling.core.ui.core.LocalSharedTransitionScope
import com.enmanuelbergling.core.ui.core.dimen
import com.enmanuelbergling.feature.movies.navigation.navigateToMovieFilter
import com.enmanuelbergling.feature.movies.navigation.navigateToMovieSearch
import com.enmanuelbergling.ktormovies.R
import com.enmanuelbergling.ktormovies.navigation.DrawerDestination
import com.enmanuelbergling.ktormovies.navigation.CtiNavHost
import com.enmanuelbergling.ktormovies.navigation.TopDestination
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun CornsTimeApp(
    state: CornTimeAppState = rememberCornTimeAppState(),
    userDetails: UserDetails,
    onLogout: () -> Unit,
    onDarkTheme: (DarkTheme) -> Unit,
) {
    val scope = rememberCoroutineScope()

    val snackBarHostState = remember { SnackbarHostState() }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    ModalNavigationDrawer(
        drawerContent = {
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
                    isSelected = { it.any { route -> state.matchRoute(route = route) } },
                    userDetails = userDetails,
                    onLogout = onLogout,
                    onLogin = {
                        scope.launch {
                            drawerState.close()
                            state.navigateToLogin()
                        }
                    }
                )
            }
        },
        gesturesEnabled = state.isTopDestination,
        drawerState = drawerState
    ) {
        Scaffold(
            bottomBar = {
                if (state.shouldShowMainBottomNav) {
                    CornBottomNav(
                        onDestination = state::navigateToTopDestination,
                        isSelected = { route -> state.matchRoute(route) }
                    )
                }
            },
            snackbarHost = { SnackbarHost(snackBarHostState) },
            topBar = {
                if (state.isTopDestination) {
                    AppTopBar(
                        scrollBehavior = scrollBehavior,
                        onOpenDrawer = {
                            scope.launch {
                                drawerState.open()
                            }
                        },
                        onSearch = state.navController::navigateToMovieSearch,
                        onFilter = state.navController::navigateToMovieFilter,
                    )
                }
            }
        ) { paddingValues ->
            Box(
                Modifier
                    .padding(paddingValues)
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
            ) {
                SharedTransitionLayout {
                    CompositionLocalProvider(value = LocalSharedTransitionScope provides this) {
                        CtiNavHost(state)
                    }
                }
            }
        }
    }

    val context = LocalContext.current

    LaunchedEffect(key1 = state.isOnline, block = {
        Log.d(TAG, "isOnline: ${state.isOnline}")
        if (!state.isOnline) {
            snackBarHostState.showSnackbar(
                context.getString(R.string.offline_message),
                duration = SnackbarDuration.Indefinite,
                withDismissAction = true
            )
        } else {
            snackBarHostState.currentSnackbarData?.dismiss()
        }
    })
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun AppTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    onOpenDrawer: () -> Unit,
    onSearch: () -> Unit,
    onFilter: () -> Unit,
) {
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.app_name)) },
        navigationIcon = {

            IconButton(
                onClick = onOpenDrawer
            ) {
                Icon(
                    imageVector = Icons.Rounded.Menu,
                    contentDescription = stringResource(R.string.drawer_icon)
                )
            }
        },
        actions = {
            IconButton(onClick = onFilter) {
                Icon(
                    imageVector = Icons.Rounded.FilterList,
                    contentDescription = stringResource(R.string.filter_icon)
                )
            }

            IconButton(onClick = onSearch) {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = stringResource(R.string.search_icon)
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}

@Composable
fun DrawerContent(
    onDrawerDestination: (DrawerDestination) -> Unit,
    darkTheme: DarkTheme,
    onDarkTheme: (DarkTheme) -> Unit,
    isSelected: @Composable (List<Any>) -> Boolean,
    userDetails: UserDetails,
    onLogout: () -> Unit,
    onLogin: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(.7f)
            .background(
                MaterialTheme.colorScheme.surface,
                RoundedCornerShape(
                    topEnd = MaterialTheme.dimen.medium,
                    bottomEnd = MaterialTheme.dimen.medium
                )
            )
    ) {
        Spacer(modifier = Modifier.height(MaterialTheme.dimen.small))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.dimen.verySmall)
        ) {
            UserDetailsUi(
                userDetails = userDetails,
                onLogout = onLogout,
                onLogin = onLogin,
                modifier = Modifier.padding(MaterialTheme.dimen.small)
            )

            DarkThemeDropDown(
                darkTheme, onDarkTheme,
            )
        }
        DrawerDestination.entries.forEach { destination ->
            NavigationDrawerItem(
                label = { Text(text = stringResource(destination.label)) },
                selected = isSelected(destination.routes),
                onClick = { onDrawerDestination(destination) },
                icon = {
                    Icon(
                        imageVector = if (isSelected(destination.routes)) destination.icon else destination.unselectedIcon,
                        contentDescription = "nav icon"
                    )
                },
                shape = MaterialTheme.shapes.small
            )
        }
    }
}

@Composable
fun UserDetailsUi(
    userDetails: UserDetails,
    onLogout: () -> Unit,
    onLogin: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isCloseSessionDropDownOpen by remember {
        mutableStateOf(false)
    }

    Column(modifier) {
        UserImage(
            userDetails.avatarPath,
        )

        Spacer(modifier = Modifier.height(MaterialTheme.dimen.small))

        Row(
            Modifier
                .clip(MaterialTheme.shapes.small)
                .clickable {
                    isCloseSessionDropDownOpen = true
                }
                .padding(MaterialTheme.dimen.small)
        ) {
            Text(text = userDetails.username.ifBlank { stringResource(R.string.nosey) })

            Spacer(modifier = Modifier.width(MaterialTheme.dimen.verySmall))

            Icon(
                imageVector = Icons.Rounded.ArrowDropDown,
                contentDescription = stringResource(R.string.drop_down_icon)
            )

            val isLoggedIn by remember(userDetails) {
                derivedStateOf {
                    userDetails.username.isNotBlank()
                }
            }

            DropdownMenu(
                expanded = isCloseSessionDropDownOpen,
                onDismissRequest = { isCloseSessionDropDownOpen = false }
            ) {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = if (isLoggedIn) stringResource(R.string.logout) else stringResource(
                                R.string.login
                            )
                        )
                    },
                    onClick = {
                        isCloseSessionDropDownOpen = false
                        if (isLoggedIn) onLogout() else onLogin()
                    }
                )
            }
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
    AnimatedContent(
        darkTheme,
        label = stringResource(R.string.dark_theme_animation),
        transitionSpec = {
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
    isSelected: @Composable (Any) -> Boolean,
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
                        contentDescription = stringResource(R.string.nav_bar_icon)
                    )
                },
                label = { Text(text = stringResource(cinemaContent.label)) }
            )
        }

    }
}