package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.SupportDraft
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
fun SupportDraftsScreen(
    viewModel: NasmatViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    val context = LocalContext.current
    val drafts by viewModel.allDrafts.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }
    var draftToEdit by remember { mutableStateOf<SupportDraft?>(null) }
    var draftToDelete by remember { mutableStateOf<SupportDraft?>(null) }

    fun copyToClipboard(text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("مسودة نسمة حياة", text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "تم نسخ المسودة للحافظة", Toast.LENGTH_SHORT).show()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CreamBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp)
                .testTag("support_drafts_screen")
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "رجوع",
                        tint = DarkGreen
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                    Text(
                        text = "مسودات شخصية لنفسي",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = DeepForest
                        )
                    )
                    Text(
                        text = "مساحة آمنة للتعبير وتجهيز ما تود قوله",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Prominent Local-Only Reassurance Banner
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SageLight.copy(alpha = 0.55f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = DarkGreen,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "هذه المسودات لن تُرسل إلى أحد مطلقاً، ولا تغادر هذا الجهاز. يمكنك صياغتها لتفريغ ذهنك أو نسخها ومشاركتها بنفسك متى ما أردت.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = DeepForest,
                            lineHeight = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (drafts.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "لا توجد مسودات حالياً.\nاضغط على زر (+) لكتابة مسودة خاصة بك.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextMuted),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(drafts, key = { it.id }) { draft ->
                        val dateStr = remember(draft.updatedAt) {
                            SimpleDateFormat("d MMMM yyyy", Locale("ar")).format(Date(draft.updatedAt))
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
                                        text = draft.title,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = DeepForest
                                        )
                                    )
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = WarmBeige.copy(alpha = 0.6f)
                                    ) {
                                        Text(
                                            text = draft.intendedRecipientRole,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                            style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = draft.content,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = TextDark,
                                        lineHeight = 22.sp
                                    )
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "آخر تحديث: $dateStr",
                                        style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                                    )

                                    Row {
                                        IconButton(
                                            onClick = { copyToClipboard("${draft.title}\n\n${draft.content}") },
                                            modifier = Modifier.size(32.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.ContentCopy,
                                                contentDescription = "نسخ",
                                                tint = DarkGreen,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                        IconButton(
                                            onClick = { draftToEdit = draft },
                                            modifier = Modifier.size(32.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Edit,
                                                contentDescription = "تعديل",
                                                tint = TextMuted,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                        IconButton(
                                            onClick = { draftToDelete = draft },
                                            modifier = Modifier.size(32.dp)
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
                        }
                    }
                    item { Spacer(modifier = Modifier.height(90.dp)) }
                }
            }
        }

        // FAB to add draft
        FloatingActionButton(
            onClick = { showAddDialog = true },
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(24.dp)
                .testTag("add_draft_fab"),
            containerColor = DarkGreen,
            contentColor = Color.White,
            shape = RoundedCornerShape(18.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "مسودة جديدة")
                Spacer(modifier = Modifier.width(6.dp))
                Text("مسودة جديدة", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
            }
        }
    }

    // Add / Edit Dialog
    if (showAddDialog) {
        DraftEditDialog(
            draft = null,
            onDismiss = { showAddDialog = false },
            onSave = { title, content, role ->
                viewModel.addDraft(title, content, role)
                showAddDialog = false
            }
        )
    }

    if (draftToEdit != null) {
        DraftEditDialog(
            draft = draftToEdit,
            onDismiss = { draftToEdit = null },
            onSave = { title, content, role ->
                viewModel.updateDraft(
                    draftToEdit!!.copy(
                        title = title,
                        content = content,
                        intendedRecipientRole = role
                    )
                )
                draftToEdit = null
            }
        )
    }

    if (draftToDelete != null) {
        GentleConfirmationDialog(
            title = "حذف المسودة؟",
            message = "هل ترغب في حذف مسودة «${draftToDelete!!.title}»؟",
            confirmText = "حذف",
            isDestructive = true,
            onConfirm = {
                viewModel.deleteDraft(draftToDelete!!.id)
                draftToDelete = null
            },
            onDismiss = { draftToDelete = null }
        )
    }
}

@Composable
fun DraftEditDialog(
    draft: SupportDraft?,
    onDismiss: () -> Unit,
    onSave: (title: String, content: String, role: String) -> Unit
) {
    var title by remember { mutableStateOf(draft?.title ?: "") }
    var content by remember { mutableStateOf(draft?.content ?: "") }
    var role by remember { mutableStateOf(draft?.intendedRecipientRole ?: "لنفسي") }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    val roles = listOf("لنفسي", "لشخص موثوق", "لمختص لاحقاً")

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(22.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
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
                        text = if (draft == null) "مسودة جديدة لنفسك" else "تعديل المسودة",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = DeepForest
                        )
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "إغلاق", tint = TextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = {
                        title = it
                        if (errorMsg != null) errorMsg = null
                    },
                    label = { Text("عنوان المسودة") },
                    placeholder = { Text("مثال: ما أشعر به هذا الأسبوع") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DarkGreen,
                        unfocusedBorderColor = SageLight
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = content,
                    onValueChange = {
                        content = it
                        if (errorMsg != null) errorMsg = null
                    },
                    label = { Text("نص المسودة") },
                    placeholder = { Text("اكتب ما تشعر به بحرية كاملة، هذه الكلمات لك وحدك...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DarkGreen,
                        unfocusedBorderColor = SageLight
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "الوجهة التقديرية للمسودة:",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = DeepForest
                    )
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    roles.forEach { r ->
                        val isSelected = role == r
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) DarkGreen else WarmBeige.copy(alpha = 0.5f),
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { role = r }
                        ) {
                            Text(
                                text = r,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = if (isSelected) Color.White else TextDark,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            )
                        }
                    }
                }

                if (errorMsg != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = errorMsg!!, color = WarmTerracotta, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(20.dp))

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
                            if (title.isBlank() || content.isBlank()) {
                                errorMsg = "يرجى كتابة العنوان والنص"
                            } else {
                                onSave(title, content, role)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("حفظ المسودة محلياً", color = Color.White)
                    }
                }
            }
        }
    }
}
