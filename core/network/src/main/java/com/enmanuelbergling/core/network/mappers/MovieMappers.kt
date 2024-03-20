package com.enmanuelbergling.core.network.mappers

import com.enmanuelbergling.core.network.dto.movie.BelongsToCollectionDTO
import com.enmanuelbergling.core.network.dto.movie.CastDTO
import com.enmanuelbergling.core.network.dto.movie.CrewDTO
import com.enmanuelbergling.core.network.dto.movie.GenreDTO
import com.enmanuelbergling.core.network.dto.movie.MovieCreditsDTO
import com.enmanuelbergling.core.network.dto.movie.MovieDTO
import com.enmanuelbergling.core.network.dto.movie.MovieDetailsDTO
import com.enmanuelbergling.core.network.dto.movie.ProductionCompanyDTO
import com.enmanuelbergling.core.network.dto.movie.ProductionCountryDTO
import com.enmanuelbergling.core.network.dto.movie.SpokenLanguageDTO
import com.enmanuelbergling.core.model.movie.BelongsToCollection
import com.enmanuelbergling.core.model.movie.Cast
import com.enmanuelbergling.core.model.movie.Crew
import com.enmanuelbergling.core.model.movie.Genre
import com.enmanuelbergling.core.model.movie.Movie
import com.enmanuelbergling.core.model.movie.MovieCredits
import com.enmanuelbergling.core.model.movie.MovieDetails
import com.enmanuelbergling.core.model.movie.ProductionCompany
import com.enmanuelbergling.core.model.movie.ProductionCountry
import com.enmanuelbergling.core.model.movie.SpokenLanguage

internal fun BelongsToCollectionDTO.toModel() = BelongsToCollection(
    id = id,
    name = name,
    posterPath = posterPath,
    backdropPath = backdropPath
)

internal fun GenreDTO.toModel() = Genre(id, name)

internal fun MovieDTO.toModel() = Movie(
    adult = adult,
    backdropPath = backdropPath,
    genreIds = genreIds,
    id = id,
    originalLanguage = originalLanguage,
    originalTitle = originalTitle,
    overview = overview,
    popularity = popularity,
    posterPath = posterPath.orEmpty(),
    releaseDate = releaseDate,
    title = title,
    video = video,
    voteAverage = voteAverage,
    voteCount = voteCount
)

internal fun MovieDetailsDTO.toModel() = MovieDetails(
    adult = adult,
    backdropPath = backdropPath,
    belongsToCollection = belongsToCollection?.toModel(),
    budget = budget,
    genres = genres.map { it.toModel() },
    homepage = homepage,
    id = id,
    imdbId = imdbId,
    originalLanguage = originalLanguage,
    originalTitle = originalTitle,
    overview = overview,
    popularity = popularity,
    posterPath = posterPath,
    productionCompanies = productionCompanies.map { it.toModel() },
    productionCountries = productionCountries.map { it.toModel() },
    releaseDate = releaseDate,
    revenue = revenue,
    runtime = runtime,
    spokenLanguages = spokenLanguages.map { it.toModel() },
    status = status,
    tagline = tagline,
    title = title,
    video = video,
    voteAverage = voteAverage,
    voteCount = voteCount
)

internal fun ProductionCompanyDTO.toModel() = ProductionCompany(
    id = id,
    logoPath = logoPath,
    name = name,
    originCountry = originCountry
)

internal fun ProductionCountryDTO.toModel() = ProductionCountry(iso31661 = iso31661, name = name)

internal fun SpokenLanguageDTO.toModel() = SpokenLanguage(
    englishName = englishName,
    iso6391 = iso6391,
    name = name
)

internal fun CastDTO.toModel() = Cast(
    adult = adult,
    gender = gender,
    id = id,
    knownForDepartment = knownForDepartment,
    name = name,
    originalName = originalName,
    popularity = popularity,
    profilePath = profilePath,
    castId = castId,
    character = character,
    creditId = creditId,
    order = order
)

internal fun CrewDTO.toModel() = Crew(
    adult = adult,
    gender = gender,
    id = id,
    knownForDepartment = knownForDepartment,
    name = name,
    originalName = originalName,
    popularity = popularity,
    profilePath = profilePath,
    creditId = creditId,
    department = department,
    job = job
)

internal fun MovieCreditsDTO.toModel() =
    MovieCredits(id = id, cast = cast.map { it.toModel() }, crew = crew.map { it.toModel() })