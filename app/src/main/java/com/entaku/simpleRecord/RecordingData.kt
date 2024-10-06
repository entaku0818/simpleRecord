package com.entaku.simpleRecord

import java.time.LocalDateTime

data class RecordingData(
    val title: String,
    val creationDate: LocalDateTime,
    val fileExtension: String,
    val khz: String,
    val bitRate: Int,
    val channels: Int,
    val duration: String,
    val filePath: String
)