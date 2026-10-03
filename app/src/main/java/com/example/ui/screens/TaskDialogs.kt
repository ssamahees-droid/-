package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.TaskItem
import com.example.ui.theme.DarkGreen
import com.example.ui.theme.DeepForest
import com.example.ui.theme.SageGreen
import com.example.ui.theme.SageLight
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.WarmBeige
import com.example.ui.theme.WarmTerracotta

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddOrEditTaskDialog(
    taskToEdit: TaskItem? = null,
    onDismiss: () -> Unit,
    onSave: (title: String, note: String, category: String, period: String, priority: String) -> Unit
) {
    var title by remember { mutableStateOf(taskToEdit?.title ?: "") }
    var note by remember { mutableStateOf(taskToEdit?.note ?: "") }
    var category by remember { mutableStateOf(taskToEdit?.category ?: "رعاية ذاتية") }
    var period by remember { mutableStateOf(taskToEdit?.period ?: "أي وقت") }
    var priority by remember { mutableStateOf(taskToEdit?.priority ?: "لطيفة") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val categories = listOf("رعاية ذاتية", "عمل", "بيت", "صحة", "دراسة", "هدوء")
    val periods = listOf("أي وقت", "صباح", "بعد الظهر", "مساء")
    val priorities = listOf("لطيفة", "عادية", "مهمة برفق")

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .testTag("task_dialog")
        ) {
            Column(
                modifier = Modifier
                    .padding(22.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (taskToEdit == null) "إضافة خطوة لطيفة" else "تعديل المهمة",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = DeepForest
                        )
                    )
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "إغلاق",
                            tint = TextMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = {
                        title = it
                        if (errorMessage != null) errorMessage = null
                    },
                    label = { Text("ما الخطوة التي تود إنجازها؟") },
                    placeholder = { Text("مثال: شرب كوب شاي دافئ بهدوء") },
                    isError = errorMessage != null,
                    supportingText = errorMessage?.let { { Text(it, color = WarmTerracotta) } },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("task_title_input"),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DarkGreen,
                        unfocusedBorderColor = SageLight
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("ملاحظة أو تذكير رقيق (اختياري)") },
                    placeholder = { Text("مثال: بدون استعجال، على مهل") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("task_note_input"),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DarkGreen,
                        unfocusedBorderColor = SageLight
                    ),
                    maxLines = 3
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "التصنيف",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = DeepForest
                    )
                )
                Spacer(modifier = Modifier.height(6.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categories.forEach { cat ->
                        val isSelected = category == cat
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) DarkGreen else WarmBeige.copy(alpha = 0.5f),
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { category = cat }
                        ) {
                            Text(
                                text = cat,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = if (isSelected) Color.White else TextDark,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "فترة اليوم",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = DeepForest
                    )
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    periods.forEach { per ->
                        val isSelected = period == per
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) SageGreen else WarmBeige.copy(alpha = 0.5f),
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { period = per }
                        ) {
                            Text(
                                text = per,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = if (isSelected) DeepForest else TextDark,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "الأولوية",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = DeepForest
                    )
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    priorities.forEach { prio ->
                        val isSelected = priority == prio
                        val color = when (prio) {
                            "مهمة برفق" -> WarmTerracotta
                            "عادية" -> DarkGreen
                            else -> SageGreen
                        }
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) color else WarmBeige.copy(alpha = 0.5f),
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { priority = prio }
                        ) {
                            Text(
                                text = prio,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = if (isSelected) Color.White else TextDark,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("إلغاء", color = TextMuted)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (title.isBlank()) {
                                errorMessage = "يرجى كتابة عنوان الخطوة"
                            } else {
                                onSave(title, note, category, period, priority)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.testTag("save_task_button")
                    ) {
                        Text("حفظ الخطوة", color = Color.White)
                    }
                }
            }
        }
    }
}

data class FeelingOption(val name: String, val emoji: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckInBottomSheet(
    onDismiss: () -> Unit,
    onSave: (feeling: String, emoji: String, note: String, energy: Int) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val feelings = listOf(
        FeelingOption("مرتاح", "🌿"),
        FeelingOption("ممتن", "🌸"),
        FeelingOption("هادئ", "☁️"),
        FeelingOption("متفائل", "☀️"),
        FeelingOption("مشتت", "🍃"),
        FeelingOption("متعب", "🛋️"),
        FeelingOption("قلق", "🌧️")
    )

    var selectedFeeling by remember { mutableStateOf(feelings[0]) }
    var note by remember { mutableStateOf("") }
    var energy by remember { mutableFloatStateOf(3f) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
                .testTag("check_in_sheet")
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "كيف حالك الآن؟",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = DeepForest
                        )
                    )
                    Text(
                        text = "تسجيل شعور يومي غير تشخيصي، لنفسك فقط",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "إغلاق", tint = TextMuted)
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "اختر ما يصفك برفق:",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = DeepForest
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                feelings.forEach { feeling ->
                    val isSelected = selectedFeeling == feeling
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .clickable { selectedFeeling = feeling }
                            .padding(4.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isSelected) SageGreen else WarmBeige.copy(alpha = 0.5f)
                                )
                                .border(
                                    width = if (isSelected) 2.dp else 0.dp,
                                    color = if (isSelected) DarkGreen else Color.Transparent,
                                    shape = CircleShape
                                )
                        ) {
                            Text(text = feeling.emoji, fontSize = 20.sp)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = feeling.name,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) DarkGreen else TextDark
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "مستوى طاقتك اليوم (${energy.toInt()} من 5):",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = DeepForest
                )
            )

            Slider(
                value = energy,
                onValueChange = { energy = it },
                valueRange = 1f..5f,
                steps = 3,
                colors = SliderDefaults.colors(
                    thumbColor = DarkGreen,
                    activeTrackColor = DarkGreen,
                    inactiveTrackColor = SageLight
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("كلمة أو خاطرة قصيرة (اختياري)") },
                placeholder = { Text("ما الذي يدور في ذهنك اليوم؟") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DarkGreen,
                    unfocusedBorderColor = SageLight
                ),
                maxLines = 3
            )

            Spacer(modifier = Modifier.height(22.dp))

            Button(
                onClick = {
                    onSave(selectedFeeling.name, selectedFeeling.emoji, note, energy.toInt())
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("save_check_in_button"),
                colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(
                    text = "تسجيل شعور اليوم",
                    color = Color.White,
                    modifier = Modifier.padding(vertical = 4.dp),
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReflectionBottomSheet(
    onDismiss: () -> Unit,
    onSave: (question: String, answer: String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val promptPool = listOf(
        "ما الذي ساعدك على الشعور بالهدوء أو الأمان اليوم؟",
        "شيء واحد بسيط وصغير تشعر بالامتنان له في هذه اللحظة؟",
        "لو كنت ستخاطب نفسك برفق الآن، ماذا تود أن تقول لها؟",
        "ما الفكرة أو الثقل الذي ترغب في تركه خلفك لليوم؟",
        "ما الجمال الذي لمسته حولك دون أن تلاحظه في البداية؟"
    )

    var currentPromptIndex by remember { mutableIntStateOf(0) }
    var answer by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val currentQuestion = promptPool[currentPromptIndex]

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
                .testTag("reflection_sheet")
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = DarkGreen,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "تأمل ذاتي لطيف",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = DeepForest
                        )
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "إغلاق", tint = TextMuted)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = WarmBeige.copy(alpha = 0.6f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "سؤال اليوم:",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = TextMuted,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        TextButton(
                            onClick = {
                                currentPromptIndex = (currentPromptIndex + 1) % promptPool.size
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "سؤال آخر",
                                modifier = Modifier.size(16.dp),
                                tint = DarkGreen
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("سؤال آخر", color = DarkGreen, fontSize = 12.sp)
                        }
                    }
                    Text(
                        text = currentQuestion,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Medium,
                            color = DeepForest,
                            lineHeight = 24.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = answer,
                onValueChange = {
                    answer = it
                    if (errorMessage != null) errorMessage = null
                },
                placeholder = { Text("اكتب أفكارك بحرية، لا توجد إجابة صحيحة أو خاطئة...") },
                isError = errorMessage != null,
                supportingText = errorMessage?.let { { Text(it, color = WarmTerracotta) } },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .testTag("reflection_answer_input"),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DarkGreen,
                    unfocusedBorderColor = SageLight
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "ملاحظة: الحفظ في «رحلتي» اختياري، وتظل أفكارك على جهازك فقط.",
                style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onDismiss) {
                    Text("إغلاق دون حفظ", color = TextMuted)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        if (answer.isBlank()) {
                            errorMessage = "يرجى كتابة بضع كلمات قبل الحفظ"
                        } else {
                            onSave(currentQuestion, answer)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.testTag("save_reflection_button")
                ) {
                    Text("حفظ في «رحلتي»", color = Color.White)
                }
            }
        }
    }
}
