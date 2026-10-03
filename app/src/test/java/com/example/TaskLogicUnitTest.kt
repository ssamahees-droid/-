package com.example

import com.example.data.model.TaskItem
import com.example.ui.viewmodel.DailyProgress
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TaskLogicUnitTest {

    @Test
    fun testTaskItemDefaultValues() {
        val task = TaskItem(title = "تنفس بهدوء")
        assertEquals("تنفس بهدوء", task.title)
        assertFalse(task.isCompleted)
        assertEquals("رعاية ذاتية", task.category)
        assertEquals("أي وقت", task.period)
        assertEquals("لطيفة", task.priority)
    }

    @Test
    fun testDailyProgressCalculation() {
        val total = 4
        val completed = 2
        val percentage = completed.toFloat() / total
        val progress = DailyProgress(total = total, completed = completed, percentage = percentage)

        assertEquals(4, progress.total)
        assertEquals(2, progress.completed)
        assertEquals(0.5f, progress.percentage)
    }

    @Test
    fun testTaskCompletionToggle() {
        val task = TaskItem(id = 1, title = "شرب ماء", isCompleted = false)
        val updatedTask = task.copy(isCompleted = true, completedAt = System.currentTimeMillis())

        assertTrue(updatedTask.isCompleted)
        assertTrue(updatedTask.completedAt != null)
    }
}
