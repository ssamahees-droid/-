package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.SupportDraft
import kotlinx.coroutines.flow.Flow

@Dao
interface SupportDraftDao {
    @Query("SELECT * FROM support_drafts ORDER BY updatedAt DESC")
    fun getAllDrafts(): Flow<List<SupportDraft>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDraft(draft: SupportDraft): Long

    @Update
    suspend fun updateDraft(draft: SupportDraft)

    @Query("DELETE FROM support_drafts WHERE id = :id")
    suspend fun deleteDraftById(id: Int)

    @Query("DELETE FROM support_drafts")
    suspend fun deleteAllDrafts()
}
