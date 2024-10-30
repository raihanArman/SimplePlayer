package com.raihan.simpleplayer.presentation.splash

import android.window.SplashScreen
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import com.raihan.navigation.Destination
import com.raihan.navigation.LocalNavigationComponent
import org.koin.androidx.compose.getViewModel
import com.raihan.navigation.composable

/**
 * @author Raihan Arman
 * @date 27/10/24
 */
fun NavGraphBuilder.splashNavigation() = run {
    composable(Destination.SplashScreen) {
        SplashScreen()
    }
}

@Composable
fun SplashScreen() {
    val viewModel: SplashViewModel = getViewModel()
    val state by viewModel.uiState.collectAsState()
    val navigator = LocalNavigationComponent.current

    LaunchedEffect(Unit) {
        viewModel.onEvent(SplashEvent.SaveContent)
    }

    LaunchedEffect(state.isSuccessful) {
        if (state.isSuccessful) {
            navigator.tryNavigateAndReplaceStartRoute(Destination.HomeScreen())
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}