package com.enmanuelbergling.core.testing.data

import com.enmanuelbergling.core.model.movie.Cast
import com.enmanuelbergling.core.model.movie.Crew
import com.enmanuelbergling.core.model.movie.Genre
import com.enmanuelbergling.core.model.movie.Movie
import com.enmanuelbergling.core.model.movie.MovieCredits
import com.enmanuelbergling.core.model.movie.MovieDetails

object FakeMovieData {

    private val GODFATHER = Movie(
        adult = false,
        backdropPath = null,
        genreIds = listOf(),
        id = 0,
        originalLanguage = "English",
        originalTitle = "The Godfather",
        overview = "This is a mafia movie",
        popularity = 9.0,
        posterPath = "godfather_path",
        releaseDate = "",
        title = "The Godfather",
        video = false,
        voteAverage = 8.0,
        voteCount = 2873
    )

    private val GODFATHER_2 = Movie(
        adult = false,
        backdropPath = null,
        genreIds = listOf(),
        id = 0,
        originalLanguage = "English",
        originalTitle = "The Godfather 2",
        overview = "This is a mafia movie, 2nd part",
        popularity = 9.0,
        posterPath = "godfather2_path",
        releaseDate = "",
        title = "The Godfather 2",
        video = false,
        voteAverage = 7.0,
        voteCount = 2673
    )

    val MOVIES = listOf(GODFATHER, GODFATHER_2)

    private val GODFATHER_GENRE = Genre(1, "mafia")

    private val GODFATHER_DETAILS = MovieDetails(
        adult = false,
        id = 0,
        originalLanguage = "English",
        originalTitle = "The Godfather 2",
        overview = "This is a mafia movie, 2nd part",
        popularity = 9.0,
        posterPath = "godfather2_path",
        releaseDate = "",
        title = "The Godfather 2",
        video = false,
        voteAverage = 7.0,
        voteCount = 2673, productionCompanies = listOf(),
        productionCountries = listOf(),
        genres = (1..4).map { GODFATHER_GENRE.copy(id = it) },
        budget = 0,
        belongsToCollection = null,
        backdropPath = "godfather_backdrop",
        homepage = "",
        runtime = 0,
        imdbId = "",
        revenue = 2_000_000,
        spokenLanguages = listOf(),
        status = "",
        tagline = ""
    )

    private val DEFAULT_GENRE = GODFATHER_GENRE

    val GENRES = (1..5).map { DEFAULT_GENRE.copy(id = it) }

    val DEFAULT_MOVIE_DETAILS = GODFATHER_DETAILS

    private val DEFAULT_CAST = Cast(
        adult = false,
        gender = 1,
        id = 0,
        knownForDepartment = "",
        name = "Tim Bergling",
        popularity = 10.0,
        profilePath = "tim_path",
        castId = 0,
        character = "Avicii",
        creditId = "",
        order = 0,
        originalName = "Tim"
    )

    private val DEFAULT_CREW = Crew(
        adult = false,
        gender = 1,
        id = 0,
        knownForDepartment = "",
        name = "Tim Bergling",
        popularity = 10.0,
        profilePath = "tim_path",
        creditId = "",
        originalName = "Tim",
        department = "",
        job = "Dj"
    )

    val DEFAULT_MOVIE_CREDITS = MovieCredits(
        id = 0,
        cast = (1..4).map { DEFAULT_CAST.copy(id = it) },
        crew = (1..4).map { DEFAULT_CREW.copy(id = it) },
    )
}