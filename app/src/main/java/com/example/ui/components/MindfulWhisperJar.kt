package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkGreen
import com.example.ui.theme.DeepForest
import com.example.ui.theme.GoldenAmber
import com.example.ui.theme.SageGreen
import com.example.ui.theme.SageLight
import com.example.ui.theme.SoftPeach
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.WarmBeige
import com.example.ui.theme.WarmTerracotta
import kotlinx.coroutines.launch
import kotlin.random.Random

data class WhisperNote(
    val id: Int,
    val category: String,
    val text: String,
    val sourceOrTakeaway: String
)

@Composable
fun MindfulWhisperJar(
    onSaveToReflections: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val whispers = remember {
        listOf(
            WhisperNote(1, "الرفق بالنفس", "«أنت لست متأخراً عن أحد؛ لكل إنسان مساره وتوقيته الخاص المناسب له تماماً.»", "تذكير يومي"),
            WhisperNote(2, "الراحة والأمان", "«ما لا يُنجز اليوم يمكنه الانتظار للغد بأمان؛ سلامك الداخلي وصحتك أهم من أي قائمة.»", "تهدئة الضغط"),
            WhisperNote(3, "التنفس والجسد", "«توقف الآن لثوانٍ معدودة… أرخِ كتفيك، فكّ الشد عن فكك، وخذ نفساً عميقاً هادئاً.»", "لحظة حضور"),
            WhisperNote(4, "الخطوة الصغيرة", "«كل خطوة صغيرة تخطوها اليوم بهدوء، حتى وإن بدت ضئيلة للآخرين، هي إنجاز حقيقي كامل.»", "خطوة بخطوة"),
            WhisperNote(5, "القبول والرحمة", "«لست مطالباً بأن تكون مثالياً في كل شيء؛ الوجود بصدق ورفق مع النفس كافٍ وزيادة.»", "سلام داخلي"),
            WhisperNote(6, "التعامل مع القلق", "«الأفكار المقلقة مجرد سحب عابرة في سماء وعيك وليست حقائق مطلقة؛ دعها تمر بسلام.»", "نقاء الذهن"),
            WhisperNote(7, "الامتنان الصغير", "«لاحظ شيئاً بسيطاً واحداً حولك يستحق الحمد: ضوء النافذة، رشفة ماء بارد، أو سكون الغرفة.»", "عين الامتنان"),
            WhisperNote(8, "شجاعة البدايات", "«لا تحتاج لأن ترى نهاية الطريق لتبدأ؛ يكفيك أن ترى الخطوة التالية وتخطوها برفق.»", "بداية هادئة")
        )
    }

    var currentIndex by remember { mutableIntStateOf(0) }
    var hasSavedCurrent by remember { mutableStateOf(false) }

    val currentWhisper = whispers[currentIndex % whispers.size]

    val infiniteTransition = rememberInfiniteTransition(label = "jar_ambient")
    val pulseGlow by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_glow"
    )

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        modifier = modifier
            .fillMaxWidth()
            .border(
                1.dp,
                Brush.verticalGradient(
                    listOf(Color.White, SageLight.copy(alpha = 0.8f))
                ),
                RoundedCornerShape(24.dp)
            )
            .testTag("mindful_whisper_jar")
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(GoldenAmber.copy(alpha = 0.35f * pulseGlow), SageLight)
                                )
                            )
                    ) {
                        Text(text = "🏺", fontSize = 22.sp)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "جرّة السكينة والهمسات",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = DeepForest
                            )
                        )
                        Text(
                            text = "اسحب همسة مطمئنة لقلبك وفكرك الآن",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SageLight.copy(alpha = 0.6f)
                ) {
                    Text(
                        text = currentWhisper.category,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = DarkGreen,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // The Jar Whisper Container with Ambient Glass feel
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = WarmBeige.copy(alpha = 0.38f),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        Brush.horizontalGradient(
                            listOf(Color.White.copy(alpha = 0.8f), SageLight.copy(alpha = 0.4f))
                        ),
                        RoundedCornerShape(20.dp)
                    )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    AnimatedContent(
                        targetState = currentWhisper,
                        transitionSpec = {
                            (fadeIn(tween(350)) + scaleIn(initialScale = 0.95f)) togetherWith fadeOut(tween(250))
                        },
                        label = "whisper_content"
                    ) { whisper ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = whisper.text,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = DeepForest,
                                    fontWeight = FontWeight.Medium,
                                    lineHeight = 26.sp,
                                    textAlign = TextAlign.Center
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "— ${whisper.sourceOrTakeaway}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = DarkGreen,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Actions Row: "سحب همسة أخرى" + "حفظ في رحلتي"
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = {
                        currentIndex++
                        hasSavedCurrent = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "همسة جديدة ✦",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = if (hasSavedCurrent) SageLight else WarmBeige.copy(alpha = 0.5f),
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .clickable {
                            if (!hasSavedCurrent) {
                                onSaveToReflections("همسة من جرّة السكينة", currentWhisper.text)
                                hasSavedCurrent = true
                            }
                        }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (hasSavedCurrent) Icons.Default.Check else Icons.Default.BookmarkBorder,
                            contentDescription = null,
                            tint = if (hasSavedCurrent) DarkGreen else TextDark,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (hasSavedCurrent) "حُفظت ✓" else "حفظ",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = if (hasSavedCurrent) DarkGreen else TextDark,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }
    }
}
