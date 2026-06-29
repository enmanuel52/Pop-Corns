package com.enmanuelbergling.feature.watchlists.created

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import assertk.assertions.isTrue
import com.enmanuelbergling.core.domain.datasource.remote.UserRemoteDS
import com.enmanuelbergling.core.model.core.NetworkException
import com.enmanuelbergling.core.model.core.SimplerUi
import com.enmanuelbergling.core.testing.datasource.remote.FakeUserRemoteDS
import com.enmanuelbergling.core.testing.datasource.remote.UserRemoteDsFunction
import com.enmanuelbergling.core.testing.extension.KoinExtension
import com.enmanuelbergling.core.testing.extension.MainCoroutineExtension
import com.enmanuelbergling.core.ui.components.messageResource
import com.enmanuelbergling.feature.watchlists.di.watchlistModule
import com.enmanuelbergling.feature.watchlists.model.CreateListEvent
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
class CreatedWatchListsVMTest : KoinTest {

    private val testDispatcher = StandardTestDispatcher()

    @JvmField
    @RegisterExtension
    val koinExtension = KoinExtension(watchlistModule)

    @JvmField
    @RegisterExtension
    val mainCoroutineExtension = MainCoroutineExtension(testDispatcher)

    private lateinit var viewModel: CreatedWatchListsVM

    @BeforeEach
    fun setUp() {
        viewModel = koinExtension.get()
    }

    @Test
    fun `when Name event is sent, name is updated and error cleared`() = runTest {
        viewModel.onCreateForm(CreateListEvent.Name("My list"))

        assertThat(viewModel.createListFormState.value.name).isEqualTo("My list")
        assertThat(viewModel.createListFormState.value.nameError).isNull()
    }

    @Test
    fun `when Description event is sent, description is updated and error cleared`() = runTest {
        viewModel.onCreateForm(CreateListEvent.Description("Cool movies"))

        assertThat(viewModel.createListFormState.value.description).isEqualTo("Cool movies")
        assertThat(viewModel.createListFormState.value.descriptionError).isNull()
    }

    @Test
    fun `when Submit is sent with blank fields, errors are set and list is not created`() = runTest {
        viewModel.onCreateForm(CreateListEvent.Submit)
        advanceUntilIdle()

        assertThat(viewModel.createListFormState.value.nameError).isNotNull()
        assertThat(viewModel.createListFormState.value.descriptionError).isNotNull()
        assertThat(viewModel.uiState.value).isEqualTo(SimplerUi.Idle)
    }

    @Test
    fun `when Submit succeeds, uiState is Success and form is reset`() = runTest {
        // Given valid fields
        viewModel.onCreateForm(CreateListEvent.Name("My list"))
        viewModel.onCreateForm(CreateListEvent.Description("Cool movies"))

        // When
        viewModel.onCreateForm(CreateListEvent.Submit)
        advanceUntilIdle()

        // Then
        assertThat(viewModel.uiState.value).isEqualTo(SimplerUi.Success)
        assertThat(viewModel.createListFormState.value.name).isEqualTo("")
        assertThat(viewModel.createListFormState.value.description).isEqualTo("")
    }

    @Test
    fun `when Submit fails, uiState is Error`() = runTest {
        // Given
        val exception = NetworkException.AuthorizationException()
        koinExtension.replaceDependencies {
            single<UserRemoteDS> {
                FakeUserRemoteDS().apply {
                    throwError(UserRemoteDsFunction.CreateWatchList to exception)
                }
            }
        }
        viewModel = koinExtension.get()
        viewModel.onCreateForm(CreateListEvent.Name("My list"))
        viewModel.onCreateForm(CreateListEvent.Description("Cool movies"))

        // When
        viewModel.onCreateForm(CreateListEvent.Submit)
        advanceUntilIdle()

        // Then
        assertThat(viewModel.uiState.value).isEqualTo(SimplerUi.Error(exception.messageResource))
    }

    @Test
    fun `when ToggleVisibility is sent, the form becomes visible`() = runTest {
        assertThat(viewModel.createListFormState.value.isVisible).isFalse()

        viewModel.onCreateForm(CreateListEvent.ToggleVisibility)

        assertThat(viewModel.createListFormState.value.isVisible).isTrue()
    }

    @Test
    fun `when ToggleVisibility hides the form, the form is reset`() = runTest {
        // Given a visible form with content
        viewModel.onCreateForm(CreateListEvent.ToggleVisibility) // visible
        viewModel.onCreateForm(CreateListEvent.Name("My list"))
        assertThat(viewModel.createListFormState.value.isVisible).isTrue()

        // When toggled off
        viewModel.onCreateForm(CreateListEvent.ToggleVisibility)

        // Then form is reset
        assertThat(viewModel.createListFormState.value.isVisible).isFalse()
        assertThat(viewModel.createListFormState.value.name).isEqualTo("")
    }

    @Test
    fun `when deleteList succeeds, uiState is Success`() = runTest {
        viewModel.deleteList(listId = 1)
        advanceUntilIdle()

        assertThat(viewModel.uiState.value).isEqualTo(SimplerUi.Success)
    }

    @Test
    fun `when deleteList fails, uiState is Error`() = runTest {
        val exception = NetworkException.AuthorizationException()
        koinExtension.replaceDependencies {
            single<UserRemoteDS> {
                FakeUserRemoteDS().apply {
                    throwError(UserRemoteDsFunction.DeleteList to exception)
                }
            }
        }
        viewModel = koinExtension.get()

        viewModel.deleteList(listId = 1)
        advanceUntilIdle()

        assertThat(viewModel.uiState.value).isEqualTo(SimplerUi.Error(exception.messageResource))
    }

    @Test
    fun `when onIdle is called, uiState is reset to Idle`() = runTest {
        viewModel.deleteList(listId = 1)
        advanceUntilIdle()

        viewModel.onIdle()

        assertThat(viewModel.uiState.value).isEqualTo(SimplerUi.Idle)
    }
}
