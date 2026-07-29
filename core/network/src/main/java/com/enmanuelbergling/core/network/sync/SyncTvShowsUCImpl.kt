package com.enmanuelbergling.core.network.sync

import android.content.Context
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.enmanuelbergling.core.domain.usecase.user.SyncTvShowsUC
import com.enmanuelbergling.core.network.sync.worker.AddTvShowToFavoriteWorker
import com.enmanuelbergling.core.network.sync.worker.MOVIE_IDS
import com.enmanuelbergling.core.network.sync.worker.RemoveTvShowFromFavoriteWorker
import com.enmanuelbergling.core.network.sync.worker.RemoveTvShowFromWatchlistWorker
import com.enmanuelbergling.core.network.sync.worker.TV_SHOW_IDS

class SyncTvShowsUCImpl(
    private val appContext: Context,
) : SyncTvShowsUC {
    private val workManager by lazy { WorkManager.getInstance(appContext) }

    override fun watchlist(
        deletedTvShowIds: List<Int>,
        favoriteTvShowIds: List<Int>
    ) {
        val removeFromWatchlistRequest =
            OneTimeWorkRequestBuilder<RemoveTvShowFromWatchlistWorker>()
                .setInputData(
                    workDataOf(MOVIE_IDS to deletedTvShowIds.plus(favoriteTvShowIds).toTypedArray())
                )
                .setConstraints(
                    Constraints(requiredNetworkType = NetworkType.CONNECTED)
                )
                .build()

        val addToFavoriteRequest = OneTimeWorkRequestBuilder<AddTvShowToFavoriteWorker>()
            .setInputData(workDataOf(TV_SHOW_IDS to favoriteTvShowIds.toTypedArray()))
            .setConstraints(Constraints(requiredNetworkType = NetworkType.CONNECTED))
            .build()

        workManager.beginWith(removeFromWatchlistRequest)
            .then(addToFavoriteRequest)
            .enqueue()
    }

    override fun deleteFromFavorite(tvShowIds: List<Int>) {
        val removeRequest = OneTimeWorkRequestBuilder<RemoveTvShowFromFavoriteWorker>()
            .setInputData(
                workDataOf(TV_SHOW_IDS to tvShowIds.toTypedArray())
            )
            .setConstraints(
                Constraints(requiredNetworkType = NetworkType.CONNECTED)
            )
            .build()

        workManager.enqueue(removeRequest)
    }
}