package com.raihan.simpleplayer.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raihan.simpleplayer.domain.ContentModel
import com.raihan.simpleplayer.domain.SaveContentUseCase
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
    private val _uiState: MutableStateFlow<UIState> = MutableStateFlow(UIState())
    val uiState = _uiState.asStateFlow()

    fun save() {
        val data = emptyList<ContentModel>()
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }
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
}

data class UIState(
    val isSuccessful: Boolean = false,
    val error: Exception? = null,
    val isLoading: Boolean? = false
)

sealed interface UIEvent {
    data object SaveContent: UIEvent
}