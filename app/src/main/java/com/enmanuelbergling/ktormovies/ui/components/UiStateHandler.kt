package com.enmanuelbergling.ktormovies.ui.components

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.enmanuelbergling.ktormovies.R
import com.enmanuelbergling.ktormovies.domain.model.core.SimplerUi


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
            SnackBarError(snackState, uiState.message, onRetry)
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
            DefaultErrorDialog(onDismissDialog = onIdle, message = uiState.message)
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