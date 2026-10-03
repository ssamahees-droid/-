package com.example.ui.screens

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CreamBackground
import com.example.ui.theme.DarkGreen
import com.example.ui.theme.DeepForest
import com.example.ui.theme.GoldenAmber
import com.example.ui.theme.SageGreen
import com.example.ui.theme.SageLight
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.WarmBeige
import com.example.ui.theme.WarmTerracotta
import com.example.ui.viewmodel.NasmatViewModel
import com.example.util.CalmAudioEngine
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

enum class BreathingPhase {
    IDLE, INHALE, HOLD, EXHALE
}

enum class BreathingPattern(val title: String, val inhaleSec: Int, val holdSec: Int, val exhaleSec: Int, val note: String) {
    CALM_4_6("تهدئة 4 - 6", 4, 0, 6, "نمط مثالي لتخفيف التوتر وإعادة التوازن للجهاز العصبي"),
    BOX_4_4("تنفس المربع", 4, 4, 4, "نمط لزيادة التركيز وتصفية الذهن قبل المهام"),
    DEEP_4_7_8("استرخاء 4 - 7 - 8", 4, 7, 8, "نمط كلاسيكي للاسترخاء العميق وتهيئة النوم")
}

@Composable
fun BreathingScreen(
    viewModel: NasmatViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler {
        CalmAudioEngine.stop()
        onBack()
    }

    DisposableEffect(Unit) {
        onDispose {
            CalmAudioEngine.stop()
        }
    }

    val context = LocalContext.current
    var selectedPattern by remember { mutableStateOf(BreathingPattern.CALM_4_6) }
    var soundEnabled by remember { mutableStateOf(false) }

    var isRunning by remember { mutableStateOf(false) }
    var currentPhase by remember { mutableStateOf(BreathingPhase.IDLE) }
    var secondsRemaining by remember { mutableIntStateOf(selectedPattern.inhaleSec) }
    var completedCycles by remember { mutableIntStateOf(0) }

    val circleScale = remember { Animatable(1f) }

    val infiniteTransition = rememberInfiniteTransition(label = "mandala_spin")
    val mandalaAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(24000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "mandala_rotation"
    )

    fun triggerVibration() {
        try {
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            if (vibrator != null && vibrator.hasVibrator()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(45, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(45)
                }
            }
        } catch (_: Exception) { }
    }

    // Coroutine loop for rhythmic breathing
    LaunchedEffect(isRunning, selectedPattern, soundEnabled) {
        if (!isRunning) {
            currentPhase = BreathingPhase.IDLE
            circleScale.animateTo(1f, tween(500))
            if (soundEnabled) CalmAudioEngine.stop()
            return@LaunchedEffect
        }

        if (soundEnabled) {
            CalmAudioEngine.start("rain")
        }

        while (isRunning) {
            // 1. INHALE
            currentPhase = BreathingPhase.INHALE
            triggerVibration()
            val inhaleDurationMs = selectedPattern.inhaleSec * 1000
            val inhaleJob = circleScale.animateTo(1.38f, tween(inhaleDurationMs, easing = LinearEasing))
            for (sec in selectedPattern.inhaleSec downTo 1) {
                if (!isRunning) break
                secondsRemaining = sec
                delay(1000)
            }

            if (!isRunning) break

            // 2. HOLD (if pattern has hold > 0)
            if (selectedPattern.holdSec > 0) {
                currentPhase = BreathingPhase.HOLD
                triggerVibration()
                for (sec in selectedPattern.holdSec downTo 1) {
                    if (!isRunning) break
                    secondsRemaining = sec
                    delay(1000)
                }
            }

            if (!isRunning) break

            // 3. EXHALE
            currentPhase = BreathingPhase.EXHALE
            triggerVibration()
            val exhaleDurationMs = selectedPattern.exhaleSec * 1000
            val exhaleJob = circleScale.animateTo(1.0f, tween(exhaleDurationMs, easing = LinearEasing))
            for (sec in selectedPattern.exhaleSec downTo 1) {
                if (!isRunning) break
                secondsRemaining = sec
                delay(1000)
            }

            if (isRunning) {
                completedCycles++
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CreamBackground)
            .padding(18.dp)
            .testTag("breathing_screen"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top App Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = {
                        isRunning = false
                        CalmAudioEngine.stop()
                        onBack()
                    },
                    modifier = Modifier.testTag("breathing_back_button")
                ) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "رجوع", tint = DarkGreen)
                }
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                    Text(
                        text = "تنفس السكينة",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = DarkGreen
                        )
                    )
                    Text(
                        text = "مساحة لاستعادة الحضور والهدوء",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                    )
                }
            }

            // Audio Toggle
            IconButton(
                onClick = {
                    soundEnabled = !soundEnabled
                    if (!soundEnabled) CalmAudioEngine.stop()
                    else if (isRunning) CalmAudioEngine.start("rain")
                }
            ) {
                Icon(
                    imageVector = if (soundEnabled) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                    contentDescription = "صوت المطر المرافق",
                    tint = if (soundEnabled) DarkGreen else TextMuted
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Technique Pattern Selector
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(BreathingPattern.values()) { pattern ->
                val isSel = selectedPattern == pattern
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isSel) DarkGreen else WarmBeige.copy(alpha = 0.5f),
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable {
                            if (!isRunning) {
                                selectedPattern = pattern
                                secondsRemaining = pattern.inhaleSec
                            }
                        }
                ) {
                    Text(
                        text = pattern.title,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = if (isSel) Color.White else TextDark,
                            fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = selectedPattern.note,
            style = MaterialTheme.typography.bodySmall.copy(color = TextMuted),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        // Artistic Mandala & Blooming Petals Canvas Visualizer
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(280.dp)
                .testTag("breathing_visualizer")
        ) {
            // Blooming Canvas Petals
            Canvas(modifier = Modifier.fillMaxSize()) {
                val center = Offset(size.width / 2f, size.height / 2f)
                val baseRadius = 80.dp.toPx() * circleScale.value
                val petalDistance = 45.dp.toPx() * (circleScale.value - 0.7f)

                // Outer pulsing aura
                drawCircle(
                    brush = Brush.radialGradient(
                        listOf(
                            if (currentPhase == BreathingPhase.HOLD) GoldenAmber.copy(alpha = 0.35f) else SageLight.copy(alpha = 0.5f),
                            Color.Transparent
                        ),
                        center = center,
                        radius = baseRadius * 1.5f
                    ),
                    radius = baseRadius * 1.5f,
                    center = center
                )

                // 8 Blooming organic petals
                val petalCount = 8
                val angleStep = (2 * PI / petalCount).toFloat()
                val rotRad = (mandalaAngle * PI / 180f).toFloat()

                for (i in 0 until petalCount) {
                    val angle = i * angleStep + rotRad
                    val petalCenter = Offset(
                        center.x + cos(angle) * petalDistance,
                        center.y + sin(angle) * petalDistance
                    )
                    drawCircle(
                        color = when (currentPhase) {
                            BreathingPhase.HOLD -> GoldenAmber.copy(alpha = 0.35f)
                            BreathingPhase.INHALE -> SageGreen.copy(alpha = 0.45f)
                            else -> SageLight.copy(alpha = 0.35f)
                        },
                        radius = 26.dp.toPx() * circleScale.value,
                        center = petalCenter
                    )
                }

                // Inner breathing ring
                drawCircle(
                    color = SageGreen.copy(alpha = 0.6f),
                    radius = baseRadius,
                    center = center,
                    style = Stroke(width = 2.dp.toPx())
                )
            }

            // Central Core Breathing Circle
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(175.dp)
                    .scale(circleScale.value)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(
                                when (currentPhase) {
                                    BreathingPhase.HOLD -> GoldenAmber.copy(alpha = 0.9f)
                                    BreathingPhase.INHALE -> SageGreen
                                    else -> DarkGreen
                                },
                                DarkGreen
                            )
                        )
                    )
                    .border(3.dp, Color.White.copy(alpha = 0.6f), CircleShape)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    val phaseText = when (currentPhase) {
                        BreathingPhase.IDLE -> "ابدأ"
                        BreathingPhase.INHALE -> "شهيق"
                        BreathingPhase.HOLD -> "تثبيت"
                        BreathingPhase.EXHALE -> "زفير"
                    }
                    Text(
                        text = phaseText,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                    if (isRunning) {
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "$secondsRemaining",
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Session counter badge
        if (completedCycles > 0) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = WarmBeige.copy(alpha = 0.6f),
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Text(
                    text = "دورات مكتملة: $completedCycles دورة هادئة",
                    style = MaterialTheme.typography.bodySmall.copy(color = DeepForest, fontWeight = FontWeight.Medium),
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                )
            }
        }

        // Control Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = { isRunning = !isRunning },
                modifier = Modifier
                    .weight(1f)
                    .height(54.dp)
                    .testTag("breathing_toggle_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isRunning) WarmTerracotta else DarkGreen
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(
                    imageVector = if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isRunning) "إيقاف مؤقت" else "بدء التنفس",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
            }

            OutlinedButton(
                onClick = {
                    isRunning = false
                    completedCycles = 0
                    currentPhase = BreathingPhase.IDLE
                    secondsRemaining = selectedPattern.inhaleSec
                    CalmAudioEngine.stop()
                },
                modifier = Modifier
                    .height(54.dp)
                    .testTag("breathing_reset_button"),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(imageVector = Icons.Default.Refresh, contentDescription = "إعادة ضبط", tint = DarkGreen)
            }
        }
    }
}
