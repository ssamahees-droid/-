package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.CheckInEntry
import com.example.data.model.ContentArticle
import com.example.data.model.ReflectionEntry
import com.example.data.model.SavedBookmark
import com.example.data.model.SupportDraft
import com.example.data.model.TaskItem
import com.example.data.repository.NasmatRepository
import com.example.data.repository.SampleContentProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class TaskCompletionFilter {
    ALL, ACTIVE, COMPLETED
}

data class DailyProgress(
    val total: Int = 0,
    val completed: Int = 0,
    val percentage: Float = 0f
)

class NasmatViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NasmatRepository
    private val prefs = application.getSharedPreferences("nasmat_hayat_prefs", Context.MODE_PRIVATE)

    init {
        val db = AppDatabase.getDatabase(application)
        repository = NasmatRepository(db)

        // Seed initial gentle tasks if first time
        initSeedData()
    }

    // Onboarding State
    private val _onboardingCompleted = MutableStateFlow(
        prefs.getBoolean("onboarding_completed", false)
    )
    val onboardingCompleted: StateFlow<Boolean> = _onboardingCompleted.asStateFlow()

    fun completeOnboarding(saveChoices: Boolean, selectedGoals: List<String> = emptyList()) {
        prefs.edit().apply {
            putBoolean("onboarding_completed", true)
            if (saveChoices && selectedGoals.isNotEmpty()) {
                putStringSet("user_goals", selectedGoals.toSet())
            }
            apply()
        }
        _onboardingCompleted.value = true

        // If user chose to save choices, add gentle starter tasks based on their goals
        if (saveChoices) {
            viewModelScope.launch {
                if (selectedGoals.contains("تنظيم يومي بهدوء")) {
                    addTask("تحديد 3 أولويات لطيفة لليوم", "بدون ضغط، خطوة صغيرة واحدة", "تنظيم اليوم", "صباح", "مهمة برفق")
                }
                if (selectedGoals.contains("التنفس والتخفيف من التوتر")) {
                    addTask("أخذ 3 دقائق للتنفس العميق", "شهيق 4 وزفير 6 لتهدئة الذهن", "رعاية ذاتية", "بعد الظهر", "لطيفة")
                }
                if (selectedGoals.contains("تسجيل المشاعر")) {
                    addTask("تسجيل شعوري في نافذة اليوم", "لحظة صدق ورفق مع النفس", "رعاية ذاتية", "مساء", "لطيفة")
                }
            }
        }
    }

    // Tasks Management
    val allTasks: StateFlow<List<TaskItem>> = repository.allTasks
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _taskFilter = MutableStateFlow(TaskCompletionFilter.ALL)
    val taskFilter: StateFlow<TaskCompletionFilter> = _taskFilter.asStateFlow()

    private val _selectedPeriodFilter = MutableStateFlow("الكل")
    val selectedPeriodFilter: StateFlow<String> = _selectedPeriodFilter.asStateFlow()

    fun setTaskFilter(filter: TaskCompletionFilter) {
        _taskFilter.value = filter
    }

    fun setPeriodFilter(period: String) {
        _selectedPeriodFilter.value = period
    }

    val filteredTasks: StateFlow<List<TaskItem>> = combine(
        allTasks,
        _taskFilter,
        _selectedPeriodFilter
    ) { tasks, filter, period ->
        tasks.filter { task ->
            val matchesFilter = when (filter) {
                TaskCompletionFilter.ALL -> true
                TaskCompletionFilter.ACTIVE -> !task.isCompleted
                TaskCompletionFilter.COMPLETED -> task.isCompleted
            }
            val matchesPeriod = if (period == "الكل") true else task.period == period
            matchesFilter && matchesPeriod
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val dailyProgress: StateFlow<DailyProgress> = allTasks.combine(_taskFilter) { tasks, _ ->
        val total = tasks.size
        val completed = tasks.count { it.isCompleted }
        val percentage = if (total > 0) completed.toFloat() / total else 0f
        DailyProgress(total = total, completed = completed, percentage = percentage)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DailyProgress()
    )

    fun addTask(
        title: String,
        note: String = "",
        category: String = "رعاية ذاتية",
        period: String = "أي وقت",
        priority: String = "لطيفة",
        isGentleHabit: Boolean = false
    ) {
        viewModelScope.launch {
            repository.insertTask(
                TaskItem(
                    title = title.trim(),
                    note = note.trim(),
                    category = category,
                    period = period,
                    priority = priority,
                    isGentleHabit = isGentleHabit
                )
            )
        }
    }

    fun toggleTaskCompletion(task: TaskItem) {
        viewModelScope.launch {
            repository.setTaskCompletion(task.id, !task.isCompleted)
        }
    }

    fun updateTask(task: TaskItem) {
        viewModelScope.launch {
            repository.updateTask(task)
        }
    }

    fun deleteTask(taskId: Int) {
        viewModelScope.launch {
            repository.deleteTaskById(taskId)
        }
    }

    // Check-ins Management
    val allCheckIns: StateFlow<List<CheckInEntry>> = repository.allCheckIns
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val latestCheckIn: StateFlow<CheckInEntry?> = repository.latestCheckIn
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun addCheckIn(feelingName: String, feelingEmoji: String, note: String, energyLevel: Int = 3) {
        viewModelScope.launch {
            repository.insertCheckIn(
                CheckInEntry(
                    feelingName = feelingName,
                    feelingEmoji = feelingEmoji,
                    note = note.trim(),
                    energyLevel = energyLevel
                )
            )
        }
    }

    fun deleteCheckIn(id: Int) {
        viewModelScope.launch {
            repository.deleteCheckInById(id)
        }
    }

    // Self Reflection Management
    val allReflections: StateFlow<List<ReflectionEntry>> = repository.allReflections
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addReflection(question: String, answer: String) {
        viewModelScope.launch {
            repository.insertReflection(
                ReflectionEntry(
                    promptQuestion = question,
                    answer = answer.trim()
                )
            )
        }
    }

    fun deleteReflection(id: Int) {
        viewModelScope.launch {
            repository.deleteReflectionById(id)
        }
    }

    // Support Drafts (Local only - clearly noted "لن تُرسل إلى أحد")
    val allDrafts: StateFlow<List<SupportDraft>> = repository.allDrafts
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addDraft(title: String, content: String, role: String = "لنفسي") {
        viewModelScope.launch {
            repository.insertDraft(
                SupportDraft(
                    title = title.trim(),
                    content = content.trim(),
                    intendedRecipientRole = role
                )
            )
        }
    }

    fun updateDraft(draft: SupportDraft) {
        viewModelScope.launch {
            repository.updateDraft(draft.copy(updatedAt = System.currentTimeMillis()))
        }
    }

    fun deleteDraft(id: Int) {
        viewModelScope.launch {
            repository.deleteDraftById(id)
        }
    }

    // Bookmarks & Content
    val allBookmarks: StateFlow<List<SavedBookmark>> = repository.allBookmarks
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _contentSearchQuery = MutableStateFlow("")
    val contentSearchQuery: StateFlow<String> = _contentSearchQuery.asStateFlow()

    private val _selectedContentCategory = MutableStateFlow("الكل")
    val selectedContentCategory: StateFlow<String> = _selectedContentCategory.asStateFlow()

    fun setContentSearchQuery(query: String) {
        _contentSearchQuery.value = query
    }

    fun setContentCategory(category: String) {
        _selectedContentCategory.value = category
    }

    val filteredArticles: StateFlow<List<ContentArticle>> = combine(
        _contentSearchQuery,
        _selectedContentCategory
    ) { query, category ->
        SampleContentProvider.articles.filter { article ->
            val matchesCategory = if (category == "الكل") true else article.category == category
            val matchesQuery = if (query.isBlank()) true else {
                article.title.contains(query, ignoreCase = true) ||
                article.subtitle.contains(query, ignoreCase = true) ||
                article.content.any { it.contains(query, ignoreCase = true) }
            }
            matchesCategory && matchesQuery
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SampleContentProvider.articles
    )

    fun toggleBookmark(article: ContentArticle, isCurrentlyBookmarked: Boolean) {
        viewModelScope.launch {
            repository.toggleBookmark(article, isCurrentlyBookmarked)
        }
    }

    // Breathing Settings
    private val _holdPhaseEnabled = MutableStateFlow(
        prefs.getBoolean("breathe_hold_enabled", true)
    )
    val holdPhaseEnabled: StateFlow<Boolean> = _holdPhaseEnabled.asStateFlow()

    fun setHoldPhaseEnabled(enabled: Boolean) {
        _holdPhaseEnabled.value = enabled
        prefs.edit().putBoolean("breathe_hold_enabled", enabled).apply()
    }

    // Clear All Local Data (Privacy guarantee)
    fun clearAllData(onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            repository.clearAllLocalData()
            prefs.edit().clear().apply()
            _onboardingCompleted.value = false
            onComplete()
        }
    }

    private fun initSeedData() {
        val isFirstLaunch = prefs.getBoolean("is_first_launch", true)
        if (isFirstLaunch) {
            prefs.edit().putBoolean("is_first_launch", false).apply()
            viewModelScope.launch {
                // Initial calm tasks to welcome the user
                repository.insertTask(
                    TaskItem(
                        title = "شرب كأس ماء بهدوء",
                        note = "ارتشاف الماء ببطء والتنفس",
                        category = "رعاية ذاتية",
                        period = "صباح",
                        priority = "لطيفة",
                        isGentleHabit = true
                    )
                )
                repository.insertTask(
                    TaskItem(
                        title = "تحديد مهمة واحدة أساسية لليوم",
                        note = "لا تشغل نفسك بالكثير؛ خطوة واحدة تصنع فارقاً",
                        category = "عمل",
                        period = "صباح",
                        priority = "مهمة برفق",
                        isGentleHabit = false
                    )
                )
                repository.insertTask(
                    TaskItem(
                        title = "استراحة 5 دقائق بدون شاشات",
                        note = "النظر بعيداً وإرخاء الكتفين",
                        category = "رعاية ذاتية",
                        period = "بعد الظهر",
                        priority = "لطيفة",
                        isGentleHabit = true
                    )
                )
            }
        }
    }
}
