package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.ReflectionEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface ReflectionDao {
    @Query("SELECT * FROM reflections ORDER BY timestamp DESC")
    fun getAllReflections(): Flow<List<ReflectionEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReflection(entry: ReflectionEntry): Long

    @Query("DELETE FROM reflections WHERE id = :id")
    suspend fun deleteReflectionById(id: Int)

    @Query("DELETE FROM reflections")
    suspend fun deleteAllReflections()
}
