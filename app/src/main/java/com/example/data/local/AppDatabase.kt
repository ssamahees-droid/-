package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.CheckInEntry
import com.example.data.model.ReflectionEntry
import com.example.data.model.SavedBookmark
import com.example.data.model.SupportDraft
import com.example.data.model.TaskItem

@Database(
    entities = [
        TaskItem::class,
        CheckInEntry::class,
        ReflectionEntry::class,
        SupportDraft::class,
        SavedBookmark::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun checkInDao(): CheckInDao
    abstract fun reflectionDao(): ReflectionDao
    abstract fun supportDraftDao(): SupportDraftDao
    abstract fun bookmarkDao(): BookmarkDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "nasmat_hayat.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
