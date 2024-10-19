package com.entaku.simpleRecord

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SharedRecordingsViewModel : ViewModel() {
    private val _selectedRecording = MutableStateFlow<RecordingData?>(null)
    val selectedRecording: StateFlow<RecordingData?> = _selectedRecording

    // Function to set the selected recording
    fun selectRecording(recording: RecordingData) {
        _selectedRecording.value = recording
    }
}
