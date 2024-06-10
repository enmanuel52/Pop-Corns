package com.enmanuelbergling.core.domain.design

interface ChainHandler<T> {

    val nextChainHandler: ChainHandler<T>?

    suspend fun handle(request: T)

    suspend operator fun invoke(request: T) {
        handle(request)
        nextChainHandler?.invoke(request)
    }
}

/**
 * This way is more flexible:
 * request goes through every handler, and next handler is mutable
 * */
interface NewChainHandler<T> {

    var nextChainHandler: NewChainHandler<T>?

    suspend fun handle(request: T): T

    suspend operator fun invoke(request: T) {
        val updatedRequest = handle(request)
        nextChainHandler?.invoke(updatedRequest)
    }
}

class CannotHandleException(val msg: String) : Exception(msg)