package com.raihan.simpleplayer.domain

import kotlinx.coroutines.flow.Flow

/**
 * @author Raihan Arman
 * @date 27/10/24
 */
interface SaveContentUseCase {
    fun save(model: List<ContentModel>): Flow<Exception?>
}