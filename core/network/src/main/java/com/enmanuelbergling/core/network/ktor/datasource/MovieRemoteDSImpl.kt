package com.enmanuelbergling.core.network.ktor.datasource

import com.enmanuelbergling.core.domain.datasource.remote.MovieRemoteDS
import com.enmanuelbergling.core.model.core.PageModel
import com.enmanuelbergling.core.model.core.ResultHandler
import com.enmanuelbergling.core.model.movie.Genre
import com.enmanuelbergling.core.model.movie.Movie
import com.enmanuelbergling.core.model.movie.MovieCredits
import com.enmanuelbergling.core.model.movie.MovieDetails
import com.enmanuelbergling.core.network.ktor.service.MovieService
import com.enmanuelbergling.core.network.ktorfit.service.MoviesFilterService
import com.enmanuelbergling.core.network.ktorfit.service.MoviesSearchService
import com.enmanuelbergling.core.network.mappers.toModel

internal class MovieRemoteDSImpl(
    private val service: MovieService,
    private val moviesFilterService: MoviesFilterService,
    private val moviesSearchService: MoviesSearchService,
) : MovieRemoteDS {
    override suspend fun getMovieDetails(id: Int): ResultHandler<MovieDetails> = safeKtorCall {
        service.getMovieDetails(id).toModel()
    }

    override suspend fun getMovieCredits(id: Int): ResultHandler<MovieCredits> = safeKtorCall {
        service.getMovieCredits(id).toModel()
    }

    override suspend fun getNowPlayingMovies(page: Int): ResultHandler<PageModel<Movie>> =
        safeKtorCall {
            val result = service.getNowPlayingMovies(page)
            val movies = result.results.map { it.toModel() }

            PageModel(movies, result.totalPages)
        }

    override suspend fun getUpcomingMovies(page: Int): ResultHandler<PageModel<Movie>> =
        safeKtorCall {
            val result = service.getUpcomingMovies(page)
            val movies = result.results.map { it.toModel() }

            PageModel(movies, result.totalPages)
        }

    override suspend fun getTopRatedMovies(page: Int): ResultHandler<PageModel<Movie>> =
        safeKtorCall {
            val result = service.getTopRatedMovies(page)
            val movies = result.results.map { it.toModel() }

            PageModel(movies, result.totalPages)
        }

    override suspend fun getPopularMovies(page: Int): ResultHandler<PageModel<Movie>> =
        safeKtorCall {
            val result = service.getPopularMovies(page)
            val movies = result.results.map { it.toModel() }

            PageModel(movies, result.totalPages)
        }

    override suspend fun getMovieGenres(): ResultHandler<List<Genre>> = safeKtorCall {
        service.getMovieGenres().genres.map { it.toModel() }
    }

    override suspend fun getMoviesByGenre(
        genres: String,
        sortBy: String,
        page: Int,
    ): ResultHandler<PageModel<Movie>> =
        safeKtorCall {
            val result = moviesFilterService.getMoviesByGenre(genres, sortBy, page)
            val movies = result.results.map { it.toModel() }

            PageModel(movies, result.totalPages)
        }

    override suspend fun searchMovie(query: String, page: Int): ResultHandler<PageModel<Movie>>
    = safeKtorCall {
        val result = moviesSearchService.searchMovie(query, page)
        val movies = result.results.map { it.toModel() }

        PageModel(movies, result.totalPages)
    }
}