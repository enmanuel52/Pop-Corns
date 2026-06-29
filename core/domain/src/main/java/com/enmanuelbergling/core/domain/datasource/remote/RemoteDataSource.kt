package com.enmanuelbergling.core.domain.datasource.remote

import com.enmanuelbergling.core.model.core.NetworkException
import com.enmanuelbergling.core.model.core.ResultHandler

interface RemoteDataSource {

    suspend fun <T> safeKtorCall(request: suspend () -> T): ResultHandler<T> = try {
        val result = request()
        ResultHandler.Success(result)
    } catch (exception: NetworkException) {
        if (exception is NetworkException.SerializationException){
            println("Serialization Exception: ${exception.stringMessage}")
        }
        ResultHandler.Error(exception)
    } catch (_: Exception) {
        ResultHandler.Error(NetworkException.DefaultException())
    }
}