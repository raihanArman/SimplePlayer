package com.raihan.simpleplayer.domain

import com.raihan.simpleplayer.utils.LoadResult
import kotlinx.coroutines.flow.Flow

/**
 * @author Raihan Arman
 * @date 27/10/24
 */
interface LoadContentUseCase {
    fun load(): Flow<LoadResult>
}