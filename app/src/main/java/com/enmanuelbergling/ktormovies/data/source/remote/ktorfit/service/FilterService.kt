package com.enmanuelbergling.ktormovies.data.source.remote.ktorfit.service

import com.enmanuelbergling.ktormovies.BuildConfig
import com.enmanuelbergling.ktormovies.data.source.remote.dto.movie.MoviePageDTO
import com.enmanuelbergling.ktormovies.util.getCurrentLanguage
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Query

internal interface FilterService {
    /**
     * @param genres when more than one split it by comas
     * */
    @GET("discover/movie")
    suspend fun getMoviesByGenre(
        @Query("with_genres") genres: String,
        @Query("sort_by") sortBy: String,
        @Query("page") page: Int,
        @Query("language") language: String = getCurrentLanguage(),
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
    ): MoviePageDTO
}