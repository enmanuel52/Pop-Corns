package com.enmanuelbergling.feature.series.home

import androidx.annotation.StringRes
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExpandedFullScreenSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarState
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.carousel.HorizontalCenteredHeroCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.enmanuelbergling.core.common.util.BASE_BACKDROP_IMAGE_URL
import com.enmanuelbergling.core.model.SeriesSection
import com.enmanuelbergling.core.model.core.SimplerUi
import com.enmanuelbergling.core.model.tv.TvShow
import com.enmanuelbergling.core.ui.R
import com.enmanuelbergling.core.ui.components.HandleUiState
import com.enmanuelbergling.core.ui.components.blendMode
import com.enmanuelbergling.core.ui.components.common.HeaderMovieTitle
import com.enmanuelbergling.core.ui.components.common.MovieCard
import com.enmanuelbergling.core.ui.components.common.MovieCardPlaceholder
import com.enmanuelbergling.core.ui.components.listItemWindAnimation
import com.enmanuelbergling.core.ui.core.dimen
import com.enmanuelbergling.core.ui.core.isScrollingForward
import com.enmanuelbergling.feature.series.home.model.SuggestionEvent
import com.enmanuelbergling.feature.series.search.ExpandedSearchBarContent
import com.valentinilk.shimmer.shimmer
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeriesScreen(
    onDetails: (id: Int) -> Unit,
    onMore: (SeriesSection) -> Unit,
    onFavorites: () -> Unit,
    onOpenDrawer: () -> Unit,
) {
    val viewModel = koinViewModel<SeriesVM> { parametersOf(false) }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isLoggedIn by viewModel.isLoggedIn.collectAsStateWithLifecycle()

    val uiData by viewModel.uiDataState.collectAsStateWithLifecycle()

    val seriesSearch = viewModel.seriesSearch.collectAsLazyPagingItems()

    val snackBarHostState = remember { SnackbarHostState() }

    val searchBarState = rememberSearchBarState()
    val textFieldState = rememberTextFieldState("")

    LaunchedEffect(textFieldState.text) {
        viewModel.onQueryChange(textFieldState.text.toString())
    }

    HandleUiState(uiState, snackState = snackBarHostState, onRetry = viewModel::loadUi)

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            SeriesTopAppBar(
                onOpenDrawer = onOpenDrawer,
                searchBarState = searchBarState,
                textFieldState = textFieldState,
                onFavorites = onFavorites,
                isLoggedIn = isLoggedIn,
            )
        },
        contentWindowInsets = WindowInsets.statusBars,
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            ExpandedFullScreenSearchBar(
                state = searchBarState,
                inputField = {
                    ExpandedSearchBarInputField(
                        textFieldState = textFieldState,
                        searchBarState = searchBarState,
                        onAddSuggestion = {
                            viewModel.onSuggestionEvent(SuggestionEvent.Add(it))
                        }
                    )
                },
                colors = SearchBarDefaults.colors(
                    dividerColor = Color.Transparent,
                )
            ) {
                ExpandedSearchBarContent(
                    series = seriesSearch,
                    searchSuggestions = uiData.searchSuggestions,
                    searchSuggestionsDeleted = uiData.searchSuggestionsDeleted,
                    onSuggestionEvent = viewModel::onSuggestionEvent,
                    textFieldState = textFieldState,
                    onSeriesDetails = onDetails,
                )
            }

            SeriesList(
                popular = uiData.popular.take(5),
                topRated = uiData.topRated,
                onTheAir = uiData.onTheAir,
                airingToday = uiData.airingToday,
                onDetails = onDetails,
                onMore = onMore,
                isLoading = uiState == SimplerUi.Loading
            )
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun SeriesTopAppBar(
    onOpenDrawer: () -> Unit,
    searchBarState: SearchBarState,
    textFieldState: TextFieldState,
    onFavorites: () -> Unit,
    isLoggedIn: Boolean,
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onOpenDrawer) {
                Icon(
                    painter = painterResource(R.drawable.bars_bottom_left),
                    contentDescription = "Sandwich menu icon"
                )
            }
        },
        title = {
            SearchBar(
                searchBarState,
                inputField = {
                    SearchBarInputField(
                        textFieldState = textFieldState,
                        searchBarState = searchBarState,
                    )
                },
            )
        },
        actions = {
            if (isLoggedIn) {
                IconButton(onClick = onFavorites) {
                    Icon(
                        painter = painterResource(R.drawable.heart_outline),
                        contentDescription = stringResource(R.string.favorites)
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchBarInputField(
    textFieldState: TextFieldState,
    searchBarState: SearchBarState,
) {
    SearchBarDefaults.InputField(
        textFieldState = textFieldState,
        searchBarState = searchBarState,
        onSearch = {},
        placeholder = {
            Text(
                text = stringResource(R.string.series),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExpandedSearchBarInputField(
    textFieldState: TextFieldState,
    searchBarState: SearchBarState,
    onAddSuggestion: (String) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()

    SearchBarDefaults.InputField(
        textFieldState = textFieldState,
        searchBarState = searchBarState,
        onSearch = { query ->
            keyboardController?.hide()
            onAddSuggestion(query)
        },
        leadingIcon = {
            IconButton(onClick = {
                textFieldState.clearText()
                scope.launch { searchBarState.animateToCollapsed() }
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBackIos,
                    contentDescription = stringResource(id = R.string.back_icon)
                )
            }
        },
        trailingIcon = {
            if (textFieldState.text.isNotBlank()) IconButton(onClick = { textFieldState.clearText() }) {
                Icon(
                    imageVector = Icons.Rounded.Clear,
                    contentDescription = stringResource(R.string.clear_string_icon)
                )
            }
        },
        placeholder = {
            Text(text = stringResource(R.string.series))
        },
    )
}

@Composable
private fun SeriesList(
    popular: List<TvShow>,
    topRated: List<TvShow>,
    onTheAir: List<TvShow>,
    airingToday: List<TvShow>,
    modifier: Modifier = Modifier,
    onDetails: (id: Int) -> Unit,
    onMore: (SeriesSection) -> Unit,
    isLoading: Boolean,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.dimen.verySmall),
        contentPadding = WindowInsets.navigationBars.asPaddingValues(),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.small),
    ) {
        headerSeries(popular, onDetails, isLoading) { onMore(SeriesSection.Popular) }

        forYouText()

        seriesSection(
            title = R.string.top_rated,
            series = topRated,
            onDetails = onDetails,
            isLoading = isLoading
        ) { onMore(SeriesSection.TopRated) }

        seriesSection(
            title = R.string.on_the_air,
            series = onTheAir,
            onDetails = onDetails,
            isLoading = isLoading
        ) { onMore(SeriesSection.OnTheAir) }

        seriesSection(
            title = R.string.airing_today,
            series = airingToday,
            onDetails = onDetails,
            isLoading = isLoading
        ) { onMore(SeriesSection.AiringToday) }
    }
}

private fun LazyListScope.seriesSection(
    @StringRes title: Int,
    series: List<TvShow>,
    onDetails: (id: Int) -> Unit,
    isLoading: Boolean,
    onMore: () -> Unit,
) {
    item {
        if (series.isEmpty() && isLoading) {
            Row(Modifier.shimmer()) {
                repeat(5) {
                    MovieCardPlaceholder(
                        modifier = Modifier
                            .padding(start = MaterialTheme.dimen.small)
                            .width(180.dp)
                    )
                }
            }
        } else {
            Column {
                SectionHeader(
                    title = stringResource(title),
                    modifier = Modifier.padding(horizontal = MaterialTheme.dimen.small),
                    onMore = onMore
                )

                Spacer(modifier = Modifier.height(MaterialTheme.dimen.small))

                val listState = rememberLazyListState()
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.small),
                    state = listState
                ) {
                    items(series) { tvShow ->
                        MovieCard(
                            imageUrl = tvShow.posterPath.orEmpty(),
                            title = tvShow.name,
                            rating = tvShow.voteAverage,
                            modifier = Modifier
                                .width(180.dp)
                                .listItemWindAnimation(
                                    listState.isScrollingForward(),
                                    Orientation.Horizontal
                                )
                        ) {
                            onDetails(tvShow.id)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
private fun LazyListScope.headerSeries(
    popular: List<TvShow>,
    onDetails: (id: Int) -> Unit,
    isLoading: Boolean,
    onMore: () -> Unit,
) {
    item {
        if (popular.isEmpty() && isLoading) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                LoadingIndicator(
                    modifier = Modifier
                        .padding(MaterialTheme.dimen.medium),
                )
            }
        } else {
            val carouselState = rememberCarouselState { popular.count() }
            Column {
                SectionHeader(
                    title = stringResource(R.string.popular),
                    modifier = Modifier.padding(horizontal = MaterialTheme.dimen.small),
                    onMore = onMore
                )

                Spacer(modifier = Modifier.height(MaterialTheme.dimen.small))

                HorizontalCenteredHeroCarousel(
                    state = carouselState,
                    itemSpacing = MaterialTheme.dimen.small,
                    maxItemWidth = 300.dp,
                    contentPadding = PaddingValues(horizontal = MaterialTheme.dimen.small)
                ) { page ->
                    val tvShow = popular.getOrNull(page)
                    tvShow?.let {
                        SeriesHeaderCard(
                            tvShow = tvShow,
                            modifier = Modifier.maskClip(CardDefaults.shape)
                        ) {
                            onDetails(tvShow.id)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SeriesHeaderCard(
    tvShow: TvShow,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    ElevatedCard(onClick = onClick, modifier = modifier) {
        Box(contentAlignment = Alignment.BottomStart) {
            AsyncImage(
                model = BASE_BACKDROP_IMAGE_URL + tvShow.backdropPath.orEmpty(),
                contentDescription = "header image",
                placeholder = painterResource(
                    id = R.drawable.pop_corn_and_cinema_backdrop
                ),
                error = painterResource(
                    id = R.drawable.pop_corn_and_cinema_backdrop
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.4f),
                contentScale = ContentScale.Crop
            )

            HeaderMovieTitle(
                title = tvShow.name,
                modifier = Modifier
                    .padding(MaterialTheme.dimen.small)
                    .blendMode(BlendMode.Difference),
            )
        }
    }
}

@Composable
private fun SectionHeader(title: String, modifier: Modifier = Modifier, onMore: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        IconButton(onClick = onMore) {
            Icon(
                painter = painterResource(R.drawable.double_right),
                contentDescription = stringResource(R.string.more_icon)
            )
        }
    }
}

private fun LazyListScope.forYouText() {
    item {
        Text(
            text = stringResource(R.string.for_you),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = MaterialTheme.dimen.small)
        )
    }
}
