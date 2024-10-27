package com.raihan.simpleplayer.cache_infra

import androidx.room.Database
import androidx.room.RoomDatabase

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