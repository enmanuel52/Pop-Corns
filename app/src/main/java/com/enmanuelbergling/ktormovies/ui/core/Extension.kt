package com.enmanuelbergling.ktormovies.ui.core

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.paging.PagingData
import androidx.paging.PagingDataDiffer
import androidx.paging.compose.LazyPagingItems
import com.enmanuelbergling.ktormovies.ui.theme.Dimen
import kotlinx.coroutines.flow.Flow

typealias Material3 = androidx.compose.material3.MaterialTheme

val Material3.dimen: Dimen
    @Composable
    @ReadOnlyComposable
    get() = LocalDimen.current