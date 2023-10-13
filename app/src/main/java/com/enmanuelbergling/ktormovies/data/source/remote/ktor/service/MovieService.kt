package com.enmanuelbergling.ktormovies.data.source.remote.ktor.service

import com.enmanuelbergling.ktormovies.BuildConfig
import com.enmanuelbergling.ktormovies.data.source.remote.dto.MovieCreditsDTO
import com.enmanuelbergling.ktormovies.data.source.remote.dto.MovieDetailsDTO
import com.enmanuelbergling.ktormovies.data.source.remote.dto.MoviePageDTO
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.KtorClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.appendPathSegments

internal class MovieService(private val httpClient: KtorClient) {

    suspend fun getTopRatedMovies(page: Int): MoviePageDTO = httpClient
        .get("movie/top_rated") {
            url {
                parameters.append(name = "page", value = "$page")
                parameters.append(name = "api_key", value = BuildConfig.API_KEY)
            }
        }
        .body()

    suspend fun getMovieDetails(id: Int): MovieDetailsDTO = httpClient
        .get("movie") {
            url {
                appendPathSegments("$id")

                parameters.append(name = "api_key", value = BuildConfig.API_KEY)
            }
        }.body()

    suspend fun getMovieCredits(id: Int): MovieCreditsDTO = httpClient
        .get("movie/$id/credits") {
            url {
                parameters.append(name = "api_key", value = BuildConfig.API_KEY)
            }
        }.body()

    //https://api.themoviedb.org/3/movie/now_playing Now Playing
    suspend fun getNowPlayingMovies(page: Int): MoviePageDTO = httpClient
        .get("movie/now_playing") {
            url {
                parameters.append(name = "api_key", value = BuildConfig.API_KEY)
                parameters.append(name = "page", value = "$page")
            }
        }.body()

    //https://api.themoviedb.org/3/movie/upcoming Coming Soon
    suspend fun getUpcomingMovies(page: Int): MoviePageDTO = httpClient
        .get("movie/upcoming") {
            url {
                parameters.append(name = "api_key", value = BuildConfig.API_KEY)
                parameters.append(name = "page", value = "$page")
            }
        }.body()
}