package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkGreen
import com.example.ui.theme.DeepForest
import com.example.ui.theme.GoldenAmber
import com.example.ui.theme.MorningDawn
import com.example.ui.theme.SageGreen
import com.example.ui.theme.SageLight
import com.example.ui.theme.SkyCalm
import com.example.ui.theme.SunsetRose
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TwilightIndigo
import com.example.ui.theme.WarmBeige
import java.util.Calendar

@Composable
fun LivingSkyAtmosphere(
    modifier: Modifier = Modifier
) {
    val currentHour = remember {
        Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    }

    val isMorning = currentHour in 5..11
    val isAfternoon = currentHour in 12..16
    val isSunset = currentHour in 17..19
    val isNight = !isMorning && !isAfternoon && !isSunset

    val skyGradient = remember(currentHour) {
        when {
            isMorning -> listOf(Color(0xFFFFF6E5), Color(0xFFE8F3EB), Color(0xFFD6EAE0))
            isAfternoon -> listOf(Color(0xFFE9F4F6), Color(0xFFDFEFE8), Color(0xFFD8EBE1))
            isSunset -> listOf(Color(0xFFFBE4D8), Color(0xFFEED5D0), Color(0xFFDCE2DF))
            else -> listOf(Color(0xFF1E2D27), Color(0xFF2B3F36), Color(0xFF1C2A24))
        }
    }

    val celestialIcon = when {
        isMorning -> "🌅"
        isAfternoon -> "☀️"
        isSunset -> "🌇"
        else -> "🌙"
    }

    val skyGreeting = when {
        isMorning -> "صباح مبارك وهادئ"
        isAfternoon -> "طاب يومك ووقفك"
        isSunset -> "سكينة الأصيل والمساء"
        else -> "ليلة هانئة وسكون تام"
    }

    val skyWisdom = when {
        isMorning -> "ابدأ يومك برفق وتأنٍ؛ لست في سباق مع أحد، خطوة واحدة طيبة تكفي."
        isAfternoon -> "في منتصف اليوم، خذ وقفة تنفس واشرب ماءً، ولا تكلف نفسك ما لا تطيق."
        isSunset -> "مع انقضاء النهار، اترك ما فات؛ ما أُنجز جميل وما لم يُنجز ينتظر بأمان."
        else -> "دع شواغل الرأس تستريح؛ لقد بذلت وسعك اليوم، واستحققت راحة هادئة."
    }

    val infiniteTransition = rememberInfiniteTransition(label = "sky_motion")
    val driftAnim by infiniteTransition.animateFloat(
        initialValue = -30f,
        targetValue = 30f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "drift_anim"
    )

    Card(
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        modifier = modifier
            .fillMaxWidth()
            .height(138.dp)
            .border(
                1.dp,
                Brush.verticalGradient(listOf(Color.White.copy(alpha = 0.7f), Color.White.copy(alpha = 0.2f))),
                RoundedCornerShape(26.dp)
            )
            .testTag("living_sky_atmosphere")
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.linearGradient(skyGradient))
        ) {
            // Ambient Floating Shapes Canvas
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height

                if (isNight) {
                    // Twinkling stars
                    val starPositions = listOf(
                        Offset(w * 0.15f, h * 0.25f),
                        Offset(w * 0.35f, h * 0.15f),
                        Offset(w * 0.55f, h * 0.3f),
                        Offset(w * 0.8f, h * 0.2f),
                        Offset(w * 0.7f, h * 0.45f)
                    )
                    starPositions.forEachIndexed { i, pos ->
                        val alpha = ((pos.x + driftAnim) % 100) / 100f
                        drawCircle(
                            color = Color.White.copy(alpha = 0.4f + 0.5f * (i % 2)),
                            radius = 2.dp.toPx(),
                            center = pos
                        )
                    }
                } else {
                    // Soft floating breeze ripples
                    drawCircle(
                        color = Color.White.copy(alpha = 0.25f),
                        radius = 60.dp.toPx(),
                        center = Offset(w * 0.85f + driftAnim, h * 0.2f)
                    )
                    drawCircle(
                        color = Color.White.copy(alpha = 0.18f),
                        radius = 45.dp.toPx(),
                        center = Offset(w * 0.15f - driftAnim * 0.5f, h * 0.75f)
                    )
                }
            }

            // Foreground Content
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Celestial Badge
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(
                            if (isNight) Color.White.copy(alpha = 0.15f) else Color.White.copy(alpha = 0.75f)
                        )
                        .border(
                            1.dp,
                            if (isNight) Color.White.copy(alpha = 0.3f) else SageLight,
                            CircleShape
                        )
                ) {
                    Text(text = celestialIcon, fontSize = 28.sp)
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = skyGreeting,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (isNight) Color.White else DeepForest
                            )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isNight) Color.White.copy(alpha = 0.15f) else SageGreen.copy(alpha = 0.3f)
                        ) {
                            Text(
                                text = "سماء اللحظة",
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 10.sp,
                                    color = if (isNight) SageLight else DarkGreen,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = skyWisdom,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = if (isNight) Color.White.copy(alpha = 0.85f) else TextDark.copy(alpha = 0.85f),
                            lineHeight = 18.sp
                        ),
                        maxLines = 2
                    )
                }
            }
        }
    }
}
