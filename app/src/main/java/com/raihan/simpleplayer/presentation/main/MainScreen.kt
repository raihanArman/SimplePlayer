package com.raihan.simpleplayer.presentation.main

import android.app.Activity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.raihan.navigation.AppNavigatorImpl
import com.raihan.navigation.Destination
import com.raihan.navigation.LocalNavigationComponent
import com.raihan.navigation.NavHostApp
import com.raihan.navigation.NavigationIntent
import com.raihan.simpleplayer.presentation.home.homeNavigation
import com.raihan.simpleplayer.presentation.splash.splashNavigation
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

/**
 * @author Raihan Arman
 * @date 28/10/24
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainScreen(modifier: Modifier = Modifier) {
//    val appNavigator = koinInject<AppNavigator>()
//    val navigationChannel = appNavigator.navigationChannel
    val navigationComponent = remember { AppNavigatorImpl() }
    CompositionLocalProvider(
        LocalNavigationComponent provides navigationComponent
    ) {
        val navController = rememberNavController()
        NavigationEffects(
            navigationChannel = navigationComponent.navigationChannel,
            navHostController = navController
        )
        NavHostApp(
            navController = navController,
            startDestination = Destination.SplashScreen
        ) {
            splashNavigation()
            homeNavigation()
        }
    }
}

@Composable
fun NavigationEffects(
    navigationChannel: Channel<NavigationIntent>,
    navHostController: NavHostController
) {
    val activity = (LocalContext.current as? Activity)
    LaunchedEffect(activity, navHostController, navigationChannel) {
        navigationChannel.receiveAsFlow().collect { intent ->
            if (activity?.isFinishing == true) {
                return@collect
            }
            when (intent) {
                is NavigationIntent.NavigateBack -> {
                    if (intent.route != null) {
                        navHostController.popBackStack(intent.route!!, intent.inclusive)
                    } else {
                        navHostController.popBackStack()
                    }
                }
                is NavigationIntent.NavigateTo -> {
                    navHostController.navigate(intent.route) {
                        launchSingleTop = intent.isSingleTop
                        intent.popUpToRoute?.let { popUpToRoute ->
                            popUpTo(popUpToRoute) {
                                inclusive = intent.inclusive
                            }
                        }
                    }
                }
                is NavigationIntent.NavigateAndReplace -> {
                    navHostController.apply {
                        popBackStack(graph.startDestinationId, true)
                        graph.setStartDestination(intent.route)
                        navigate(intent.route)
                    }
                }
            }
        }
    }
}
