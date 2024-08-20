package com.enmanuelbergling.feature.movies.list.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.enmanuelbergling.feature.movies.paging.usecase.GetSectionMoviesUC

internal class UpcomingMoviesVM(getSectionMoviesUC: GetSectionMoviesUC) : ViewModel() {
    val movies = getSectionMoviesUC.getUpcoming().cachedIn(viewModelScope)
}