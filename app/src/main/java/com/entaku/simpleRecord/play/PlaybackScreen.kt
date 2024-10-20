package com.entaku.simpleRecord.play

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.entaku.simpleRecord.RecordingData
import java.time.LocalDateTime
import java.util.UUID

@Composable
fun PlaybackScreen(
    recordingData: RecordingData, // Accept RecordingData
    playbackState: PlaybackState, // PlaybackStateを渡す
    onPlayPause: () -> Unit, // 再生/一時停止を制御する関数
    onStop: () -> Unit, // 再生停止を制御する関数
    onNavigateBack: () -> Unit // ナビゲーションバックを制御する関数
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Playing: ${recordingData.title}")

        Button(onClick = { onPlayPause() }) {
            Text(text = if (playbackState.isPlaying) "Pause" else "Play")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LinearProgressIndicator(
            progress = { (playbackState.currentPosition.toFloat() / 1000) / (recordingData.duration.toFloat() ) },
            modifier = Modifier
                .height(16.dp),
        )


        Text(text = "Time: ${playbackState.currentPosition / 1000} sec / ${recordingData.duration} sec")

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            onStop()
            onNavigateBack()
        }) {
            Text(text = "Back to Recordings")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PlaybackScreenPreview() {
    val dummyRecordingData = RecordingData(
        uuid = UUID.randomUUID(),
        title = "Sample Recording",
        creationDate = LocalDateTime.now(),
        fileExtension = ".mp3",
        khz = "44.1",
        bitRate = 128,
        channels = 2,
        duration = 120, // Assuming duration in seconds
        filePath = "dummy/path/to/audio/file.mp3"
    )

    val dummyPlaybackState = PlaybackState(
        isPlaying = false,
        currentPosition = 10000
    )

    PlaybackScreen(
        recordingData = dummyRecordingData,
        playbackState = dummyPlaybackState, // PlaybackStateを渡す
        onPlayPause = { /* handle play/pause */ },
        onStop = { /* handle stop */ },
        onNavigateBack = {}
    )
}

