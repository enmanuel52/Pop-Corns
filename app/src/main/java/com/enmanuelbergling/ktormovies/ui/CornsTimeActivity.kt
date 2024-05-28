package com.enmanuelbergling.ktormovies.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.enmanuelbergling.core.common.android_util.isOnline
import com.enmanuelbergling.core.model.settings.DarkTheme
import com.enmanuelbergling.core.ui.theme.CornTimeTheme
import org.koin.android.ext.android.inject
import org.koin.core.component.KoinComponent

class CornsTimeActivity : ComponentActivity(), KoinComponent {

    private val viewModel: CornTimeVM by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        setContent {

            val isOnlineState by isOnline.collectAsStateWithLifecycle(initialValue = true)
            val darkTheme by viewModel.darkTheme.collectAsStateWithLifecycle(initialValue = DarkTheme.System)

            val userDetails by viewModel.userDetails.collectAsStateWithLifecycle()

            CornTimeTheme(darkTheme = darkTheme) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val appState = rememberCornTimeAppState(
                        isOnline = isOnlineState,
                        darkTheme = darkTheme
                    )
                    CornsTimeApp(
                        state = appState,
                        userDetails = userDetails,
                    )
                }
            }
        }
    }
}