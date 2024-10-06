package com.entaku.simpleRecord.db

import android.content.Context
import androidx.room.*

@Entity(tableName = "recordings")
data class RecordingEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "creation_date") val creationDate: Long,
    @ColumnInfo(name = "file_extension") val fileExtension: String,
    @ColumnInfo(name = "khz") val khz: String,
    @ColumnInfo(name = "bit_rate") val bitRate: Int,
    @ColumnInfo(name = "channels") val channels: Int,
    @ColumnInfo(name = "duration") val duration: String,
    @ColumnInfo(name = "file_path") val filePath: String
)

@Dao
interface RecordingDao {
    @Insert
    suspend fun insert(recording: RecordingEntity)

}

@Database(entities = [RecordingEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recordingDao(): RecordingDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
