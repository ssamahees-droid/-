package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "check_ins")
data class CheckInEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val feelingName: String,
    val feelingEmoji: String,
    val note: String = "",
    val energyLevel: Int = 3, // 1 to 5 (مستوى الطاقة برفق)
    val timestamp: Long = System.currentTimeMillis()
)
