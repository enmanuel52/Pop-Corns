package com.enmanuelbergling.core.testing.data

import com.enmanuelbergling.core.model.user.UserDetails
import com.enmanuelbergling.core.model.user.WatchList

object FakeUserData {

    val FAKE_USER = UserDetails(username = "timAsshole", name = "Tim", avatarPath = "tim_avatar")

    val FAKE_WATCH_LIST = WatchList(
        description = "A few comedies to chill",
        favoriteCount = 1,
        id = 0,
        itemCount = 4,
        iso6391 = "",
        listType = "",
        name = "comedy",
        posterPath = null
    )
}