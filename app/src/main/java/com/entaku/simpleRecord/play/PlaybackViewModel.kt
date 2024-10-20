package com.entaku.simpleRecord.play

import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException

// Playbackの状態をまとめたデータクラス
data class PlaybackState(
    val isPlaying: Boolean = false,
    val currentPosition: Int = 0
)

class PlaybackViewModel : ViewModel() {

    // 状態をStateFlowにまとめる
    private val _playbackState = MutableStateFlow(PlaybackState())
    val playbackState: StateFlow<PlaybackState> = _playbackState

    private var mediaPlayer: MediaPlayer? = null
    private var updateJob: Job? = null

    fun setupMediaPlayer(filePath: String) {
        mediaPlayer = MediaPlayer().apply {
            try {
                setDataSource(filePath)
                prepare()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            }
        }
    }

    fun playOrPause() {
        mediaPlayer?.let {
            if (_playbackState.value.isPlaying) {
                it.pause()
                stopUpdatingProgress()
            } else {
                it.start()
                startUpdatingProgress()
            }
            // isPlayingの状態を更新
            _playbackState.value = _playbackState.value.copy(isPlaying = !_playbackState.value.isPlaying)
        }
    }

    private fun startUpdatingProgress() {
        updateJob = viewModelScope.launch {
            while (_playbackState.value.isPlaying) {
                // currentPositionの状態を更新
                _playbackState.value = _playbackState.value.copy(
                    currentPosition = mediaPlayer?.currentPosition ?: 0
                )
                delay(100) // Update every 100ms
            }
        }
    }

    private fun stopUpdatingProgress() {
        updateJob?.cancel()
    }

    fun stopPlayback() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.stop()
            }
            it.reset()
            it.release()
            mediaPlayer = null
        }
        stopUpdatingProgress()
        // 再生停止時に状態をリセット
        _playbackState.value = _playbackState.value.copy(
            isPlaying = false,
            currentPosition = 0
        )
    }

    override fun onCleared() {
        super.onCleared()
        stopPlayback()
    }
}
