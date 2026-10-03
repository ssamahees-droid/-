package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Mood
import androidx.compose.material.icons.filled.Nature
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.CheckInEntry
import com.example.data.model.TaskItem
import com.example.ui.components.GentleConfirmationDialog
import com.example.ui.components.GentleProgressCard
import com.example.ui.components.LivingSkyAtmosphere
import com.example.ui.components.MindfulWhisperJar
import com.example.ui.components.PeriodChip
import com.example.ui.components.PriorityBadge
import com.example.ui.theme.CreamBackground
import com.example.ui.theme.DarkGreen
import com.example.ui.theme.DeepForest
import com.example.ui.theme.SageGreen
import com.example.ui.theme.SageLight
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.WarmBeige
import com.example.ui.theme.WarmTerracotta
import com.example.ui.viewmodel.NasmatViewModel
import com.example.ui.viewmodel.TaskCompletionFilter
import com.example.util.CalmAudioEngine
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: NasmatViewModel,
    onNavigateToBreathing: () -> Unit,
    onNavigateToLightSpace: () -> Unit,
    onNavigateToDrafts: () -> Unit,
    onNavigateToGwayaHekaya: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val tasks by viewModel.filteredTasks.collectAsState()
    val allTasks by viewModel.allTasks.collectAsState()
    val progress by viewModel.dailyProgress.collectAsState()
    val filter by viewModel.taskFilter.collectAsState()
    val selectedPeriod by viewModel.selectedPeriodFilter.collectAsState()
    val latestCheckIn by viewModel.latestCheckIn.collectAsState()

    var showAddTaskDialog by remember { mutableStateOf(false) }
    var taskToEdit by remember { mutableStateOf<TaskItem?>(null) }
    var taskToDelete by remember { mutableStateOf<TaskItem?>(null) }
    var showCheckInSheet by remember { mutableStateOf(false) }
    var showReflectionSheet by remember { mutableStateOf(false) }

    val arabicDate = remember {
        val sdf = SimpleDateFormat("EEEE، d MMMM", Locale("ar"))
        sdf.format(Date())
    }

    Box(modifier = modifier.fillMaxSize().background(CreamBackground)) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp)
                .testTag("home_screen_scroll"),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Top Bar
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "نسمة حياة",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = DarkGreen
                            )
                        )
                        Text(
                            text = arabicDate,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = TextMuted
                            )
                        )
                    }

                    // Gentle feeling button / avatar
                    Surface(
                        shape = CircleShape,
                        color = SageLight,
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .clickable { showCheckInSheet = true }
                            .testTag("open_checkin_button")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            if (latestCheckIn != null) {
                                Text(
                                    text = latestCheckIn!!.feelingEmoji,
                                    fontSize = 22.sp
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Spa,
                                    contentDescription = "تسجيل شعور",
                                    tint = DarkGreen,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Atmospheric Living Sky of the Present
            item {
                LivingSkyAtmosphere()
            }

            // Hero Gentle Banner
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = WarmBeige.copy(alpha = 0.5f))
                ) {
                    Column {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(115.dp)
                                .clip(RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp))
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.hero_nasmat_hayat_1790892087751),
                                contentDescription = "نسمة حياة",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Color.Black.copy(alpha = 0.15f)
                                    )
                            )
                        }

                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "«على مهل وبلا ضغط، اختر ما يناسبك الآن. ليس مطلوباً إيجاد جواب لكل شيء اليوم.»",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Medium,
                                    color = DeepForest,
                                    lineHeight = 22.sp
                                )
                            )
                        }
                    }
                }
            }

            // The Soul-Whisper Jar (جرّة السكينة)
            item {
                MindfulWhisperJar(
                    onSaveToReflections = { question, answer ->
                        viewModel.addReflection(question, answer)
                    }
                )
            }

            // Quick Soundscape & Relaxing Music
            item {
                val isAudioPlaying by CalmAudioEngine.isPlayingFlow.collectAsState()
                val currentTrack by CalmAudioEngine.currentTrackFlow.collectAsState()

                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isAudioPlaying) DeepForest else Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .clickable {
                            if (isAudioPlaying) CalmAudioEngine.stop()
                            else CalmAudioEngine.start(currentTrack?.id ?: "music_piano")
                        }
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(if (isAudioPlaying) SageLight.copy(alpha = 0.25f) else SageLight)
                        ) {
                            Text(text = currentTrack?.iconEmoji ?: "🎹", fontSize = 20.sp)
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isAudioPlaying) currentTrack?.title ?: "مشغّل السكينة" else "موسيقى السكينة والاسترخاء",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (isAudioPlaying) Color.White else DeepForest
                                )
                            )
                            Text(
                                text = if (isAudioPlaying) "انقر للإيقاف المؤقت • تردد 432Hz دافئ" else "ألحان بيانو هادئة لتهدئة الذهن بنقرة واحدة",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = if (isAudioPlaying) SageLight else TextMuted,
                                    fontSize = 11.sp
                                )
                            )
                        }

                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(if (isAudioPlaying) Color.White else DarkGreen)
                        ) {
                            Icon(
                                imageVector = if (isAudioPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = if (isAudioPlaying) "إيقاف" else "تشغيل",
                                tint = if (isAudioPlaying) DeepForest else Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            // Daily Progress
            item {
                GentleProgressCard(
                    total = progress.total,
                    completed = progress.completed,
                    percentage = progress.percentage
                )
            }

            // Quick Habits / Gentle Presets
            item {
                Column {
                    Text(
                        text = "إضافة سريعة لخطوة هادئة",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = DeepForest
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val quickHabits = listOf(
                            Triple("💧 شرب كأس ماء", "رعاية ذاتية", "أي وقت"),
                            Triple("🫁 تنفس عميق 3 دقائق", "رعاية ذاتية", "أي وقت"),
                            Triple("🚶‍♀️ مشي خفيف 10 دقائق", "صحة", "بعد الظهر"),
                            Triple("☕ استراحة شاي بلا شاشات", "هدوء", "صباح"),
                            Triple("🪴 ترتيب طاولة العمل", "بيت", "صباح")
                        )
                        items(quickHabits) { habit ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = SageLight.copy(alpha = 0.5f),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable {
                                        viewModel.addTask(
                                            title = habit.first,
                                            category = habit.second,
                                            period = habit.third,
                                            priority = "لطيفة",
                                            isGentleHabit = true
                                        )
                                    }
                            ) {
                                Text(
                                    text = habit.first,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontWeight = FontWeight.Medium,
                                        color = DeepForest
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // Filter Tabs (الكل، المتبقية، المكتملة)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "مهام اليوم اللطيفة",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = DeepForest
                        )
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        PeriodChip(
                            text = "الكل",
                            isSelected = filter == TaskCompletionFilter.ALL,
                            onClick = { viewModel.setTaskFilter(TaskCompletionFilter.ALL) }
                        )
                        PeriodChip(
                            text = "المتبقية",
                            isSelected = filter == TaskCompletionFilter.ACTIVE,
                            onClick = { viewModel.setTaskFilter(TaskCompletionFilter.ACTIVE) }
                        )
                        PeriodChip(
                            text = "المكتملة",
                            isSelected = filter == TaskCompletionFilter.COMPLETED,
                            onClick = { viewModel.setTaskFilter(TaskCompletionFilter.COMPLETED) }
                        )
                    }
                }
            }

            // Period Filters (صباح، بعد الظهر، مساء)
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    val periods = listOf("الكل", "صباح", "بعد الظهر", "مساء")
                    periods.forEach { period ->
                        val isSelected = selectedPeriod == period
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) SageGreen.copy(alpha = 0.35f) else Color.Transparent,
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { viewModel.setPeriodFilter(period) }
                        ) {
                            Text(
                                text = period,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (isSelected) DarkGreen else TextMuted,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            )
                        }
                    }
                }
            }

            // Tasks List
            if (tasks.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = WarmBeige.copy(alpha = 0.3f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Nature,
                                contentDescription = null,
                                tint = SageGreen,
                                modifier = Modifier.size(42.dp)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = if (allTasks.isEmpty()) "لا توجد خطوات مضافة بعد" else "لا توجد مهام في هذا التصفية",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Medium,
                                    color = DeepForest
                                )
                            )
                            Text(
                                text = "اضغط على زر (+) في الأسفل لإضافة خطوة لطيفة ليومك",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextMuted,
                                    fontSize = 12.sp
                                )
                            )
                        }
                    }
                }
            } else {
                items(tasks, key = { it.id }) { task ->
                    TaskCardItem(
                        task = task,
                        onToggle = { viewModel.toggleTaskCompletion(task) },
                        onEdit = { taskToEdit = task },
                        onDelete = { taskToDelete = task }
                    )
                }
            }

            // Mindful Shortcuts Grid
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "واحات للسكينة والترويح",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = DeepForest
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MindfulQuickCard(
                        title = "تمرين التنفس",
                        subtitle = "شهيق 4 وزفير 6",
                        icon = Icons.Default.Air,
                        bgColor = SageLight.copy(alpha = 0.6f),
                        onClick = onNavigateToBreathing,
                        modifier = Modifier.weight(1f)
                    )
                    MindfulQuickCard(
                        title = "تأمل ذاتي",
                        subtitle = "سؤال مفتوح قصير",
                        icon = Icons.Default.SelfImprovement,
                        bgColor = WarmBeige.copy(alpha = 0.8f),
                        onClick = { showReflectionSheet = true },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MindfulQuickCard(
                        title = "مساحة خفيفة",
                        subtitle = "ألعاب وأفكار جميلة",
                        icon = Icons.Default.AutoAwesome,
                        bgColor = WarmBeige.copy(alpha = 0.8f),
                        onClick = onNavigateToLightSpace,
                        modifier = Modifier.weight(1f)
                    )
                    MindfulQuickCard(
                        title = "مسودة لنفسي",
                        subtitle = "خواطر لا تُرسل لأحد",
                        icon = Icons.Default.EditNote,
                        bgColor = SageLight.copy(alpha = 0.6f),
                        onClick = onNavigateToDrafts,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Featured Gwaya Hekaya game shortcut card
                MindfulQuickCard(
                    title = "لعبة «جوايا حكاية» 🌟",
                    subtitle = "رحلة من ٦ محطات لفهم الذات وإعادة صياغة الأفكار برفق",
                    icon = Icons.Default.Spa,
                    bgColor = SageLight.copy(alpha = 0.85f),
                    onClick = onNavigateToGwayaHekaya,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(90.dp))
            }
        }

        // Floating Action Button for adding new tasks
        FloatingActionButton(
            onClick = { showAddTaskDialog = true },
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(24.dp)
                .testTag("add_task_fab"),
            containerColor = DarkGreen,
            contentColor = Color.White,
            shape = RoundedCornerShape(18.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "إضافة خطوة")
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "خطوة جديدة",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }

    // Add / Edit Task Dialog
    if (showAddTaskDialog) {
        AddOrEditTaskDialog(
            taskToEdit = null,
            onDismiss = { showAddTaskDialog = false },
            onSave = { title, note, category, period, priority ->
                viewModel.addTask(title, note, category, period, priority)
                showAddTaskDialog = false
            }
        )
    }

    if (taskToEdit != null) {
        AddOrEditTaskDialog(
            taskToEdit = taskToEdit,
            onDismiss = { taskToEdit = null },
            onSave = { title, note, category, period, priority ->
                viewModel.updateTask(
                    taskToEdit!!.copy(
                        title = title,
                        note = note,
                        category = category,
                        period = period,
                        priority = priority
                    )
                )
                taskToEdit = null
            }
        )
    }

    // Delete confirmation dialog
    if (taskToDelete != null) {
        GentleConfirmationDialog(
            title = "حذف الخطوة؟",
            message = "هل ترغب في حذف «${taskToDelete!!.title}»؟",
            confirmText = "حذف",
            isDestructive = true,
            onConfirm = {
                viewModel.deleteTask(taskToDelete!!.id)
                taskToDelete = null
            },
            onDismiss = { taskToDelete = null }
        )
    }

    // Check-in BottomSheet
    if (showCheckInSheet) {
        CheckInBottomSheet(
            onDismiss = { showCheckInSheet = false },
            onSave = { feeling, emoji, note, energy ->
                viewModel.addCheckIn(feeling, emoji, note, energy)
                showCheckInSheet = false
            }
        )
    }

    // Reflection BottomSheet
    if (showReflectionSheet) {
        ReflectionBottomSheet(
            onDismiss = { showReflectionSheet = false },
            onSave = { question, answer ->
                viewModel.addReflection(question, answer)
                showReflectionSheet = false
            }
        )
    }
}

@Composable
fun TaskCardItem(
    task: TaskItem,
    onToggle: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cardColor by animateColorAsState(
        targetValue = if (task.isCompleted) WarmBeige.copy(alpha = 0.35f) else Color.White,
        animationSpec = tween(200),
        label = "cardColor"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("task_item_${task.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (task.isCompleted) 0.dp else 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Checkbox Icon Button
            IconButton(
                onClick = onToggle,
                modifier = Modifier
                    .size(36.dp)
                    .testTag("task_checkbox_${task.id}")
            ) {
                if (task.isCompleted) {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = "مكتملة",
                        tint = DarkGreen,
                        modifier = Modifier.size(26.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Outlined.Circle,
                        contentDescription = "غير مكتملة",
                        tint = SageGreen,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable(onClick = onToggle)
            ) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = if (task.isCompleted) FontWeight.Normal else FontWeight.SemiBold,
                        color = if (task.isCompleted) TextMuted else TextDark,
                        textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                if (task.note.isNotBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = task.note,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextMuted,
                            fontSize = 12.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PriorityBadge(priority = task.priority)

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = WarmBeige.copy(alpha = 0.5f)
                    ) {
                        Text(
                            text = task.category,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TextDark,
                                fontSize = 10.sp
                            )
                        )
                    }

                    if (task.period != "أي وقت") {
                        Text(
                            text = "• ${task.period}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TextMuted,
                                fontSize = 10.sp
                            )
                        )
                    }
                }
            }

            // Actions: Edit and Delete
            IconButton(
                onClick = onEdit,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "تعديل",
                    tint = TextMuted.copy(alpha = 0.7f),
                    modifier = Modifier.size(18.dp)
                )
            }

            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.DeleteOutline,
                    contentDescription = "حذف",
                    tint = TextMuted.copy(alpha = 0.7f),
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
fun MindfulQuickCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    bgColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = bgColor,
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.6f))
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = DarkGreen,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = DeepForest
                    )
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                )
            }
        }
    }
}
