package com.enmanuelbergling.feature.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Button
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
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.enmanuelbergling.core.ui.R
import com.enmanuelbergling.core.ui.components.ArtisticBackground
import com.enmanuelbergling.core.ui.components.HandleUiState
import com.enmanuelbergling.core.ui.core.dimen
import com.enmanuelbergling.core.ui.design.CtiTextField
import com.enmanuelbergling.core.ui.theme.CornTimeTheme
import com.enmanuelbergling.feature.auth.model.LoginEvent
import com.enmanuelbergling.feature.auth.model.LoginForm
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeChild
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginRoute(onLoginSucceed: () -> Unit, onBack: () -> Unit) {

    val viewModel = koinViewModel<LoginVM>()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val formState by viewModel.loginFormState.collectAsStateWithLifecycle()

    HandleUiState(uiState = uiState, onIdle = viewModel::onIdle, onSuccess = onLoginSucceed)

    LoginScreen(
        formState = formState,
        onLoginEvent = viewModel::onLoginFormEvent,
        onBack
    )
}

@Composable
fun LoginScreen(
    formState: LoginForm,
    onLoginEvent: (LoginEvent) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val hazeState = remember { HazeState() }

    Box(modifier = modifier.fillMaxSize()) {
        ArtisticBackground(
            Modifier
                .fillMaxSize()
                .haze(hazeState)
        )

        TopBar(onBack)

        LoginFormUi(
            formState = formState,
            onLoginEvent = onLoginEvent,
            modifier = Modifier
                .align(Alignment.Center)
                .hazeChild(
                    hazeState,
                    shape = MaterialTheme.shapes.small,
                    style = HazeStyle(blurRadius = 16.dp)
                )
                .padding(MaterialTheme.dimen.medium, MaterialTheme.dimen.lessLarge)
        )

        val uriHandler = LocalUriHandler.current

        SignIn(
            Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = MaterialTheme.dimen.medium)
        ) {
            uriHandler.openUri("https://www.themoviedb.org/signup")
        }
    }
}

@Composable
private fun TopBar(onBack: () -> Unit) {
    Column(modifier = Modifier.padding(top = MaterialTheme.dimen.medium)) {
        IconButton(onClick = onBack) {
            Icon(imageVector = Icons.Rounded.ArrowBackIosNew, contentDescription = "arrow back")
        }

        Spacer(modifier = Modifier.height(MaterialTheme.dimen.almostGiant))

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
            LoginForm(),
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
fun LoginFormUi(formState: LoginForm, onLoginEvent: (LoginEvent) -> Unit, modifier: Modifier) {


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.large),
        modifier = modifier,
    ) {

        CtiTextField(
            text = formState.username,
            onTextChange = { onLoginEvent(LoginEvent.Username(it)) },
            hint = stringResource(R.string.username),
            errorText = formState.usernameError,
        )

        CtiTextField(
            text = formState.password,
            onTextChange = { onLoginEvent(LoginEvent.Password(it)) },
            hint = stringResource(R.string.password),
            errorText = formState.passwordError,
            trailingIcon = {
                IconButton(onClick = { onLoginEvent(LoginEvent.PasswordVisibility) }) {
                    Icon(
                        imageVector = if (formState.isPasswordVisible) Icons.Rounded.Visibility else Icons.Rounded.VisibilityOff,
                        contentDescription = stringResource(R.string.password_visibility_icon)
                    )
                }
            },
            visualTransformation = if (formState.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        )

        val errorFound by remember {
            derivedStateOf {
                formState.usernameError != null || formState.passwordError != null
            }
        }

        Button(
            onClick = { onLoginEvent(LoginEvent.Submit) },
            enabled = !errorFound,
        ) {
            Text(
                text = stringResource(R.string.log_in),
                modifier = Modifier.padding(horizontal = MaterialTheme.dimen.small)
            )
        }
    }
}