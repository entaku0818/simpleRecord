package com.entaku.simpleRecord.record

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.media.MediaRecorder
import android.os.Build
import android.os.Environment
import com.entaku.simpleRecord.RecordingData
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import java.io.IOException
import java.time.LocalDateTime
import java.time.Duration


enum class RecordingState {
    IDLE,      // 録音準備完了
    RECORDING, // 録音中
    ERROR,
    FINISHED
}

data class RecordingUiState(
    val recordingState: RecordingState = RecordingState.IDLE,
    val currentFilePath: String? = null,
    val currentVolume: Int = 0
)

class RecordViewModel(private val repository: RecordingRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(RecordingUiState())
    val uiState: StateFlow<RecordingUiState> = _uiState

    private var mediaRecorder: MediaRecorder? = null
    private var startTime: Long = 0
    private var volumeMonitorJob: Job? = null

    // 録音設定パラメータ
    private val fileExtension = "3gp"
    private val outputFormat = MediaRecorder.OutputFormat.THREE_GPP
    private val audioEncoder = MediaRecorder.AudioEncoder.AMR_NB
    private val sampleRate = 44100
    private val bitRate = 16
    private val channels = 1

    fun startRecording(applicationContext: Context) {
        val externalFilesDir = applicationContext.getExternalFilesDir(Environment.DIRECTORY_MUSIC)
        val fileName = "recording_${System.currentTimeMillis()}"
        val outputFile = "${externalFilesDir?.absolutePath}/$fileName.$fileExtension"

        mediaRecorder = createMediaRecorder(applicationContext).apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(outputFormat)
            setAudioEncoder(audioEncoder)
            setAudioSamplingRate(sampleRate)
            setAudioEncodingBitRate(bitRate)
            setAudioChannels(channels)
            setOutputFile(outputFile)
            try {
                prepare()
                start()
                startTime = System.currentTimeMillis()
                _uiState.update { it.copy(recordingState = RecordingState.RECORDING, currentFilePath = outputFile) }
                startVolumeMonitoring()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun startVolumeMonitoring() {
        volumeMonitorJob?.cancel()
        volumeMonitorJob = viewModelScope.launch {
            while (true) {
                mediaRecorder?.let { recorder ->
                    try {
                        val amplitude = recorder.maxAmplitude
                        val normalizedVolume = (amplitude.toFloat() / 32767.0f * 100).toInt().coerceIn(0, 100)
                        _uiState.update { it.copy(currentVolume = normalizedVolume) }
                    } catch (e: IllegalStateException) {
                        e.printStackTrace()
                    }
                }
                delay(100)
            }
        }
    }

    fun stopRecording() {
        volumeMonitorJob?.cancel()
        volumeMonitorJob = null

        mediaRecorder?.apply {
            try {
                stop()
            } catch (e: IllegalStateException) {
                e.printStackTrace()
                return
            } finally {
                release()
            }
        }
        mediaRecorder = null

        val endTime = System.currentTimeMillis()
        val duration = Duration.ofMillis(endTime - startTime)

        _uiState.value.currentFilePath?.let { filePath ->
            val recordingData = RecordingData(
                title = "Recording ${LocalDateTime.now()}",
                creationDate = LocalDateTime.now(),
                fileExtension = fileExtension,
                khz = sampleRate.toString(),
                bitRate = bitRate,
                channels = channels,
                duration = duration.seconds,
                filePath = filePath
            )

            viewModelScope.launch {
                repository.saveRecordingData(recordingData)
            }
        }

        _uiState.update {
            it.copy(
                recordingState = RecordingState.FINISHED,
                currentFilePath = null,
                currentVolume = 0
            )
        }
    }

    private fun formatDuration(duration: Duration): String {
        val hours = duration.toHours()
        val minutes = duration.toMinutes() % 60
        val seconds = duration.seconds % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    private fun createMediaRecorder(context: Context): MediaRecorder {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            @Suppress("DEPRECATION")
            MediaRecorder()
        }
    }

    override fun onCleared() {
        super.onCleared()
        volumeMonitorJob?.cancel()
        mediaRecorder?.release()
        mediaRecorder = null
    }
}