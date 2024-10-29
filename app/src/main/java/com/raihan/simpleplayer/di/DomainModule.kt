package com.raihan.simpleplayer.di

import com.raihan.simpleplayer.cache.LoadContentLocalUseCase
import com.raihan.simpleplayer.cache.SaveContentLocalUseCase
import com.raihan.simpleplayer.domain.LoadContentUseCase
import com.raihan.simpleplayer.domain.SaveContentUseCase
import org.koin.dsl.module

/**
 * @author Raihan Arman
 * @date 27/10/24
 */
val domainModule = module {
    single<LoadContentUseCase> { LoadContentLocalUseCase(get()) }
    single<SaveContentUseCase> { SaveContentLocalUseCase(get()) }
}