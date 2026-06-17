package com.enmanuelbergling.feature.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalAutofillManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.enmanuelbergling.core.ui.R
import com.enmanuelbergling.core.ui.components.ArtisticBackground
import com.enmanuelbergling.core.ui.components.HandleUiState
import com.enmanuelbergling.core.ui.core.ObserveAsEvents
import com.enmanuelbergling.core.ui.core.dimen
import com.enmanuelbergling.core.ui.design.CtiTextField
import com.enmanuelbergling.core.ui.theme.CornTimeTheme
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.blur.HazeBlurStyle
import dev.chrisbanes.haze.blur.blurEffect
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginRoute(onLoginSucceed: () -> Unit, onBack: () -> Unit) {

    val viewModel = koinViewModel<LoginVM>()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.uiEvents) { event ->
        when (event) {
            LoginEvent.LoginSuccess -> onLoginSucceed()
        }
    }

    HandleUiState(uiState = uiState.uiState, onIdle = viewModel::onIdle, onSuccess = onLoginSucceed)

    LoginScreen(
        state = uiState,
        onAction = viewModel::onAction,
        onBack = onBack
    )
}

@Composable
fun LoginScreen(
    state: LoginState,
    onAction: (LoginAction) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val hazeState = remember { HazeState() }

    Box(modifier = modifier.fillMaxSize()) {
        ArtisticBackground(
            Modifier
                .fillMaxSize()
                .hazeSource(hazeState)
        )

        TopBar(onBack)

        LoginFormUi(
            state = state,
            onAction = onAction,
            modifier = Modifier
                .align(Alignment.Center)
                .clip(CardDefaults.shape)
                .hazeEffect(state = hazeState) {
                    blurEffect {
                        style = HazeBlurStyle(
                            blurRadius = 10.dp,
                            colorEffect = null,
                        )
                    }
                }
                .padding(MaterialTheme.dimen.medium, MaterialTheme.dimen.lessLarge)
        )

        val uriHandler = LocalUriHandler.current

        SignIn(
            Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
        ) {
            uriHandler.openUri("https://www.themoviedb.org/signup")
        }
    }
}

@Composable
private fun TopBar(onBack: () -> Unit) {
    Column(modifier = Modifier.statusBarsPadding()) {
        IconButton(onClick = onBack) {
            Icon(imageVector = Icons.Rounded.ArrowBackIosNew, contentDescription = "arrow back")
        }

        Spacer(modifier = Modifier.height(MaterialTheme.dimen.medium))

        Text(
            text = stringResource(id = R.string.welcome_back).let {
                val words = it.split(" ")
                buildString {
                    words.forEachIndexed { index, word ->
                        append(word)
                        if (index != word.lastIndex) append("\n")
                    }
                }
            },
            style = MaterialTheme.typography.displaySmall,
            maxLines = 2,
            modifier = Modifier.padding(start = MaterialTheme.dimen.small)
        )

    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPrev() {
    CornTimeTheme {

        LoginScreen(
            LoginState(),
            {}, {},
            Modifier.fillMaxSize(),
        )
    }
}

@Composable
private fun SignIn(modifier: Modifier = Modifier, onSignIn: () -> Unit) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Text(text = "Do you have an account?", style = MaterialTheme.typography.bodySmall)

        Spacer(modifier = Modifier.width(MaterialTheme.dimen.verySmall))

        TextButton(onClick = onSignIn) {
            Text(text = stringResource(R.string.sign_in))
        }
    }
}

@Composable
fun LoginFormUi(state: LoginState, onAction: (LoginAction) -> Unit, modifier: Modifier) {

    val autofillManager = LocalAutofillManager.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.large),
        modifier = modifier,
    ) {

        CtiTextField(
            text = state.username,
            onTextChange = { onAction(LoginAction.OnUsernameChange(it)) },
            hint = stringResource(R.string.username),
            errorText = state.usernameError,
            modifier = Modifier.semantics {
                contentType = ContentType.Username + ContentType.NewUsername
            }
        )

        CtiTextField(
            text = state.password,
            onTextChange = { onAction(LoginAction.OnPasswordChange(it)) },
            hint = stringResource(R.string.password),
            errorText = state.passwordError,
            trailingIcon = {
                IconButton(onClick = { onAction(LoginAction.OnPasswordVisibilityClick) }) {
                    Icon(
                        imageVector = if (state.isPasswordVisible) Icons.Rounded.Visibility else Icons.Rounded.VisibilityOff,
                        contentDescription = stringResource(R.string.password_visibility_icon)
                    )
                }
            },
            visualTransformation = if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.semantics {
                contentType = ContentType.Password + ContentType.NewPassword
            },
        )

        val errorFound by remember {
            derivedStateOf {
                state.usernameError != null || state.passwordError != null
            }
        }

        Button(
            onClick = {
                autofillManager?.commit()
                onAction(LoginAction.OnLoginClick)
            },
            enabled = !errorFound,
        ) {
            Text(
                text = stringResource(R.string.log_in),
                modifier = Modifier.padding(horizontal = MaterialTheme.dimen.small)
            )
        }
    }
}
