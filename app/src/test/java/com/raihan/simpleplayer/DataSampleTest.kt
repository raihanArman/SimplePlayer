package com.raihan.simpleplayer

import com.raihan.simpleplayer.cache.LocalContentModel
import com.raihan.simpleplayer.domain.ContentModel

/**
 * @author Raihan Arman
 * @date 27/10/24
 */
val content = listOf(
    ContentModel(
        id = 1,
        title = "title",
        description = "description",
        videoUrl = "videoUrl",
        adsUrl = "adsUrl"
    )
)

val localContent = listOf(
    LocalContentModel(
        id = 1,
        title = "title",
        description = "description",
        videoUrl = "videoUrl",
        adsUrl = "adsUrl"
    )
)