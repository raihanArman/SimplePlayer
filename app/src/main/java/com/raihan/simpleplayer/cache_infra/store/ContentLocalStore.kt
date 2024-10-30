package com.raihan.simpleplayer.cache_infra.store

import com.raihan.simpleplayer.cache.store.ContentStore
import com.raihan.simpleplayer.cache.model.LocalContentModel
import com.raihan.simpleplayer.cache.store.insertResult
import com.raihan.simpleplayer.cache_infra.entity.LocalContentEntity
import com.raihan.simpleplayer.cache_infra.database.ContentDao
import com.raihan.simpleplayer.utils.RetrievalResult
import com.raihan.simpleplayer.utils.RetrieveCachedResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * @author Raihan Arman
 * @date 27/10/24
 */
class ContentLocalStore(
    private val dao: ContentDao
): ContentStore {
    override fun retrieve(): Flow<RetrievalResult> = flow {
        val data = dao.load()
        if (data.isNotEmpty()) {
            emit(RetrieveCachedResult.Found(data.map { it.toLocalContentModel() }))
        } else {
            emit(RetrieveCachedResult.Empty)
        }
    }
    override fun insert(data: List<LocalContentModel>): Flow<insertResult> = flow {
        try {
            dao.insert(data.map { it.toEntityModel() })
            emit(null)
        } catch (e: Exception) {
            emit(e)
        }
    }

    override fun isExists(): Flow<Boolean> = flow {
        val isNotEmpty = dao.isTableNotEmpty()
        emit(isNotEmpty)
    }

    private fun LocalContentEntity.toLocalContentModel() = LocalContentModel(
        id = id,
        title = title,
        description = description,
        videoUrl = videoUrl,
        adsUrl = adsUrl,
        thumbUrl = thumbUrl,
    )

    private fun LocalContentModel.toEntityModel() = LocalContentEntity(
        id = id,
        title = title,
        description = description,
        videoUrl = videoUrl,
        adsUrl = adsUrl,
        thumbUrl = thumbUrl,
    )
}