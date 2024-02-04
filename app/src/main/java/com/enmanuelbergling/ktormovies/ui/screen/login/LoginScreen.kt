package com.enmanuelbergling.ktormovies.ui.screen.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Key
import androidx.compose.material.icons.rounded.Person3
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Button
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.enmanuelbergling.ktormovies.R
import com.enmanuelbergling.ktormovies.ui.components.CtiTextField
import com.enmanuelbergling.ktormovies.ui.components.HandleUiState
import com.enmanuelbergling.ktormovies.ui.core.dimen
import com.enmanuelbergling.ktormovies.ui.screen.login.model.LoginEvent
import com.enmanuelbergling.ktormovies.ui.screen.login.model.LoginForm
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.koin.koinViewModel

@Composable
fun LoginRoute(onLoginSucceed: () -> Unit) {

    val viewModel = koinViewModel<LoginVM>()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val formState by viewModel.loginFormState.collectAsStateWithLifecycle()

    HandleUiState(uiState = uiState, onIdle = viewModel::onIdle, onSuccess = onLoginSucceed)

    LoginScreen(
        formState = formState,
        onLoginEvent ={onLoginSucceed()} //viewModel::onLoginFormEvent
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
            contentDescription = "background login image",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )

        LoginFormUi(
            formState,
            onLoginEvent,
            Modifier.align(Alignment.Center)
        )

        SignIn(
            Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = MaterialTheme.dimen.medium)
        )
    }
}

@Composable
private fun SignIn(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Text(text = "Do you have an account?", style = MaterialTheme.typography.bodySmall)

        Spacer(modifier = Modifier.height(MaterialTheme.dimen.small))

        TextButton(onClick = { /*TODO("Head for page")*/ }) {
            Text(text = "Sign In")
        }
    }
}

@Composable
fun LoginFormUi(formState: LoginForm, onLoginEvent: (LoginEvent) -> Unit, modifier: Modifier) {
    ElevatedCard(modifier = modifier) {
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
                hint = "Username*",
                errorText = formState.usernameError,
                leadingIcon = Icons.Rounded.Person3,
            )

            CtiTextField(
                text = formState.password,
                onTextChange = { onLoginEvent(LoginEvent.Password(it)) },
                hint = "Password*",
                errorText = formState.passwordError,
                leadingIcon = Icons.Rounded.Key,
                trailingIcon = {
                    IconButton(onClick = { onLoginEvent(LoginEvent.PasswordVisibility) }) {
                        Icon(
                            imageVector = if (formState.isPasswordVisible) Icons.Rounded.Visibility else Icons.Rounded.VisibilityOff,
                            contentDescription = "password visibility icon"
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
                enabled = !errorFound
            ) {
                Text(text = "Log In")
            }
        }
    }
}