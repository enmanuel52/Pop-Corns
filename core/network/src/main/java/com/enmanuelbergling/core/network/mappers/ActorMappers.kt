package com.enmanuelbergling.core.network.mappers

import com.enmanuelbergling.core.network.dto.actor.ActorDTO
import com.enmanuelbergling.core.network.dto.actor.ActorDetailsDTO
import com.enmanuelbergling.core.network.dto.actor.KnownMovieDTO
import com.enmanuelbergling.core.model.actor.Actor
import com.enmanuelbergling.core.model.actor.ActorDetails
import com.enmanuelbergling.core.model.actor.KnownMovie

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

internal fun ActorDTO.toModel() = Actor(
    adult = adult,
    gender = gender,
    id = id,
    knownForDepartment = knownForDepartment,
    name = name,
    originalName = originalName,
    popularity = popularity,
    profilePath = profilePath
)

internal fun KnownMovieDTO.toModel() = KnownMovie(
    id = id,
    posterPath = posterPath,
    title = title,
    voteAverage = voteAverage
)