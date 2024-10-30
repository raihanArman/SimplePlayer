package com.raihan.simpleplayer.di

import com.raihan.simpleplayer.cache_infra.database.AppDatabase
import com.raihan.simpleplayer.utils.DatabaseUtils
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * @author Raihan Arman
 * @date 27/10/24
 */
val databaseModule = module {
    single {
        DatabaseUtils.createDatabase(
            context = androidContext(),
            klass = AppDatabase::class.java
        )
    }

    factory { get<AppDatabase>().contentDao() }
}