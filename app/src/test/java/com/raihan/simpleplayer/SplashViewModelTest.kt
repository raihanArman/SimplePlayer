package com.raihan.simpleplayer

import app.cash.turbine.test
import com.raihan.simpleplayer.domain.SaveContentUseCase
import com.raihan.simpleplayer.presentation.splash.SplashEvent
import com.raihan.simpleplayer.presentation.splash.SplashViewModel
import com.raihan.simpleplayer.utils.content
import io.mockk.MockKAnnotations
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertTrue
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
class SplashViewModelTest {
    private val useCase: SaveContentUseCase = mockk()
    private lateinit var sut: SplashViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)
        sut = SplashViewModel(useCase)
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @Test
    fun testInitiateState() {
        val uiState = sut.uiState.value

        assertTrue(!uiState.isSuccessful)
        assertTrue(uiState.isLoading == false)
        assertTrue(uiState.error == null)
    }

    @Test
    fun testSaveContentOnFailed() = runBlocking {
        val exception = Exception()

        every {
            useCase.save(content)
        } returns flowOf(exception)

        sut.onEvent(SplashEvent.SaveContent)

        sut.uiState.take(1).test {
            val result = awaitItem()
            assertEquals(false, result.isLoading)
            assertEquals(exception, result.error)
            assertEquals(false, result.isSuccessful)

            awaitComplete()
        }

        verify(exactly = 1) {
            useCase.save(content)
        }

        confirmVerified(useCase)
    }

    @Test
    fun testSaveContentOnSuccess() = runBlocking {
        every {
            useCase.save(content)
        } returns flowOf(null)

        sut.onEvent(SplashEvent.SaveContent)

        sut.uiState.take(1).test {
            val result = awaitItem()
            assertEquals(false, result.isLoading)
            assertEquals(null, result.error)
            assertEquals(true, result.isSuccessful)

            awaitComplete()
        }

        verify(exactly = 1) {
            useCase.save(content)
        }

        confirmVerified(useCase)
    }

}