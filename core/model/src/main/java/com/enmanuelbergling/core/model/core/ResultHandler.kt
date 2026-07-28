package com.enmanuelbergling.core.model.core

sealed interface ResultHandler<T> {

    data class Success<T>(val data: T?) : ResultHandler<T>

    data class Error<T>(val exception: NetworkException) : ResultHandler<T>

    fun getOrNull()= when(this){
        is Error<*> -> null
        is Success -> data
    }
}