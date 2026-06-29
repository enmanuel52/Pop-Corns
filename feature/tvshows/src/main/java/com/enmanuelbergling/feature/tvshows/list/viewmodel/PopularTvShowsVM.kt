package com.enmanuelbergling.feature.tvshows.list.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.enmanuelbergling.feature.tvshows.paging.usecase.GetSectionTvShowsUC

internal class PopularTvShowsVM(getSectionTvShowsUC: GetSectionTvShowsUC) : ViewModel() {
    val tvShows = getSectionTvShowsUC.getPopular().cachedIn(viewModelScope)
}
