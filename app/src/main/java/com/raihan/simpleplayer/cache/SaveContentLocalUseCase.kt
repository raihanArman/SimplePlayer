package com.raihan.simpleplayer.cache

import com.raihan.simpleplayer.domain.ContentModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * @author Raihan Arman
 * @date 27/10/24
 */
class SaveContentLocalUseCase(
    private val store: ContentStore
) {
    fun save(model: List<ContentModel>): Flow<Exception?> = flow {
        if (!store.isExists()) {
            store.insert(model.map { it.toLocalContentModel() }).collect { exception ->
                if (exception != null) {
                    emit(exception)
                } else {
                    emit(null)
                }
            }
        } else {
            emit(null)
        }
    }

    private fun ContentModel.toLocalContentModel() = LocalContentModel(
        id = id,
        title = title,
        description = description,
        videoUrl = videoUrl,
        adsUrl = adsUrl
    )
}