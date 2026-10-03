package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.VolumeDown
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
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
import com.example.util.CalmAudioEngine
import com.example.util.SoundTrack

@Composable
fun SoundscapeTab(
    modifier: Modifier = Modifier
) {
    val isPlaying by CalmAudioEngine.isPlayingFlow.collectAsState()
    val currentTrack by CalmAudioEngine.currentTrackFlow.collectAsState()
    val volume by CalmAudioEngine.masterVolumeFlow.collectAsState()
    val remainingTimer by CalmAudioEngine.remainingTimerSeconds.collectAsState()

    var selectedCategory by remember { mutableStateOf("الكل") }
    val categories = listOf("الكل", "موسيقى هادئة", "أصوات الطبيعة", "ترددات السكينة")

    val filteredTracks = remember(selectedCategory) {
        if (selectedCategory == "الكل") CalmAudioEngine.availableTracks
        else CalmAudioEngine.availableTracks.filter { it.category == selectedCategory }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Active Soundscape Master Controller Card
        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isPlaying) DeepForest else Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(if (isPlaying) SageLight.copy(alpha = 0.25f) else SageLight)
                            ) {
                                Text(
                                    text = currentTrack?.iconEmoji ?: "🎵",
                                    fontSize = 22.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = if (isPlaying) currentTrack?.title ?: "مشغّل السكينة" else "اختر مقطعاً للاسترخاء",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (isPlaying) Color.White else DeepForest
                                    )
                                )
                                Text(
                                    text = if (isPlaying) currentTrack?.category ?: "موسيقى مهدئة" else "ألحان وأصوات مصممة لراحة الذهن",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = if (isPlaying) SageLight else TextMuted
                                    )
                                )
                            }
                        }

                        // Play/Pause Big Button
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(if (isPlaying) Color.White else DarkGreen)
                                .clickable {
                                    if (isPlaying) CalmAudioEngine.stop()
                                    else CalmAudioEngine.start(currentTrack?.id ?: "music_piano")
                                }
                        ) {
                            Icon(
                                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = if (isPlaying) "إيقاف" else "تشغيل",
                                tint = if (isPlaying) DeepForest else Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }

                    // Visualizer & Benefits when playing
                    if (isPlaying) {
                        Spacer(modifier = Modifier.height(14.dp))

                        // Pulsing Visualizer Canvas
                        val infiniteTransition = rememberInfiniteTransition(label = "pulse_rings")
                        val ring1 by infiniteTransition.animateFloat(
                            initialValue = 0.6f,
                            targetValue = 1.3f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(2200, easing = FastOutSlowInEasing),
                                repeatMode = RepeatMode.Reverse
                            ),
                            label = "ring1"
                        )
                        val ring2 by infiniteTransition.animateFloat(
                            initialValue = 1.2f,
                            targetValue = 0.7f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(1800, easing = FastOutSlowInEasing),
                                repeatMode = RepeatMode.Reverse
                            ),
                            label = "ring2"
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color.Black.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                val w = size.width
                                val h = size.height
                                drawCircle(
                                    color = SageLight.copy(alpha = 0.25f),
                                    radius = 24.dp.toPx() * ring1,
                                    center = Offset(w * 0.5f, h * 0.5f),
                                    style = Stroke(width = 2.dp.toPx())
                                )
                                drawCircle(
                                    color = SageGreen.copy(alpha = 0.35f),
                                    radius = 16.dp.toPx() * ring2,
                                    center = Offset(w * 0.5f, h * 0.5f),
                                    style = Stroke(width = 1.5.dp.toPx())
                                )
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.GraphicEq,
                                    contentDescription = null,
                                    tint = SageLight,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = currentTrack?.benefits ?: "تهدئة الجهاز العصبي والراحة",
                                    color = Color.White,
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Volume Control Slider
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(imageVector = Icons.Default.VolumeDown, contentDescription = null, tint = SageLight, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Slider(
                                value = volume,
                                onValueChange = { CalmAudioEngine.setVolume(it) },
                                colors = SliderDefaults.colors(
                                    thumbColor = Color.White,
                                    activeTrackColor = SageLight,
                                    inactiveTrackColor = Color.White.copy(alpha = 0.25f)
                                ),
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(imageVector = Icons.Default.VolumeUp, contentDescription = null, tint = SageLight, modifier = Modifier.size(18.dp))
                        }

                        // Sleep Timer Controls
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.Timer, contentDescription = null, tint = SageLight, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (remainingTimer != null) "مؤقت النوم: ${remainingTimer!! / 60}:${String.format("%02d", remainingTimer!! % 60)}"
                                    else "مؤقت الإيقاف التلقائي:",
                                    style = MaterialTheme.typography.labelSmall.copy(color = SageLight)
                                )
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                listOf(15, 30, 45).forEach { min ->
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = if (remainingTimer != null && remainingTimer!! > (min - 15) * 60 && remainingTimer!! <= min * 60) DarkGreen else Color.White.copy(alpha = 0.15f),
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .clickable { CalmAudioEngine.setTimer(min) }
                                    ) {
                                        Text(
                                            text = "$min د",
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                            style = MaterialTheme.typography.labelSmall.copy(color = Color.White, fontSize = 10.sp)
                                        )
                                    }
                                }
                                if (remainingTimer != null) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = WarmTerracotta.copy(alpha = 0.3f),
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .clickable { CalmAudioEngine.cancelTimer() }
                                    ) {
                                        Text(
                                            text = "إلغاء",
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                            style = MaterialTheme.typography.labelSmall.copy(color = Color.White, fontSize = 10.sp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Category Filter Chips
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(categories) { cat ->
                    val isSel = selectedCategory == cat
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSel) DarkGreen else WarmBeige.copy(alpha = 0.5f),
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { selectedCategory = cat }
                    ) {
                        Text(
                            text = cat,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = if (isSel) Color.White else TextDark,
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal
                            )
                        )
                    }
                }
            }
        }

        // Track List Cards
        items(filteredTracks) { track ->
            val isCurrent = currentTrack?.id == track.id
            val isCurrentPlaying = isCurrent && isPlaying

            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .then(
                        if (isCurrentPlaying) Modifier.border(1.5.dp, DarkGreen, RoundedCornerShape(18.dp))
                        else Modifier
                    )
                    .clip(RoundedCornerShape(18.dp))
                    .clickable {
                        if (isCurrentPlaying) CalmAudioEngine.stop()
                        else CalmAudioEngine.start(track.id)
                    }
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(if (isCurrentPlaying) SageLight else WarmBeige.copy(alpha = 0.6f))
                    ) {
                        Text(text = track.iconEmoji, fontSize = 24.sp)
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = track.title,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (isCurrentPlaying) DarkGreen else DeepForest
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = track.subtitle,
                            style = MaterialTheme.typography.bodySmall.copy(color = TextDark, fontSize = 11.sp),
                            lineHeight = 16.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = SageLight.copy(alpha = 0.5f)
                            ) {
                                Text(
                                    text = track.bpmOrFrequency,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    style = MaterialTheme.typography.labelSmall.copy(color = DeepForest, fontSize = 9.sp)
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "• ${track.category}",
                                style = MaterialTheme.typography.labelSmall.copy(color = TextMuted, fontSize = 10.sp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(if (isCurrentPlaying) DarkGreen else WarmBeige.copy(alpha = 0.5f))
                    ) {
                        Icon(
                            imageVector = if (isCurrentPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isCurrentPlaying) "إيقاف" else "تشغيل",
                            tint = if (isCurrentPlaying) Color.White else DeepForest,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
