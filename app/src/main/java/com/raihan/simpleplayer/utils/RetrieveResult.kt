package com.raihan.simpleplayer.utils

import com.raihan.simpleplayer.cache.model.LocalContentModel

/**
 * @author Raihan Arman
 * @date 27/10/24
 */
sealed class RetrieveCachedResult {
    data object Empty: RetrieveCachedResult()
    data class Found(val data: List<LocalContentModel>): RetrieveCachedResult()
    data class Failure(val exception: Exception): RetrieveCachedResult()
}

typealias RetrievalResult = RetrieveCachedResult