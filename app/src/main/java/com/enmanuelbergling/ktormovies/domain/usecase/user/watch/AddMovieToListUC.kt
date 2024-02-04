package com.enmanuelbergling.ktormovies.domain.usecase.user.watch

import com.enmanuelbergling.ktormovies.data.source.remote.domain.UserRemoteDS

class AddMovieToListUC(private val remoteDS: UserRemoteDS) {
    suspend operator fun invoke(
        movieId: Int,
        listId: Int,
        sessionId: String,
    ) = remoteDS.addMovieToList(movieId = movieId, listId = listId, sessionId = sessionId)
}