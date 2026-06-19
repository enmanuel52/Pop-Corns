package com.enmanuelbergling.core.domain.usecase.user.favorite

import com.enmanuelbergling.core.domain.datasource.remote.UserRemoteDS

class AddMovieToFavoritesUC(private val remoteDS: UserRemoteDS) {

    suspend operator fun invoke(
        movieId: Int,
    ) = remoteDS.addMovieToFavorites(movieId = movieId)
}
