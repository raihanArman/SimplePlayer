package com.raihan.simpleplayer

import androidx.lifecycle.ViewModel
import com.raihan.simpleplayer.cache.SaveContentLocalUseCase
import com.raihan.simpleplayer.domain.ContentModel
import io.mockk.MockKAnnotations
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

/**
 * @author Raihan Arman
 * @date 27/10/24
 */
interface SaveContentUseCase {
    fun save(model: List<ContentModel>): Flow<Exception?>
}

class SplashViewModel: ViewModel() {
    private val _uiState: MutableStateFlow<UIState> = MutableStateFlow(UIState())
    val uiState = _uiState.asStateFlow()
}

data class UIState(
    val isSuccessful: Boolean = true,
)

sealed interface UIEvent {
    data object SaveContent: UIEvent
}

class SplashViewModelTest {
    private val usecase: SaveContentUseCase = mockk()
    private lateinit var sut: SplashViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)
        sut = SplashViewModel()
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @Test
    fun testInitiateState() {
        val uiState = sut.uiState.value

        assertTrue(uiState.isSuccessful)
    }
}