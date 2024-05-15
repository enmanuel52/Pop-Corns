package com.enmanuelbergling.core.domain.datasource.remote

import com.enmanuelbergling.core.model.core.PageModel
import com.enmanuelbergling.core.model.core.ResultHandler
import com.enmanuelbergling.core.model.movie.Genre
import com.enmanuelbergling.core.model.movie.Movie
import com.enmanuelbergling.core.model.movie.MovieCredits
import com.enmanuelbergling.core.model.movie.MovieDetails

interface MovieRemoteDS : RemoteDataSource {

    suspend fun getMovieDetails(id: Int): ResultHandler<MovieDetails>

    suspend fun getMovieCredits(id: Int): ResultHandler<MovieCredits>

    suspend fun getNowPlayingMovies(page: Int = 1): ResultHandler<PageModel<Movie>>

    suspend fun getUpcomingMovies(page: Int = 1): ResultHandler<PageModel<Movie>>


    suspend fun getTopRatedMovies(page: Int = 1): ResultHandler<PageModel<Movie>>

    suspend fun getPopularMovies(page: Int = 1): ResultHandler<PageModel<Movie>>
    suspend fun getMovieGenres(): ResultHandler<List<Genre>>

    suspend fun getMoviesByGenre(
        genres: String,
        sortBy: String,
        page: Int,
    ): ResultHandler<PageModel<Movie>>

    suspend fun searchMovie(
        query: String,
        page: Int,
    ): ResultHandler<PageModel<Movie>>
}