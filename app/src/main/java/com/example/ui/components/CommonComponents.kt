package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkGreen
import com.example.ui.theme.DeepForest
import com.example.ui.theme.SageGreen
import com.example.ui.theme.SageLight
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.WarmBeige
import com.example.ui.theme.WarmTerracotta

@Composable
fun GentleProgressCard(
    total: Int,
    completed: Int,
    percentage: Float,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("gentle_progress_card"),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "شجرة الإنجاز الهادئ",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = DeepForest
                            )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (completed == total && total > 0) "🌸" else if (completed > 0) "🌿" else "🌱",
                            fontSize = 18.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    val encouragment = when {
                        total == 0 -> "يومك صفحة بيضاء، ابدأ بخطوة لطيفة واحدة متى شئت"
                        completed == total -> "أتممت كل خطوات اليوم برفق، مبارك سكينة إنجازك!"
                        completed > 0 -> "أنجزت $completed من أصل $total خطوة، كل سعي طيب محسوب"
                        else -> "خذ وقتك، كل خطوة تبدأ بلحظة هدوء ونَفَس عميق"
                    }
                    Text(
                        text = encouragment,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextMuted,
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(62.dp)
                ) {
                    CircularProgressIndicator(
                        progress = { if (total > 0) percentage else 0f },
                        modifier = Modifier.size(62.dp),
                        color = DarkGreen,
                        trackColor = SageLight,
                        strokeWidth = 6.dp
                    )
                    Text(
                        text = "${(percentage * 100).toInt()}%",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = DeepForest
                        )
                    )
                }
            }

            if (total > 0) {
                Spacer(modifier = Modifier.height(12.dp))
                // Milestone Leaves Indicator
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val step1Done = percentage >= 0.33f
                    val step2Done = percentage >= 0.66f
                    val step3Done = percentage >= 1f

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = if (step1Done) "🌱" else "⚪", fontSize = 13.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "البداية",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = if (step1Done) DarkGreen else TextMuted,
                                fontWeight = if (step1Done) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 10.sp
                            )
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = if (step2Done) "🌿" else "⚪", fontSize = 13.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "الاستمرار",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = if (step2Done) DarkGreen else TextMuted,
                                fontWeight = if (step2Done) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 10.sp
                            )
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = if (step3Done) "🌸" else "⚪", fontSize = 13.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "الاكتمال",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = if (step3Done) DarkGreen else TextMuted,
                                fontWeight = if (step3Done) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 10.sp
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PriorityBadge(priority: String) {
    val (bgColor, textColor) = when (priority) {
        "مهمة برفق" -> Pair(WarmTerracotta.copy(alpha = 0.2f), WarmTerracotta)
        "عادية" -> Pair(SageGreen.copy(alpha = 0.25f), DeepForest)
        else -> Pair(SageLight.copy(alpha = 0.5f), DarkGreen)
    }

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = bgColor
    ) {
        Text(
            text = priority,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Medium,
                color = textColor,
                fontSize = 11.sp
            )
        )
    }
}

@Composable
fun PeriodChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = if (isSelected) DarkGreen else WarmBeige.copy(alpha = 0.5f),
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
            style = MaterialTheme.typography.labelMedium.copy(
                color = if (isSelected) Color.White else TextDark,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
        )
    }
}

@Composable
fun GentleConfirmationDialog(
    title: String,
    message: String,
    confirmText: String = "تأكيد",
    dismissText: String = "إلغاء",
    isDestructive: Boolean = false,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (isDestructive) WarmTerracotta else DarkGreen
                )
            )
        },
        text = {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium.copy(color = TextDark)
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isDestructive) WarmTerracotta else DarkGreen
                )
            ) {
                Text(confirmText, color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(dismissText, color = TextMuted)
            }
        },
        shape = RoundedCornerShape(18.dp),
        containerColor = MaterialTheme.colorScheme.surface
    )
}
