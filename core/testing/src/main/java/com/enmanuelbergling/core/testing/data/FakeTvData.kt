package com.enmanuelbergling.core.testing.data

import com.enmanuelbergling.core.model.movie.Cast
import com.enmanuelbergling.core.model.movie.Crew
import com.enmanuelbergling.core.model.movie.Genre
import com.enmanuelbergling.core.model.tv.Episode
import com.enmanuelbergling.core.model.tv.EpisodeDetails
import com.enmanuelbergling.core.model.tv.Season
import com.enmanuelbergling.core.model.tv.SeasonDetails
import com.enmanuelbergling.core.model.tv.TvAccountStates
import com.enmanuelbergling.core.model.tv.TvShow
import com.enmanuelbergling.core.model.tv.TvShowDetails

object FakeTvData {

    private val BREAKING_BAD = TvShow(
        id = 1,
        name = "Breaking Bad",
        originalName = "Breaking Bad",
        overview = "A chemistry teacher turns to cooking meth.",
        posterPath = "breaking_bad_poster",
        backdropPath = "breaking_bad_backdrop",
        genreIds = listOf(18),
        originalLanguage = "en",
        popularity = 9.0,
        firstAirDate = "2008-01-20",
        voteAverage = 8.9,
        voteCount = 12000,
    )

    private val THE_WIRE = TvShow(
        id = 2,
        name = "The Wire",
        originalName = "The Wire",
        overview = "Baltimore drug scene seen through eyes of law enforcement.",
        posterPath = "the_wire_poster",
        backdropPath = "the_wire_backdrop",
        genreIds = listOf(80),
        originalLanguage = "en",
        popularity = 8.5,
        firstAirDate = "2002-06-02",
        voteAverage = 8.6,
        voteCount = 5000,
    )

    val TV_SHOWS = listOf(BREAKING_BAD, THE_WIRE)

    private val DEFAULT_GENRE = Genre(1, "Drama")

    private val DEFAULT_SEASON = Season(
        id = 10,
        seasonNumber = 1,
        name = "Season 1",
        overview = "The first season.",
        posterPath = "season_1_poster",
        episodeCount = 7,
        airDate = "2008-01-20",
        voteAverage = 8.0,
    )

    val DEFAULT_TV_SHOW_DETAILS = TvShowDetails(
        id = 1,
        name = "Breaking Bad",
        originalName = "Breaking Bad",
        overview = "A chemistry teacher turns to cooking meth.",
        posterPath = "breaking_bad_poster",
        backdropPath = "breaking_bad_backdrop",
        genres = (1..3).map { DEFAULT_GENRE.copy(id = it) },
        firstAirDate = "2008-01-20",
        lastAirDate = "2013-09-29",
        numberOfSeasons = 5,
        numberOfEpisodes = 62,
        status = "Ended",
        tagline = "Remember my name",
        voteAverage = 8.9,
        voteCount = 12000,
        seasons = (1..5).map { DEFAULT_SEASON.copy(id = 10 + it, seasonNumber = it) },
    )

    private val DEFAULT_EPISODE = Episode(
        id = 100,
        episodeNumber = 1,
        seasonNumber = 1,
        name = "Pilot",
        overview = "Walter White starts cooking.",
        stillPath = "pilot_still",
        airDate = "2008-01-20",
        runtime = 58,
        voteAverage = 8.2,
        voteCount = 300,
    )

    val DEFAULT_SEASON_DETAILS = SeasonDetails(
        id = 10,
        seasonNumber = 1,
        name = "Season 1",
        overview = "The first season.",
        posterPath = "season_1_poster",
        airDate = "2008-01-20",
        episodes = (1..7).map { DEFAULT_EPISODE.copy(id = 100 + it, episodeNumber = it) },
    )

    private val DEFAULT_CAST = Cast(
        adult = false,
        gender = 2,
        id = 0,
        knownForDepartment = "Acting",
        name = "Bryan Cranston",
        originalName = "Bryan Cranston",
        popularity = 10.0,
        profilePath = "cranston_path",
        castId = 0,
        character = "Walter White",
        creditId = "",
        order = 0,
    )

    private val DEFAULT_CREW = Crew(
        adult = false,
        gender = 2,
        id = 0,
        knownForDepartment = "Directing",
        name = "Vince Gilligan",
        originalName = "Vince Gilligan",
        popularity = 9.0,
        profilePath = "gilligan_path",
        creditId = "",
        department = "Directing",
        job = "Director",
    )

    val DEFAULT_EPISODE_DETAILS = EpisodeDetails(
        id = 100,
        episodeNumber = 1,
        seasonNumber = 1,
        name = "Pilot",
        overview = "Walter White starts cooking.",
        stillPath = "pilot_still",
        airDate = "2008-01-20",
        runtime = 58,
        voteAverage = 8.2,
        voteCount = 300,
        guestStars = (1..4).map { DEFAULT_CAST.copy(id = it) },
        crew = (1..4).map { DEFAULT_CREW.copy(id = it) },
    )

    val DEFAULT_TV_ACCOUNT_STATES = TvAccountStates(
        id = 1,
        favorite = false,
        watchlist = false,
    )
}
