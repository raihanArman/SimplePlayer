package com.raihan.simpleplayer.cache_infra

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * @author Raihan Arman
 * @date 27/10/24
 */
@Dao
interface ContentDao {
    @Query("SELECT * FROM local_content")
    suspend fun load(): List<LocalContentEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: List<LocalContentEntity>)

    @Query("SELECT EXISTS(SELECT 1 FROM local_content)")
    suspend fun isTableNotEmpty(): Boolean
}