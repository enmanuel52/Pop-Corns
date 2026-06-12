package com.enmanuelbergling.feature.watchlists.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.enmanuelbergling.core.ui.R
import com.enmanuelbergling.core.ui.components.DeleteMovieConfirmationDialog

@Composable
internal fun RemoveFromWatchlistDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    DeleteMovieConfirmationDialog(
        onDismiss = onDismiss,
        onDelete = onConfirm,
        title = stringResource(R.string.remove_movie_from_watchlist_title)
    )
}