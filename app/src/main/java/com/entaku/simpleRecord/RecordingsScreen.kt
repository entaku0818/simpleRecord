package com.entaku.simpleRecord

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
@Composable
fun RecordingsScreen(onNavigateToRecordScreen: () -> Unit) {
    var recordings by remember { mutableStateOf(List(10) { "Recording ${it + 1}" }) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(recordings) { recording ->
                RecordingItem(recording)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Button(
            onClick = {
                onNavigateToRecordScreen()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = "Start Recording")
        }
    }
}

@Composable
fun RecordingItem(recording: String) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = recording,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRecordingsScreen() {
    RecordingsScreen(onNavigateToRecordScreen = {})
}