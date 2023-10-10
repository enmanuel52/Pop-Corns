package com.enmanuelbergling.ktormovies.domain.model.core

sealed interface ResultHandler<T> {

    data class Success<T>(val data: T?) : ResultHandler<T>

    data class Error<T>(val exception: Exception) : ResultHandler<T>
}