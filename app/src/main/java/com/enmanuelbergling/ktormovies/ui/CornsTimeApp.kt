package com.enmanuelbergling.ktormovies.ui

import android.content.res.Configuration
import android.util.Log
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ExitToApp
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.enmanuelbergling.core.common.util.TAG
import com.enmanuelbergling.core.model.user.UserDetails
import com.enmanuelbergling.core.ui.core.LocalSharedTransitionScope
import com.enmanuelbergling.core.ui.core.dimen
import com.enmanuelbergling.core.ui.theme.CornTimeTheme
import com.enmanuelbergling.feature.movies.navigation.MoviesDestination
import com.enmanuelbergling.ktormovies.R
import com.enmanuelbergling.ktormovies.navigation.CtiNavHost
import com.enmanuelbergling.ktormovies.navigation.TopDestination
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

enum class NewDrawerState {
    Open, Closed
}

val NewDrawerWidth = 200.dp

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalFoundationApi::class)
@Composable
fun CornsTimeApp(
    state: CornTimeAppState = rememberCornTimeAppState(),
    userDetails: UserDetails?,
) {
    val scope = rememberCoroutineScope()

    val snackBarHostState = remember { SnackbarHostState() }

    val density = LocalDensity.current

    val drawerWidthPx = with(density) { NewDrawerWidth.toPx() }

    val decayAnimationSpec = rememberSplineBasedDecay<Float>()

    val draggableAnchors = DraggableAnchors {
        NewDrawerState.Open at drawerWidthPx
        NewDrawerState.Closed at 0f
    }

    val draggableState = remember {
        AnchoredDraggableState(
            initialValue = NewDrawerState.Closed,
            positionalThreshold = { distance: Float -> distance * 0.6f },
            velocityThreshold = { with(density) { 80.dp.toPx() } },
            decayAnimationSpec = decayAnimationSpec,
            snapAnimationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow),
            anchors = draggableAnchors
        )
    }

    val containerColor by animateColorAsState(
        targetValue = lerp(
            start = MaterialTheme.colorScheme.surface,
            stop = MaterialTheme.colorScheme.tertiaryContainer,
            fraction = draggableState.requireOffset() / drawerWidthPx
        ),
        label = "container color animation",
    )

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .background(containerColor)
                .padding(paddingValues)
        ) {
            if (state.isTopDestination) {
                DrawerContent(
                    onDrawerDestination = { drawerDestination ->
                        scope.launch {
                            draggableState.animateTo(NewDrawerState.Closed)
                        }
                        state.navigateToDrawerDestination(drawerDestination)
                    },
                    isSelected = { route -> state.matchRoute(route = route) },
                    userDetails = userDetails,
                    onCloseDrawer = {
                        scope.launch {
                            draggableState.animateTo(NewDrawerState.Closed)
                        }
                    },
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(NewDrawerWidth)
                        .padding(MaterialTheme.dimen.medium),
                    containerColor = containerColor
                )
            }

            SharedTransitionLayout {
                CompositionLocalProvider(value = LocalSharedTransitionScope provides this) {
                    CtiNavHost(
                        state = state,
                        onOpenDrawer = {
                            scope.launch {
                                draggableState.animateTo(NewDrawerState.Open)
                            }
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer {
                                translationX = draggableState.requireOffset()

                                val fraction = draggableState.requireOffset() / drawerWidthPx
                                val scale = lerp(1f, .8f, fraction)

                                scaleX = scale
                                scaleY = scale

                                val percent = fraction
                                    .times(5)
                                    .roundToInt()
                                    .coerceAtLeast(0)

                                shape = RoundedCornerShape(percent)
                                clip = true
                            }
                            .anchoredDraggable(draggableState, Orientation.Horizontal)
                    )
                }
            }
        }
    }

    val context = LocalContext.current

    LaunchedEffect(key1 = state.isOnline) {
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
    }
}


@Composable
fun DrawerContent(
    onDrawerDestination: (TopDestination) -> Unit,
    onCloseDrawer: () -> Unit,
    isSelected: @Composable (route: Any) -> Boolean,
    userDetails: UserDetails?,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
) {
    Column(
        modifier = Modifier
            .background(containerColor) then modifier,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        CompositionLocalProvider(value = LocalContentColor provides contentColorFor(containerColor)) {
            IconButton(onClick = onCloseDrawer) {
                Icon(imageVector = Icons.Rounded.Clear, contentDescription = "close drawer icon")
            }

            Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.medium)) {
                Text(
                    text = stringResource(R.string.menu), style = MaterialTheme.typography.displayMedium,
                    color = LocalContentColor.current.copy(alpha = .6f)
                )

                Spacer(modifier = Modifier.height(MaterialTheme.dimen.mediumSmall))

                TopDestination.entries
                    .filterNot { it.loginRequired && userDetails == null }
                    .forEach { destination ->
                        NavDrawerItem(
                            label = stringResource(destination.label),
                            selected = isSelected(destination.route),
                            onClick = { onDrawerDestination(destination) },
                            imageVector = destination.icon,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
            }

            NavDrawerItem(
                label = stringResource(id = R.string.logout),
                selected = false,
                imageVector = Icons.AutoMirrored.Rounded.ExitToApp,
                modifier = Modifier.scale(.85f)
            ) {
                TODO("log out from here")
            }
        }
    }
}

@Composable
fun NavDrawerItem(
    label: String,
    selected: Boolean,
    imageVector: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val colorAnimation by animateColorAsState(
        targetValue = if (selected) LocalContentColor.current
        else LocalContentColor.current.copy(alpha = .5f),
        label = "color animation",
    )

    Row(
        modifier = modifier.clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = imageVector.toString(),
            tint = colorAnimation,
        )

        Spacer(modifier = Modifier.width(MaterialTheme.dimen.mediumSmall))

        Text(
            text = label,
            fontWeight = FontWeight.SemiBold,
            color = colorAnimation,
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DrawerContentPrev() {
    CornTimeTheme {
        DrawerContent(
            onDrawerDestination = {},
            isSelected = { it == MoviesDestination },
            userDetails = null,
            modifier = Modifier
                .height(700.dp)
                .width(NewDrawerWidth)
                .padding(12.dp),
            onCloseDrawer = {},
        )
    }
}