package com.enmanuelbergling.ktormovies.data.source.remote.domain

import com.enmanuelbergling.ktormovies.domain.model.core.ResultHandler

interface RemoteDataSource {

    suspend fun <T> safeKtorCall(request: suspend () -> T): ResultHandler<T> = try {
        val result = request()
        ResultHandler.Success(result)
    } catch (exception: Exception) {
        ResultHandler.Error(exception)
    }
}