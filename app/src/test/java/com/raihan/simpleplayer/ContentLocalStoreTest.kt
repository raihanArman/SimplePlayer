package com.raihan.simpleplayer

import app.cash.turbine.test
import com.raihan.simpleplayer.cache.LocalContentModel
import com.raihan.simpleplayer.cache.insertResult
import com.raihan.simpleplayer.utils.RetrievalResult
import com.raihan.simpleplayer.utils.RetrieveCachedResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * @author Raihan Arman
 * @date 27/10/24
 */
data class LocalContentEntity(
    val id: Int,
    val title: String,
    val description: String,
    val videoUrl: String,
    val adsUrl: String
)

interface ContentDao {
    suspend fun load(): List<LocalContentEntity>
    suspend fun insert(data: List<LocalContentEntity>)
    suspend fun isTableNotEmpty(): Boolean
}

class ContentLocalStore(
    private val dao: ContentDao
) {
    fun retrieve(): Flow<RetrievalResult> = flow {
        val data = dao.load()
        if (data.isNotEmpty()) {
            emit(RetrieveCachedResult.Found(data.map { it.toLocalContentModel() }))
        } else {
            emit(RetrieveCachedResult.Empty)
        }
    }
    fun insert(data: List<LocalContentModel>): Flow<insertResult> = flow {
        try {
            dao.insert(data.map { it.toEntityModel() })
            emit(null)
        } catch (e: Exception) {
            emit(e)
        }
    }

    fun isExists(): Boolean = false

    private fun LocalContentEntity.toLocalContentModel() = LocalContentModel(
        id = id,
        title = title,
        description = description,
        videoUrl = videoUrl,
        adsUrl = adsUrl
    )

    private fun LocalContentModel.toEntityModel() = LocalContentEntity(
        id = id,
        title = title,
        description = description,
        videoUrl = videoUrl,
        adsUrl = adsUrl
    )
}

class ContentLocalStoreTest {
    private val dao: ContentDao = mockk()
    private lateinit var sut: ContentLocalStore

    @Before
    fun setup() {
        sut = ContentLocalStore(dao)
    }

    @Test
    fun testRetrieveOnDataIsExists() = runBlocking {
        coEvery {
            dao.load()
        } returns entity

        sut.retrieve().test {
            val result = awaitItem()
            if (result is RetrieveCachedResult.Found) {
                assert(result.data == localContent)
                awaitComplete()
            }
        }

        coVerify {
            dao.load()
        }

        confirmVerified(dao)
    }

    @Test
    fun testRetrieveOnDataIsNotExists() = runBlocking {
        coEvery {
            dao.load()
        } returns emptyList()

        sut.retrieve().test {
            val result = awaitItem()
            assert(result is RetrieveCachedResult.Empty)
            awaitComplete()
        }

        coVerify {
            dao.load()
        }

        confirmVerified(dao)
    }

    @Test
    fun testInsertFailed() = runBlocking {
        val exception = Exception()

        coEvery {
            dao.insert(entity)
        } throws exception

        sut.insert(localContent).test {
            val result = awaitItem()
            assertEquals(exception::class.java, result!!::class.java)
            awaitComplete()
        }

        coVerify {
            dao.insert(entity)
        }

        confirmVerified(dao)
    }

    @Test
    fun testInsertSuccess() = runBlocking {
        coEvery {
            dao.insert(entity)
        } returns Unit

        sut.insert(localContent).test {
            val result = awaitItem()
            assertEquals(null, result)
            awaitComplete()
        }

        coVerify {
            dao.insert(entity)
        }

        confirmVerified(dao)
    }
}