package com.raihan.simpleplayer.cache_infra.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author Raihan Arman
 * @date 27/10/24
 */
@Entity(tableName = "local_content")
data class LocalContentEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val videoUrl: String,
    val adsUrl: String,
    val thumbUrl: String,
)