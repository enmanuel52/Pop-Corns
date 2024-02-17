package com.enmanuelbergling.ktormovies.data.source.remote.ktorfit.service

import com.enmanuelbergling.ktormovies.BuildConfig
import com.enmanuelbergling.ktormovies.data.source.remote.dto.movie.MoviePageDTO
import com.enmanuelbergling.ktormovies.util.getCurrentLanguage
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Query
import io.ktor.client.statement.HttpResponse

internal interface SearchService {

    @GET("search/movie")
    suspend fun searchMovie(
        @Query("query") query: String,
        @Query("page") page: Int,
    ): MoviePageDTO
}