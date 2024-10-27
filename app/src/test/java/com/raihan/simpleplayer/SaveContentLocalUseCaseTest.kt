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
class SaveContentLocalUseCaseTest {
    private val store = mockk<ContentStore>()
    private lateinit var sut: SaveContentLocalUseCase

    @Before
    fun setup() {
        sut = SaveContentLocalUseCase(store)
    }

    @Test
    fun testInsertErrorWithCacheDoesNotExists() {
        val exception = Exception()

        expect(
            sut = sut,
            isExists = false,
            expected = exception,
        )
    }

    @Test
    fun testInsertSuccessWithCacheDoesNotExists() {
        expect(
            sut = sut,
            isExists = false,
            expected = null,
        )
    }

    @Test
    fun testInsertWithCacheExists() {
        expect(
            sut = sut,
            isExists = true,
            expected = null,
            insertVerify = 0
        )
    }

    private fun expect(
        sut: SaveContentLocalUseCase,
        isExists: Boolean,
        expected: insertResult,
        insertVerify: Int = 1
    ) = runBlocking {
        every {
            store.isExists()
        } returns isExists

        every {
            store.insert(localContent)
        } returns flowOf(expected)

        sut.save(content).test {
            val result = awaitItem()
            if (result == null) {
                assertEquals(expected, result)
            } else {
                assertEquals(expected!!::class.java, result::class.java)
            }

            awaitComplete()
        }

        verify(exactly = 1) {
            store.isExists()
        }

        verify(exactly = insertVerify) {
            store.insert(localContent)
        }

        confirmVerified(store)
    }
}