package com.raihan.simpleplayer.utils

import com.raihan.simpleplayer.cache.model.LocalContentModel
import com.raihan.simpleplayer.cache_infra.entity.LocalContentEntity
import com.raihan.simpleplayer.domain.model.ContentModel

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
        adsUrl = "adsUrl",
        thumbUrl = "thumbUrl"
    )
)

val localContent = listOf(
    LocalContentModel(
        id = 1,
        title = "title",
        description = "description",
        videoUrl = "videoUrl",
        adsUrl = "adsUrl",
        thumbUrl = "thumbUrl"
    )
)

val entity = listOf(
    LocalContentEntity(
        id = 1,
        title = "title",
        description = "description",
        videoUrl = "videoUrl",
        adsUrl = "adsUrl",
        thumbUrl = "thumbUrl"
    )
)