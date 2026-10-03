package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

@Composable
fun ProfileScreen(
    viewModel: NasmatViewModel,
    onNavigateToDrafts: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val holdEnabled by viewModel.holdPhaseEnabled.collectAsState()
    var showClearDataDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CreamBackground)
            .padding(horizontal = 18.dp)
            .verticalScroll(rememberScrollState())
            .testTag("profile_screen")
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "الملف والخصوصية",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = DarkGreen
            )
        )
        Text(
            text = "معلومات التخزين المحلي، السلامة، وضبط التفضيلات",
            style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Privacy & Offline Badge Card
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(SageLight)
                    ) {
                        Icon(imageVector = Icons.Default.Shield, contentDescription = null, tint = DarkGreen)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "بياناتك على جهازك فقط",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = DeepForest
                            )
                        )
                        Text(
                            text = "خصوصية تامة بدون حساب أو خوادم خارجية",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "جميع ما تسجله في «نسمة حياة» — من مهام يومية، وتسجيلات مشاعر، وتأملات، ومسودات — يُحفظ محلياً على هاتفك دون مشاركة أو نقل لأي طرف خارجي أو خادم سحابي.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = TextDark,
                        lineHeight = 22.sp
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Shortcuts & Settings
        Text(
            text = "تفضيلات التطبيق",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = DeepForest
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Breathing hold option
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "تثبيت النفس لثانيتين في تمرين التنفس",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Medium,
                                color = DeepForest
                            )
                        )
                        Text(
                            text = "إضافة مرحلة تثبيت هادئة بين الشهيق والزفير",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextMuted, fontSize = 11.sp)
                        )
                    }
                    Switch(
                        checked = holdEnabled,
                        onCheckedChange = { viewModel.setHoldPhaseEnabled(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = DarkGreen,
                            uncheckedTrackColor = SageLight
                        )
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Support drafts shortcut
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = WarmBeige.copy(alpha = 0.4f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable(onClick = onNavigateToDrafts)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.EditNote, contentDescription = null, tint = DarkGreen)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "إدارة المسودات الشخصية",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = DeepForest
                                )
                            )
                        }
                        Text(text = "عرض ❯", color = DarkGreen, fontSize = 12.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Safety & Scope Notice
        Text(
            text = "إشعار السلامة وحدود الخدمة",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = DeepForest
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = WarmBeige.copy(alpha = 0.5f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.HelpOutline, contentDescription = null, tint = DarkGreen)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "«نسمة حياة» مساحة توعية ورفق",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = DeepForest
                        )
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "هذا التطبيق مخصص للتوعية العامة، تنظيم المهام، وممارسات الهدوء والتنفس. ليس بديلاً عن التشخيص الطبي أو الرعاية النفسية المتخصصة أو خدمات الطوارئ.\n\nإذا كنت أنت أو شخص قريب يمر بحالة طارئة أو ضيق شديد، يرجى التواصل فوراً مع خدمات الطوارئ المحلية في بلدك أو التحدث مع شخص موثوق ومقرب.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextDark,
                        lineHeight = 20.sp
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Clear All Data Section
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "إدارة البيانات وإعادة التهيئة",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = DeepForest
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "يمكنك مسح كافة المهام والمدخلات المحلية بالكامل وإعادة التطبيق إلى حالته الأولى.",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                )
                Spacer(modifier = Modifier.height(14.dp))

                OutlinedButton(
                    onClick = { showClearDataDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("clear_all_data_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = WarmTerracotta)
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteForever,
                        contentDescription = null,
                        tint = WarmTerracotta,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "حذف جميع البيانات المحلية",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(100.dp))
    }

    if (showClearDataDialog) {
        GentleConfirmationDialog(
            title = "مسح جميع البيانات المحلية؟",
            message = "سيتم حذف كل المهام، تسجيلات الشعور، التأملات، والمسودات نهائياً من هذا الهاتف، والعودة إلى شاشة الترحيب. لا يمكن التراجع عن هذا الإجراء.",
            confirmText = "مسح كل شيء",
            isDestructive = true,
            onConfirm = {
                viewModel.clearAllData {
                    Toast.makeText(context, "تم مسح جميع البيانات المحلية بنجاح", Toast.LENGTH_SHORT).show()
                }
                showClearDataDialog = false
            },
            onDismiss = { showClearDataDialog = false }
        )
    }
}
