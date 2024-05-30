package com.enmanuelbergling.feature.settings.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForwardIos
import androidx.compose.material.icons.automirrored.rounded.ExitToApp
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.enmanuelbergling.core.common.android_util.removeAllDynamicShortCuts
import com.enmanuelbergling.core.common.util.BASE_IMAGE_URL
import com.enmanuelbergling.core.ui.R
import com.enmanuelbergling.core.ui.components.ArtisticBackground
import com.enmanuelbergling.core.ui.core.dimen
import com.enmanuelbergling.core.ui.theme.CornTimeTheme
import com.enmanuelbergling.feature.settings.model.DarkThemeUi
import com.enmanuelbergling.feature.settings.model.SettingItem
import com.enmanuelbergling.feature.settings.model.SettingUiEvent
import com.enmanuelbergling.feature.settings.model.SettingUiState
import com.enmanuelbergling.feature.settings.model.UserUi
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsRoute(onBack: () -> Unit, onLogin: () -> Unit) {

    val viewModel = koinViewModel<SettingsVM>()

    val darkMode by viewModel.darkThemeState.collectAsStateWithLifecycle(initialValue = DarkThemeUi.System)
    val dynamicColor by viewModel.dynamicColorState.collectAsStateWithLifecycle(initialValue = false)
    val userState by viewModel.userState.collectAsStateWithLifecycle(initialValue = UserUi())
    val menuState by viewModel.menuVisibleState.collectAsStateWithLifecycle()

    SettingsScreen(
        onBack = onBack,
        onLogin = onLogin,
        uiState = SettingUiState(
            userState,
            darkMode,
            dynamicColor,
            menuState.darkThemeVisible,
            menuState.dynamicColorVisible
        ),
        onEvent = viewModel::onEvent,
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun SettingsScreen(
    onBack: () -> Unit,
    onLogin: () -> Unit,
    uiState: SettingUiState,
    onEvent: (SettingUiEvent) -> Unit,
) {
    var visibleState by rememberSaveable {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = Unit) {
        visibleState = true
    }

    val context = LocalContext.current

    Scaffold(topBar = {
        TopAppBar(title = { Text(text = stringResource(id = R.string.settings)) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBackIosNew,
                        contentDescription = "icon back"
                    )
                }
            },
            actions = {
                if (!uiState.userDetails.isEmpty) {
                    IconButton(onClick = {
                        onEvent(SettingUiEvent.Logout)
                        context.removeAllDynamicShortCuts()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ExitToApp,
                            contentDescription = "logout icon"
                        )
                    }

                }
            },
            colors = TopAppBarDefaults.topAppBarColors(Color.Transparent, Color.Transparent)
        )
    }) { paddingValues ->
        Box {
            ArtisticBackground(Modifier.fillMaxSize())


            Column(Modifier.padding(paddingValues)) {
                ProfileWrapper(
                    userState = uiState.userDetails,
                    visibleState = visibleState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(4f),
                    onLogin = onLogin
                )

                SettingOptions(
                    darkTheme = uiState.darkTheme,
                    dynamicColor = uiState.dynamicColor,
                    visibleState = visibleState,
                    modifier = Modifier.weight(6f),
                    onEvent = onEvent
                )
            }
        }
    }

    if (uiState.darkThemeMenuOpen) {
        DarkThemeMenu(
            darkTheme = uiState.darkTheme,
            onDismiss = { onEvent(SettingUiEvent.DarkThemeMenu) }) { theme ->
            onEvent(SettingUiEvent.DarkThemeEvent(theme))
        }
    }

    if (uiState.dynamicThemeMenuOpen) {
        DynamicColorMenu(
            active = uiState.dynamicColor,
            onDismiss = { onEvent(SettingUiEvent.DynamicColorMenu) }) { active ->
            onEvent(SettingUiEvent.DynamicColor(active))
        }
    }
}

@Composable
private fun SettingOptions(
    darkTheme: DarkThemeUi,
    dynamicColor: Boolean,
    visibleState: Boolean,
    modifier: Modifier,
    onEvent: (SettingUiEvent) -> Unit,
) {

    AnimatedVisibility(
        visible = visibleState,
        enter = slideInVertically(spring(Spring.DampingRatioLowBouncy, Spring.StiffnessLow)) { it },
        modifier = modifier,
    ) {

        LazyColumn(
            contentPadding = PaddingValues(vertical = MaterialTheme.dimen.mediumSmall),
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colorScheme.surface, shape = MaterialTheme.shapes.medium
                )

        ) {
            item {
                SettingItemUi(
                    item = SettingItem.DarkMode,
                    textValue = darkTheme.label,
                    modifier = Modifier.fillMaxWidth(),
                ) { onEvent(SettingUiEvent.DarkThemeMenu) }
            }

            item {
                SettingItemUi(
                    item = SettingItem.DynamicColor,
                    textValue = if (dynamicColor) "On" else "Off",
                    modifier = Modifier.fillMaxWidth(),
                ) { onEvent(SettingUiEvent.DynamicColorMenu) }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun DynamicColorMenu(
    active: Boolean,
    onDismiss: () -> Unit,
    onDynamicTheme: (Boolean) -> Unit,
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier.navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.verySmall)
        ) {
            SelectionText(
                text = "On",
                selected = active,
                modifier = Modifier.height(TextFieldDefaults.MinHeight)
            ) {
                onDynamicTheme(true)
                onDismiss()
            }
            SelectionText(
                text = "Off",
                selected = !active,
                modifier = Modifier.height(TextFieldDefaults.MinHeight)
            ) {
                onDynamicTheme(false)
                onDismiss()
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun DarkThemeMenu(
    darkTheme: DarkThemeUi,
    onDismiss: () -> Unit,
    onDarkTheme: (DarkThemeUi) -> Unit,
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier.navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.verySmall)
        ) {
            DarkThemeUi.entries.forEach { theme ->
                SelectionText(
                    text = theme.label,
                    selected = theme == darkTheme,
                    modifier = Modifier.height(TextFieldDefaults.MinHeight)
                ) {
                    onDarkTheme(theme)
                    onDismiss()
                }
            }
        }
    }
}

@Composable
fun SelectionText(
    text: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val dotColor by animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.primary else Color.Transparent,
        label = "dot color"
    )
    val textColor by animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.primary else LocalContentColor.current,
        label = "text color"
    )

    Row(
        modifier
            .clickable { onClick() }
            .padding(horizontal = MaterialTheme.dimen.small),
        verticalAlignment = Alignment.CenterVertically) {


        Icon(
            imageVector = Icons.Rounded.PlayArrow,
            contentDescription = "selected icon",
            modifier = Modifier.size(MaterialTheme.dimen.mediumSmall),
            tint = dotColor
        )

        Spacer(modifier = Modifier.width(MaterialTheme.dimen.small))

        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = textColor,
            modifier = Modifier.weight(1f)
        )
    }
}

@PreviewLightDark
@Composable
private fun SelectionTextPrev() {
    CornTimeTheme {
        SelectionText(
            text = "System",
            selected = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {}
    }
}

@Composable
internal fun SettingItemUi(
    item: SettingItem,
    modifier: Modifier = Modifier,
    textValue: String = "",
    onClick: () -> Unit,
) {
    val iconContainerColor = MaterialTheme.colorScheme.tertiaryContainer

    Row(
        modifier
            .clickable { onClick() }
            .padding(all = MaterialTheme.dimen.medium),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.small)) {
        Box(
            modifier = Modifier
                .background(iconContainerColor, MaterialTheme.shapes.small)
                .padding(MaterialTheme.dimen.small),
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = "setting icon",
                tint = contentColorFor(iconContainerColor)
            )
        }

        Text(
            text = stringResource(item.label),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = textValue,
            style = MaterialTheme.typography.bodyMedium,
        )

        Icon(
            imageVector = Icons.AutoMirrored.Rounded.ArrowForwardIos,
            contentDescription = "forward icon",
            Modifier.size(14.dp)
        )
    }
}

@PreviewLightDark
@Composable
private fun SettingItemUiPrev() {
    CornTimeTheme {

        SettingItemUi(
            SettingItem.DarkMode, Modifier.fillMaxWidth(), "System"
        ) {}
    }
}

@Composable
internal fun ProfileWrapper(
    userState: UserUi,
    visibleState: Boolean,
    modifier: Modifier = Modifier,
    onLogin: () -> Unit,
) {

    Box(modifier = modifier, contentAlignment = Alignment.Center) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            ProfileUi(
                username = userState.username,
                imageUrl = userState.avatarPath,
                modifier = Modifier.size(110.dp),
                visibleState = visibleState,
            )

            Spacer(modifier = Modifier.height(MaterialTheme.dimen.small))

            if (userState.isEmpty) {
                TextButton(onClick = onLogin) {
                    Text(text = stringResource(id = R.string.login))
                }
            }
        }
    }
}

@Composable
fun ProfileUi(
    username: String,
    imageUrl: String?,
    modifier: Modifier = Modifier,
    visibleState: Boolean = true,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.small)
    ) {
        AnimatedVisibility(
            visible = visibleState, enter = expandIn(
                spring(Spring.DampingRatioLowBouncy, Spring.StiffnessLow),
                expandFrom = Alignment.Center
            ), modifier = Modifier.clip(CircleShape)
        ) {

            ProfileImage(imageUrl, modifier)
        }

        if (username.isNotBlank()) {
            Text(
                text = username,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun ProfileUiPrev() {
    CornTimeTheme {

        ProfileUi(username = "Tim", imageUrl = "", modifier = Modifier.size(80.dp))
    }
}

@Composable
fun ProfileImage(avatarPath: String?, modifier: Modifier = Modifier) {
    val isPreview = LocalInspectionMode.current

    val imageModifier = Modifier
        .border(
            MaterialTheme.dimen.verySmall, MaterialTheme.colorScheme.primary, CircleShape
        )
        .padding(MaterialTheme.dimen.small)
        .sizeIn(60.dp, 60.dp)
        .clip(CircleShape)
        .then(modifier)
    if (isPreview) {
        Image(
            painter = painterResource(id = R.drawable.mr_bean),
            contentDescription = "profile image",
            modifier = imageModifier,
            contentScale = ContentScale.Crop
        )
    } else {
        AsyncImage(
            model = "$BASE_IMAGE_URL$avatarPath",
            contentDescription = "profile image",
            modifier = imageModifier,
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.mr_bean),
            error = painterResource(id = R.drawable.mr_bean),
        )
    }
}
