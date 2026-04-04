package ru.rtmrslnv.androidpractice.models

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface JobInfoDao {
    @Query("SELECT * FROM jobInfoUi")
    fun getAll() : Flow<List<JobInfoUI>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(jobInfo: JobInfoUI)
}