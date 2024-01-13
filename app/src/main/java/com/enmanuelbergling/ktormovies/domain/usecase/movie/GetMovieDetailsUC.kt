package com.enmanuelbergling.ktormovies.domain.usecase.movie

import com.enmanuelbergling.ktormovies.data.source.remote.domain.MovieRemoteDS

class GetMovieDetailsUC(
    private val dataSource: MovieRemoteDS
) {
    suspend operator fun invoke(id: Int) = dataSource.getMovieDetails(id)
}