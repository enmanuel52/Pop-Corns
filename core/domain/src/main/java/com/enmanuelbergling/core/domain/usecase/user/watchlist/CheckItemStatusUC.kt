package com.enmanuelbergling.core.domain.usecase.user.watchlist

import com.enmanuelbergling.core.domain.datasource.remote.UserRemoteDS

/**
 * check whether the movie is part or not from a list
 * */
class CheckItemStatusUC(private val userRemoteDS: UserRemoteDS) {
    suspend operator fun invoke(
        listId: Int,
        movieId: Int,
    ) = userRemoteDS.checkItemStatus(listId, movieId)
}