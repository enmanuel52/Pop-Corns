package com.enmanuelbergling.core.domain.usecase.user.watchlist

import com.enmanuelbergling.core.domain.datasource.remote.UserRemoteDS

class AddMovieToListUC(private val remoteDS: UserRemoteDS) {
    suspend operator fun invoke(
        movieId: Int,
        listId: Int,
        sessionId: String,
    ) = remoteDS.addMovieToList(movieId = movieId, listId = listId, sessionId = sessionId)
}