package com.enmanuelbergling.feature.series.list.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.enmanuelbergling.feature.series.paging.usecase.GetSectionSeriesUC

internal class AiringTodaySeriesVM(getSectionSeriesUC: GetSectionSeriesUC) : ViewModel() {
    val series = getSectionSeriesUC.getAiringToday().cachedIn(viewModelScope)
}
