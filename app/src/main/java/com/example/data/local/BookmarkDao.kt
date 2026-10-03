package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.SavedBookmark
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao {
    @Query("SELECT * FROM bookmarks ORDER BY savedAt DESC")
    fun getAllBookmarks(): Flow<List<SavedBookmark>>

    @Query("SELECT EXISTS(SELECT 1 FROM bookmarks WHERE articleId = :articleId)")
    fun isBookmarked(articleId: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: SavedBookmark)

    @Query("DELETE FROM bookmarks WHERE articleId = :articleId")
    suspend fun deleteBookmarkById(articleId: String)

    @Query("DELETE FROM bookmarks")
    suspend fun deleteAllBookmarks()
}
