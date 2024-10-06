package com.entaku.simpleRecord

import android.util.Log
import com.entaku.simpleRecord.db.AppDatabase
import com.entaku.simpleRecord.db.RecordingEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.ZoneOffset

interface RecordingRepository {
    suspend fun saveRecordingData(recordingData: RecordingData)
}
class RecordingRepositoryImpl(private val database: AppDatabase) : RecordingRepository {
    companion object {
        private const val TAG = "RecordingRepositoryImpl"
    }

    override suspend fun saveRecordingData(recordingData: RecordingData) {
        withContext(Dispatchers.IO) {
            try {
                val recordingEntity = RecordingEntity(
                    title = recordingData.title,
                    creationDate = recordingData.creationDate.toEpochSecond(ZoneOffset.UTC),
                    fileExtension = recordingData.fileExtension,
                    khz = recordingData.khz,
                    bitRate = recordingData.bitRate,
                    channels = recordingData.channels,
                    duration = recordingData.duration,
                    filePath = recordingData.filePath
                )
                database.recordingDao().insert(recordingEntity)
                Log.d(TAG, "Recording data saved successfully: ${recordingData.title}")
            } catch (e: Exception) {
                Log.e(TAG, "Error saving recording data: ${e.message}", e)
            }
        }
    }
}