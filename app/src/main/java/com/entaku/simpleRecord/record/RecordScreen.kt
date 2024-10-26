package com.entaku.simpleRecord.record

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import com.entaku.simpleRecord.formatTime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.Duration


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordScreen(
    uiStateFlow: StateFlow<RecordingUiState>,
    onStartRecording: () -> Unit,
    onStopRecording: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val uiState by uiStateFlow.collectAsState()
    val context = LocalContext.current

    // FINISHED 状態を検知して onNavigateBack を実行
    LaunchedEffect(uiState.recordingState) {
        if (uiState.recordingState == RecordingState.FINISHED) {
            onNavigateBack()
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            if (granted) {
                onStartRecording()
            }
        }
    )

    fun checkPermissionAndStartRecording() {
        when (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)) {
            PermissionChecker.PERMISSION_GRANTED -> {
                onStartRecording()
            }
            else -> {
                permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Record") },
                navigationIcon = {
                    IconButton(onClick = { onNavigateBack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                if (uiState.recordingState == RecordingState.RECORDING) {
                    Text(
                        text = uiState.elapsedTime.formatTime(),
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
                Button(
                    onClick = {
                        when (uiState.recordingState) {
                            RecordingState.RECORDING -> onStopRecording()
                            RecordingState.IDLE -> checkPermissionAndStartRecording()
                            RecordingState.ERROR -> checkPermissionAndStartRecording()
                            RecordingState.FINISHED -> {}  // 何もしない
                        }
                    },
                    enabled = uiState.recordingState != RecordingState.FINISHED
                ) {
                    Text(
                        when (uiState.recordingState) {
                            RecordingState.RECORDING -> "Stop Recording"
                            RecordingState.IDLE -> "Start Recording"
                            RecordingState.ERROR -> "Retry Recording"
                            RecordingState.FINISHED -> "Finishing..."
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))



                if (uiState.recordingState == RecordingState.RECORDING) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Text(
                            text = "Volume: ${uiState.currentVolume}%",
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        LinearProgressIndicator(
                            progress = { uiState.currentVolume / 100f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp),
                            color = when {
                                uiState.currentVolume > 80 -> MaterialTheme.colorScheme.error
                                uiState.currentVolume > 60 -> MaterialTheme.colorScheme.tertiary
                                else -> MaterialTheme.colorScheme.primary
                            },
                            trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRecordScreen() {
    RecordScreen(
        uiStateFlow = MutableStateFlow(RecordingUiState()),
        onStartRecording = {},
        onStopRecording = {},
        onNavigateBack = {}
    )
}

