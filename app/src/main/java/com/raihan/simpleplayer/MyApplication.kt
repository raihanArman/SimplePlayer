package com.raihan.simpleplayer

import android.app.Application
import com.raihan.simpleplayer.di.cacheModule
import com.raihan.simpleplayer.di.databaseModule
import com.raihan.simpleplayer.di.domainModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

/**
 * @author Raihan Arman
 * @date 27/10/24
 */
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApplication)
            modules(
                listOf(
                    databaseModule,
                    cacheModule,
                    domainModule,
                )
            )
        }
    }
}