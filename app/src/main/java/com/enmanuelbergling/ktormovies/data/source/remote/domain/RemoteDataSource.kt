package com.enmanuelbergling.ktormovies.data.source.remote.domain

import android.util.Log
import com.enmanuelbergling.ktormovies.domain.TAG
import com.enmanuelbergling.ktormovies.domain.model.core.ResultHandler

interface RemoteDataSource {

    suspend fun <T> safeKtorCall(request: suspend () -> T): ResultHandler<T> = try {
        val result = request()
        Log.d(TAG, "safeKtorCall: $result")
        ResultHandler.Success(result)
    } catch (exception: Exception) {
        Log.d(TAG, "safeKtorCall: ${exception.message}")
        ResultHandler.Error(exception)
    }
}