package com.enmanuelbergling.ktormovies.data.source.remote.mappers

import com.enmanuelbergling.ktormovies.data.source.remote.dto.actor.ActorDTO
import com.enmanuelbergling.ktormovies.data.source.remote.dto.actor.ActorDetailsDTO
import com.enmanuelbergling.ktormovies.data.source.remote.dto.actor.ActorKnownForDTO
import com.enmanuelbergling.ktormovies.domain.model.actor.Actor
import com.enmanuelbergling.ktormovies.domain.model.actor.ActorDetails
import com.enmanuelbergling.ktormovies.domain.model.actor.ActorKnownFor

internal fun ActorDetailsDTO.toModel() = ActorDetails(
    adult = adult,
    alsoKnownAs = alsoKnownAs,
    biography = biography,
    birthday = birthday,
    deathday = deathday,
    gender = gender,
    homepage = homepage,
    id = id,
    imdbId = imdbId,
    knownForDepartment = knownForDepartment,
    name = name,
    placeOfBirth = placeOfBirth,
    popularity = popularity,
    profilePath = profilePath
)

internal fun ActorKnownForDTO.toModel() = ActorKnownFor(
    adult = adult,
    backdropPath = backdropPath,
    id = id,
    name = name,
    originalLanguage = originalLanguage,
    originalName = originalName,
    overview = overview,
    posterPath = posterPath,
    mediaType = mediaType,
    genreIds = genreIds,
    popularity = popularity,
    firstAirDate = firstAirDate,
    voteAverage = voteAverage,
    voteCount = voteCount,
    originCountry = originCountry,
    title = title,
    originalTitle = originalTitle,
    releaseDate = releaseDate,
    video = video
)

internal fun ActorDTO.toModel() = Actor(
    adult = adult,
    gender = gender,
    id = id,
    knownForDepartment = knownForDepartment,
    name = name,
    originalName = originalName,
    popularity = popularity,
    profilePath = profilePath,
    knownFor = knownFor.map { it.toModel() })