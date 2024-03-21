package com.enmanuelbergling.ktormovies.ui.screen.movie.details

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.enmanuelbergling.core.model.core.SimplerUi
import com.enmanuelbergling.core.model.movie.MovieDetails
import com.enmanuelbergling.core.model.user.WatchList
import com.enmanuelbergling.core.ui.components.HandleUiState
import com.enmanuelbergling.core.ui.components.RatingStars
import com.enmanuelbergling.core.ui.core.dimen
import com.enmanuelbergling.core.ui.core.isAppending
import com.enmanuelbergling.core.ui.core.isEmpty
import com.enmanuelbergling.core.ui.core.isRefreshing
import com.enmanuelbergling.ktormovies.R
import com.enmanuelbergling.ktormovies.domain.BASE_BACKDROP_IMAGE_URL
import com.enmanuelbergling.ktormovies.ui.screen.movie.components.ActorCard
import com.enmanuelbergling.ktormovies.ui.screen.movie.components.ActorsRowPlaceholder
import com.enmanuelbergling.ktormovies.ui.screen.movie.details.model.MovieDetailsUiData
import com.enmanuelbergling.ktormovies.ui.screen.movie.details.model.PersonUiItem
import com.enmanuelbergling.ktormovies.ui.screen.movie.details.model.toPersonUi
import com.enmanuelbergling.ktormovies.ui.screen.watchlist.components.WatchListCard
import kotlinx.coroutines.launch
import moe.tlaster.precompose.koin.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailsScreen(id: Int, onActor: (actorId: Int) -> Unit, onBack: () -> Unit) {

    val viewModel = koinViewModel<MovieDetailsVM> { parametersOf(id) }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val watchList = viewModel.watchlists.collectAsLazyPagingItems()
    val withinListsState by viewModel.withinListsState.collectAsStateWithLifecycle()

    val uiData by viewModel.uiDataState.collectAsStateWithLifecycle()

    LaunchedEffect(watchList.isEmpty) {
        if (!watchList.isEmpty && !watchList.isAppending && !watchList.isRefreshing) {
            viewModel.checkMovieOnLists(watchList.itemSnapshotList.items)
        }
    }

    MovieDetailsScreen(
        uiData = uiData,
        uiState = uiState,
        hasWatchList = !watchList.isEmpty,
        onActor = onActor,
        onBack = onBack,
        onRetry = viewModel::loadPage
    ) {
        SheetContent(
            watchList = watchList,
            withinListsState = withinListsState,
            details = uiData.details,
            onAddToMovieList = viewModel::addMovieToList
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MovieDetailsScreen(
    uiData: MovieDetailsUiData,
    uiState: SimplerUi,
    hasWatchList: Boolean,
    onActor: (actorId: Int) -> Unit,
    onBack: () -> Unit,
    onRetry: () -> Unit,
    watchListsSheet: @Composable () -> Unit,
) {

    val (details, creditsState) = uiData

    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = SheetState(false, SheetValue.PartiallyExpanded)
    )

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    HandleUiState(
        uiState = uiState,
        snackState = scaffoldState.snackbarHostState,
        onRetry,
        getFocus = details == null
    )

    BottomSheetScaffold(
        snackbarHost = { SnackbarHost(scaffoldState.snackbarHostState) },
        scaffoldState = scaffoldState,
        sheetContent = {
            watchListsSheet()
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth()
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.small),
                modifier = Modifier
                    .fillMaxSize()
            ) {

                details?.let {
                    detailsImage(backdropUrl = BASE_BACKDROP_IMAGE_URL + details.backdropPath)

                    information(
                        details.title,
                        details.releaseYear,
                        details.voteAverage.toFloat(),
                        details.formattedGenres,
                        details.duration
                    )

                    if (hasWatchList) {
                        addToListButton {
                            scope.launch {
                                scaffoldState.bottomSheetState.expand()
                            }
                        }
                    }


                    overview(details.overview)

                    persons(
                        title = context.getString(R.string.cast),
                        persons = creditsState?.cast.orEmpty().map { it.toPersonUi() }.distinct(),
                        isLoading = uiState == SimplerUi.Loading && creditsState == null,
                        onActor = onActor
                    )

                    persons(
                        title = context.getString(R.string.crew),
                        persons = creditsState?.crew.orEmpty().map { it.toPersonUi() }.distinct(),
                        isLoading = uiState == SimplerUi.Loading && creditsState == null,
                        onActor = onActor
                    )
                }

            }

            BoxWithConstraints(
                modifier = Modifier
                    .align(Alignment.TopStart)
            ) {
                Box(
                    modifier = Modifier
                        .size(maxWidth)
                        .blur(MaterialTheme.dimen.veryLarge, BlurredEdgeTreatment(CircleShape))
                )

                IconButton(
                    onClick = onBack,
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBackIosNew,
                        contentDescription = stringResource(id = R.string.back_icon)
                    )
                }
            }
        }
    }

}

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
private fun SheetContent(
    watchList: LazyPagingItems<WatchList>,
    withinListsState: List<WatchList>,
    details: MovieDetails?,
    onAddToMovieList: (movieId: Int, WatchList) -> Unit,
) {
    LazyColumn(contentPadding = PaddingValues(MaterialTheme.dimen.small)) {
        stickyHeader {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.surfaceColorAtElevation(
                            BottomSheetDefaults.Elevation
                        )
                    )
                    .padding(vertical = MaterialTheme.dimen.small),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.watch_lists),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        items(watchList.itemCount) {
            val list = watchList[it]
            list?.let {
                val listContainerColor by animateColorAsState(
                    targetValue = if (list in withinListsState)
                        MaterialTheme.colorScheme.secondary
                    else MaterialTheme.colorScheme.surface,
                    label = stringResource(R.string.list_background_animation),
                )

                WatchListCard(
                    name = list.name,
                    description = list.description,
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = listContainerColor
                ) {
                    if (list !in withinListsState) {
                        onAddToMovieList(details?.id ?: 0, list)
                    }
                }
            }
        }
    }
}

private fun LazyListScope.addToListButton(
    onClick: () -> Unit,
) {
    item {
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(onClick = onClick) {
                Text(text = stringResource(R.string.add_to_watch_list))
            }
        }
    }
}

private fun LazyListScope.persons(
    title: String,
    persons: List<PersonUiItem>,
    isLoading: Boolean = false,
    onActor: (personId: Int) -> Unit,
) {
    item {
        Column(Modifier.padding(all = MaterialTheme.dimen.small)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(start = MaterialTheme.dimen.small)
            )

            Spacer(modifier = Modifier.height(MaterialTheme.dimen.small))

            if (isLoading && persons.isEmpty()) {
                ActorsRowPlaceholder()
            } else {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.small)) {
                    items(persons) { person ->
                        ActorCard(
                            imageUrl = person.imageUrl,
                            name = person.name,
                            modifier = Modifier.width(110.dp)
                        ) {
                            onActor(person.id)
                        }
                    }
                }
            }
        }
    }
}

private fun LazyListScope.overview(overview: String) {
    item {
        var expanded by remember {
            mutableStateOf(false)
        }

        Column(Modifier.padding(all = MaterialTheme.dimen.small)) {
            Text(
                text = "Overview",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = overview,
                modifier = Modifier
                    .clickable { expanded = !expanded }
                    .animateContentSize(
                        spring(Spring.DampingRatioLowBouncy, Spring.StiffnessLow)
                    ),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = if (expanded) Int.MAX_VALUE else 3,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

private fun LazyListScope.information(
    title: String,
    year: String,
    rating: Float,
    genres: String,
    duration: String,
) {
    item {
        Column(Modifier.padding(MaterialTheme.dimen.small)) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.dimen.small),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    //getting year
                    text = "$title ($year)",
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.SemiBold
                )

                //it comes 10 for server
                RatingStars(value = rating.div(2))
            }

            Text(
                text = "$genres - $duration",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Light
            )
        }
    }
}

private fun LazyListScope.detailsImage(
    backdropUrl: String?,
) {
    item {
        AsyncImage(
            model = backdropUrl,
            contentDescription = stringResource(R.string.poster_image),
            placeholder = painterResource(
                id = R.drawable.pop_corn_and_cinema_backdrop
            ),
            error = painterResource(
                id = R.drawable.pop_corn_and_cinema_backdrop
            ),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .animateContentSize()
                .fillMaxWidth(),
        )
    }
}
