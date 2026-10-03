package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.CheckInEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface CheckInDao {
    @Query("SELECT * FROM check_ins ORDER BY timestamp DESC")
    fun getAllCheckIns(): Flow<List<CheckInEntry>>

    @Query("SELECT * FROM check_ins ORDER BY timestamp DESC LIMIT 1")
    fun getLatestCheckIn(): Flow<CheckInEntry?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCheckIn(entry: CheckInEntry): Long

    @Query("DELETE FROM check_ins WHERE id = :id")
    suspend fun deleteCheckInById(id: Int)

    @Query("DELETE FROM check_ins")
    suspend fun deleteAllCheckIns()
}
