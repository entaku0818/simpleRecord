package com.entaku.simpleRecord

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter




@Composable
fun RecordingsScreen(onNavigateToRecordScreen: () -> Unit) {
    var recordings by remember {
        mutableStateOf(
            List(20) { // リストの件数を増やしました
                RecordingData(
                    title = "Recording ${it + 1}",
                    creationDate = LocalDateTime.now().minusDays(it.toLong()),
                    fileExtension = "wav",
                    khz = "44",
                    bitRate = 16,
                    channels = 2,
                    duration = "00:0${it + 1}:30",
                    filePath = "/path/to/recording${it + 1}.wav"
                )
            }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(recordings) { recording ->
                RecordingListItem(recording)
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
fun RecordingListItem(recording: RecordingData) {
    val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
    val formattedDate = recording.creationDate.format(formatter)

    Card(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = recording.title,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = recording.duration,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(1.dp))

            // 録音時間、ファイルの情報を表示
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = formattedDate,
                    style = MaterialTheme.typography.bodyMedium
                )
                Row(){
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
}


@Preview(showBackground = true)
@Composable
fun PreviewRecordingsScreen() {
    RecordingsScreen(onNavigateToRecordScreen = {})
}