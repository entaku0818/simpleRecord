package com.entaku.simpleRecord

import android.media.MediaRecorder
import android.os.Environment
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.IOException

// UIの状態を保持するデータクラス
data class RecordingUiState(
    val isRecording: Boolean = false,
    val outputFile: String = ""
)

class RecordViewModel : ViewModel() {

    // 状態をまとめたStateFlow
    private val _uiState = MutableStateFlow(RecordingUiState())
    val uiState: StateFlow<RecordingUiState> = _uiState

    private var mediaRecorder: MediaRecorder? = null

    // 録音を開始するメソッド
    fun startRecording(applicationContext: android.content.Context) {
        val externalFilesDir = applicationContext.getExternalFilesDir(Environment.DIRECTORY_MUSIC)
        val outputFile = "${externalFilesDir?.absolutePath}/recording_${System.currentTimeMillis()}.3gp"

        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setOutputFile(outputFile)
            try {
                prepare()
                start()
                _uiState.value = _uiState.value.copy(isRecording = true, outputFile = outputFile)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    // 録音を停止するメソッド
    fun stopRecording() {
        mediaRecorder?.apply {
            stop()
            release()
        }
        mediaRecorder = null
        // 録音停止状態に更新
        _uiState.value = _uiState.value.copy(isRecording = false)
    }


}

