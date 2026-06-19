package com.enmanuelbergling.feature.favorites.di

import com.enmanuelbergling.feature.favorites.home.FavoriteMoviesVM
import com.enmanuelbergling.feature.favorites.paging.GetPaginatedFavoriteMoviesUC
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val favoritesModule = module {
    singleOf(::GetPaginatedFavoriteMoviesUC)

    viewModelOf(::FavoriteMoviesVM)
}
