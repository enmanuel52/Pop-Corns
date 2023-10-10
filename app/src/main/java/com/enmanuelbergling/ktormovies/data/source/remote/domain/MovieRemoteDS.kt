package com.enmanuelbergling.ktormovies.data.source.remote.domain

import com.enmanuelbergling.ktormovies.domain.model.MovieDetails
import com.enmanuelbergling.ktormovies.domain.model.core.ResultHandler

interface MovieRemoteDS : RemoteDataSource {

    suspend fun getMovieDetails(id: Int): ResultHandler<MovieDetails>
}