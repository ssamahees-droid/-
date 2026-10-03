package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val note: String = "",
    val category: String = "رعاية ذاتية", // رعاية ذاتية, عمل, بيت, صحة, دراسة, هدوء
    val period: String = "أي وقت",       // صباح, بعد الظهر, مساء, أي وقت
    val priority: String = "لطيفة",     // لطيفة, عادية, مهمة برفق
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val completedAt: Long? = null,
    val isGentleHabit: Boolean = false
)
