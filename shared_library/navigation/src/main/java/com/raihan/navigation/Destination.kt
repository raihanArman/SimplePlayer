package com.raihan.navigation

/**
 * @author Raihan Arman
 * @date 28/10/24
 */
sealed class Destination(protected val route: String, vararg params: String) {
    val fullRoute: String = if (params.isEmpty()) route else {
        val builder = StringBuilder(route)
        params.forEach { builder.append("/{$it}") }
        builder.toString()
    }

    sealed class NoArgumentsDestination(route: String) : Destination(route) {
        operator fun invoke(): String = route
    }

    data object SplashScreen : NoArgumentsDestination("Splash")
    data object HomeScreen : NoArgumentsDestination("home")
    data object PlayerScreen : NoArgumentsDestination("player",
//        *arrayOf("content_model")
    ) {
        const val CONTENT_MODEL_KEY = "content_model"

        operator fun invoke(
            contentModelJson: String)
        : String = route.appendParams(
//            CONTENT_MODEL_KEY to contentModelJson
        )
    }
}

internal fun String.appendParams(vararg params: Pair<String, Any?>): String {
    val builder = StringBuilder(this)

    params.forEach {
        it.second?.toString()?.let { arg ->
            builder.append("/$arg")
        }
    }

    return builder.toString()
}