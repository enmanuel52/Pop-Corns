package com.enmanuelbergling.core.network.sync.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.enmanuelbergling.core.domain.datasource.remote.UserRemoteDS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

class RemoveFromWatchlistWorker(
    appContext: Context,
    private val params: WorkerParameters,
    private val userRemoteDS: UserRemoteDS
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val ids =
            params.inputData.getIntArray(MOVIE_IDS) ?: return@withContext Result.failure()

        val result = ids.map {
            async {
                userRemoteDS.removeMovieFromAccountWatchlist(movieId = it).getOrNull()
            }
        }.awaitAll()

        if (result.all { it != null }) Result.success()
        else Result.failure()
    }
}

const val MOVIE_IDS = "WORKER_MOVIE_IDS"