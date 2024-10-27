package com.raihan.simpleplayer

import app.cash.turbine.test
import com.raihan.simpleplayer.cache.LocalContentModel
import com.raihan.simpleplayer.cache.insertResult
import com.raihan.simpleplayer.cache_infra.ContentDao
import com.raihan.simpleplayer.cache_infra.ContentLocalStore
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

    @Test
    fun testTableIsNotEmpty() = runBlocking {
        coEvery {
            dao.isTableNotEmpty()
        } returns true

        sut.isExists().test {
            val result = awaitItem()
            assertEquals(true, result)
            awaitComplete()
        }

        coVerify {
            dao.isTableNotEmpty()
        }

        confirmVerified(dao)
    }
}