package com.enmanuelbergling.feature.watchlists.home

import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.enmanuelbergling.core.domain.usecase.auth.GetSavedSessionIdUC
import com.enmanuelbergling.core.domain.usecase.form.BasicFormValidationUC
import com.enmanuelbergling.core.domain.usecase.user.watchlist.CreateListUC
import com.enmanuelbergling.core.domain.usecase.user.watchlist.DeleteListUC
import com.enmanuelbergling.core.model.core.ResultHandler
import com.enmanuelbergling.core.model.core.SimplerUi
import com.enmanuelbergling.core.model.user.AccountListsFilter
import com.enmanuelbergling.core.model.user.WatchList
import com.enmanuelbergling.core.network.paging.usecase.core.GetFilteredPagingFlowUC
import com.enmanuelbergling.core.ui.components.messageResource
import com.enmanuelbergling.feature.watchlists.model.CreateListEvent
import com.enmanuelbergling.feature.watchlists.model.CreateListForm
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

class WatchListVM(
    getPaginatedLists: GetFilteredPagingFlowUC<WatchList, AccountListsFilter>,
    getSessionId: GetSavedSessionIdUC,
    private val createListUC: CreateListUC,
    private val deleteListUC: DeleteListUC,
    private val basicFormValidationUC: BasicFormValidationUC,
) : ViewModel() {

    val sessionId = getSessionId().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ""
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val lists: Flow<PagingData<WatchList>> =
        sessionId.filter { it.isNotBlank() }
            .flatMapLatest {
                getPaginatedLists(
                    AccountListsFilter(
                        sessionId = sessionId.value
                    )
                ).cachedIn(
                    viewModelScope
                )
            }

    private val _createListFormState = MutableStateFlow(CreateListForm())
    val createListFormState get() = _createListFormState.asStateFlow()

    private val _uiState = MutableStateFlow<SimplerUi>(SimplerUi.Idle)
    val uiState = _uiState.asStateFlow()

    fun onCreateForm(event: CreateListEvent) {
        _createListFormState.update {
            when (event) {
                is CreateListEvent.Description -> it.copy(
                    description = event.value,
                    descriptionError = null
                )

                is CreateListEvent.Name -> it.copy(name = event.value, nameError = null)
                CreateListEvent.Submit -> {
                    if (validateFields()) {
                        createList()
                    }
                    it
                }

                CreateListEvent.OpenForm -> {
                    it.copy(isVisible = !it.isVisible).let { form ->
                        if (!form.isVisible) {
                            //reset
                            CreateListForm()
                        } else form
                    }
                }
            }
        }
    }

    private fun createList() = viewModelScope.launch {
        _uiState.update { SimplerUi.Loading }
        when (val result = createListUC(
            listPost = createListFormState.value.toPost(), sessionId = sessionId.value
        )) {
            is ResultHandler.Error -> _uiState.update { SimplerUi.Error(result.exception.messageResource) }
            is ResultHandler.Success -> _uiState.update { SimplerUi.Success }.also {
                _createListFormState.update { CreateListForm() }
            }
        }
    }

    fun deleteList(listId: Int) = viewModelScope.launch {
        _uiState.update { SimplerUi.Loading }
        when (val result = deleteListUC(listId, sessionId.value)) {
            is ResultHandler.Error -> _uiState.update { SimplerUi.Error(result.exception.messageResource) }
            is ResultHandler.Success -> _uiState.update { SimplerUi.Success }
        }
    }

    private fun validateFields(): Boolean {
        val nameValidation = basicFormValidationUC(createListFormState.value.name)
        val descriptionValidation = basicFormValidationUC(createListFormState.value.description)

        _createListFormState.update {
            it.copy(
                nameError = nameValidation.errorMessage,
                descriptionError = descriptionValidation.errorMessage
            )
        }

        return nameValidation.isSuccess && descriptionValidation.isSuccess
    }

    fun onIdle() {
        _uiState.update { SimplerUi.Idle }
    }
}