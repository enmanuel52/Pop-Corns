package com.enmanuelbergling.ktormovies.data.source.remote.ktor.service

import com.enmanuelbergling.ktormovies.data.source.remote.dto.movie.MovieCreditsDTO
import com.enmanuelbergling.ktormovies.data.source.remote.dto.movie.MovieDetailsDTO
import com.enmanuelbergling.ktormovies.data.source.remote.dto.movie.MoviePageDTO
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.KtorClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.appendPathSegments

internal class MovieService(private val httpClient: KtorClient) {

    suspend fun getTopRatedMovies(page: Int): MoviePageDTO = httpClient
        .get("movie/top_rated") {
            url {
                parameters.append(name = "page", value = "$page")
            }
        }
        .body()

    suspend fun getMovieDetails(id: Int): MovieDetailsDTO = httpClient
        .get("movie") {
            url {
                appendPathSegments("$id")
            }
        }.body()

    suspend fun getMovieCredits(id: Int): MovieCreditsDTO = httpClient
        .get("movie/$id/credits")
        .body()

    suspend fun getNowPlayingMovies(page: Int): MoviePageDTO = httpClient
        .get("movie/now_playing") {
            url {
                parameters.append(name = "page", value = "$page")
            }
        }.body()

    suspend fun getUpcomingMovies(page: Int): MoviePageDTO = httpClient
        .get("movie/upcoming") {
            url {
                parameters.append(name = "page", value = "$page")
            }
        }.body()

    suspend fun getPopularMovies(page: Int): MoviePageDTO = httpClient
        .get("movie/popular") {
            url {
                parameters.append(name = "page", value = "$page")
            }
        }.body()
}