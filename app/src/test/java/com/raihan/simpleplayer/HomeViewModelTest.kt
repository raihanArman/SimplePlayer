package com.raihan.simpleplayer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.turbine.test
import com.raihan.simpleplayer.cache.LoadContentLocalUseCase
import com.raihan.simpleplayer.domain.ContentModel
import com.raihan.simpleplayer.utils.LoadCacheResult
import com.raihan.simpleplayer.utils.LoadResult
import com.raihan.simpleplayer.utils.content
import io.mockk.MockKAnnotations
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

class HomeViewModel(
    private val useCase: LoadContentLocalUseCase
): ViewModel() {
    private val _uiState: MutableStateFlow<HomeState> = MutableStateFlow(HomeState())
    val uiState = _uiState.asStateFlow()

    private fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            useCase.load().collect { result ->
                when(result) {
                    is LoadCacheResult.Success -> {
                        _uiState.update { it.copy(isLoading = false, data = result.data) }
                    }

                    is LoadCacheResult.Failure -> {
                        _uiState.update { it.copy(isLoading = false, error = result.exception) }
                    }
                }
            }
        }
    }

    fun onEvent(event: HomeEvent) {
        when(event) {
            is HomeEvent.LoadContent -> {
                load()
            }
        }
    }

}

data class HomeState(
    val isLoading: Boolean = false,
    val data: List<ContentModel>? = null,
    val error: Exception? = null
)

sealed interface HomeEvent {
    data object LoadContent: HomeEvent
}


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