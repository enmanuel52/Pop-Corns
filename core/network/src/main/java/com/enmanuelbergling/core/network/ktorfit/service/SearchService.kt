package com.enmanuelbergling.core.network.ktorfit.service

import com.enmanuelbergling.core.network.dto.movie.MoviePageDTO
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Query

internal interface SearchService {

    @GET("search/movie")
    suspend fun searchMovie(
        @Query("query") query: String,
        @Query("page") page: Int,
    ): MoviePageDTO
}