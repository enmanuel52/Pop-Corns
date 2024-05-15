package com.enmanuelbergling.core.testing.data

import com.enmanuelbergling.core.model.actor.ActorDetails

object FakeActorData {

    val ACTOR_DETAILS = ActorDetails(
        adult = false,
        alsoKnownAs = listOf(),
        biography = "",
        birthday = "",
        deathday = null,
        gender = 1,
        homepage = null,
        id = 0,
        imdbId = "",
        knownForDepartment = "",
        name = "Tim Bergling",
        placeOfBirth = "Sweden",
        popularity = 10.0,
        profilePath = "tim_path"
    )
}