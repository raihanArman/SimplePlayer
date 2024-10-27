package com.raihan.simpleplayer.presentation.splash

import android.window.SplashScreen
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.koin.androidx.compose.getViewModel

/**
 * @author Raihan Arman
 * @date 27/10/24
 */
@Composable
fun SplashScreen() {
    val viewModel: SplashViewModel = getViewModel()
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onEvent(SplashEvent.SaveContent)
    }

    LaunchedEffect(state.isSuccessful) {
        if (state.isSuccessful) {
            // Navigate to next screen
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Splash Screen")
    }
}