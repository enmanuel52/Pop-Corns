package com.enmanuelbergling.core.network.mappers

import com.enmanuelbergling.core.model.tv.Episode
import com.enmanuelbergling.core.model.tv.EpisodeDetails
import com.enmanuelbergling.core.model.tv.Season
import com.enmanuelbergling.core.model.tv.SeasonDetails
import com.enmanuelbergling.core.model.tv.TvAccountStates
import com.enmanuelbergling.core.model.tv.TvShow
import com.enmanuelbergling.core.model.tv.TvShowDetails
import com.enmanuelbergling.core.network.dto.tv.EpisodeDTO
import com.enmanuelbergling.core.network.dto.tv.EpisodeDetailsDTO
import com.enmanuelbergling.core.network.dto.tv.SeasonDTO
import com.enmanuelbergling.core.network.dto.tv.SeasonDetailsDTO
import com.enmanuelbergling.core.network.dto.tv.TvAccountStatesDTO
import com.enmanuelbergling.core.network.dto.tv.TvShowDTO
import com.enmanuelbergling.core.network.dto.tv.TvShowDetailsDTO

internal fun TvShowDTO.toModel() = TvShow(
    id = id,
    name = name,
    originalName = originalName,
    overview = overview,
    posterPath = posterPath,
    backdropPath = backdropPath,
    genreIds = genreIds,
    originalLanguage = originalLanguage,
    popularity = popularity,
    firstAirDate = firstAirDate,
    voteAverage = voteAverage,
    voteCount = voteCount,
)

internal fun TvShowDetailsDTO.toModel() = TvShowDetails(
    id = id,
    name = name,
    originalName = originalName,
    overview = overview,
    posterPath = posterPath,
    backdropPath = backdropPath,
    genres = genres.map { it.toModel() },
    firstAirDate = firstAirDate,
    lastAirDate = lastAirDate,
    numberOfSeasons = numberOfSeasons,
    numberOfEpisodes = numberOfEpisodes,
    status = status,
    tagline = tagline,
    voteAverage = voteAverage,
    voteCount = voteCount,
    seasons = seasons.map { it.toModel() },
)

internal fun SeasonDTO.toModel() = Season(
    id = id,
    seasonNumber = seasonNumber,
    name = name,
    overview = overview,
    posterPath = posterPath,
    episodeCount = episodeCount,
    airDate = airDate,
    voteAverage = voteAverage,
)

internal fun SeasonDetailsDTO.toModel() = SeasonDetails(
    id = id,
    seasonNumber = seasonNumber,
    name = name,
    overview = overview,
    posterPath = posterPath,
    airDate = airDate,
    episodes = episodes.map { it.toModel() },
)

internal fun EpisodeDTO.toModel() = Episode(
    id = id,
    episodeNumber = episodeNumber,
    seasonNumber = seasonNumber,
    name = name,
    overview = overview,
    stillPath = stillPath,
    airDate = airDate,
    runtime = runtime,
    voteAverage = voteAverage,
    voteCount = voteCount,
)

internal fun EpisodeDetailsDTO.toModel() = EpisodeDetails(
    id = id,
    episodeNumber = episodeNumber,
    seasonNumber = seasonNumber,
    name = name,
    overview = overview,
    stillPath = stillPath,
    airDate = airDate,
    runtime = runtime,
    voteAverage = voteAverage,
    voteCount = voteCount,
    guestStars = guestStars.map { it.toModel() },
    crew = crew.map { it.toModel() },
)

internal fun TvAccountStatesDTO.toModel() = TvAccountStates(
    id = id,
    favorite = favorite,
    watchlist = watchlist,
)
