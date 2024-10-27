package com.raihan.simpleplayer.utils

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * @author Raihan Arman
 * @date 27/10/24
 */
object DatabaseUtils {
    fun <T : RoomDatabase> createDatabase(
        context: Context,
        klass: Class<T>,
    ): T {
        return Room.databaseBuilder(
            context,
            klass, "MyDatabase.db"
        ).fallbackToDestructiveMigration()
            .build()
    }
}