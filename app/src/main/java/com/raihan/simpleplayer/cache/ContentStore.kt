package com.raihan.simpleplayer.cache

import com.raihan.simpleplayer.utils.RetrievalResult
import kotlinx.coroutines.flow.Flow

/**
 * @author Raihan Arman
 * @date 27/10/24
 */
interface ContentStore {
    fun retrieve(): Flow<RetrievalResult>
}