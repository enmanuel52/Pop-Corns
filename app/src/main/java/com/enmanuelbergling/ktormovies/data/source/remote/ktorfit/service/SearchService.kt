package com.enmanuelbergling.ktormovies.data.source.remote.ktorfit.service

import com.enmanuelbergling.ktormovies.data.source.remote.dto.movie.MoviePageDTO
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Query

internal interface SearchService {

    @GET("search/movie")
    suspend fun searchMovie(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("language") language: String = "en-US",
    ): MoviePageDTO
}