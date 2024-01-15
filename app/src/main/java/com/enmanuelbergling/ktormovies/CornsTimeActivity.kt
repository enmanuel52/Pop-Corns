package com.enmanuelbergling.ktormovies

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.enmanuelbergling.ktormovies.domain.model.settings.DarkTheme
import com.enmanuelbergling.ktormovies.ui.theme.CornTimeTheme
import com.enmanuelbergling.ktormovies.util.isOnline
import moe.tlaster.precompose.PreComposeApp
import org.koin.compose.KoinContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CornsTimeActivity : ComponentActivity(), KoinComponent {

    private val viewModel: CornTimeVM by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        setContent {
            PreComposeApp {
                KoinContext {

                    val isOnlineState by isOnline.collectAsStateWithLifecycle(initialValue = true)
                    val darkTheme by viewModel.darkTheme.collectAsStateWithLifecycle(initialValue = DarkTheme.System)

                    CornTimeTheme(darkTheme = darkTheme) {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            CornsTimeApp(
                                state = rememberPreCtiAppState(
                                    isOnline = isOnlineState,
                                    darkTheme = darkTheme
                                ),
                                onDarkTheme = viewModel::setDarkTheme
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CornTimeTheme {
        Greeting("Android")
    }
}