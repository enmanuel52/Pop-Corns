package com.enmanuelbergling.ktormovies.ui

import android.util.Log
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.enmanuelbergling.core.common.util.TAG
import com.enmanuelbergling.core.model.user.UserDetails
import com.enmanuelbergling.core.ui.components.UserImage
import com.enmanuelbergling.core.ui.core.LocalSharedTransitionScope
import com.enmanuelbergling.core.ui.core.dimen
import com.enmanuelbergling.feature.movies.navigation.navigateToMovieFilter
import com.enmanuelbergling.feature.movies.navigation.navigateToMovieSearch
import com.enmanuelbergling.ktormovies.R
import com.enmanuelbergling.ktormovies.navigation.CtiNavHost
import com.enmanuelbergling.ktormovies.navigation.DrawerDestination
import com.enmanuelbergling.ktormovies.navigation.TopDestination
import com.enmanuelbergling.ktormovies.navigation.loginRequired
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun CornsTimeApp(
    state: CornTimeAppState = rememberCornTimeAppState(),
    userDetails: UserDetails?,
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
                    isSelected = { it.any { route -> state.matchRoute(route = route) } },
                    userDetails = userDetails,
                    onSettings = {
                        scope.launch {
                            drawerState.close()
                            state.navigateToDrawerDestination(DrawerDestination.Settings)
                        }
                    }
                )
            }
        },
        gesturesEnabled = state.isTopDestination,
        drawerState = drawerState,
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
    isSelected: @Composable (List<Any>) -> Boolean,
    userDetails: UserDetails?,
    onSettings: () -> Unit,
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
        CompositionLocalProvider(value = LocalContentColor provides MaterialTheme.colorScheme.onPrimaryContainer) {

            UserDetailsUi(
                userDetails = userDetails,
                onSettings = onSettings,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(
                        start = MaterialTheme.dimen.small,
                        top = MaterialTheme.dimen.medium,
                    )
                    .padding(MaterialTheme.dimen.small)
            )
        }

        DrawerDestination.entries
            .filterNot { it.loginRequired && userDetails == null }
            .forEach { destination ->
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
                    shape = MaterialTheme.shapes.small,
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = Color.Transparent,
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                    )
                )
            }
    }
}

@Composable
fun UserDetailsUi(
    userDetails: UserDetails?,
    onSettings: () -> Unit,
    modifier: Modifier = Modifier,
) {

    Column(
        Modifier
            .clickable { onSettings() }
            .then(modifier)) {
        UserImage(
            userDetails?.avatarPath,
        )

        Spacer(modifier = Modifier.height(MaterialTheme.dimen.small))

        Text(text = userDetails?.username.orEmpty().ifBlank { stringResource(R.string.nosey) })
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