package com.enmanuelbergling.feature.favorites.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.enmanuelbergling.core.domain.datasource.remote.UserRemoteDS
import com.enmanuelbergling.core.model.movie.Movie
import kotlinx.coroutines.flow.Flow

internal class GetPaginatedFavoriteMoviesUC(private val userRemoteDS: UserRemoteDS) {
    operator fun invoke(): Flow<PagingData<Movie>> = Pager(
        config = PagingConfig(pageSize = 20, enablePlaceholders = false),
        pagingSourceFactory = {
            FavoriteMoviesSource(
                userRemoteDS
            )
        }
    ).flow
}
