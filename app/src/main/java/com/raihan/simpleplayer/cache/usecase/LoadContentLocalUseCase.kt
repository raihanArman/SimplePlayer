package com.raihan.simpleplayer.cache.usecase

import com.raihan.simpleplayer.cache.model.LocalContentModel
import com.raihan.simpleplayer.cache.store.ContentStore
import com.raihan.simpleplayer.domain.model.ContentModel
import com.raihan.simpleplayer.domain.usecase.LoadContentUseCase
import com.raihan.simpleplayer.utils.LoadCacheResult
import com.raihan.simpleplayer.utils.LoadResult
import com.raihan.simpleplayer.utils.RetrieveCachedResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * @author Raihan Arman
 * @date 27/10/24
 */
class LoadContentLocalUseCase(
    private val store: ContentStore
): LoadContentUseCase {
    override fun load(): Flow<LoadResult> = flow {
        store.retrieve().collect { result ->
            when (result) {
                RetrieveCachedResult.Empty -> {
                    emit(LoadCacheResult.Success(emptyList()))
                }
                is RetrieveCachedResult.Failure -> {
                    emit(LoadCacheResult.Failure(result.exception))
                }
                is RetrieveCachedResult.Found -> {
                    emit(LoadCacheResult.Success(result.data.map {
                        it.toContentModel()
                    }))
                }
            }
        }
    }

    private fun LocalContentModel.toContentModel() = ContentModel(
        id = id,
        title = title,
        description = description,
        videoUrl = videoUrl,
        adsUrl = adsUrl,
        thumbUrl = thumbUrl
    )
}