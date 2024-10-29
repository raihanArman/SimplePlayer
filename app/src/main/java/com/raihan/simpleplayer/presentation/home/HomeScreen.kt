package com.raihan.simpleplayer.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import org.koin.androidx.compose.getViewModel
import com.raihan.navigation.composable
import com.raihan.simpleplayer.presentation.splash.SplashEvent

/**
 * @author Raihan Arman
 * @date 27/10/24
 */
fun NavGraphBuilder.homeNavigation() = run {
    composable(Destination.HomeScreen) {
        HomeScreen()
    }
}

@Composable
fun HomeScreen() {
    val viewModel: HomeViewModel = getViewModel()
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        println("Ampas kuda -> hit")
        viewModel.onEvent(HomeEvent.LoadContent)
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }

        if (state.data != null) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(state.data!!) {
                    Text("${it.title}")
                }
            }
        }
    }

}