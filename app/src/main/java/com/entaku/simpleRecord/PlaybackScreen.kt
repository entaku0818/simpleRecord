package com.entaku.simpleRecord

import android.media.MediaPlayer
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.io.IOException

@Composable
fun PlaybackScreen(
    recordingData: RecordingData, // Accept RecordingData
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    var isPlaying by remember { mutableStateOf(false) }
    val mediaPlayer = remember { MediaPlayer() }

    // Setup MediaPlayer
    LaunchedEffect(key1 = recordingData.filePath) {
        try {
            mediaPlayer.setDataSource(recordingData.filePath)
            mediaPlayer.prepare()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Playing: ${recordingData.title}")

        Button(onClick = {
            if (isPlaying) {
                mediaPlayer.pause()
            } else {
                mediaPlayer.start()
            }
            isPlaying = !isPlaying
        }) {
            Text(text = if (isPlaying) "Pause" else "Play")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Duration: ${recordingData.duration}")

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            mediaPlayer.stop()
            mediaPlayer.release()
            onNavigateBack()
        }) {
            Text(text = "Back to Recordings")
        }
    }
}
