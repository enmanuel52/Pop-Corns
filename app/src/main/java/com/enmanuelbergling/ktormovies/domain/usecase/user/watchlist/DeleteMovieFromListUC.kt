package com.enmanuelbergling.ktormovies.domain.usecase.user.watchlist

import com.enmanuelbergling.ktormovies.data.source.remote.domain.UserRemoteDS
import com.enmanuelbergling.ktormovies.domain.model.core.NetworkException
import com.enmanuelbergling.ktormovies.domain.model.core.ResultHandler

class DeleteMovieFromListUC(private val remoteDS: UserRemoteDS) {
    suspend operator fun invoke(
        movieId: Int,
        listId: Int,
        sessionId: String,
    ) = remoteDS.deleteMovieFromList(movieId = movieId, listId = listId, sessionId = sessionId)
        .let { resultHandler ->
            when (resultHandler) {
                is ResultHandler.Error -> resultHandler
                is ResultHandler.Success -> {
                    if (resultHandler.data?.statusMessage?.contains("success", true) == true) {
                        resultHandler
                    } else {
                        ResultHandler.Error(NetworkException(resultHandler.data?.statusMessage.orEmpty()))
                    }
                }
            }
        }
}