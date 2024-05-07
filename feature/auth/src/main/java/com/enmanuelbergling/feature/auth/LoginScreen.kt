package com.enmanuelbergling.feature.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Key
import androidx.compose.material.icons.rounded.Person3
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedCard
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.enmanuelbergling.core.ui.R
import com.enmanuelbergling.core.ui.components.HandleUiState
import com.enmanuelbergling.core.ui.core.dimen
import com.enmanuelbergling.core.ui.design.CtiTextField
import com.enmanuelbergling.feature.auth.model.LoginEvent
import com.enmanuelbergling.feature.auth.model.LoginForm
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginRoute(onLoginSucceed: () -> Unit) {

    val viewModel = koinViewModel<LoginVM>()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val formState by viewModel.loginFormState.collectAsStateWithLifecycle()

    HandleUiState(uiState = uiState, onIdle = viewModel::onIdle, onSuccess = onLoginSucceed)

    LoginScreen(
        formState = formState,
        onLoginEvent = viewModel::onLoginFormEvent
    )
}

@Composable
fun LoginScreen(
    formState: LoginForm,
    onLoginEvent: (LoginEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.pop_corn_background),
            contentDescription = stringResource(R.string.background_login_image),
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        LoginFormUi(
            formState,
            onLoginEvent,
            Modifier
                .align(Alignment.Center)
                .padding(horizontal = MaterialTheme.dimen.lessLarge)
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
private fun SignIn(modifier: Modifier = Modifier, onSignIn: () -> Unit) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Text(text = "Do you have an account?", style = MaterialTheme.typography.bodySmall)

        Spacer(modifier = Modifier.height(MaterialTheme.dimen.small))

        TextButton(onClick = onSignIn) {
            Text(text = stringResource(R.string.sign_in))
        }
    }
}

@Composable
fun LoginFormUi(formState: LoginForm, onLoginEvent: (LoginEvent) -> Unit, modifier: Modifier) {
    ElevatedCard(modifier = modifier, shape = MaterialTheme.shapes.medium) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.large),
            modifier = Modifier.padding(
                horizontal = MaterialTheme.dimen.medium,
                vertical = MaterialTheme.dimen.large
            ),
        ) {

            CtiTextField(
                text = formState.username,
                onTextChange = { onLoginEvent(LoginEvent.Username(it)) },
                hint = stringResource(R.string.username),
                errorText = formState.usernameError,
                leadingIcon = Icons.Rounded.Person3,
            )

            CtiTextField(
                text = formState.password,
                onTextChange = { onLoginEvent(LoginEvent.Password(it)) },
                hint = stringResource(R.string.password),
                errorText = formState.passwordError,
                leadingIcon = Icons.Rounded.Key,
                trailingIcon = {
                    IconButton(onClick = { onLoginEvent(LoginEvent.PasswordVisibility) }) {
                        Icon(
                            imageVector = if (formState.isPasswordVisible) Icons.Rounded.Visibility else Icons.Rounded.VisibilityOff,
                            contentDescription = stringResource(R.string.password_visibility_icon)
                        )
                    }
                },
                visualTransformation = if (formState.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()
            )

            val errorFound by remember {
                derivedStateOf {
                    formState.usernameError != null || formState.passwordError != null
                }
            }

            Button(
                onClick = { onLoginEvent(LoginEvent.Submit) },
                enabled = !errorFound,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(ButtonDefaults.MinHeight.times(1.3f))
            ) {
                Text(text = stringResource(R.string.log_in))
            }
        }
    }
}