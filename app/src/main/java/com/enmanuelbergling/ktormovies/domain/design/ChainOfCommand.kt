package com.enmanuelbergling.ktormovies.domain.design

interface ChainHandler<T> {

    val nextChainHandler: ChainHandler<T>?

    suspend fun handle(request: T)

    suspend operator fun invoke(request: T) {
        handle(request)
        nextChainHandler?.invoke(request)
    }
}

class CannotHandleException(val msg: String) : Exception(msg)