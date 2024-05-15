package com.enmanuelbergling.core.network.ktorfit.service

import com.enmanuelbergling.core.network.dto.movie.MoviePageDTO
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Query

internal interface MoviesFilterService {
    /**
     * @param genres when more than one split it by comas
     * */
    @GET("discover/movie")
    suspend fun getMoviesByGenre(
        @Query("with_genres") genres: String,
        @Query("sort_by") sortBy: String,
        @Query("page") page: Int,
    ): MoviePageDTO
}