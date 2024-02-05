package com.enmanuelbergling.ktormovies.ui.screen.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch

@Composable
fun ListScreen() {
    val snackBarHostState = remember {
        SnackbarHostState()
    }
    val scope = rememberCoroutineScope()

    Scaffold(Modifier.fillMaxSize(), snackbarHost = { SnackbarHost(snackBarHostState) }) {
        Box(modifier = Modifier
            .padding(it)
            .fillMaxSize()) {
            Button(onClick = {
                scope.launch {
                    snackBarHostState.showSnackbar("Button clicked")
                }
            }, modifier = Modifier.align(Alignment.Center)) {
                Text(text = "Click me")
            }
        }
    }
}