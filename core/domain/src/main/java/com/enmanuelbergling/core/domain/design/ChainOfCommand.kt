package com.enmanuelbergling.core.domain.design

/**
 * This way is more flexible:
 * request goes through every handler, and next handler is mutable
 * */
interface ChainHandler<T> {

    var nextChainHandler: ChainHandler<T>?

    suspend fun handle(request: T): T

    suspend operator fun invoke(request: T) {
        val updatedRequest = handle(request)
        nextChainHandler?.invoke(updatedRequest)
    }
}

class CannotHandleException(val msg: String) : Exception(msg)