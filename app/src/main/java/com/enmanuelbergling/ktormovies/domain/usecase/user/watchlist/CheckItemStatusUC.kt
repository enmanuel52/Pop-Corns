package com.enmanuelbergling.ktormovies.domain.usecase.user.watchlist

import com.enmanuelbergling.ktormovies.data.source.remote.domain.UserRemoteDS

/**
 * check whether the movie is part or not from a list
 * */
class CheckItemStatusUC(private val userRemoteDS: UserRemoteDS) {
    suspend operator fun invoke(
        listId: Int,
        movieId: Int,
    ) = userRemoteDS.checkItemStatus(listId, movieId)
}