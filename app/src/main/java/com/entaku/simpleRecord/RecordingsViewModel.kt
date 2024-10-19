package com.entaku.simpleRecord

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entaku.simpleRecord.record.RecordingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RecordingsViewModel(
    private val repository: RecordingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecordingsUiState())
    val uiState: StateFlow<RecordingsUiState> = _uiState.asStateFlow()

    fun loadRecordings() {
        viewModelScope.launch {
            repository.getAllRecordings().collect { recordings ->
                _uiState.value = _uiState.value.copy(
                    recordings = recordings,
                    isLoading = false
                )
            }
        }
    }

}

data class RecordingsUiState(
    val recordings: List<RecordingData> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)