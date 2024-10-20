package com.entaku.simpleRecord.play

import android.media.MediaPlayer
import android.util.Log
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
                Log.e("MediaPlayer", "Failed to set data source", e)
            } catch (e: IllegalStateException) {
                Log.e("MediaPlayer", "Illegal state during media preparation", e)
            }
        }
    }

    fun playOrPause() {
        mediaPlayer?.let {
            if (_playbackState.value.isPlaying) {
                it.pause()
                stopUpdatingProgress()
                Log.d("Playback", "Paused")
                _playbackState.value = _playbackState.value.copy(isPlaying = false)
            } else {
                try {
                    it.start()
                    Log.d("Playback", "Started")
                    _playbackState.value = _playbackState.value.copy(isPlaying = true)

                    // 進行状況の更新を開始
                    startUpdatingProgress()
                } catch (e: IllegalStateException) {
                    Log.e("MediaPlayer", "Error starting playback", e)
                }
            }
        }
    }

    private fun startUpdatingProgress() {
        // すでにジョブが実行中の場合はキャンセル
        updateJob?.cancel()

        updateJob = viewModelScope.launch {
            while (_playbackState.value.isPlaying) {
                val position = mediaPlayer?.currentPosition ?: 0
                _playbackState.value = _playbackState.value.copy(
                    currentPosition = position
                )
                Log.d("currentPosition", position.toString())
                Log.e("currentPosition", mediaPlayer?.isPlaying.toString())

                delay(100) // 100msごとに更新
            }
        }
    }

    private fun stopUpdatingProgress() {
        updateJob?.cancel()
        updateJob = null
    }

    fun stopPlayback() {
        mediaPlayer?.let {
            try {
                if (it.isPlaying) {
                    it.stop()
                }
                it.reset()
                it.release()
            } catch (e: IllegalStateException) {
                Log.e("MediaPlayer", "Error stopping media player", e)
            } finally {
                mediaPlayer = null
            }
        }
        stopUpdatingProgress()
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
