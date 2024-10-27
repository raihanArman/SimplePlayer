package com.raihan.simpleplayer

import app.cash.turbine.test
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
import java.util.Date

/**
 * @author Raihan Arman
 * @date 27/10/24
 */

sealed class RetrieveCachedResult {
    data object Empty: RetrieveCachedResult()
    data class Found(val cryptoFeed: List<LocalContentModel>): RetrieveCachedResult()
    data class Failure(val exception: Exception): RetrieveCachedResult()
}

typealias RetrievalResult = RetrieveCachedResult

interface ContentStore {
    fun retrieve(): Flow<RetrievalResult>
}

sealed class LoadCacheResult {
    data class Success(val data: List<ContentModel>): LoadCacheResult()
    data class Failure(val exception: Exception): LoadCacheResult()
}

typealias LoadResult = LoadCacheResult

class GetContentLocalUseCase(
    private val store: ContentStore
) {
    fun load(): Flow<LoadResult> = flow {
        store.retrieve().collect { result ->
            when (result) {
                RetrieveCachedResult.Empty -> {
                    emit(LoadCacheResult.Success(emptyList()))
                }
                is RetrieveCachedResult.Failure -> {
                    emit(LoadCacheResult.Failure(result.exception))
                }
                is RetrieveCachedResult.Found -> {}
            }
        }
    }
}

class GetContentLocalUseCaseTest {
    private val store = mockk<ContentStore>()
    private lateinit var sut: GetContentLocalUseCase

    @Before
    fun setup () {
        sut = GetContentLocalUseCase(store)
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

        every {
            store.retrieve()
        } returns flowOf(RetrieveCachedResult.Failure(retrievalError))

        sut.load().test {
            val result = awaitItem()
            if (result is LoadCacheResult.Failure) {
                assertEquals(LoadCacheResult.Failure(retrievalError), result)
            }

            awaitComplete()
        }

        verify(exactly = 1) {
            store.retrieve()
        }

        confirmVerified(store)
    }

    @Test
    fun testLoadDeliversEmptyDataOnEmptyCache() = runBlocking {
        every {
            store.retrieve()
        } returns flowOf(RetrieveCachedResult.Empty)

        sut.load().test {
            val result = awaitItem()
            if (result is LoadCacheResult.Success) {
                assertEquals(LoadCacheResult.Success(emptyList()), result)
            }

            awaitComplete()
        }

        verify(exactly = 1) {
            store.retrieve()
        }

        confirmVerified(store)
    }

    private fun expect(
        sut: GetContentLocalUseCase,
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