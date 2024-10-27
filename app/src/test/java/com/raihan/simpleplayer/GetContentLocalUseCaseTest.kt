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

/**
 * @author Raihan Arman
 * @date 27/10/24
 */

interface ContentStore {
    fun retrieve(): Flow<List<LocalContentModel>>
}

class GetContentLocalUseCase(
    private val store: ContentStore
) {
    fun load(): Flow<List<ContentModel>> = flow {
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