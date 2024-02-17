package com.enmanuelbergling.ktormovies.data.source.remote.domain

import android.util.Log
import com.enmanuelbergling.ktormovies.domain.TAG
import com.enmanuelbergling.ktormovies.domain.model.core.NetworkException
import com.enmanuelbergling.ktormovies.domain.model.core.ResultHandler

interface RemoteDataSource {

    suspend fun <T> safeKtorCall(request: suspend () -> T): ResultHandler<T> = try {
        val result = request()
        Log.d(TAG, "safeKtorCall: $result")
        ResultHandler.Success(result)
    } catch (exception: NetworkException) {
        ResultHandler.Error(exception)
    } catch (exception: Exception) {
        ResultHandler.Error(NetworkException.DefaultException)
    }
}