package com.raihan.simpleplayer

import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

/**
 * @author Raihan Arman
 * @date 27/10/24
 */

interface ContentStore {
    fun retrieve(): List<LocalContentModel>
}

class GetContentLocalUseCase {

}

class GetContentLocalUseCaseTest {
    private val store = mockk<ContentStore>()
    private lateinit var sut: GetContentLocalUseCase

    @Before
    fun setup () {
        sut = GetContentLocalUseCase()
    }

    @Test
    fun testInitDoesNotLoadUponCreation() = runBlocking {
        verify(exactly = 0) {
            store.retrieve()
        }

        confirmVerified(store)
    }

}