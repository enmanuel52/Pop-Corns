package com.enmanuelbergling.core.domain.usecase.user.watchlist

import com.enmanuelbergling.core.domain.datasource.remote.UserRemoteDS

class DeleteMovieFromListUC(private val remoteDS: UserRemoteDS) {
    suspend operator fun invoke(
        movieId: Int,
        listId: Int,
        sessionId: String,
    ) = remoteDS.deleteMovieFromList(movieId = movieId, listId = listId, sessionId = sessionId)
}