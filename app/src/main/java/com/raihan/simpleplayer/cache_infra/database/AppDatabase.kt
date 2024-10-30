package com.raihan.simpleplayer.cache_infra.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.raihan.simpleplayer.cache_infra.entity.LocalContentEntity

/**
 * @author Raihan Arman
 * @date 27/10/24
 */
@Database(
    entities = [
        LocalContentEntity::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun contentDao(): ContentDao
}