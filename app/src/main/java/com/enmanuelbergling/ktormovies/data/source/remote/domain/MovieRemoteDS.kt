package com.enmanuelbergling.ktormovies.data.source.remote.domain

import com.enmanuelbergling.ktormovies.data.source.remote.dto.MovieCreditsDTO
import com.enmanuelbergling.ktormovies.domain.model.MovieCredits
import com.enmanuelbergling.ktormovies.domain.model.MovieDetails
import com.enmanuelbergling.ktormovies.domain.model.core.ResultHandler

interface MovieRemoteDS : RemoteDataSource {

    suspend fun getMovieDetails(id: Int): ResultHandler<MovieDetails>

    suspend fun getMovieCredits(id: Int): ResultHandler<MovieCredits>
}