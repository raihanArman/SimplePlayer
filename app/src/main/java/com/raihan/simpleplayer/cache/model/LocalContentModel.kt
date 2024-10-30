package com.raihan.simpleplayer.cache.model

/**
 * @author Raihan Arman
 * @date 27/10/24
 */
data class LocalContentModel(
    val id: Int,
    val title: String,
    val description: String,
    val videoUrl: String,
    val adsUrl: String,
    val thumbUrl: String,
)
