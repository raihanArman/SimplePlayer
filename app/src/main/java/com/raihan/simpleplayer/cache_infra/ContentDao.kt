package com.raihan.simpleplayer.cache_infra

/**
 * @author Raihan Arman
 * @date 27/10/24
 */
interface ContentDao {
    suspend fun load(): List<LocalContentEntity>
    suspend fun insert(data: List<LocalContentEntity>)
    suspend fun isTableNotEmpty(): Boolean
}