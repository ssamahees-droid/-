package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Nature
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SentimentSatisfied
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CreamBackground
import com.example.ui.theme.DarkGreen
import com.example.ui.theme.DeepForest
import com.example.ui.theme.SageGreen
import com.example.ui.theme.SageLight
import com.example.ui.theme.SoftPeach
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.WarmBeige
import com.example.ui.theme.WarmTerracotta
import com.example.ui.viewmodel.NasmatViewModel
import com.example.util.CalmAudioEngine
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.sin

data class MemoryCard(val id: Int, val emoji: String, var isFlipped: Boolean = false, var isMatched: Boolean = false)

data class TouchRipple(val offset: Offset, val radius: Float, val color: Color)

data class MindfulRiddle(val question: String, val answer: String, val reflection: String)

data class CalmThisOrThat(val optionA: String, val optionB: String, val takeaway: String)

@Composable
fun LightSpaceScreen(
    viewModel: NasmatViewModel,
    onLaunchGwayaHekaya: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("الألعاب والألغاز", "صوتيات السكينة", "مشاهد بصرية", "تلوين وملاحظة", "ابتسامة دافئة")

    // Stop audio on screen disposal
    DisposableEffect(Unit) {
        onDispose {
            CalmAudioEngine.stop()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CreamBackground)
            .padding(horizontal = 18.dp)
            .testTag("light_space_screen")
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "المساحة الخفيفة الشاملة",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = DarkGreen
            )
        )
        Text(
            text = "ألعاب، صوتيات طبيعية، مشاهد تأملية، وتلوين للترويح والسكينة",
            style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Horizontal scrollable tabs
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(tabs.indices.toList()) { index ->
                val isSelected = selectedTab == index
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = if (isSelected) DarkGreen else WarmBeige.copy(alpha = 0.55f),
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { selectedTab = index }
                ) {
                    Text(
                        text = tabs[index],
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = if (isSelected) Color.White else TextDark,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        when (selectedTab) {
            0 -> GamesAndRiddlesSection(viewModel = viewModel, onLaunchGwayaHekaya = onLaunchGwayaHekaya)
            1 -> AudioSoundscapesSection()
            2 -> TranquilVisualScenesSection()
            3 -> ArtAndNoticingSection()
            4 -> GentleSmilesTab()
        }
    }
}

// -----------------------------------------------------------------------------------------
// 1. GAMES & RIDDLES SECTION
// -----------------------------------------------------------------------------------------

@Composable
fun GamesAndRiddlesSection(
    viewModel: NasmatViewModel,
    onLaunchGwayaHekaya: () -> Unit
) {
    var subTab by remember { mutableIntStateOf(0) }
    val subTabs = listOf("جوايا حكاية 🌟", "لعبة الذاكرة", "ألغاز مريحة", "هذا أم ذاك")

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            subTabs.forEachIndexed { index, title ->
                val isSelected = subTab == index
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (isSelected) (if (index == 0) DarkGreen else SageGreen.copy(alpha = 0.35f)) else Color.Transparent,
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { subTab = index }
                ) {
                    Text(
                        text = title,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = if (isSelected) (if (index == 0) Color.White else DarkGreen) else TextMuted,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        when (subTab) {
            0 -> GwayaHekayaBannerAndLauncher(onLaunch = onLaunchGwayaHekaya)
            1 -> GentleMemoryGameTab()
            2 -> MindfulRiddlesTab()
            3 -> ThisOrThatTab()
        }
    }
}

@Composable
fun GwayaHekayaBannerAndLauncher(onLaunch: () -> Unit) {
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onLaunch),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = SageLight
                ) {
                    Text(
                        text = "لعبة استكشاف الذات 🌟",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = DarkGreen,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Surface(
                    shape = CircleShape,
                    color = WarmBeige.copy(alpha = 0.5f),
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = "🍃", fontSize = 18.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "جوايا حكاية — نسمة الحياة",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = DeepForest
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "«العالم الذي يشرح نفسه بنفسه»\nرحلة تفاعلية من ٦ محطات مريحة: الموقف، الفكرة، الشعور، إشارة الجسم، الاحتياج، والقصة الجديدة الأكثر رفقاً.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = TextDark,
                    lineHeight = 22.sp
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onLaunch,
                colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "خوض تجربة «جوايا حكاية» الآن ❯",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun GentleMemoryGameTab() {
    val coroutineScope = rememberCoroutineScope()
    var cardCountMode by remember { mutableIntStateOf(8) } // 8 or 12 cards

    val symbols8 = listOf("🌿", "🌸", "🍃", "☀️")
    val symbols12 = listOf("🌿", "🌸", "🍃", "☀️", "🌙", "🌊")

    var cards by remember(cardCountMode) {
        val pool = if (cardCountMode == 8) symbols8 else symbols12
        mutableStateOf((pool + pool).shuffled().mapIndexed { i, em -> MemoryCard(i, em) })
    }

    var flippedIndices by remember { mutableStateOf<List<Int>>(emptyList()) }
    var isChecking by remember { mutableStateOf(false) }

    fun resetGame() {
        val pool = if (cardCountMode == 8) symbols8 else symbols12
        cards = (pool + pool).shuffled().mapIndexed { i, em -> MemoryCard(i, em) }
        flippedIndices = emptyList()
        isChecking = false
    }

    val allMatched = cards.isNotEmpty() && cards.all { it.isMatched }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (allMatched) "رائع! طاب خاطرك وذهنك 🌸" else "طابق رموز الطبيعة بهدوء",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = if (allMatched) DarkGreen else DeepForest
                )
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                TextButton(onClick = {
                    cardCountMode = if (cardCountMode == 8) 12 else 8
                    resetGame()
                }) {
                    Text(
                        text = if (cardCountMode == 8) "12 بطاقة" else "8 بطاقات",
                        color = DarkGreen,
                        fontSize = 12.sp
                    )
                }
                IconButton(onClick = { resetGame() }) {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = "إعادة", tint = DarkGreen)
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            itemsIndexed(cards) { index, card ->
                val isVisible = card.isFlipped || card.isMatched
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .height(68.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            when {
                                card.isMatched -> SageLight
                                isVisible -> Color.White
                                else -> DarkGreen
                            }
                        )
                        .clickable(enabled = !isVisible && !isChecking) {
                            if (flippedIndices.size < 2) {
                                cards = cards.mapIndexed { i, c -> if (i == index) c.copy(isFlipped = true) else c }
                                val newFlipped = flippedIndices + index
                                flippedIndices = newFlipped

                                if (newFlipped.size == 2) {
                                    isChecking = true
                                    coroutineScope.launch {
                                        delay(800)
                                        val first = cards[newFlipped[0]]
                                        val second = cards[newFlipped[1]]
                                        if (first.emoji == second.emoji) {
                                            cards = cards.mapIndexed { i, c ->
                                                if (i == newFlipped[0] || i == newFlipped[1]) c.copy(isMatched = true) else c
                                            }
                                        } else {
                                            cards = cards.mapIndexed { i, c ->
                                                if (i == newFlipped[0] || i == newFlipped[1]) c.copy(isFlipped = false) else c
                                            }
                                        }
                                        flippedIndices = emptyList()
                                        isChecking = false
                                    }
                                }
                            }
                        }
                ) {
                    if (isVisible) {
                        Text(text = card.emoji, fontSize = 26.sp)
                    } else {
                        Text(text = "🌱", fontSize = 18.sp, color = SageLight)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "لعبة هادئة للتركيز البصري دون مؤقت زمني أو تنافس قسري.",
            style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
        )
    }
}

@Composable
fun MindfulRiddlesTab() {
    val riddles = listOf(
        MindfulRiddle(
            question = "شيء يعبر النهار والليل ولا يتعب، يسقي الظمآن ويهدئ الصدور إذا جرى بهدوء؟",
            answer = "الماء والينبوع العذب 💧",
            reflection = "كن كالماء انسيابياً وليناً؛ يجد طريقه حول الصخور بهدوء دون صخب."
        ),
        MindfulRiddle(
            question = "أملك مفاتيح لكل الأبواب لكن بلا أقفال، وأحمل أنغاماً تطيب بها النفوس؟",
            answer = "البيانو / الآلة الموسيقية 🎵",
            reflection = "نغمة واحدة هادئة كفيلة بتبديد ساعات من التشتت والضجيج."
        ),
        MindfulRiddle(
            question = "أخف من الريشة، ومع ذلك لا يستطيع أشد الناس قوة حبسي لأكثر من دقائق؟",
            answer = "النَفَس الشهيق والزفير 🫁",
            reflection = "نفسك هو أثمن هدية حية تتجدد في كل لحظة، امنحه حقه من العمق والراحة."
        ),
        MindfulRiddle(
            question = "كلما أخذت مني كَبِرتُ، وكلما وضعت فيّ صَغُرتُ؟",
            answer = "الحفرة في الأرض 🪴",
            reflection = "أحياناً إفراغ ما في القلب هو الطريقة الوحيدة لصنع مساحة جديدة للسلام."
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        riddles.forEach { riddle ->
            var isRevealed by remember { mutableStateOf(false) }

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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Psychology, contentDescription = null, tint = DarkGreen)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "لغز تأملي",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = DeepForest
                                )
                            )
                        }

                        TextButton(onClick = { isRevealed = !isRevealed }) {
                            Text(
                                text = if (isRevealed) "إخفاء الحل" else "كشف الحل",
                                color = DarkGreen,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = riddle.question,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Medium,
                            color = DeepForest,
                            lineHeight = 22.sp
                        )
                    )

                    AnimatedVisibility(visible = isRevealed) {
                        Column {
                            Spacer(modifier = Modifier.height(10.dp))
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = WarmBeige.copy(alpha = 0.6f),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = "الحل: ${riddle.answer}",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = DarkGreen
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = riddle.reflection,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = TextMuted,
                                            lineHeight = 18.sp
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ThisOrThatTab() {
    val choices = listOf(
        CalmThisOrThat(
            optionA = "مشي هادئ تحت رذاذ المطر 🌧️",
            optionB = "جلسة بجانب مدفأة مع كتاب ورقي 📖",
            takeaway = "كلا الخيارين يمنحك لحظة انسجام وتفريغ للذهن، اختر ما تميل له روحك اليوم."
        ),
        CalmThisOrThat(
            optionA = "شروق الشمس على قمة جبلية 🌄",
            optionB = "غروب الشمس على شاطئ البحر 🌅",
            takeaway = "البدايات والنهائيات تحملان الجمال نفسه إذا رافقها الرضا والسكينة."
        ),
        CalmThisOrThat(
            optionA = "كوب شاي بالنعناع واللافندر 🍵",
            optionB = "فنجان قهوة دافئة في الصباح الباكر ☕",
            takeaway = "الطقس البسيط الذي تمنح فيه نفسك استراحة حقيقية هو ما يصنع الفرق."
        ),
        CalmThisOrThat(
            optionA = "الصمت التام والتأمل في الطبيعة 🍃",
            optionB = "موسيقى هادئة تملأ الغرفة بالدفء 🎶",
            takeaway = "لكل مزاج احتياجه، استمع لما يريده جسدك وامنحه إياه دون تردد."
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        choices.forEach { choice ->
            var selectedChoice by remember { mutableStateOf<String?>(null) }

            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "ما الذي تشعر بالانجذاب نحوه الآن؟",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = DeepForest
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = if (selectedChoice == "A") DarkGreen else WarmBeige.copy(alpha = 0.5f),
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(14.dp))
                                .clickable { selectedChoice = "A" }
                        ) {
                            Text(
                                text = choice.optionA,
                                modifier = Modifier.padding(12.dp),
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = if (selectedChoice == "A") Color.White else TextDark,
                                    fontWeight = if (selectedChoice == "A") FontWeight.Bold else FontWeight.Normal,
                                    textAlign = TextAlign.Center
                                )
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = if (selectedChoice == "B") DarkGreen else WarmBeige.copy(alpha = 0.5f),
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(14.dp))
                                .clickable { selectedChoice = "B" }
                        ) {
                            Text(
                                text = choice.optionB,
                                modifier = Modifier.padding(12.dp),
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = if (selectedChoice == "B") Color.White else TextDark,
                                    fontWeight = if (selectedChoice == "B") FontWeight.Bold else FontWeight.Normal,
                                    textAlign = TextAlign.Center
                                )
                            )
                        }
                    }

                    AnimatedVisibility(visible = selectedChoice != null) {
                        Column {
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = choice.takeaway,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextMuted,
                                    lineHeight = 18.sp
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------------------
// 2. AUDIO & SOUNDSCAPES SECTION (Live Local Sound Synthesis)
// -----------------------------------------------------------------------------------------

data class SoundscapeItem(val id: String, val title: String, val subtitle: String, val icon: String, val color: Color)

@Composable
fun AudioSoundscapesSection() {
    val coroutineScope = rememberCoroutineScope()
    var isPlaying by remember { mutableStateOf(CalmAudioEngine.isCurrentlyPlaying()) }
    var activeSoundId by remember { mutableStateOf(CalmAudioEngine.getActiveMode()) }
    var selectedDurationMinutes by remember { mutableIntStateOf(0) } // 0 = continuous

    val soundscapes = listOf(
        SoundscapeItem("rain", "مطر هادئ على النافذة", "رذاذ خفيف مهدئ للجهاز العصبي", "🌧️", Color(0xFF6B8E88)),
        SoundscapeItem("waves", "أمواج البحر المتهادية", "مد وجزر طبيعي يساعد على النوم", "🌊", Color(0xFF5A8492)),
        SoundscapeItem("wind", "نسيم حفيف أوراق الشجر", "همس الغابة المريح للذهن", "🍃", Color(0xFF75977E)),
        SoundscapeItem("noise", "ضوضاء بيضاء نقية", "عزل الضجيج الخارجي وتعزيز التركيز", "🌙", Color(0xFF88849E)),
        SoundscapeItem("tone", "نغمة السكون (432 هرتز)", "تردد تأملي منسجم مع الطبيعة", "🧘", Color(0xFF9E8475))
    )

    fun togglePlay(soundId: String) {
        if (isPlaying && activeSoundId == soundId) {
            CalmAudioEngine.stop()
            isPlaying = false
        } else {
            activeSoundId = soundId
            CalmAudioEngine.start(soundId)
            isPlaying = true

            if (selectedDurationMinutes > 0) {
                coroutineScope.launch {
                    delay(selectedDurationMinutes * 60 * 1000L)
                    CalmAudioEngine.stop()
                    isPlaying = false
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 80.dp)
            .testTag("audio_soundscapes_section")
    ) {
        // Player Banner with Animated Waveform
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = DarkGreen),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        val activeSound = soundscapes.find { it.id == activeSoundId }
                        Text(
                            text = if (isPlaying) "يعمل الآن: ${activeSound?.title ?: "صوت طبيعي"}" else "مشغل صوتيات السكينة",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                        Text(
                            text = if (isPlaying) "صوت بيئي مولّد محلياً بلا توقف" else "اختر صوتاً مهدئاً من القائمة أدناه",
                            style = MaterialTheme.typography.bodySmall.copy(color = SageLight)
                        )
                    }

                    Surface(
                        shape = CircleShape,
                        color = if (isPlaying) WarmTerracotta else SageGreen,
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .clickable {
                                if (isPlaying) {
                                    CalmAudioEngine.stop()
                                    isPlaying = false
                                } else {
                                    togglePlay(activeSoundId)
                                }
                            }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = if (isPlaying) "إيقاف" else "تشغيل",
                                tint = Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Animated Sound Wave
                AnimatedAudioVisualizer(isPlaying = isPlaying)

                Spacer(modifier = Modifier.height(12.dp))

                // Duration selector chips
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "مؤقت النوم:", style = MaterialTheme.typography.labelSmall.copy(color = SageLight))
                    listOf(0 to "مستمر", 5 to "5 دقائق", 15 to "15 دقيقة", 30 to "30 دقيقة").forEach { (min, label) ->
                        val isSel = selectedDurationMinutes == min
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSel) SageLight else Color.White.copy(alpha = 0.15f),
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { selectedDurationMinutes = min }
                        ) {
                            Text(
                                text = label,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (isSel) DeepForest else Color.White,
                                    fontSize = 10.sp
                                )
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Soundscape Cards List
        Text(
            text = "اختر الأصوات المحيطية:",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = DeepForest
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        soundscapes.forEach { item ->
            val isCurrentActive = isPlaying && activeSoundId == item.id
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isCurrentActive) SageLight.copy(alpha = 0.5f) else Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { togglePlay(item.id) }
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
                            .background(WarmBeige)
                    ) {
                        Text(text = item.icon, fontSize = 22.sp)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = DeepForest
                            )
                        )
                        Text(
                            text = item.subtitle,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                        )
                    }

                    IconButton(onClick = { togglePlay(item.id) }) {
                        Icon(
                            imageVector = if (isCurrentActive) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = if (isCurrentActive) DarkGreen else TextMuted
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "ملاحظة: صوتيات طبيعية مدمجة ومولدة محلياً بالجهاز بالكامل دون الحاجة للاتصال بالإنترنت أو تحميل ملفات صوتية غير مرخصة.",
            style = MaterialTheme.typography.bodySmall.copy(color = TextMuted, fontSize = 11.sp)
        )
    }
}

@Composable
fun AnimatedAudioVisualizer(isPlaying: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "audio_bars")
    val heights = List(16) { i ->
        infiniteTransition.animateFloat(
            initialValue = 4f,
            targetValue = if (isPlaying) (12f + (i * 7 % 24)) else 4f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 350 + (i * 45 % 300), easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "bar_$i"
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(34.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        heights.forEach { h ->
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(h.value.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(if (isPlaying) SageLight else Color.White.copy(alpha = 0.3f))
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
    }
}

// -----------------------------------------------------------------------------------------
// 3. TRANQUIL VISUAL SCENES (Interactive Video/Canvas Tranquility Cards)
// -----------------------------------------------------------------------------------------

data class TranquilScene(
    val id: String,
    val title: String,
    val description: String,
    val prompt: String
)

@Composable
fun TranquilVisualScenesSection() {
    val scenes = listOf(
        TranquilScene("ocean", "أمواج البحر المتهادية", "حركة مائية انسيابية متكررة لتهدئة الأفكار", "تأمل حركة الموج وتخيل أفكارك القلقة تذوب مع الرغوة الناعمة."),
        TranquilScene("leaves", "تساقط أوراق الخريف", "أوراق تتهادى برفق مع حركة النسيم", "لاحظ كيف تسلم الورقة نفسها للجاذبية بلا مقاومة أو توتر."),
        TranquilScene("stars", "سماء الليل والنجوم", "أضواء متلألئة في سكون الليل الواسع", "أنت جزء من هذا الكون الفسيح، ما يشغلك اليوم سيمر بسلام."),
        TranquilScene("fire", "لهب المدفأة الدافئ", "شرارات متصاعدة ونور يبعث على الطمأنينة", "اشعر بالدفء يسري في أطرافك وأرخِ عضلات كتفيك.")
    )

    var currentSceneIndex by remember { mutableIntStateOf(0) }
    var isAnimationPlaying by remember { mutableStateOf(true) }
    val activeScene = scenes[currentSceneIndex]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 80.dp)
            .testTag("tranquil_scenes_section")
    ) {
        // Scene Switcher Selector
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(scenes.indices.toList()) { index ->
                val isSelected = currentSceneIndex == index
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isSelected) DarkGreen else WarmBeige.copy(alpha = 0.6f),
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { currentSceneIndex = index }
                ) {
                    Text(
                        text = scenes[index].title,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = if (isSelected) Color.White else TextDark,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Main Animated Visual Canvas Card (Acts as the Tranquil Video Screen)
        Card(
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                when (activeScene.id) {
                    "ocean" -> AnimatedOceanCanvas(isAnimationPlaying)
                    "leaves" -> AnimatedLeavesCanvas(isAnimationPlaying)
                    "stars" -> AnimatedNightSkyCanvas(isAnimationPlaying)
                    "fire" -> AnimatedFireplaceCanvas(isAnimationPlaying)
                }

                // Play / Pause Overlay Control
                Surface(
                    shape = CircleShape,
                    color = Color.Black.copy(alpha = 0.4f),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(12.dp)
                        .clip(CircleShape)
                        .clickable { isAnimationPlaying = !isAnimationPlaying }
                ) {
                    Icon(
                        imageVector = if (isAnimationPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Scene Explanation & Guided Meditation Prompt
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = activeScene.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = DeepForest
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = activeScene.description,
                    style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = WarmBeige.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "«${activeScene.prompt}»",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = DeepForest,
                            fontWeight = FontWeight.Medium,
                            lineHeight = 22.sp
                        ),
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "بطاقات بصرية متحركة توضيحية تحاكي المشاهد الطبيعية لإراحة العين دون تشغيل وسائط غير مرخصة.",
            style = MaterialTheme.typography.bodySmall.copy(color = TextMuted, fontSize = 11.sp)
        )
    }
}

@Composable
fun AnimatedOceanCanvas(isPlaying: Boolean) {
    val transition = rememberInfiniteTransition(label = "ocean_wave")
    val waveOffset by transition.animateFloat(
        initialValue = 0f,
        targetValue = if (isPlaying) (2 * PI).toFloat() else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "waveOffset"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        // Deep water background
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFF1B3B4A), Color(0xFF2E6377), Color(0xFF5694A6))
            )
        )

        // Wave 1 (back)
        val path1 = Path().apply {
            moveTo(0f, height * 0.55f)
            for (x in 0..width.toInt() step 10) {
                val y = height * 0.55f + sin(x * 0.015f + waveOffset) * 18f
                lineTo(x.toFloat(), y)
            }
            lineTo(width, height)
            lineTo(0f, height)
            close()
        }
        drawPath(path1, color = Color(0x884A8E9F))

        // Wave 2 (front)
        val path2 = Path().apply {
            moveTo(0f, height * 0.65f)
            for (x in 0..width.toInt() step 10) {
                val y = height * 0.65f + sin(x * 0.012f - waveOffset + 1f) * 24f
                lineTo(x.toFloat(), y)
            }
            lineTo(width, height)
            lineTo(0f, height)
            close()
        }
        drawPath(path2, color = Color(0xBB73B8C7))

        // Foam layer
        val path3 = Path().apply {
            moveTo(0f, height * 0.75f)
            for (x in 0..width.toInt() step 10) {
                val y = height * 0.75f + sin(x * 0.018f + waveOffset * 0.8f) * 14f
                lineTo(x.toFloat(), y)
            }
            lineTo(width, height)
            lineTo(0f, height)
            close()
        }
        drawPath(path3, color = Color(0x66FFFFFF))
    }
}

@Composable
fun AnimatedLeavesCanvas(isPlaying: Boolean) {
    val transition = rememberInfiniteTransition(label = "leaves_drift")
    val time by transition.animateFloat(
        initialValue = 0f,
        targetValue = if (isPlaying) 1f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 5000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "time"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFF283B2E), Color(0xFF384D3C), Color(0xFF4C6652))
            )
        )

        val leafPositions = listOf(
            Triple(0.2f, 0.1f, Color(0xFFD69458)),
            Triple(0.5f, 0.3f, Color(0xFFBA694A)),
            Triple(0.8f, 0.2f, Color(0xFFC7A855)),
            Triple(0.35f, 0.6f, Color(0xFFE28B5E)),
            Triple(0.7f, 0.7f, Color(0xFF8CAF7A))
        )

        leafPositions.forEachIndexed { i, (baseX, baseY, leafColor) ->
            val progress = (time + baseY) % 1f
            val currentY = progress * size.height
            val currentX = (baseX * size.width) + sin((time * 2 * PI + i).toFloat()) * 30f

            drawCircle(
                color = leafColor.copy(alpha = 0.85f),
                radius = 10f,
                center = Offset(currentX, currentY)
            )
            drawOval(
                color = leafColor,
                topLeft = Offset(currentX - 12f, currentY - 6f),
                size = Size(24f, 12f)
            )
        }
    }
}

@Composable
fun AnimatedNightSkyCanvas(isPlaying: Boolean) {
    val transition = rememberInfiniteTransition(label = "twinkle")
    val twinkle by transition.animateFloat(
        initialValue = 0.3f,
        targetValue = if (isPlaying) 1.0f else 0.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "twinkle"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFF0F172A), Color(0xFF1E293B), Color(0xFF334155))
            )
        )

        // Moon
        drawCircle(
            color = Color(0xFFFFF9E6),
            radius = 24f,
            center = Offset(size.width * 0.8f, size.height * 0.25f)
        )
        drawCircle(
            color = Color(0xFF0F172A),
            radius = 20f,
            center = Offset(size.width * 0.78f, size.height * 0.23f)
        )

        // Twinkling stars
        val starCoords = listOf(
            Pair(0.15f, 0.2f), Pair(0.35f, 0.15f), Pair(0.5f, 0.35f),
            Pair(0.25f, 0.5f), Pair(0.65f, 0.2f), Pair(0.45f, 0.7f),
            Pair(0.7f, 0.6f), Pair(0.85f, 0.45f), Pair(0.1f, 0.8f)
        )

        starCoords.forEachIndexed { idx, (sx, sy) ->
            val factor = if (idx % 2 == 0) twinkle else (1.3f - twinkle)
            drawCircle(
                color = Color.White.copy(alpha = factor.coerceIn(0.2f, 1f)),
                radius = if (idx % 3 == 0) 3.5f else 2f,
                center = Offset(sx * size.width, sy * size.height)
            )
        }
    }
}

@Composable
fun AnimatedFireplaceCanvas(isPlaying: Boolean) {
    val transition = rememberInfiniteTransition(label = "flicker")
    val flameScale by transition.animateFloat(
        initialValue = 0.85f,
        targetValue = if (isPlaying) 1.15f else 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "flameScale"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFF591D0E), Color(0xFF2A0C06), Color(0xFF140503)),
                center = Offset(size.width * 0.5f, size.height * 0.75f),
                radius = size.width * 0.7f
            )
        )

        val cx = size.width * 0.5f
        val cy = size.height * 0.75f

        // Outer warm glow
        drawCircle(
            color = Color(0x66FF6600),
            radius = 65f * flameScale,
            center = Offset(cx, cy - 20f)
        )

        // Inner yellow flame
        drawCircle(
            color = Color(0xAAFFB732),
            radius = 38f * flameScale,
            center = Offset(cx, cy - 10f)
        )

        // White core
        drawCircle(
            color = Color(0xEEFFFFEE),
            radius = 16f,
            center = Offset(cx, cy)
        )
    }
}

// -----------------------------------------------------------------------------------------
// 4. ART & NOTICING SECTION
// -----------------------------------------------------------------------------------------

@Composable
fun ArtAndNoticingSection() {
    val ripples = remember { mutableStateListOf<TouchRipple>() }
    val palette = listOf(
        Pair("مريمي", SageGreen),
        Pair("خوخي", WarmTerracotta.copy(alpha = 0.6f)),
        Pair("سماء", Color(0xFF6B9BAA)),
        Pair("غابة", DarkGreen.copy(alpha = 0.6f)),
        Pair("ذهبي", Color(0xFFD4B157))
    )
    var selectedColorIndex by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 80.dp)
    ) {
        // Color Palette Selector
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "اختر لون السكينة ثم المس أو ارسم:",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = DeepForest
                )
            )
            TextButton(onClick = { ripples.clear() }) {
                Text("مسح اللوحة", color = DarkGreen, fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            palette.forEachIndexed { idx, (name, color) ->
                val isSel = selectedColorIndex == idx
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(color)
                        .clickable { selectedColorIndex = idx }
                        .then(
                            if (isSel) Modifier.clip(CircleShape).background(Color.Black.copy(alpha = 0.2f))
                            else Modifier
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isSel) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Drawing Canvas
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
                .pointerInput(selectedColorIndex) {
                    detectTapGestures { offset ->
                        val picked = palette[selectedColorIndex].second
                        ripples.add(TouchRipple(offset, 45f, picked))
                        if (ripples.size > 25) ripples.removeAt(0)
                    }
                }
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                ripples.forEach { ripple ->
                    drawCircle(color = ripple.color, radius = ripple.radius, center = ripple.offset)
                    drawCircle(color = ripple.color.copy(alpha = 0.25f), radius = ripple.radius + 25f, center = ripple.offset)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Notice Something Beautiful Card
        NoticeSomethingBeautifulTab()
    }
}

@Composable
fun NoticeSomethingBeautifulTab() {
    val prompts = listOf(
        "انظر حولك الآن، وابحث عن تفصيل صغير في الغرفة بلون يبعث على الراحة.",
        "تأمل الضوء في هذه اللحظة؛ كيف يسقط على الجدار أو الطاولة؟",
        "استمع بإنصات: ما أهدأ صوت يمكنك سماعه في محيطك الآن؟",
        "تلمس ملمساً ناعماً أو دافئاً بقربك وامنح نفسك بضع ثوانٍ للشعور به.",
        "تذكر شخصاً أو لحظة صغيرة جعلتك تبتسم مؤخراً."
    )

    var promptIndex by remember { mutableIntStateOf(0) }
    var note by remember { mutableStateOf("") }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Nature, contentDescription = null, tint = DarkGreen, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "تمرين ملاحظة بسيط",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = DeepForest)
                    )
                }

                TextButton(onClick = { promptIndex = (promptIndex + 1) % prompts.size }) {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = "اقتراح آخر", modifier = Modifier.size(16.dp), tint = DarkGreen)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("اقتراح آخر", color = DarkGreen, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = prompts[promptIndex],
                style = MaterialTheme.typography.bodyLarge.copy(color = DeepForest, fontWeight = FontWeight.Medium, lineHeight = 24.sp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                placeholder = { Text("ما الشيء الجميل الذي لاحظته؟ (مؤقت لنفسك)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = DarkGreen, unfocusedBorderColor = SageLight),
                maxLines = 3
            )
        }
    }
}

// -----------------------------------------------------------------------------------------
// 5. GENTLE SMILES TAB
// -----------------------------------------------------------------------------------------

@Composable
fun GentleSmilesTab() {
    val smiles = listOf(
        "نباتك المنزلي لا يحكم عليك إذا لم تُنجز كل مهامك اليوم، هو فقط يقدّر قطرة الماء التي تسقيه إياها 🪴",
        "تذكر: حتى أسرع القطارات تقف في محطات للاستراحة وإعادة التزود بالوقود 🚂",
        "النوم ليس كسلاً؛ هو صيانة مجانية لجهازك العصبي يمنحك إياها جسدك كل ليلة 🛌",
        "«أحياناً أعظم إنجاز لليوم هو أنك تجاوزته بلطف مع نفسك دون معارك داخلية» ✨",
        "فنجان القهوة أو الشاي لا يبالي بجدولك الزمني، هو يريدك فقط أن تستمتع برائحته الدافئة ☕"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        smiles.forEach { smile ->
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.SentimentSatisfied,
                        contentDescription = null,
                        tint = DarkGreen,
                        modifier = Modifier.size(24.dp).padding(top = 2.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = smile,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = DeepForest,
                            lineHeight = 22.sp
                        )
                    )
                }
            }
        }
    }
}
