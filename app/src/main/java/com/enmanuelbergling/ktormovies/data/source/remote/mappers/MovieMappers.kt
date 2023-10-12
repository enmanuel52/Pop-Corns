package com.enmanuelbergling.ktormovies.data.source.remote.mappers

import com.enmanuelbergling.ktormovies.data.source.remote.dto.BelongsToCollectionDTO
import com.enmanuelbergling.ktormovies.data.source.remote.dto.CastDTO
import com.enmanuelbergling.ktormovies.data.source.remote.dto.CrewDTO
import com.enmanuelbergling.ktormovies.data.source.remote.dto.GenreDTO
import com.enmanuelbergling.ktormovies.data.source.remote.dto.MovieCreditsDTO
import com.enmanuelbergling.ktormovies.data.source.remote.dto.MovieDTO
import com.enmanuelbergling.ktormovies.data.source.remote.dto.MovieDetailsDTO
import com.enmanuelbergling.ktormovies.data.source.remote.dto.ProductionCompanyDTO
import com.enmanuelbergling.ktormovies.data.source.remote.dto.ProductionCountryDTO
import com.enmanuelbergling.ktormovies.data.source.remote.dto.SpokenLanguageDTO
import com.enmanuelbergling.ktormovies.domain.model.BelongsToCollection
import com.enmanuelbergling.ktormovies.domain.model.Cast
import com.enmanuelbergling.ktormovies.domain.model.Crew
import com.enmanuelbergling.ktormovies.domain.model.Genre
import com.enmanuelbergling.ktormovies.domain.model.Movie
import com.enmanuelbergling.ktormovies.domain.model.MovieCredits
import com.enmanuelbergling.ktormovies.domain.model.MovieDetails
import com.enmanuelbergling.ktormovies.domain.model.ProductionCompany
import com.enmanuelbergling.ktormovies.domain.model.ProductionCountry
import com.enmanuelbergling.ktormovies.domain.model.SpokenLanguage

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
    posterPath = posterPath,
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