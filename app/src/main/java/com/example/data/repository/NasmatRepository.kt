package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.model.CheckInEntry
import com.example.data.model.ContentArticle
import com.example.data.model.ReflectionEntry
import com.example.data.model.SavedBookmark
import com.example.data.model.SupportDraft
import com.example.data.model.TaskItem
import kotlinx.coroutines.flow.Flow

class NasmatRepository(private val db: AppDatabase) {

    // Tasks
    val allTasks: Flow<List<TaskItem>> = db.taskDao().getAllTasks()

    suspend fun insertTask(task: TaskItem): Long = db.taskDao().insertTask(task)

    suspend fun updateTask(task: TaskItem) = db.taskDao().updateTask(task)

    suspend fun setTaskCompletion(id: Int, isCompleted: Boolean) {
        val completedAt = if (isCompleted) System.currentTimeMillis() else null
        db.taskDao().setTaskCompletion(id, isCompleted, completedAt)
    }

    suspend fun deleteTaskById(id: Int) = db.taskDao().deleteTaskById(id)

    suspend fun deleteAllTasks() = db.taskDao().deleteAllTasks()

    // Check-ins
    val allCheckIns: Flow<List<CheckInEntry>> = db.checkInDao().getAllCheckIns()
    val latestCheckIn: Flow<CheckInEntry?> = db.checkInDao().getLatestCheckIn()

    suspend fun insertCheckIn(entry: CheckInEntry): Long = db.checkInDao().insertCheckIn(entry)

    suspend fun deleteCheckInById(id: Int) = db.checkInDao().deleteCheckInById(id)

    suspend fun deleteAllCheckIns() = db.checkInDao().deleteAllCheckIns()

    // Reflections
    val allReflections: Flow<List<ReflectionEntry>> = db.reflectionDao().getAllReflections()

    suspend fun insertReflection(entry: ReflectionEntry): Long = db.reflectionDao().insertReflection(entry)

    suspend fun deleteReflectionById(id: Int) = db.reflectionDao().deleteReflectionById(id)

    suspend fun deleteAllReflections() = db.reflectionDao().deleteAllReflections()

    // Support Drafts
    val allDrafts: Flow<List<SupportDraft>> = db.supportDraftDao().getAllDrafts()

    suspend fun insertDraft(draft: SupportDraft): Long = db.supportDraftDao().insertDraft(draft)

    suspend fun updateDraft(draft: SupportDraft) = db.supportDraftDao().updateDraft(draft)

    suspend fun deleteDraftById(id: Int) = db.supportDraftDao().deleteDraftById(id)

    suspend fun deleteAllDrafts() = db.supportDraftDao().deleteAllDrafts()

    // Bookmarks
    val allBookmarks: Flow<List<SavedBookmark>> = db.bookmarkDao().getAllBookmarks()

    fun isBookmarked(articleId: String): Flow<Boolean> = db.bookmarkDao().isBookmarked(articleId)

    suspend fun toggleBookmark(article: ContentArticle, isCurrentlyBookmarked: Boolean) {
        if (isCurrentlyBookmarked) {
            db.bookmarkDao().deleteBookmarkById(article.id)
        } else {
            db.bookmarkDao().insertBookmark(
                SavedBookmark(
                    articleId = article.id,
                    title = article.title,
                    category = article.category,
                    summary = article.subtitle
                )
            )
        }
    }

    // Complete local cleanup
    suspend fun clearAllLocalData() {
        db.taskDao().deleteAllTasks()
        db.checkInDao().deleteAllCheckIns()
        db.reflectionDao().deleteAllReflections()
        db.supportDraftDao().deleteAllDrafts()
        db.bookmarkDao().deleteAllBookmarks()
    }
}
