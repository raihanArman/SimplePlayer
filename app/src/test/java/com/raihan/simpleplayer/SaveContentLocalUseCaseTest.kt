package com.raihan.simpleplayer

import app.cash.turbine.test
import com.raihan.simpleplayer.cache.ContentStore
import com.raihan.simpleplayer.cache.LocalContentModel
import com.raihan.simpleplayer.cache.insertResult
import com.raihan.simpleplayer.domain.ContentModel
import com.raihan.simpleplayer.utils.RetrievalResult
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

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

class SaveContentLocalUseCaseTest {
    private val store = mockk<ContentStore>()
    private lateinit var sut: SaveContentLocalUseCase

    @Before
    fun setup() {
        sut = SaveContentLocalUseCase(store)
    }

    @Test
    fun testInsertErrorWithCacheDoesNotExists() = runBlocking {
        val exception = Exception()

        every {
            store.isExists()
        } returns false

        every {
            store.insert(localContent)
        } returns flowOf(exception)

        sut.save(content).test {
            val result = awaitItem()
            assertEquals(exception::class.java, result!!::class.java)

            awaitComplete()
        }

        verify(exactly = 1) {
            store.isExists()
        }

        verify(exactly = 1) {
            store.insert(localContent)
        }

        confirmVerified(store)
    }

    @Test
    fun testInsertSuccessWithCacheDoesNotExists() = runBlocking {
        every {
            store.isExists()
        } returns false

        every {
            store.insert(localContent)
        } returns flowOf(null)

        sut.save(content).test {
            val result = awaitItem()
            assertEquals(null, result)

            awaitComplete()
        }

        verify(exactly = 1) {
            store.isExists()
        }

        verify(exactly = 1) {
            store.insert(localContent)
        }

        confirmVerified(store)
    }
}