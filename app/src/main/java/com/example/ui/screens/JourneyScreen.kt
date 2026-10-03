package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CheckInEntry
import com.example.data.model.ReflectionEntry
import com.example.data.model.SavedBookmark
import com.example.data.model.TaskItem
import com.example.ui.components.GentleConfirmationDialog
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun JourneyScreen(
    viewModel: NasmatViewModel,
    modifier: Modifier = Modifier
) {
    val checkIns by viewModel.allCheckIns.collectAsState()
    val reflections by viewModel.allReflections.collectAsState()
    val allTasks by viewModel.allTasks.collectAsState()
    val bookmarks by viewModel.allBookmarks.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("التأملات", "سجل الشعور", "المهام المنجزة", "المفضلة")

    var itemToDelete by remember { mutableStateOf<Any?>(null) }

    val completedTasks = remember(allTasks) { allTasks.filter { it.isCompleted } }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CreamBackground)
            .padding(horizontal = 18.dp)
            .testTag("journey_screen")
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "رحلتي الهادئة",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = DarkGreen
            )
        )
        Text(
            text = "أثر خطواتك وتأملاتك المحفوظة محلياً برضا ورفق",
            style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Tabs
        TabRow(
            selectedTabIndex = selectedTab,
            modifier = Modifier.clip(RoundedCornerShape(16.dp)),
            containerColor = WarmBeige.copy(alpha = 0.5f),
            divider = {}
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTab == index) DarkGreen else TextMuted,
                                fontSize = 12.sp
                            )
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        when (selectedTab) {
            0 -> ReflectionsJourneyTab(
                reflections = reflections,
                onDelete = { itemToDelete = it }
            )
            1 -> CheckInsJourneyTab(
                checkIns = checkIns,
                onDelete = { itemToDelete = it }
            )
            2 -> CompletedTasksJourneyTab(
                tasks = completedTasks,
                onDelete = { itemToDelete = it }
            )
            3 -> BookmarksJourneyTab(
                bookmarks = bookmarks,
                onDelete = { itemToDelete = it }
            )
        }
    }

    if (itemToDelete != null) {
        GentleConfirmationDialog(
            title = "حذف العنصر؟",
            message = "هل ترغب في حذف هذا الإدخال من رحلتك؟",
            confirmText = "حذف",
            isDestructive = true,
            onConfirm = {
                when (val item = itemToDelete) {
                    is ReflectionEntry -> viewModel.deleteReflection(item.id)
                    is CheckInEntry -> viewModel.deleteCheckIn(item.id)
                    is TaskItem -> viewModel.deleteTask(item.id)
                    is SavedBookmark -> viewModel.toggleBookmark(
                        com.example.data.model.ContentArticle(
                            id = item.articleId,
                            title = item.title,
                            subtitle = item.summary,
                            category = item.category,
                            readTimeMinutes = 3,
                            content = emptyList(),
                            gentleTakeaway = ""
                        ),
                        true
                    )
                }
                itemToDelete = null
            },
            onDismiss = { itemToDelete = null }
        )
    }
}

@Composable
fun ReflectionsJourneyTab(
    reflections: List<ReflectionEntry>,
    onDelete: (ReflectionEntry) -> Unit
) {
    if (reflections.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "لم تحفظ تأملات بعد.\nيمكنك كتابة تأمل ذاتي وحفظه هنا في أي وقت.",
                style = MaterialTheme.typography.bodyMedium.copy(color = TextMuted),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(reflections, key = { it.id }) { reflection ->
                val dateStr = remember(reflection.timestamp) {
                    SimpleDateFormat("d MMMM yyyy • h:mm a", Locale("ar")).format(Date(reflection.timestamp))
                }
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = dateStr,
                                style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                            )
                            IconButton(
                                onClick = { onDelete(reflection) },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DeleteOutline,
                                    contentDescription = "حذف",
                                    tint = TextMuted,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = reflection.promptQuestion,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = DarkGreen
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = reflection.answer,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = TextDark,
                                lineHeight = 24.sp
                            )
                        )
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}

@Composable
fun CheckInsJourneyTab(
    checkIns: List<CheckInEntry>,
    onDelete: (CheckInEntry) -> Unit
) {
    if (checkIns.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "لا توجد تسجيلات شعور مسجلة حتى الآن.",
                style = MaterialTheme.typography.bodyMedium.copy(color = TextMuted)
            )
        }
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(checkIns, key = { it.id }) { checkIn ->
                val dateStr = remember(checkIn.timestamp) {
                    SimpleDateFormat("EEEE، d MMMM • h:mm a", Locale("ar")).format(Date(checkIn.timestamp))
                }
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
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
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(SageLight)
                        ) {
                            Text(text = checkIn.feelingEmoji, fontSize = 22.sp)
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = checkIn.feelingName,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = DeepForest
                                    )
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "طاقة: ${checkIn.energyLevel}/5",
                                    style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                                )
                            }
                            if (checkIn.note.isNotBlank()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = checkIn.note,
                                    style = MaterialTheme.typography.bodySmall.copy(color = TextDark)
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = dateStr,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TextMuted,
                                    fontSize = 11.sp
                                )
                            )
                        }

                        IconButton(
                            onClick = { onDelete(checkIn) },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.DeleteOutline,
                                contentDescription = "حذف",
                                tint = TextMuted,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}

@Composable
fun CompletedTasksJourneyTab(
    tasks: List<TaskItem>,
    onDelete: (TaskItem) -> Unit
) {
    if (tasks.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "المهام التي تكتمل برفق ستظهر هنا كأثر جميل لإنجازك.",
                style = MaterialTheme.typography.bodyMedium.copy(color = TextMuted)
            )
        }
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(tasks, key = { it.id }) { task ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = WarmBeige.copy(alpha = 0.45f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = null,
                            tint = DarkGreen,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = task.title,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Medium,
                                    color = DeepForest
                                )
                            )
                            Text(
                                text = "${task.category} • ${task.priority}",
                                style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                            )
                        }
                        IconButton(
                            onClick = { onDelete(task) },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.DeleteOutline,
                                contentDescription = "حذف",
                                tint = TextMuted,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}

@Composable
fun BookmarksJourneyTab(
    bookmarks: List<SavedBookmark>,
    onDelete: (SavedBookmark) -> Unit
) {
    if (bookmarks.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "لم تحفظ مقالات في المفضلة بعد.",
                style = MaterialTheme.typography.bodyMedium.copy(color = TextMuted)
            )
        }
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(bookmarks, key = { it.articleId }) { bookmark ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Bookmark,
                            contentDescription = null,
                            tint = DarkGreen,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = bookmark.title,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = DeepForest
                                )
                            )
                            Text(
                                text = bookmark.category,
                                style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                            )
                        }
                        IconButton(
                            onClick = { onDelete(bookmark) },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.DeleteOutline,
                                contentDescription = "إزالة",
                                tint = TextMuted,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}
