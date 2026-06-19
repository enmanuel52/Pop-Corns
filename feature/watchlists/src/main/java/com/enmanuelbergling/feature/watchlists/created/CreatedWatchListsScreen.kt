package com.enmanuelbergling.feature.watchlists.created

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.enmanuelbergling.core.model.user.WatchList
import com.enmanuelbergling.core.ui.R
import com.enmanuelbergling.core.ui.components.DeleteMovieConfirmationDialog
import com.enmanuelbergling.core.ui.components.HandleUiState
import com.enmanuelbergling.core.ui.components.NewerDragListItem
import com.enmanuelbergling.core.ui.components.PullToRefreshContainer
import com.enmanuelbergling.core.ui.components.common.WatchListCard
import com.enmanuelbergling.core.ui.components.common.WatchListCardPlaceholder
import com.enmanuelbergling.core.ui.core.dimen
import com.enmanuelbergling.core.ui.core.isRefreshing
import com.enmanuelbergling.core.ui.core.shimmerIf
import com.enmanuelbergling.feature.watchlists.components.CreateListDialog
import com.enmanuelbergling.feature.watchlists.model.CreateListEvent
import com.enmanuelbergling.feature.watchlists.model.CreateListForm
import org.koin.androidx.compose.koinViewModel


private const val NO_LIST = -1

@Composable
fun CreatedWatchListsRoute(
    onDetails: (listId: Int, listName: String) -> Unit,
    onBack: () -> Unit,
) {
    val viewModel = koinViewModel<CreatedWatchListsVM>()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lists = viewModel.lists.collectAsLazyPagingItems()
    val createListForm by viewModel.createListFormState.collectAsStateWithLifecycle()

    HandleUiState(
        uiState = uiState, onIdle = viewModel::onIdle
    ) {
        lists.refresh()
        viewModel.onIdle()
    }

    CreatedWatchListsScreen(
        lists,
        createListForm,
        viewModel::onCreateForm,
        viewModel::deleteList,
        onDetails,
        onBack = onBack,
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun CreatedWatchListsScreen(
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

    var pickedList by rememberSaveable {
        mutableIntStateOf(NO_LIST)
    }

    if (pickedList != NO_LIST) {
        DeleteMovieConfirmationDialog(onDismiss = { pickedList = NO_LIST }, onDelete = {
            val tempListId = pickedList
            pickedList = NO_LIST
            onDeleteList(tempListId)
        })
    }

    Scaffold(
        Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.watch_lists)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBackIosNew,
                            contentDescription = stringResource(R.string.back_icon)
                        )
                    }
                },
                actions = {
                    if (!lists.isRefreshing) {
                        IconButton(onClick = { onCreateFormEvent(CreateListEvent.ToggleVisibility) }) {
                            Icon(
                                painter = painterResource(R.drawable.add),
                                contentDescription = stringResource(R.string.add_icon)
                            )
                        }
                    }
                })
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        contentWindowInsets = WindowInsets.statusBars,
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            PullToRefreshContainer(
                refreshing = false,
                onRefresh = lists::refresh,
                modifier = Modifier
                    .shimmerIf { lists.isRefreshing }
                    .padding(horizontal = MaterialTheme.dimen.verySmall),
                verticalArrangement = Arrangement.spacedBy(
                    MaterialTheme.dimen.small,
                ),
                contentPadding = WindowInsets.navigationBars.asPaddingValues(),
            ) {
                if (lists.isRefreshing) {
                    items(12) {
                        WatchListCardPlaceholder()
                    }
                } else {
                    items(lists.itemCount) { index ->
                        val list = lists[index]
                        list?.let {
                            val bottomWith = remember {
                                (-80).dp
                            }

                            NewerDragListItem(
                                bottomContentWidth = with(LocalDensity.current) { bottomWith.toPx() },
                                bottomContent = {
                                    Box(Modifier.align(Alignment.CenterEnd)) {

                                        IconButton(
                                            onClick = { pickedList = it.id },
                                            modifier = Modifier.padding(horizontal = MaterialTheme.dimen.small)

                                        ) {
                                            Icon(
                                                painter = painterResource(R.drawable.trash),
                                                contentDescription = stringResource(id = R.string.delete_icon)
                                            )
                                        }
                                    }
                                },
                                modifier = Modifier.background(
                                    MaterialTheme.colorScheme.surfaceVariant,
                                    CardDefaults.elevatedShape
                                ),
                            ) {
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
    }

    if (lists.loadState.refresh is LoadState.Error) {
        val defaultMessage = stringResource(R.string.not_session_found_message)
        LaunchedEffect(key1 = Unit) {
            val listError = lists.loadState.refresh as? LoadState.Error
            snackbarHostState.showSnackbar(
                listError?.error?.message ?: defaultMessage,
                withDismissAction = true,
                duration = SnackbarDuration.Indefinite
            )
        }
    }

    if (createListForm.isVisible) {
        CreateListDialog(form = createListForm, onEvent = onCreateFormEvent)
    }
}
