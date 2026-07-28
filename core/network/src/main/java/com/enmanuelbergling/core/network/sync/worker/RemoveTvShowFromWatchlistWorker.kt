package com.enmanuelbergling.core.network.sync.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.enmanuelbergling.core.domain.datasource.remote.TvRemoteDS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

class RemoveTvShowFromWatchlistWorker(
    appContext: Context,
    private val params: WorkerParameters,
    private val tvRemoteDS: TvRemoteDS
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val ids =
            params.inputData.getIntArray(TV_SHOW_IDS) ?: return@withContext Result.failure()

        val result = ids.map {
            async {
                tvRemoteDS.removeTvFromAccountWatchlist(tvShowId = it).getOrNull()
            }
        }.awaitAll()

        if (result.all { it != null }) Result.success()
        else Result.failure()
    }
}
