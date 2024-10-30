package com.raihan.simpleplayer.domain.model

/**
 * @author Raihan Arman
 * @date 27/10/24
 */
data class ContentModel(
    val id: Int,
    val title: String,
    val description: String,
    val videoUrl: String,
    val adsUrl: String,
    val thumbUrl: String,
)
