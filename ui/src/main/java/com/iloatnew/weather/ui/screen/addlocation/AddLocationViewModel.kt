package com.iloatnew.weather.ui.screen.addlocation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AddLocationViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(AddLocationUiState())
    val uiState = _uiState.asStateFlow()

    fun toggleIsLoading(isLoading: Boolean) {
        _uiState.update {
            it.copy(isLoading = isLoading)
        }
    }
}

data class AddLocationUiState(
    val isLoading: Boolean = true,
)
