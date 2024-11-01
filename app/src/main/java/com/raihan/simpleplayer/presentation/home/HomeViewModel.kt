package com.raihan.simpleplayer.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raihan.simpleplayer.domain.model.ContentModel
import com.raihan.simpleplayer.domain.usecase.LoadContentUseCase
import com.raihan.simpleplayer.utils.LoadCacheResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * @author Raihan Arman
 * @date 27/10/24
 */
class HomeViewModel(
    private val useCase: LoadContentUseCase
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