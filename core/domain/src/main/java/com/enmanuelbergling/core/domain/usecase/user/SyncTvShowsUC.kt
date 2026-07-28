package com.enmanuelbergling.core.domain.usecase.user

interface SyncTvShowsUC {

    /**
     * 2 actions move from watchlist to favorite and delete from watchlist
     * */
    fun watchlist(
        deletedTvShowIds: List<Int>,
        favoriteTvShowIds: List<Int>,
    )

    fun deleteFromFavorite(
        tvShowIds: List<Int>,
    )
}