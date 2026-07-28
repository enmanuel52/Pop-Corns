package com.enmanuelbergling.core.network.sync

import android.content.Context
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.enmanuelbergling.core.domain.usecase.user.SyncWatchlistUC
import com.enmanuelbergling.core.network.sync.worker.AddToFavoriteWorker
import com.enmanuelbergling.core.network.sync.worker.MOVIE_IDS
import com.enmanuelbergling.core.network.sync.worker.RemoveFromFavoriteWorker
import com.enmanuelbergling.core.network.sync.worker.RemoveFromWatchlistWorker

class SyncWatchlistUCImpl(
    private val appContext: Context,
) : SyncWatchlistUC {
    private val workManager by lazy { WorkManager.getInstance(appContext) }

    override fun watchlist(
        deletedMovieIds: List<Int>,
        favoriteMovieIds: List<Int>
    ) {
        val removeFromWatchlistRequest = OneTimeWorkRequestBuilder<RemoveFromWatchlistWorker>()
            .setInputData(
                workDataOf(MOVIE_IDS to deletedMovieIds.plus(favoriteMovieIds))
            )
            .setConstraints(Constraints(requiredNetworkType = NetworkType.CONNECTED))
            .build()

        val addToFavoriteRequest = OneTimeWorkRequestBuilder<AddToFavoriteWorker>()
            .setInputData(workDataOf(MOVIE_IDS to favoriteMovieIds))
            .setConstraints(Constraints(requiredNetworkType = NetworkType.CONNECTED))
            .build()

        workManager.beginWith(removeFromWatchlistRequest)
            .then(addToFavoriteRequest)
            .enqueue()
    }

    override fun deleteFromFavorite(movieIds: List<Int>) {
        val removeRequest = OneTimeWorkRequestBuilder<RemoveFromFavoriteWorker>()
            .setInputData(workDataOf(MOVIE_IDS to movieIds))
            .setConstraints(Constraints(requiredNetworkType = NetworkType.CONNECTED))
            .build()

        workManager.enqueue(removeRequest)
    }
}