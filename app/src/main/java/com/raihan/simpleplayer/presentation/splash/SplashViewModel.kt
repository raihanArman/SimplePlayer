package com.raihan.simpleplayer.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raihan.simpleplayer.domain.ContentModel
import com.raihan.simpleplayer.domain.SaveContentUseCase
import com.raihan.simpleplayer.utils.content
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * @author Raihan Arman
 * @date 27/10/24
 */
class SplashViewModel(
    private val useCase: SaveContentUseCase
): ViewModel() {
    private val _uiState: MutableStateFlow<SplashState> = MutableStateFlow(SplashState())
    val uiState = _uiState.asStateFlow()

    private fun save() {
        val data = content
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }
            delay(1000L)
            useCase.save(data).collect { exception ->
                if (exception == null) {
                    _uiState.update {
                        it.copy(isLoading = false, isSuccessful = true)
                    }
                } else {
                    _uiState.update {
                        it.copy(isLoading = false, isSuccessful = false, error = exception)
                    }
                }
            }
        }
    }

    fun onEvent(event: SplashEvent) {
        when(event) {
            is SplashEvent.SaveContent -> {
                save()
            }
        }
    }
}

data class SplashState(
    val isSuccessful: Boolean = false,
    val error: Exception? = null,
    val isLoading: Boolean? = false
)

sealed interface SplashEvent {
    data object SaveContent: SplashEvent
}