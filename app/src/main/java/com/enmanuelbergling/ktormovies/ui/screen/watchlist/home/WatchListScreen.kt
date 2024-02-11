package com.enmanuelbergling.ktormovies.ui.screen.watchlist.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBackIos
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.enmanuelbergling.ktormovies.domain.model.user.WatchList
import com.enmanuelbergling.ktormovies.ui.components.CtiContentDialog
import com.enmanuelbergling.ktormovies.ui.components.CtiTextField
import com.enmanuelbergling.ktormovies.ui.components.HandleUiState
import com.enmanuelbergling.ktormovies.ui.components.PullToRefreshContainer
import com.enmanuelbergling.ktormovies.ui.core.dimen
import com.enmanuelbergling.ktormovies.ui.core.isRefreshing
import com.enmanuelbergling.ktormovies.ui.core.shimmerIf
import com.enmanuelbergling.ktormovies.ui.screen.watchlist.components.WatchListCard
import com.enmanuelbergling.ktormovies.ui.screen.watchlist.components.WatchListCardPlaceholder
import com.enmanuelbergling.ktormovies.ui.screen.watchlist.model.CreateListEvent
import com.enmanuelbergling.ktormovies.ui.screen.watchlist.model.CreateListForm
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.koin.koinViewModel

@Composable
fun WatchListRoute(onDetails: (listId: Int, listName: String) -> Unit, onBack: () -> Unit) {
    val viewModel = koinViewModel<WatchListVM>()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lists = viewModel.lists.collectAsLazyPagingItems()
    val createListForm by viewModel.createListFormState.collectAsStateWithLifecycle()

    HandleUiState(uiState = uiState, onIdle = viewModel::onIdle, onSuccess = lists::refresh)

    WatchListScreen(
        lists,
        createListForm,
        viewModel::onCreateForm,
        viewModel::deleteList,
        onDetails,
        onBack
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun WatchListScreen(
    lists: LazyPagingItems<WatchList>,
    createListForm: CreateListForm,
    onCreateFormEvent: (CreateListEvent) -> Unit,
    onDeleteList: (listId: Int) -> Unit,
    onDetails: (listId: Int, listName: String) -> Unit,
    onBack: () -> Unit,
) {
    val snackbarHostState = remember {
        SnackbarHostState()
    }

    Scaffold(Modifier.fillMaxSize(), topBar = {
        CenterAlignedTopAppBar(title = { Text(text = "Watch Lists") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBackIos,
                        contentDescription = "back icon"
                    )
                }
            },
            actions = {
                if (!lists.isRefreshing) {
                    IconButton(onClick = { onCreateFormEvent(CreateListEvent.OpenForm) }) {
                        Icon(imageVector = Icons.Rounded.Add, contentDescription = "add icon")
                    }
                }
            })
    }, snackbarHost = {
        SnackbarHost(snackbarHostState)
    }) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            PullToRefreshContainer(
                refreshing = false,
                onRefresh = lists::refresh,
                modifier = Modifier
                    .shimmerIf { lists.isRefreshing },
                verticalArrangement = Arrangement.spacedBy(
                    MaterialTheme.dimen.small,
                ),
                contentPadding = PaddingValues(MaterialTheme.dimen.small)
            ) {
                if (lists.isRefreshing) {
                    items(12) {
                        WatchListCardPlaceholder()
                    }
                } else {
                    items(lists) { list ->
                        list?.let {
                            WatchListCard(
                                name = list.name,
                                description = list.description,
                                Modifier.fillMaxWidth()
                            ) {
                                onDetails(list.id, list.name)
                            }
                        }
                    }
                }
            }
        }
    }

    if (lists.loadState.refresh is LoadState.Error) {
        LaunchedEffect(key1 = Unit) {
            val listError = lists.loadState.refresh as? LoadState.Error
            snackbarHostState.showSnackbar(
                listError?.error?.message ?: "Not session found, login and try again",
                withDismissAction = true,
                duration = SnackbarDuration.Indefinite
            )
        }
    }

    if (createListForm.isVisible) {
        CtiContentDialog(
            onDismiss = { onCreateFormEvent(CreateListEvent.OpenForm) },
            title = { Text("New List") },
            confirmButton = {
                TextButton(onClick = { onCreateFormEvent(CreateListEvent.Submit) }) {
                    Text(text = "Create")
                }
            },
            verticalSpacing = MaterialTheme.dimen.medium
        ) {
            item {
                CtiTextField(
                    text = createListForm.name,
                    onTextChange = { onCreateFormEvent(CreateListEvent.Name(it)) },
                    hint = "List name*",
                    leadingIcon = Icons.Rounded.Info,
                    errorText = createListForm.nameError,
                )
            }

            item {
                CtiTextField(
                    text = createListForm.description,
                    onTextChange = { onCreateFormEvent(CreateListEvent.Description(it)) },
                    hint = "List description*",
                    leadingIcon = Icons.Rounded.Info,
                    errorText = createListForm.descriptionError
                )
            }
        }
    }
}