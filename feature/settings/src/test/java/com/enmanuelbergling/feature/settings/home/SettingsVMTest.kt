package com.enmanuelbergling.feature.settings.home

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import assertk.assertions.isTrue
import com.enmanuelbergling.core.domain.datasource.preferences.UserPreferenceDS
import com.enmanuelbergling.core.model.user.UserDetails
import com.enmanuelbergling.core.testing.extension.KoinExtension
import com.enmanuelbergling.core.testing.extension.MainCoroutineExtension
import com.enmanuelbergling.feature.settings.di.settingsModule
import com.enmanuelbergling.feature.settings.model.DarkThemeUi
import com.enmanuelbergling.feature.settings.model.SettingUiEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.test.KoinTest
import org.koin.test.get
import org.koin.test.inject

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsVMTest : KoinTest {

    private val testDispatcher = StandardTestDispatcher()

    @JvmField
    @RegisterExtension
    val koinExtension = KoinExtension(settingsModule)

    @JvmField
    @RegisterExtension
    val mainCoroutineExtension = MainCoroutineExtension(testDispatcher)

    private val userPreferenceDS: UserPreferenceDS by koinExtension.inject()

    private lateinit var viewModel: SettingsVM

    @BeforeEach
    fun setUp() {
        viewModel = koinExtension.get()
    }

    @Test
    fun `userState reflects saved user updates and clear`() = runTest {
        viewModel.userState.test {
            // Initial: no user
            assertThat(awaitItem()).isNull()

            // When user is saved
            userPreferenceDS.updateUser(UserDetails(id = 1, username = "john"))
            assertThat(awaitItem()?.username).isEqualTo("john")

            // When user is cleared
            userPreferenceDS.clear()
            assertThat(awaitItem()).isNull()
        }
    }

    @Test
    fun `when DarkThemeEvent is sent, dark theme is persisted`() = runTest {
        viewModel.darkThemeState.test {
            // Default
            assertThat(awaitItem()).isEqualTo(DarkThemeUi.System)

            // When
            viewModel.onEvent(SettingUiEvent.DarkThemeEvent(DarkThemeUi.Yes))
            advanceUntilIdle()

            // Then
            assertThat(awaitItem()).isEqualTo(DarkThemeUi.Yes)
        }
    }

    @Test
    fun `when DynamicColor is sent, dynamic color is persisted`() = runTest {
        viewModel.dynamicColorState.test {
            // Default
            assertThat(awaitItem()).isFalse()

            // When
            viewModel.onEvent(SettingUiEvent.DynamicColor(true))
            advanceUntilIdle()

            // Then
            assertThat(awaitItem()).isTrue()
        }
    }

    @Test
    fun `when DarkThemeMenu is sent, dark theme menu visibility toggles`() = runTest {
        assertThat(viewModel.menuVisibleState.value.darkThemeVisible).isFalse()

        viewModel.onEvent(SettingUiEvent.DarkThemeMenu)
        advanceUntilIdle()
        assertThat(viewModel.menuVisibleState.value.darkThemeVisible).isTrue()

        viewModel.onEvent(SettingUiEvent.DarkThemeMenu)
        advanceUntilIdle()
        assertThat(viewModel.menuVisibleState.value.darkThemeVisible).isFalse()
    }

    @Test
    fun `when DynamicColorMenu is sent, dynamic color menu visibility toggles`() = runTest {
        assertThat(viewModel.menuVisibleState.value.dynamicColorVisible).isFalse()

        viewModel.onEvent(SettingUiEvent.DynamicColorMenu)
        advanceUntilIdle()
        assertThat(viewModel.menuVisibleState.value.dynamicColorVisible).isTrue()

        viewModel.onEvent(SettingUiEvent.DynamicColorMenu)
        advanceUntilIdle()
        assertThat(viewModel.menuVisibleState.value.dynamicColorVisible).isFalse()
    }

    @Test
    fun `when Logout is sent, the saved user is cleared`() = runTest {
        // Given a logged-in user
        userPreferenceDS.updateUser(UserDetails(id = 1, username = "john"))

        viewModel.userState.test {
            assertThat(awaitItem()).isNotNull()

            // When
            viewModel.onEvent(SettingUiEvent.Logout)
            advanceUntilIdle()

            // Then
            assertThat(awaitItem()).isNull()
        }
    }

    @Test
    fun `when Login is sent, nothing changes`() = runTest {
        viewModel.onEvent(SettingUiEvent.Login)
        advanceUntilIdle()

        assertThat(viewModel.menuVisibleState.value.darkThemeVisible).isFalse()
        assertThat(viewModel.menuVisibleState.value.dynamicColorVisible).isFalse()
    }
}
