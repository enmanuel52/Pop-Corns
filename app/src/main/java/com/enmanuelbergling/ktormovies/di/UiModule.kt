package com.enmanuelbergling.ktormovies.di

import com.enmanuelbergling.ktormovies.ui.screen.detail.DetailsVM
import com.enmanuelbergling.ktormovies.ui.screen.home.HomeVM
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val vmModule = module {
    viewModelOf(::DetailsVM)
    viewModelOf(::HomeVM)
}