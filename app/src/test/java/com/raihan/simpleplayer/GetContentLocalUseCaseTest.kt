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
        store.retrieve().collect {

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

}