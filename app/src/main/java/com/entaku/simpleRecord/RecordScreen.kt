package com.entaku.simpleRecord

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import android.Manifest


@Composable
fun RecordScreen(
    uiStateFlow: StateFlow<RecordingUiState>,
    onStartRecording: () -> Unit,
    onStopRecording: () -> Unit
) {
    // StateFlow を collect してUIに反映
    val uiState by uiStateFlow.collectAsState()

    val context = LocalContext.current

    // パーミッションリクエスト用のランチャー
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            if (granted) {
                onStartRecording() // 許可が下りたら録音開始
            }
        }
    )

    // 録音開始時にパーミッションチェックとリクエスト
    fun checkPermissionAndStartRecording() {
        when (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)) {
            PermissionChecker.PERMISSION_GRANTED -> {
                // すでに許可されていれば録音を開始
                onStartRecording()
            }
            else -> {
                // 許可されていなければリクエストを表示
                permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                if (uiState.isRecording) {
                    onStopRecording()
                } else {
                    checkPermissionAndStartRecording()
                }
            }
        ) {
            Text(if (uiState.isRecording) "Stop Recording" else "Start Recording")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(if (uiState.isRecording) "Recording in progress..." else "Ready to record")
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRecordScreen() {
    RecordScreen(
        uiStateFlow = MutableStateFlow(RecordingUiState()),
        onStartRecording = {},
        onStopRecording = {}
    )
}

