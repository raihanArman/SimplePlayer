package com.raihan.simpleplayer

import app.cash.turbine.test
import com.raihan.simpleplayer.cache.store.ContentStore
import com.raihan.simpleplayer.cache.store.insertResult
import com.raihan.simpleplayer.cache.usecase.SaveContentLocalUseCase
import com.raihan.simpleplayer.utils.content
import com.raihan.simpleplayer.utils.localContent
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
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
        } returns flowOf(isExists)

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