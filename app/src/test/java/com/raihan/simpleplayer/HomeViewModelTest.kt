package com.raihan.simpleplayer

import app.cash.turbine.test
import com.raihan.simpleplayer.cache.usecase.LoadContentLocalUseCase
import com.raihan.simpleplayer.presentation.home.HomeEvent
import com.raihan.simpleplayer.presentation.home.HomeViewModel
import com.raihan.simpleplayer.utils.LoadCacheResult
import com.raihan.simpleplayer.utils.content
import io.mockk.MockKAnnotations
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * @author Raihan Arman
 * @date 27/10/24
 */
class HomeViewModelTest {
    private val useCase: LoadContentLocalUseCase = mockk()
    private lateinit var sut: HomeViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)
        sut = HomeViewModel(useCase)
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @Test
    fun testLoadContent() = runBlocking {
        every {
            useCase.load()
        } returns flowOf(LoadCacheResult.Success(content))

        sut.onEvent(HomeEvent.LoadContent)

        sut.uiState.take(1).test {
            val result = awaitItem()

            assertEquals(false, result.isLoading)
            assertEquals(content, result.data)
            assertEquals(null, result.error)

            awaitComplete()
        }

        verify(exactly = 1) {
            useCase.load()
        }

        confirmVerified(useCase)
    }

}