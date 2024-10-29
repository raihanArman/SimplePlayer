package com.raihan.navigation

import org.koin.dsl.module

/**
 * @author Raihan Arman
 * @date 28/10/24
 */
object NavigationModule {
    val navigatorModule = module {
        single<AppNavigator> { AppNavigatorImpl() }
    }
}
