package com.raihan.simpleplayer.cache_infra

/**
 * @author Raihan Arman
 * @date 27/10/24
 */
data class LocalContentEntity(
    val id: Int,
    val title: String,
    val description: String,
    val videoUrl: String,
    val adsUrl: String
)