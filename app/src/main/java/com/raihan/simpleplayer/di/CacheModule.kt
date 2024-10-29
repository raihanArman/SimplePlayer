package com.raihan.simpleplayer.di

import com.raihan.simpleplayer.cache.ContentStore
import com.raihan.simpleplayer.cache_infra.ContentLocalStore
import org.koin.dsl.module

/**
 * @author Raihan Arman
 * @date 27/10/24
 */
val cacheModule = module {
    single<ContentStore> { ContentLocalStore(get()) }
}