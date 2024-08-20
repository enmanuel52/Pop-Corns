package com.enmanuelbergling.feature.movies.list.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.enmanuelbergling.feature.movies.paging.usecase.GetSectionMoviesUC

internal class PopularMoviesVM(getSectionMoviesUC: GetSectionMoviesUC) : ViewModel() {
    val movies = getSectionMoviesUC.getPopular().cachedIn(viewModelScope)
}