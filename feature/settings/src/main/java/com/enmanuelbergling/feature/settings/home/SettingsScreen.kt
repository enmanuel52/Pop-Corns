package com.enmanuelbergling.feature.settings.home

import androidx.compose.animation.animateColorAsState
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.enmanuelbergling.core.common.util.BASE_IMAGE_URL
import com.enmanuelbergling.core.model.settings.DarkTheme
import com.enmanuelbergling.core.model.user.UserDetails
import com.enmanuelbergling.core.ui.R
import com.enmanuelbergling.core.ui.core.dimen
import com.enmanuelbergling.core.ui.theme.CornTimeTheme
import com.enmanuelbergling.feature.settings.model.SettingItem
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsRoute(onBack: () -> Unit, onLogin: () -> Unit) {

    val viewModel = koinViewModel<SettingsVM>()

    val darkMode by viewModel.darkThemeState.collectAsStateWithLifecycle(initialValue = DarkTheme.System)
    val userState by viewModel.userState.collectAsStateWithLifecycle(initialValue = UserDetails())
    val darkThemeMenuVisible by viewModel.darkThemeMenuState.collectAsStateWithLifecycle()

    SettingsScreen(
        onBack = onBack,
        onLogin = onLogin,
        userState = userState,
        darkTheme = darkMode,
        darkThemeMenuVisible = darkThemeMenuVisible,
        onLogout = viewModel::onLogout,
        onDarkTheme = viewModel::onDarkTheme,
        onToggleDarkThemeMenu = viewModel::onToggleDarkThemeMenu
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun SettingsScreen(
    onBack: () -> Unit,
    onLogin: () -> Unit,
    userState: UserDetails,
    darkTheme: DarkTheme,
    darkThemeMenuVisible: Boolean,
    onLogout: () -> Unit,
    onDarkTheme: (DarkTheme) -> Unit,
    onToggleDarkThemeMenu: () -> Unit,
) {

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.settings)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBackIosNew,
                            contentDescription = "icon back"
                        )
                    }
                }, actions = {
                    if (userState.username.isNotBlank()) {
                        IconButton(onClick = onLogout) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ExitToApp,
                                contentDescription = "logout icon"
                            )
                        }

                    }
                }, scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->
        Column(Modifier.padding(paddingValues)) {
            ProfileWrapper(
                userState,
                Modifier
                    .fillMaxWidth()
                    .weight(4f),
                onLogin
            )

            LazyColumn(
                contentPadding = PaddingValues(vertical = MaterialTheme.dimen.large),
                modifier = Modifier
                    .weight(6f)
                    .fillMaxWidth()
                    .clip(
                        RoundedCornerShape(
                            topStartPercent = 15,
                            topEndPercent = 15
                        )
                    )
            ) {
                item {
                    SettingItemUi(
                        item = SettingItem.DarkMode,
                        textValue = darkTheme.label,
                        modifier = Modifier.fillMaxWidth(),
                        onClick = onToggleDarkThemeMenu
                    )
                }
            }
        }
    }

    if (darkThemeMenuVisible) {
        ModalBottomSheet(onDismissRequest = onToggleDarkThemeMenu) {
            Column(
                modifier = Modifier.navigationBarsPadding(),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.verySmall)
            ) {
                DarkTheme.entries.forEach { theme ->
                    SelectionText(text = theme.label, selected = theme == darkTheme) {
                        onDarkTheme(theme)
                        onToggleDarkThemeMenu()
                    }
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
            .padding(vertical = MaterialTheme.dimen.small),
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
            text = "System", selected = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {}
    }
}

@Composable
fun SettingItemUi(
    item: SettingItem,
    modifier: Modifier = Modifier,
    textValue: String = "",
    onClick: () -> Unit,
) {
    Row(
        modifier
            .clickable { onClick() }
            .padding(all = MaterialTheme.dimen.medium),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.small)
    ) {
        Box(
            modifier = Modifier
                .background(item.iconContainerColor, MaterialTheme.shapes.small)
                .padding(MaterialTheme.dimen.small),
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = "setting icon",
                tint = contentColorFor(item.iconContainerColor)
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
            SettingItem.DarkMode,
            Modifier.fillMaxWidth(),
            "System"
        ) {}
    }
}

@Composable
fun ProfileWrapper(userState: UserDetails, modifier: Modifier = Modifier, onLogin: () -> Unit) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        //Background with shaders

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            ProfileUi(
                username = userState.username,
                imageUrl = userState.avatarPath,
                modifier = Modifier.size(80.dp)
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
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.small)
    ) {
        ProfileImage(imageUrl, modifier)

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
            MaterialTheme.dimen.superSmall,
            MaterialTheme.colorScheme.primary,
            CircleShape
        )
        .padding(MaterialTheme.dimen.verySmall)
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
            contentScale = ContentScale.Crop
        )
    }
}
