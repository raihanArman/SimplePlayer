package com.raihan.navigation

import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel

/**
 * @author Raihan Arman
 * @date 28/10/24
 */
class AppNavigatorImpl : AppNavigator {
    override val navigationChannel = Channel<NavigationIntent>(
        capacity = Int.MAX_VALUE,
        onBufferOverflow = BufferOverflow.DROP_LATEST
    )

    override suspend fun navigateBack(route: String?, inclusive: Boolean) {
        navigationChannel.send(
            NavigationIntent.NavigateBack(
                route = route,
                inclusive = inclusive
            )
        )
    }

    override fun tryNavigateBack(route: String?, inclusive: Boolean) {
        navigationChannel.trySend(
            NavigationIntent.NavigateBack(
                route = route,
                inclusive = inclusive
            )
        )
    }

    override suspend fun navigateTo(
        route: String,
        popUpToRoute: String?,
        inclusive: Boolean,
        isSingleTop: Boolean
    ) {
        navigationChannel.send(
            NavigationIntent.NavigateTo(
                route = route,
                popUpToRoute = popUpToRoute,
                inclusive = inclusive,
                isSingleTop = isSingleTop,
            )
        )
    }

    override fun tryNavigateTo(
        route: String,
        popUpToRoute: String?,
        inclusive: Boolean,
        isSingleTop: Boolean
    ) {
        navigationChannel.trySend(
            NavigationIntent.NavigateTo(
                route = route,
                popUpToRoute = popUpToRoute,
                inclusive = inclusive,
                isSingleTop = isSingleTop,
            )
        )
    }

    override fun tryNavigateAndReplaceStartRoute(route: String) {
        navigationChannel.trySend(
            NavigationIntent.NavigateAndReplace(route)
        )
    }
}

val LocalNavigationComponent = staticCompositionLocalOf<AppNavigator> {
    error("error provided")
}
