package com.raihan.simpleplayer.domain

/**
 * @author Raihan Arman
 * @date 27/10/24
 */
data class ContentModel(
    val id: Int,
    val title: String,
    val description: String,
    val videoUrl: String,
    val adsUrl: String
)
