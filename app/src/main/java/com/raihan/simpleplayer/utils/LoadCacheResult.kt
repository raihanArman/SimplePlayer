package com.raihan.simpleplayer.utils

import com.raihan.simpleplayer.domain.ContentModel

/**
 * @author Raihan Arman
 * @date 27/10/24
 */
sealed class LoadCacheResult {
    data class Success(val data: List<ContentModel>): LoadCacheResult()
    data class Failure(val exception: Exception): LoadCacheResult()
}

typealias LoadResult = LoadCacheResult
