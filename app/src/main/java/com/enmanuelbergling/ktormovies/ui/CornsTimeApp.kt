package com.enmanuelbergling.ktormovies.ui

import android.util.Log
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.enmanuelbergling.core.common.util.TAG
import com.enmanuelbergling.core.model.user.UserDetails
import com.enmanuelbergling.core.ui.components.UserImage
import com.enmanuelbergling.core.ui.core.LocalSharedTransitionScope
import com.enmanuelbergling.core.ui.core.dimen
import com.enmanuelbergling.ktormovies.R
import com.enmanuelbergling.ktormovies.navigation.CtiNavHost
import com.enmanuelbergling.ktormovies.navigation.TopDestination
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
                    isSelected = { route -> state.matchRoute(route = route) },
                    userDetails = userDetails,
                    onSettings = {
                        scope.launch {
                            drawerState.close()
                            state.navigateToDrawerDestination(TopDestination.Settings)
                        }
                    }
                )
            }
        },
        gesturesEnabled = state.isTopDestination,
        drawerState = drawerState,
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackBarHostState) },
        ) { paddingValues ->
            SharedTransitionLayout(
                Modifier
                    .padding(paddingValues)
            ) {
                CompositionLocalProvider(value = LocalSharedTransitionScope provides this) {
                    CtiNavHost(
                        state = state,
                        onOpenDrawer = {
                            scope.launch {
                                drawerState.open()
                            }
                        }
                    )
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
fun DrawerContent(
    onDrawerDestination: (TopDestination) -> Unit,
    isSelected: @Composable (route: Any) -> Boolean,
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

        TopDestination.entries
            .filterNot { it.loginRequired && userDetails == null }
            .forEach { destination ->
                NavigationDrawerItem(
                    label = { Text(text = stringResource(destination.label)) },
                    selected = isSelected(destination.route),
                    onClick = { onDrawerDestination(destination) },
                    icon = {
                        Icon(
                            imageVector = if (isSelected(destination.route)) destination.icon
                            else destination.unselectedIcon,
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