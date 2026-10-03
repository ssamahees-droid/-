package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Forward10
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay10
import androidx.compose.material.icons.filled.VolumeDown
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PurposefulVideo
import com.example.ui.theme.CreamBackground
import com.example.ui.theme.DarkGreen
import com.example.ui.theme.DeepForest
import com.example.ui.theme.SageGreen
import com.example.ui.theme.SageLight
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.WarmBeige
import com.example.ui.theme.WarmTerracotta
import com.example.util.CalmAudioEngine
import kotlinx.coroutines.delay
import kotlin.math.sin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PurposefulVideoPlayerModal(
    video: PurposefulVideo,
    onDismiss: () -> Unit,
    onSaveToJourney: (title: String, summary: String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var isPlaying by remember { mutableStateOf(true) }
    var currentSeconds by remember { mutableIntStateOf(0) }
    val totalSeconds = 240 // 4 minutes simulated video length
    var soundEnabled by remember { mutableStateOf(true) }
    var hasSaved by remember { mutableStateOf(false) }

    val checkedSteps = remember { mutableStateListOf<Int>() }

    // Start soothing background ambience while watching
    LaunchedEffect(video.soundtrackId) {
        if (soundEnabled) {
            CalmAudioEngine.start(video.soundtrackId)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            // keep state clean
        }
    }

    // Playback progress loop
    LaunchedEffect(isPlaying) {
        while (isPlaying && currentSeconds < totalSeconds) {
            delay(1000)
            currentSeconds++
        }
        if (currentSeconds >= totalSeconds) {
            isPlaying = false
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = CreamBackground,
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(18.dp)
        ) {
            // Header Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "إغلاق", tint = DarkGreen)
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SageLight
                ) {
                    Text(
                        text = "مرئي مطمئن • ${video.category}",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall.copy(color = DeepForest, fontWeight = FontWeight.Bold)
                    )
                }

                IconButton(
                    onClick = {
                        soundEnabled = !soundEnabled
                        if (soundEnabled) CalmAudioEngine.start(video.soundtrackId)
                        else CalmAudioEngine.stop()
                    }
                ) {
                    Icon(
                        imageVector = if (soundEnabled) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                        contentDescription = "الصوت المرافق",
                        tint = if (soundEnabled) DarkGreen else TextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Cinematic Calming Video Canvas Player
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(DeepForest)
            ) {
                // Animated Ambient Canvas
                val infiniteTransition = rememberInfiniteTransition(label = "video_waves")
                val phaseAnim by infiniteTransition.animateFloat(
                    initialValue = 0f,
                    targetValue = (2 * Math.PI).toFloat(),
                    animationSpec = infiniteRepeatable(
                        animation = tween(4000, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart
                    ),
                    label = "phase"
                )
                val pulseAnim by infiniteTransition.animateFloat(
                    initialValue = 0.85f,
                    targetValue = 1.15f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(2500, easing = FastOutSlowInEasing),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "pulse"
                )

                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height
                    val centerY = h * 0.5f

                    // Soft ambient gradient glow
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                SageLight.copy(alpha = 0.28f * pulseAnim),
                                Color.Transparent
                            ),
                            center = Offset(w * 0.5f, centerY),
                            radius = w * 0.45f
                        )
                    )

                    // Calming Waveform 1
                    val path1 = Path().apply {
                        moveTo(0f, centerY)
                        var x = 0f
                        while (x <= w) {
                            val y = centerY + sin((x / w * 3 * Math.PI + phaseAnim).toDouble()).toFloat() * (24f * pulseAnim)
                            lineTo(x, y)
                            x += 10f
                        }
                    }
                    drawPath(
                        path = path1,
                        color = SageGreen.copy(alpha = 0.6f),
                        style = Stroke(width = 3.dp.toPx())
                    )

                    // Calming Waveform 2
                    val path2 = Path().apply {
                        moveTo(0f, centerY)
                        var x = 0f
                        while (x <= w) {
                            val y = centerY + sin((x / w * 4 * Math.PI - phaseAnim * 0.8).toDouble()).toFloat() * (16f * pulseAnim)
                            lineTo(x, y)
                            x += 10f
                        }
                    }
                    drawPath(
                        path = path2,
                        color = Color.White.copy(alpha = 0.45f),
                        style = Stroke(width = 2.dp.toPx())
                    )
                }

                // Overlay Content
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color.Black.copy(alpha = 0.4f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = video.iconEmoji, fontSize = 14.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = video.durationText,
                                    color = Color.White,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (soundEnabled) DarkGreen.copy(alpha = 0.85f) else Color.Black.copy(alpha = 0.4f)
                        ) {
                            Text(
                                text = if (soundEnabled) "موسيقى تأملية مرافقة 🎵" else "صوت صامت 🔇",
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp)
                            )
                        }
                    }

                    // Centered Speaker & Play Action
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = video.speaker,
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = video.speakerRole,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = SageLight,
                                fontSize = 11.sp
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Controls Row
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            IconButton(onClick = { currentSeconds = (currentSeconds - 10).coerceAtLeast(0) }) {
                                Icon(imageVector = Icons.Default.Replay10, contentDescription = "تراجع 10 ثوانٍ", tint = Color.White)
                            }

                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .background(Color.White)
                                    .clickable { isPlaying = !isPlaying }
                            ) {
                                Icon(
                                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = if (isPlaying) "إيقاف مؤقت" else "تشغيل",
                                    tint = DeepForest,
                                    modifier = Modifier.size(32.dp)
                                )
                            }

                            IconButton(onClick = { currentSeconds = (currentSeconds + 10).coerceAtMost(totalSeconds) }) {
                                Icon(imageVector = Icons.Default.Forward10, contentDescription = "تقديم 10 ثوانٍ", tint = Color.White)
                            }
                        }
                    }

                    // Progress Slider
                    Column {
                        val progressFraction = currentSeconds.toFloat() / totalSeconds.toFloat()
                        Slider(
                            value = progressFraction,
                            onValueChange = { frac ->
                                currentSeconds = (frac * totalSeconds).toInt()
                            },
                            colors = SliderDefaults.colors(
                                thumbColor = Color.White,
                                activeTrackColor = SageLight,
                                inactiveTrackColor = Color.White.copy(alpha = 0.3f)
                            ),
                            modifier = Modifier.height(16.dp)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            val elapsedMin = currentSeconds / 60
                            val elapsedSec = currentSeconds % 60
                            val totalMin = totalSeconds / 60
                            val totalSec = totalSeconds % 60
                            Text(
                                text = String.format("%02d:%02d", elapsedMin, elapsedSec),
                                color = Color.White.copy(alpha = 0.8f),
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp)
                            )
                            Text(
                                text = String.format("%02d:%02d", totalMin, totalSec),
                                color = Color.White.copy(alpha = 0.8f),
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Video Title & Description
            Text(
                text = video.title,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = DeepForest,
                    lineHeight = 26.sp
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = video.description,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = TextDark,
                    lineHeight = 22.sp
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Inspiring Quote Card
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = SageLight.copy(alpha = 0.45f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "💡", fontSize = 22.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = video.quote,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = DarkGreen,
                            lineHeight = 20.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Key Takeaways Section (أهم ومضات الجلسة)
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "✨", fontSize = 18.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "أهم ومضات الجلسة:",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = DeepForest
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    video.keyTakeaways.forEach { takeaway ->
                        Row(
                            modifier = Modifier.padding(vertical = 4.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(text = "🌱", fontSize = 14.sp, modifier = Modifier.padding(top = 2.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = takeaway,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = TextDark,
                                    lineHeight = 20.sp
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Practical Exercise Section (التمرين العملي المقتبس)
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = WarmBeige.copy(alpha = 0.45f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "🧘", fontSize = 18.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = video.practicalExerciseTitle,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = DarkGreen
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "خطوات عملية يمكنك تجربتها وتحديد ما أكملته:",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    video.practicalExerciseSteps.forEachIndexed { idx, step ->
                        val isDone = checkedSteps.contains(idx)
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isDone) SageLight.copy(alpha = 0.6f) else Color.White,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    if (isDone) checkedSteps.remove(idx)
                                    else checkedSteps.add(idx)
                                }
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = isDone,
                                    onCheckedChange = {
                                        if (isDone) checkedSteps.remove(idx)
                                        else checkedSteps.add(idx)
                                    },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = DarkGreen,
                                        uncheckedColor = TextMuted
                                    )
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = step,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = if (isDone) DarkGreen else TextDark,
                                        fontWeight = if (isDone) FontWeight.Bold else FontWeight.Normal
                                    )
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Action Buttons
            Button(
                onClick = {
                    val summary = "فيديو: ${video.title}\nالناصح: ${video.speaker}\nالخلاصة: ${video.quote}"
                    onSaveToJourney(video.title, summary)
                    hasSaved = true
                },
                enabled = !hasSaved,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (hasSaved) SageGreen else DarkGreen
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (hasSaved) Icons.Default.Check else Icons.Default.Bookmark,
                        contentDescription = null,
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (hasSaved) "تم الحفظ في دفتر رحلتي ✓" else "حفظ هذه الومضة في مفكرة رحلتي",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
