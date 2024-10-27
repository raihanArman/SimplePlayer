package com.raihan.simpleplayer.cache

import com.raihan.simpleplayer.domain.ContentModel
import com.raihan.simpleplayer.domain.SaveContentUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

/**
 * @author Raihan Arman
 * @date 27/10/24
 */
class SaveContentLocalUseCase(
    private val store: ContentStore
): SaveContentUseCase {
    override fun save(model: List<ContentModel>): Flow<Exception?> = flow {
        val exists = store.isExists().first()

        if (!exists) {
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