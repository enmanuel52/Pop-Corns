package com.enmanuelbergling.ktormovies.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.enmanuelbergling.core.common.android_util.ShortCutModel
import com.enmanuelbergling.core.common.android_util.addDynamicShortCut
import com.enmanuelbergling.core.common.android_util.isOnline
import com.enmanuelbergling.core.common.android_util.removeAllDynamicShortCuts
import com.enmanuelbergling.core.model.settings.DarkTheme
import com.enmanuelbergling.core.ui.model.WatchlistShortcut
import com.enmanuelbergling.core.ui.theme.CornTimeTheme
import com.enmanuelbergling.feature.movies.navigation.navigateToMovieSearch
import com.enmanuelbergling.feature.watchlists.navigation.navigateToListDetailsScreen
import com.enmanuelbergling.ktormovies.R
import org.koin.android.ext.android.inject
import org.koin.core.component.KoinComponent

private const val NO_WATCHLIST = -1

class CornsTimeActivity : ComponentActivity(), KoinComponent {

    private val viewModel: CornTimeVM by inject()

    private val searchMovieShortCutClicked = intent.getStringExtra("search_movie_extra") != null

    private val watchlistShortCutId =
        intent.getIntExtra(getString(R.string.watch_list_id_extra), NO_WATCHLIST)

    private val watchListShortCutName =
        intent.getStringExtra(getString(R.string.watch_list_name_extra)).orEmpty()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        setContent {

            val isOnlineState by isOnline.collectAsStateWithLifecycle(initialValue = true)
            val darkTheme by viewModel.darkTheme.collectAsStateWithLifecycle(initialValue = DarkTheme.System)
            val dynamicColor by viewModel.dynamicColor.collectAsStateWithLifecycle(initialValue = false)

            val userDetails by viewModel.userDetails.collectAsStateWithLifecycle()

            CornTimeTheme(darkTheme = darkTheme, dynamicColor = dynamicColor) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val appState = rememberCornTimeAppState(
                        isOnline = isOnlineState,
                    )
                    CornsTimeApp(
                        state = appState,
                        userDetails = userDetails,
                    )

                    LaunchedEffect(key1 = Unit) {
                        if (searchMovieShortCutClicked) {
                            appState.navController.navigateToMovieSearch()
                        }
                    }

                    LaunchedEffect(key1 = userDetails?.isEmpty) {
                        //we collected at least once
                        if (userDetails != null) {
                            //no user logged
                            if (userDetails!!.isEmpty) {
                                removeAllDynamicShortCuts()
                            } else {
                                if (watchlistShortCutId != NO_WATCHLIST) {
                                    appState.navController.navigateToListDetailsScreen(
                                        listId = watchlistShortCutId,
                                        listName = watchListShortCutName
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}