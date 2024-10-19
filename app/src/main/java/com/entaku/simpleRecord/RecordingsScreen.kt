package com.entaku.simpleRecord

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID


@Composable
fun RecordingsScreen(
    state: RecordingsUiState,
    onNavigateToRecordScreen: () -> Unit,
    onRefresh: () -> Unit,
    onNavigateToPlaybackScreen: (RecordingData) -> Unit,
    onDeleteClick: (UUID) -> Unit,
    onEditRecordingName: (UUID, String) -> Unit
) {

    LaunchedEffect(key1 = Unit) {
        onRefresh()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(alignment = Alignment.CenterHorizontally))
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(state.recordings) { recording ->
                    RecordingListItem(
                        recording = recording,
                        onItemClick = { onNavigateToPlaybackScreen(recording) },
                        onDeleteClick = { recording.uuid?.let { onDeleteClick(it) } },
                        onEditNameClick = { newTitle ->
                            recording.uuid?.let { onEditRecordingName(it,newTitle) }
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        Button(
            onClick = onNavigateToRecordScreen,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = "Start Recording")
        }
    }
}

@Composable
fun RecordingListItem(
    recording: RecordingData,
    onItemClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onEditNameClick: (String) -> Unit
) {
    val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
    val formattedDate = recording.creationDate.format(formatter)

    var expanded by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showEditNameDialog by remember { mutableStateOf(false) }

    // Set a maximum length for the title and append ellipsis if it exceeds the limit
    val maxTitleLength = 20
    val shortenedTitle = if (recording.title.length > maxTitleLength) {
        recording.title.take(maxTitleLength) + "..."
    } else {
        recording.title
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick() },
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Recording title and duration
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = shortenedTitle, // Use the truncated title
                        style = MaterialTheme.typography.bodyLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = recording.duration,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                IconButton(
                    onClick = { expanded = true },
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert, // メニュー用のアイコン
                        contentDescription = "Menu", // アクセシビリティ用の説明
                        modifier = Modifier.size(24.dp) // アイコン自体のサイズを指定
                    )
                }

                // Dropdown menu
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        onClick = {
                            expanded = false
                            showEditNameDialog = true
                                  },
                        text = { Text("Edit Name") }
                    )
                    DropdownMenuItem(
                        onClick = {
                            expanded = false
                            showDeleteDialog = true // Show confirmation dialog
                        },
                        text = { Text("Delete") }
                    )
                }
            }

            Spacer(modifier = Modifier.height(1.dp))

            // Recording information display
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = formattedDate,
                    style = MaterialTheme.typography.bodyMedium
                )
                Row {
                    Text(
                        text = "${recording.khz} kHz",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(text = "/")
                    Text(
                        text = "${recording.bitRate} bit",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(text = "/")
                    Text(
                        text = "${recording.channels} ch",
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Text(
                    text = recording.fileExtension.uppercase(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }

    // 削除確認用モーダルダイアログ
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("削除確認") },
            text = { Text("本当に削除してもよろしいですか？") },
            confirmButton = {
                Button(onClick = {
                    onDeleteClick() // 実際の削除アクションを呼び出し
                    showDeleteDialog = false
                }) {
                    Text("はい")
                }
            },
            dismissButton = {
                Button(onClick = { showDeleteDialog = false }) {
                    Text("いいえ")
                }
            }
        )
    }

    if (showEditNameDialog) {
        EditNameDialog(
            currentName = recording.title, // 現在の名前を表示
            onConfirm = { newName ->
                onEditNameClick(newName) // 新しい名前をコールバックで渡す
            },
            onDismiss = { showEditNameDialog = false } // ダイアログを閉じる
        )
    }
}

@Composable
fun EditNameDialog(
    currentName: String,
    onConfirm: (String) -> Unit, // 新しい名前を渡すコールバック
    onDismiss: () -> Unit // ダイアログを閉じるコールバック
) {
    var newName by remember { mutableStateOf(currentName) } // 新しい名前を保存する状態

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("名前を編集") },
        text = {
            Column {
                Text("新しい名前を入力してください")
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = newName,
                    onValueChange = { newName = it },
                    label = { Text("名前") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                onConfirm(newName) // 新しい名前を返す
                onDismiss() // ダイアログを閉じる
            }) {
                Text("保存")
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text("キャンセル")
            }
        }
    )
}



@Preview(showBackground = true)
@Composable
fun PreviewRecordingsScreen() {
    val sampleRecordings = List(5) { index ->
        RecordingData(
            uuid = UUID.randomUUID(),
            title = "RecordingRecordingRecording ${index + 1}",
            creationDate = LocalDateTime.now().minusDays(index.toLong()),
            fileExtension = "wav",
            khz = "44",
            bitRate = 16,
            channels = 2,
            duration = "00:0${index + 1}:30",
            filePath = "/path/to/recording${index + 1}.wav"
        )
    }

    val sampleState = RecordingsUiState(
        recordings = sampleRecordings,
        isLoading = false,
        error = null
    )

    RecordingsScreen(
        state = sampleState,
        onNavigateToRecordScreen = {},
        onRefresh = {},
        onNavigateToPlaybackScreen = {},
        onDeleteClick = {},
        onEditRecordingName = { a,b ->
        }
    )
}