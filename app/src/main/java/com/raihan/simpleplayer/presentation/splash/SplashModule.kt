package com.raihan.simpleplayer.presentation.splash

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


/**
 * @author Raihan Arman
 * @date 27/10/24
 */
val splashModule = module {
    viewModel {
        SplashViewModel(get())
    }
}