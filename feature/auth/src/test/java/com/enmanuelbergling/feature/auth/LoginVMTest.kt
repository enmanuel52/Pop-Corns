package com.enmanuelbergling.feature.auth

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import assertk.assertions.isTrue
import com.enmanuelbergling.core.domain.datasource.remote.AuthRemoteDS
import com.enmanuelbergling.core.model.core.NetworkException
import com.enmanuelbergling.core.model.core.SimplerUi
import com.enmanuelbergling.core.testing.datasource.remote.AuthRemoteDsFunction
import com.enmanuelbergling.core.testing.datasource.remote.FakeAuthRemoteDS
import com.enmanuelbergling.core.testing.extension.KoinExtension
import com.enmanuelbergling.core.testing.extension.MainCoroutineExtension
import com.enmanuelbergling.core.ui.components.messageResource
import com.enmanuelbergling.feature.auth.di.loginModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.test.KoinTest
import org.koin.test.get

@OptIn(ExperimentalCoroutinesApi::class)
class LoginVMTest : KoinTest {

    private val testDispatcher = StandardTestDispatcher()

    @JvmField
    @RegisterExtension
    val koinExtension = KoinExtension(*loginModule.toTypedArray())

    @JvmField
    @RegisterExtension
    val mainCoroutineExtension = MainCoroutineExtension(testDispatcher)

    private lateinit var viewModel: LoginVM

    @BeforeEach
    fun setUp() {
        viewModel = koinExtension.get()
    }

    @Test
    fun `when OnUsernameChange is called, username is updated and error cleared`() = runTest {
        viewModel.onAction(LoginAction.OnLoginClick)
        advanceUntilIdle()
        assertThat(viewModel.uiState.value.usernameError).isNotNull()

        viewModel.onAction(LoginAction.OnUsernameChange("john"))

        assertThat(viewModel.uiState.value.username).isEqualTo("john")
        assertThat(viewModel.uiState.value.usernameError).isNull()
    }

    @Test
    fun `when OnPasswordChange is called, password is updated and error cleared`() = runTest {
        viewModel.onAction(LoginAction.OnLoginClick)
        advanceUntilIdle()
        assertThat(viewModel.uiState.value.passwordError).isNotNull()

        viewModel.onAction(LoginAction.OnPasswordChange("secret"))

        assertThat(viewModel.uiState.value.password).isEqualTo("secret")
        assertThat(viewModel.uiState.value.passwordError).isNull()
    }

    @Test
    fun `when OnPasswordVisibilityClick is called, visibility toggles`() = runTest {
        assertThat(viewModel.uiState.value.isPasswordVisible).isFalse()

        viewModel.onAction(LoginAction.OnPasswordVisibilityClick)
        assertThat(viewModel.uiState.value.isPasswordVisible).isTrue()

        viewModel.onAction(LoginAction.OnPasswordVisibilityClick)
        assertThat(viewModel.uiState.value.isPasswordVisible).isFalse()
    }

    @Test
    fun `when OnLoginClick is called with blank fields, errors are set and login is not attempted`() =
        runTest {
            // Given blank username and password (default state)

            // When
            viewModel.onAction(LoginAction.OnLoginClick)
            advanceUntilIdle()

            // Then
            assertThat(viewModel.uiState.value.usernameError).isNotNull()
            assertThat(viewModel.uiState.value.passwordError).isNotNull()
            assertThat(viewModel.uiState.value.uiState).isEqualTo(SimplerUi.Idle)
        }

    @Test
    fun `when OnLoginClick succeeds, uiState is Idle and LoginSuccess is emitted`() = runTest {
        // Given valid fields
        viewModel.onAction(LoginAction.OnUsernameChange("john"))
        viewModel.onAction(LoginAction.OnPasswordChange("secret"))

        viewModel.uiEvents.test {
            // When
            viewModel.onAction(LoginAction.OnLoginClick)
            advanceUntilIdle()

            // Then
            assertThat(awaitItem()).isEqualTo(LoginEvent.LoginSuccess)
            assertThat(viewModel.uiState.value.uiState).isEqualTo(SimplerUi.Idle)
        }
    }

    @Test
    fun `when OnLoginClick fails, uiState is error and no event is emitted`() = runTest {
        // Given the login chain fails
        val networkException = NetworkException.DefaultException
        koinExtension.replaceDependencies {
            single<AuthRemoteDS> {
                FakeAuthRemoteDS().apply {
                    throwError(AuthRemoteDsFunction.CreateRequestToken to networkException)
                }
            }
        }
        viewModel = koinExtension.get()
        viewModel.onAction(LoginAction.OnUsernameChange("john"))
        viewModel.onAction(LoginAction.OnPasswordChange("secret"))

        viewModel.uiEvents.test {
            // When
            viewModel.onAction(LoginAction.OnLoginClick)
            advanceUntilIdle()

            // Then
            assertThat(viewModel.uiState.value.uiState)
                .isEqualTo(SimplerUi.Error(networkException.messageResource))
            expectNoEvents()
        }
    }

    @Test
    fun `when onIdle is called, uiState is reset to Idle`() = runTest {
        // Given an error state
        koinExtension.replaceDependencies {
            single<AuthRemoteDS> {
                FakeAuthRemoteDS().apply {
                    throwError(AuthRemoteDsFunction.CreateRequestToken to NetworkException.ReadTimeOutException)
                }
            }
        }
        viewModel = koinExtension.get()
        viewModel.onAction(LoginAction.OnUsernameChange("john"))
        viewModel.onAction(LoginAction.OnPasswordChange("secret"))
        viewModel.onAction(LoginAction.OnLoginClick)
        advanceUntilIdle()
        assertThat(viewModel.uiState.value.uiState).isInstanceOf(SimplerUi.Error::class.java)

        // When
        viewModel.onIdle()

        // Then
        assertThat(viewModel.uiState.value.uiState).isEqualTo(SimplerUi.Idle)
    }
}
