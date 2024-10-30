package com.raihan.simpleplayer.cache.store

import com.raihan.simpleplayer.cache.model.LocalContentModel
import com.raihan.simpleplayer.utils.RetrievalResult
import kotlinx.coroutines.flow.Flow

typealias insertResult = Exception?

interface ContentStore {
    fun retrieve(): Flow<RetrievalResult>
    fun insert(data: List<LocalContentModel>): Flow<insertResult>
    fun isExists(): Flow<Boolean>
}