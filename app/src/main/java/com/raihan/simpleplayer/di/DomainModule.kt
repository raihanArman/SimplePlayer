package com.raihan.simpleplayer.di

import com.raihan.simpleplayer.cache.usecase.LoadContentLocalUseCase
import com.raihan.simpleplayer.cache.usecase.SaveContentLocalUseCase
import com.raihan.simpleplayer.domain.usecase.LoadContentUseCase
import com.raihan.simpleplayer.domain.usecase.SaveContentUseCase
import org.koin.dsl.module

/**
 * @author Raihan Arman
 * @date 27/10/24
 */
val domainModule = module {
    single<LoadContentUseCase> { LoadContentLocalUseCase(get()) }
    single<SaveContentUseCase> { SaveContentLocalUseCase(get()) }
}