package com.enmanuelbergling.core.domain.design

import com.enmanuelbergling.core.model.core.NetworkException

/**
 * This way is more flexible:
 * request goes through every handler, and next handler is mutable
 * */
interface ChainHandler<T> {

    var nextChainHandler: ChainHandler<T>?

    suspend fun handle(request: T): T

    @Throws(CannotHandleException::class)
    suspend operator fun invoke(request: T) {
        val updatedRequest = handle(request)
        nextChainHandler?.invoke(updatedRequest)
    }
}

class CannotHandleException(msg: String, val throwable: Throwable) :
    Exception(msg, throwable)
