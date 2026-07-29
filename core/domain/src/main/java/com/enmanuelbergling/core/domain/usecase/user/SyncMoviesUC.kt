package com.enmanuelbergling.core.domain.usecase.user

interface SyncMoviesUC {

    /**
     * 2 actions move from watchlist to favorite and delete from watchlist
     * */
    fun watchlist(
        deletedMovieIds: List<Int>,
        favoriteMovieIds: List<Int>,
    )

    fun deleteFromFavorite(
        movieIds: List<Int>,
    )
}