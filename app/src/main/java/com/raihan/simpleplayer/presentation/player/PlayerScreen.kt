package com.raihan.simpleplayer.presentation.player

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import com.raihan.navigation.Destination
import com.raihan.navigation.composable
import com.raihan.simpleplayer.domain.ContentModel
import com.raihan.simpleplayer.presentation.splash.SplashScreen

/**
 * @author Raihan Arman
 * @date 29/10/24
 */
fun NavGraphBuilder.playerNavigation() = run {
    composable(Destination.PlayerScreen) { navBackStackEntry ->
//        val playerModelJson = navBackStackEntry.arguments?.getString(Destination.PlayerScreen.CONTENT_MODEL_KEY)
//        val playerModel = playerModelJson?.fromJsonToPlayerModel()

        PlayerScreen()

    }
}
@Composable
fun PlayerScreen() {
    Box {
       Text("Check")
    }
}