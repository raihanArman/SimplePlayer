package com.raihan.simpleplayer

import app.cash.turbine.test
import com.raihan.simpleplayer.cache.store.ContentStore
import com.raihan.simpleplayer.cache.usecase.LoadContentLocalUseCase
import com.raihan.simpleplayer.utils.LoadCacheResult
import com.raihan.simpleplayer.utils.LoadResult
import com.raihan.simpleplayer.utils.RetrieveCachedResult
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

class LoadContentLocalUseCaseTest {
    private val store = mockk<ContentStore>()
    private lateinit var sut: LoadContentLocalUseCase

    @Before
    fun setup () {
        sut = LoadContentLocalUseCase(store)
    }

    @Test
    fun testInitDoesNotLoadUponCreation() = runBlocking {
        verify(exactly = 0) {
            store.retrieve()
        }

        confirmVerified(store)
    }

    @Test
    fun testLoadRequestCacheRetrieval() = runBlocking {
        every {
            store.retrieve()
        } returns flowOf()

        sut.load().test {
            awaitComplete()
        }

        verify(exactly = 1) {
            store.retrieve()
        }

        confirmVerified(store)
    }

    @Test
    fun testLoadFailsOnRetrievalError() = runBlocking {
        val retrievalError = Exception()

        expect(
            sut = sut,
            expectResult = LoadCacheResult.Failure(retrievalError),
            action = {
                every {
                    store.retrieve()
                } returns flowOf(RetrieveCachedResult.Failure(retrievalError))


            },
            retrieveExactly = 1,
        )
    }

    @Test
    fun testLoadDeliversEmptyDataOnEmptyCache() = runBlocking {
        expect(
            sut = sut,
            expectResult = LoadCacheResult.Success(emptyList()),
            action = {
                every {
                    store.retrieve()
                } returns flowOf(RetrieveCachedResult.Empty)
            },
            retrieveExactly = 1
        )
    }

    @Test
    fun testLoadDeliversWithDataOnSuccess() = runBlocking {
        expect(
            sut = sut,
            expectResult = LoadCacheResult.Success(content),
            action = {
                every {
                    store.retrieve()
                } returns flowOf(RetrieveCachedResult.Found(localContent))
            },
            retrieveExactly = 1
        )
    }

    private fun expect(
        sut: LoadContentLocalUseCase,
        expectResult: LoadResult,
        action: () -> Unit,
        retrieveExactly: Int = -1
    ) = runBlocking {
        action()

        sut.load().test {
            when(val receivedResult = awaitItem()) {
                is LoadCacheResult.Failure -> {
                    assertEquals(expectResult, receivedResult)
                }
                is LoadCacheResult.Success -> {
                    assertEquals(expectResult, receivedResult)
                }
            }

            awaitComplete()
        }

        verify(exactly = retrieveExactly) {
            store.retrieve()
        }

        confirmVerified(store)
    }
}