package com.enmanuelbergling.core.ui.components

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.enmanuelbergling.core.model.core.SimplerUi
import com.enmanuelbergling.core.ui.R


@Composable
fun HandlerPagingUiState(items: LazyPagingItems<*>, snackState: SnackbarHostState) {
    when (val uiState = items.loadState.refresh) {
        is LoadState.Error -> {
            SnackBarError(snackState, uiState.error.message.orEmpty(), items::retry)
        }

        LoadState.Loading -> {}
        is LoadState.NotLoading -> {}
    }
}

@Composable
fun HandleUiState(
    uiState: SimplerUi,
    snackState: SnackbarHostState,
    onRetry: () -> Unit,
    getFocus: Boolean = false,
) {
    when (uiState) {
        is SimplerUi.Error -> {
            SnackBarError(snackState, stringResource(uiState.messageRes), onRetry)
        }

        SimplerUi.Idle, SimplerUi.Success -> {}
        SimplerUi.Loading -> if (getFocus) {
            LoadingDialog()
        }
    }
}

@Composable
fun HandleUiState(
    uiState: SimplerUi,
    onIdle: () -> Unit,
    onSuccess: () -> Unit,
) {
    when (uiState) {
        is SimplerUi.Error -> {
            DefaultErrorDialog(
                onDismissDialog = onIdle,
                message = stringResource(uiState.messageRes)
            )
        }

        SimplerUi.Idle -> {}
        SimplerUi.Success -> onSuccess()
        SimplerUi.Loading -> LoadingDialog()
    }
}

@Composable
private fun SnackBarError(
    snackState: SnackbarHostState,
    errorMessage: String,
    onRetry: () -> Unit,
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = Unit) {
        val snackResult = snackState.showSnackbar(
            message = errorMessage, actionLabel = context.getString(R.string.retry),
            withDismissAction = true,
            duration = SnackbarDuration.Indefinite
        )
        when (snackResult) {
            SnackbarResult.Dismissed -> snackState.currentSnackbarData?.dismiss()
            SnackbarResult.ActionPerformed -> onRetry()
        }
    }
}