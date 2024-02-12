package com.enmanuelbergling.ktormovies.domain.usecase.movie

import com.enmanuelbergling.ktormovies.data.source.remote.domain.MovieRemoteDS

class GetMovieGenresUC(private val remoteDS: MovieRemoteDS) {
    suspend operator fun invoke() = remoteDS.getMovieGenres()
}