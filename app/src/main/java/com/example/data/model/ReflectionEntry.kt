package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reflections")
data class ReflectionEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val promptQuestion: String,
    val answer: String,
    val timestamp: Long = System.currentTimeMillis()
)
