package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarks")
data class SavedBookmark(
    @PrimaryKey
    val articleId: String,
    val title: String,
    val category: String,
    val summary: String,
    val savedAt: Long = System.currentTimeMillis()
)
