package com.raihan.simpleplayer.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraphBuilder
import com.raihan.navigation.Destination
import org.koin.androidx.compose.getViewModel
import com.raihan.navigation.composable
import com.raihan.simpleplayer.presentation.splash.SplashEvent
import com.raihan.simpleplayer.utils.BaseImageView

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
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(25.dp),
                horizontalArrangement = Arrangement.spacedBy(25.dp),
                verticalArrangement = Arrangement.spacedBy(25.dp),
                columns = GridCells.Fixed(2),
                state = rememberLazyGridState()
            ) {
                items(state.data!!) {
                    ItemContent(
                        title = it.title,
                        thumbUrl = it.thumbUrl
                    ) { }
                }
            }
        }
    }

}

@Composable
fun ItemContent(
    title: String,
    thumbUrl: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            BaseImageView(
                modifier = Modifier.fillMaxSize(),
                url = thumbUrl
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(color = Color.Black.copy(alpha = 0.5f))
                    .padding(5.dp),
            ) {
                Text(
                    title,
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
        }
    }

}