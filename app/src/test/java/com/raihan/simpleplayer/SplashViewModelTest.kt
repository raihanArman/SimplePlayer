package com.raihan.simpleplayer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.turbine.test
import com.raihan.simpleplayer.cache.SaveContentLocalUseCase
import com.raihan.simpleplayer.domain.ContentModel
import com.raihan.simpleplayer.domain.SaveContentUseCase
import com.raihan.simpleplayer.ui.splash.SplashViewModel
import io.mockk.MockKAnnotations
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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

        sut.save()

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

        sut.save()

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