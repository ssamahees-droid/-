package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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

data class GameChapter(val id: String, val label: String, val title: String, val intro: String)

data class PresetSituation(val key: String, val chapterId: String, val label: String, val text: String)

data class BodySignal(val key: String, val label: String)

data class CoreNeed(val key: String, val title: String, val text: String)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GwayaHekayaGame(
    viewModel: NasmatViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }
    val context = LocalContext.current

    val chapters = listOf(
        GameChapter("relationships", "العلاقات", "فصل القرب والمسافة", "نكتشف ما بين الرسائل والحدود والرغبة في أن نكون مفهومين."),
        GameChapter("work", "العمل", "فصل الإنجاز والضغط", "نفرّق بين النتيجة وقيمتنا، ونستعيد خطوة ممكنة وسط الزحمة."),
        GameChapter("family", "الأسرة والبيت", "فصل الأمان والتواصل", "نسمع أثر الكلام القريب ونبني مساحة أهدى للحوار مع أنفسنا والآخرين."),
        GameChapter("study", "الدراسة والثقة", "فصل التعلّم والشجاعة", "نحوّل الخوف من التقييم إلى خطوات صغيرة تذكّرنا أن التعلّم ليس حكماً على قيمتنا.")
    )

    val presetSituations = listOf(
        PresetSituation("no-reply", "relationships", "رسالة بلا رد", "أرسلت رسالة من وقت طويل… وحتى الآن لا يوجد رد."),
        PresetSituation("misunderstood", "relationships", "لم يفهمني أحد", "حاولت شرح ما بداخلي، لكن شعرت أن لا أحد يفهمني."),
        PresetSituation("rejection", "relationships", "التعرض لرفض", "تقدمت لفرصة مهمة ورُفضت، وشعرت أن الأبواب أُغلقت."),
        PresetSituation("boundary", "relationships", "قلت «لا»", "قلت «لا» لطلب لا يناسبني، وبعدها شعرت بالذنب والتقصير."),
        PresetSituation("criticism", "work", "تعرضت لنقد", "تعرضت لملاحظات نقدية أمام زملائي وشعرت بالارتباك."),
        PresetSituation("overload", "work", "تراكم المهام", "المسؤوليات تراكمت ولم أعد أعرف من أين أبدأ."),
        PresetSituation("comparison", "work", "مقارنة نفسي", "رأيت شخصاً يتقدم بسرعة وبدأت بمقارنة نفسي به بقسوة."),
        PresetSituation("family-talk", "family", "حوار متوتر بالبيت", "تحدثت مع شخص قريب وانتهى الحوار بشعور بالضيق وعدم الفهم."),
        PresetSituation("decision", "family", "قرار محيّر", "أمامي خياران مهمان وكلما أردت اختيار أحدهما خفت من الندم."),
        PresetSituation("plan-change", "family", "الخطة تغيرت", "خطة كنت أنتظرها أُلغيت فجأة واضطررت للبدء من جديد."),
        PresetSituation("exam-anxiety", "study", "قلق الاختبار", "لدي اختبار قريب وكلما بدأت بالمذاكرة شعرت أنني سأنسى كل شيء."),
        PresetSituation("study-delay", "study", "صعوبة البدء", "تأخرت في المذاكرة وبدأت ألوم نفسي بدلاً من أخذ خطوة صغيرة.")
    )

    // Game state across the 6 stages
    var stageIndex by remember { mutableIntStateOf(0) } // 0=Situation, 1=Thought, 2=Feeling, 3=Body, 4=Need, 5=Reframe/Finish
    val stageNames = listOf("الموقف", "الفكرة", "الشعور", "الجسم", "الاحتياج", "القصة الجديدة")

    var selectedChapterId by remember { mutableStateOf("relationships") }
    var situationText by remember { mutableStateOf(presetSituations[0].text) }
    var selectedThought by remember { mutableStateOf("أكيد أنا غير مهم أو غير كافٍ") }
    var selectedFeeling by remember { mutableStateOf("قلق") }
    var selectedBodySignal by remember { mutableStateOf("نَفَسي قصير شوية") }
    var selectedNeed by remember { mutableStateOf("أمان: أحتاج أن أتذكر أن قيمتي ثابتة") }
    var hasSavedToJourney by remember { mutableStateOf(false) }

    // Dynamic suggestions based on situation keywords
    val thoughtOptions = remember(situationText) {
        when {
            situationText.contains("رسالة") || situationText.contains("رد") -> listOf(
                "أكيد أنا غير مهم عندهم",
                "أنا زعلتهم بدون ما أقصد",
                "الناس بتتجاهلني لما أكون محتاجهم"
            )
            situationText.contains("امتحان") || situationText.contains("مذاكرة") || situationText.contains("اختبار") -> listOf(
                "لو لم أكن ممتازاً، فهذا يعني أنني فاشل",
                "الوقت فات ومستحيل ألحق",
                "الآخرين أذكى وأهدأ مني"
            )
            situationText.contains("رفض") || situationText.contains("اترفضت") || situationText.contains("خطة") -> listOf(
                "الباب أُغلق وانتهت الحكاية",
                "حظي دائماً هكذا في الأشياء المهمة",
                "كل جهدي السابق ضاع بلا قيمة"
            )
            situationText.contains("قلت «لا»") || situationText.contains("طلب") -> listOf(
                "لو رفضت فأنا أناني ومقصر",
                "الناس ستغضب مني وتتغير معاملتهم",
                "لا يحق لي أن أضع راحتي قبل غيري"
            )
            else -> listOf(
                "أنا السبب في تعقيد الأمور",
                "الأمور لن تتحسن بسهولة",
                "الكل يتوقع مني الكمال دائماً"
            )
        }
    }

    val feelingOptions = listOf("قلق", "ذنب", "حيرة", "إحباط", "حزن", "خوف من التقصير", "تشتت", "غضب هادئ")

    val bodySignals = listOf(
        BodySignal("chest", "نَفَسي قصير ومكتوم"),
        BodySignal("shoulders", "كتفاي ورقبتي مشدودان"),
        BodySignal("stomach", "معدتي منقبضة"),
        BodySignal("head", "رأسي ممتلئ بالسيناريوهات"),
        BodySignal("heart", "نبضات قلبي متسارعة"),
        BodySignal("throat", "غصة خفيفة في حلقي")
    )

    val coreNeeds = listOf(
        CoreNeed("safety", "أمان", "أحتاج أن أتذكر أن قيمتي كإنسان ثابتة ولا تتغير بالمواقف."),
        CoreNeed("space", "مساحة ووقت", "أحتاج أن أمنح نفسي وقتاً للاستيعاب قبل إطلاق الأحكام."),
        CoreNeed("clarity", "وضوح", "أفضل معرفة الحقائق بدل التخمين وبناء القصص القلقة."),
        CoreNeed("step", "خطوة صغيرة", "أحتاج للبدء بجزء صغير يمكن إنجازه الآن بلا إرهاق."),
        CoreNeed("listening", "إصغاء وتعبير", "أحتاج أن أعبّر عما بداخلي بوضوح وهدوء دون هجوم.")
    )

    // Reframe narrative content
    val oldStory = selectedThought
    val newStory = remember(selectedThought, selectedNeed) {
        when {
            selectedThought.contains("غير مهم") -> "«أنا متأثر لأن الموقف يهمني… وهناك احتمالات واقعية كثيرة غير كوني غير مهم.»"
            selectedThought.contains("ممتاز") || selectedThought.contains("فاشل") -> "«أنا أتعلّم وأتطور بالتدريج، وقيمتي ليست محصورة في درجة أو تقييم واحد.»"
            selectedThought.contains("الباب أُغلق") -> "«الخطة تغيرت، لكن خبرتي مستمرة ولا زالت هناك خطوات بديلة يمكنني اختيارها.»"
            selectedThought.contains("أناني") -> "«حدودي اللطيفة تحميني لأكون حاضراً بصدق، وليست دليلاً على الأنانية.»"
            else -> "«هذا موقف عابر ومشاعر مفهومة، وبإمكاني التعامل معه خطوة بخطوة برفق.»"
        }
    }

    val practiceStep = remember(selectedChapterId) {
        when (selectedChapterId) {
            "relationships" -> "تمرين: قبل تصديق القصة المقلقة، اكتب احتمالين بديلين منطقيين (مثلاً: الطرف الآخر منشغل أو لم ينتبه)."
            "study" -> "تمرين: حدد أصغر خطوة قابلة للتنفيذ خلال 10 دقائق فقط (مثل قراءة صفحة واحدة) وابدأ بها بلا انتظار المزاج المثالي."
            "work" -> "تمرين: قسّم المهمة الكبيرة إلى 3 خطوات بسيطة، وركّز على إتمام الخطوة الأولى فقط اليوم."
            else -> "تمرين: خذ 3 أنفاس بطيئة وعميقة، وذكّر نفسك: «لست مضطراً لحل كل شيء اليوم، خطوة واحدة تكفي»."
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CreamBackground)
            .padding(18.dp)
            .testTag("gwaya_hekay_screen")
    ) {
        // Top Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "رجوع", tint = DarkGreen)
                }
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "جوايا حكاية",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = DarkGreen
                            )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = SageLight
                        ) {
                            Text(
                                text = "مساحة آمنة للفضول",
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall.copy(color = DeepForest, fontSize = 10.sp)
                            )
                        }
                    }
                    Text(
                        text = "العالم الذي يشرح نفسه بنفسه • رحلة فهم ما بداخلنا",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                    )
                }
            }

            IconButton(
                onClick = {
                    try {
                        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://gwaya-hekay-nn9lqavd.manus.space/"))
                        context.startActivity(browserIntent)
                    } catch (e: Exception) {
                        Toast.makeText(context, "يمكنك فتح اللعبة مباشرة عبر تبويب موقع اللعبة المباشر", Toast.LENGTH_SHORT).show()
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.OpenInBrowser,
                    contentDescription = "فتح الرابط الأصلي",
                    tint = TextMuted,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        var isWebMode by remember { mutableStateOf(false) }

        // Mode Switcher: Native Offline vs Live Web
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (!isWebMode) DarkGreen else WarmBeige.copy(alpha = 0.5f),
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { isWebMode = false }
            ) {
                Text(
                    text = "🍃 الرحلة التفاعلية (أوفلاين)",
                    modifier = Modifier.padding(vertical = 8.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = if (!isWebMode) Color.White else TextDark,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (isWebMode) DarkGreen else WarmBeige.copy(alpha = 0.5f),
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { isWebMode = true }
            ) {
                Text(
                    text = "🌐 موقع اللعبة المباشر",
                    modifier = Modifier.padding(vertical = 8.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = if (isWebMode) Color.White else TextDark,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (isWebMode) {
            var isWebLoading by remember { mutableStateOf(true) }
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    AndroidView(
                        factory = { ctx ->
                            WebView(ctx).apply {
                                layoutParams = ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT
                                )
                                settings.apply {
                                    javaScriptEnabled = true
                                    domStorageEnabled = true
                                    databaseEnabled = true
                                    useWideViewPort = true
                                    loadWithOverviewMode = true
                                    cacheMode = WebSettings.LOAD_DEFAULT
                                }
                                webViewClient = object : WebViewClient() {
                                    override fun onPageFinished(view: WebView?, url: String?) {
                                        super.onPageFinished(view, url)
                                        isWebLoading = false
                                    }
                                }
                                loadUrl("https://gwaya-hekay-nn9lqavd.manus.space/")
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )

                    if (isWebLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.White.copy(alpha = 0.85f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                CircularProgressIndicator(color = DarkGreen)
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "جاري فتح موقع اللعبة…",
                                    style = MaterialTheme.typography.bodyMedium.copy(color = DeepForest)
                                )
                            }
                        }
                    }
                }
            }
        } else {

        // Progress Stepper (01 to 06)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            stageNames.forEachIndexed { idx, name ->
                val isCurrent = stageIndex == idx
                val isPassed = stageIndex > idx
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { stageIndex = idx }
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(
                                when {
                                    isCurrent -> DarkGreen
                                    isPassed -> SageGreen
                                    else -> WarmBeige
                                }
                            )
                    ) {
                        if (isPassed) {
                            Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        } else {
                            Text(
                                text = "${idx + 1}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (isCurrent) Color.White else TextDark,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = name,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 10.sp,
                            color = if (isCurrent) DarkGreen else TextMuted,
                            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Companion Mascot Banner
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = WarmBeige.copy(alpha = 0.55f),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(SageLight)
                ) {
                    Text(text = "🍃", fontSize = 18.sp)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "نسمة، رفيق الرحلة:",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = DarkGreen
                        )
                    )
                    Text(
                        text = when (stageIndex) {
                            0 -> "أنا معك… نأخذها خطوة بخطوة لنفهم ما حدث بهدوء."
                            1 -> "الأفكار أحياناً تتسابق لتأليف قصة كاملة؛ دعنا نراقبها برفق."
                            2 -> "المشاعر رسائل تود أن تُسمع، بلا حكم بالصواب أو الخطأ."
                            3 -> "الجسم يلتقط الإشارة قبل الكلام؛ أين تشعر بالثقل الآن؟"
                            4 -> "خلف كل شعور قوي يكمن احتياج إنساني بسيط ومحترم."
                            else -> "الآن تتضح الحكاية: القصة القديمة كانت مجرد خوف، والقصة الجديدة تصنع مساحة للسلام."
                        },
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = DeepForest,
                            lineHeight = 18.sp
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Stage Content (Scrollable Container)
        Box(modifier = Modifier.weight(1f)) {
            Crossfade(targetState = stageIndex, label = "game_stage_crossfade") { stage ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(bottom = 16.dp)
                ) {
                    when (stage) {
                        0 -> StageSituation(
                            chapters = chapters,
                            selectedChapterId = selectedChapterId,
                            onSelectChapter = { selectedChapterId = it },
                            presetSituations = presetSituations.filter { it.chapterId == selectedChapterId },
                            situationText = situationText,
                            onSelectPreset = { situationText = it },
                            onChangeText = { situationText = it }
                        )
                        1 -> StageThought(
                            suggestions = thoughtOptions,
                            selectedThought = selectedThought,
                            onSelectThought = { selectedThought = it }
                        )
                        2 -> StageFeeling(
                            feelings = feelingOptions,
                            selectedFeeling = selectedFeeling,
                            onSelectFeeling = { selectedFeeling = it }
                        )
                        3 -> StageBody(
                            signals = bodySignals,
                            selectedSignal = selectedBodySignal,
                            onSelectSignal = { selectedBodySignal = it }
                        )
                        4 -> StageNeed(
                            needs = coreNeeds,
                            selectedNeed = selectedNeed,
                            onSelectNeed = { selectedNeed = it }
                        )
                        5 -> StageReframe(
                            situation = situationText,
                            oldStory = oldStory,
                            newStory = newStory,
                            practice = practiceStep,
                            feeling = selectedFeeling,
                            bodySignal = selectedBodySignal,
                            hasSaved = hasSavedToJourney,
                            onSaveToJourney = {
                                viewModel.addReflection(
                                    question = "حكاية من داخلي: $situationText",
                                    answer = "القصة السابقة: $oldStory\nالقصة الجديدة: $newStory\nالاحتياج الحقيقي: $selectedNeed"
                                )
                                hasSavedToJourney = true
                            },
                            onRestart = {
                                stageIndex = 0
                                hasSavedToJourney = false
                            }
                        )
                    }
                }
            }
        }

        // Navigation Footer Buttons (السابق / التالي)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (stageIndex > 0) {
                OutlinedButton(
                    onClick = { stageIndex-- },
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("السابق", color = TextDark)
                }
            } else {
                Spacer(modifier = Modifier.width(10.dp))
            }

            if (stageIndex < 5) {
                Button(
                    onClick = { stageIndex++ },
                    colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("الخطوة التالية", color = Color.White, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
        }
    }
}

// -----------------------------------------------------------------------------------------
// STAGE 1: SITUATION (الموقف)
// -----------------------------------------------------------------------------------------
@Composable
fun StageSituation(
    chapters: List<GameChapter>,
    selectedChapterId: String,
    onSelectChapter: (String) -> Unit,
    presetSituations: List<PresetSituation>,
    situationText: String,
    onSelectPreset: (String) -> Unit,
    onChangeText: (String) -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = "المحطة الأولى: ما الموقف الذي يدور في ذهنك؟",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = DeepForest)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "اختر فصلاً من مجالات الحياة أو اختر موقفاً جاهزاً أو اكتب موقفك الخاص:",
                style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Chapters Selector Chips
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(chapters) { chapter ->
                    val isSel = selectedChapterId == chapter.id
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSel) DarkGreen else WarmBeige.copy(alpha = 0.5f),
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onSelectChapter(chapter.id) }
                    ) {
                        Text(
                            text = chapter.label,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = if (isSel) Color.White else TextDark,
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Preset Situations
            Text(
                text = "مواقف شائعة في هذا الفصل:",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = DeepForest)
            )
            Spacer(modifier = Modifier.height(8.dp))

            presetSituations.forEach { preset ->
                val isChosen = situationText == preset.text
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isChosen) SageLight.copy(alpha = 0.6f) else WarmBeige.copy(alpha = 0.35f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onSelectPreset(preset.text) }
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "🌱", fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(text = preset.label, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = DeepForest))
                            Text(text = preset.text, style = MaterialTheme.typography.bodySmall.copy(color = TextMuted))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Custom Text Input
            OutlinedTextField(
                value = situationText,
                onValueChange = onChangeText,
                label = { Text("أو اكتب الموقف بكلماتك الخاصة") },
                placeholder = { Text("مثال: حدث موقف اليوم جعلني أشعر بالحيرة...") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DarkGreen,
                    unfocusedBorderColor = SageLight
                ),
                maxLines = 3
            )
        }
    }
}

// -----------------------------------------------------------------------------------------
// STAGE 2: THOUGHT (الفكرة)
// -----------------------------------------------------------------------------------------
@Composable
fun StageThought(
    suggestions: List<String>,
    selectedThought: String,
    onSelectThought: (String) -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = "المحطة الثانية: ما الفكرة التلقائية التي قفزت لذهنك؟",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = DeepForest)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "العقل يحاول فوراً استنتاج سيناريو لحمايتنا، اختر الفكرة الأقرب أو دوّنها:",
                style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
            )

            Spacer(modifier = Modifier.height(14.dp))

            suggestions.forEach { thought ->
                val isSel = selectedThought == thought
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = if (isSel) SageLight.copy(alpha = 0.6f) else WarmBeige.copy(alpha = 0.35f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { onSelectThought(thought) }
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Lightbulb, contentDescription = null, tint = if (isSel) DarkGreen else TextMuted, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = thought,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                color = DeepForest
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = selectedThought,
                onValueChange = onSelectThought,
                label = { Text("فكرتك بكلماتك الخاصة") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = DarkGreen, unfocusedBorderColor = SageLight)
            )

            Spacer(modifier = Modifier.height(10.dp))

            var thoughtNature by remember { mutableStateOf<String?>(null) }

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                shape = RoundedCornerShape(14.dp),
                color = WarmBeige.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "ميزان الحكمة: هل هذه الفكرة حقيقة أم احتمال؟",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = DeepForest)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (thoughtNature == "possibility") DarkGreen else Color.White,
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { thoughtNature = "possibility" }
                        ) {
                            Text(
                                text = "احتمال ورأي عابر",
                                modifier = Modifier.padding(vertical = 8.dp),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (thoughtNature == "possibility") Color.White else TextDark,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (thoughtNature == "fact") DarkGreen else Color.White,
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { thoughtNature = "fact" }
                        ) {
                            Text(
                                text = "حقيقة واقعة",
                                modifier = Modifier.padding(vertical = 8.dp),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (thoughtNature == "fact") Color.White else TextDark,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }

                    if (thoughtNature != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (thoughtNature == "possibility")
                                "رائع! إدراك أنها احتمال يفتح مساحة واسعة بينك وبين القلق والتوتر."
                            else
                                "حتى مع الوقائع الصعبة، قيمتك ثابتة دائماً ولديك خيارات هادئة للتعامل معها.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = DarkGreen,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------------------
// STAGE 3: FEELING (الشعور)
// -----------------------------------------------------------------------------------------
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StageFeeling(
    feelings: List<String>,
    selectedFeeling: String,
    onSelectFeeling: (String) -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = "المحطة الثالثة: ما الشعور المصاحب لهذه الفكرة؟",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = DeepForest)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "تسمية الشعور بدقة هي أول خطوة نحو تهدئة حدته:",
                style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
            )

            Spacer(modifier = Modifier.height(16.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                feelings.forEach { feeling ->
                    val isSel = selectedFeeling == feeling
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (isSel) DarkGreen else WarmBeige.copy(alpha = 0.5f),
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .clickable { onSelectFeeling(feeling) }
                    ) {
                        Text(
                            text = feeling,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = if (isSel) Color.White else TextDark,
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal
                            )
                        )
                    }
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------------------
// STAGE 4: BODY (الجسم)
// -----------------------------------------------------------------------------------------
@Composable
fun StageBody(
    signals: List<BodySignal>,
    selectedSignal: String,
    onSelectSignal: (String) -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = "المحطة الرابعة: أين تشعر بالإشارة في جسمك؟",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = DeepForest)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "الجسم يستجيب بصدق قبل الكلمات؛ نسمعه برفق دون أن نعتبره نبوءة سلبية:",
                style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
            )

            Spacer(modifier = Modifier.height(14.dp))

            signals.forEach { signal ->
                val isSel = selectedSignal == signal.label
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = if (isSel) SageLight.copy(alpha = 0.65f) else WarmBeige.copy(alpha = 0.35f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { onSelectSignal(signal.label) }
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "🫀", fontSize = 18.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = signal.label,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                color = DeepForest
                            )
                        )
                    }
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------------------
// STAGE 5: NEED (الاحتياج)
// -----------------------------------------------------------------------------------------
@Composable
fun StageNeed(
    needs: List<CoreNeed>,
    selectedNeed: String,
    onSelectNeed: (String) -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = "المحطة الخامسة: ما الاحتياج الحقيقي في هذا الموقف؟",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = DeepForest)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "كل شعور هو دعوة لتلبية حاجة إنسانية مشروعة وطبيعية:",
                style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
            )

            Spacer(modifier = Modifier.height(14.dp))

            needs.forEach { need ->
                val label = "${need.title}: ${need.text}"
                val isSel = selectedNeed == label
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = if (isSel) SageLight.copy(alpha = 0.7f) else WarmBeige.copy(alpha = 0.35f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { onSelectNeed(label) }
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "✨", fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = need.title,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = DarkGreen
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = need.text,
                            style = MaterialTheme.typography.bodySmall.copy(color = TextDark, lineHeight = 18.sp)
                        )
                    }
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------------------
// STAGE 6: REFRAME (نظرة أوسع والقصة الجديدة)
// -----------------------------------------------------------------------------------------
@Composable
fun StageReframe(
    situation: String,
    oldStory: String,
    newStory: String,
    practice: String,
    feeling: String,
    bodySignal: String,
    hasSaved: Boolean,
    onSaveToJourney: () -> Unit,
    onRestart: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        // Journal Summary Card (دفتر الرحلة)
        Card(
            shape = RoundedCornerShape(22.dp),
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
                        Icon(imageVector = Icons.Default.MenuBook, contentDescription = null, tint = DarkGreen)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "دفتر الرحلة • الحكاية اتضحت",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = DeepForest
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Situation
                Text(text = "الموقف:", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
                Text(text = situation, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium, color = DeepForest))

                Spacer(modifier = Modifier.height(10.dp))

                // Comparison Box: Old vs New Story
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = WarmTerracotta.copy(alpha = 0.12f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "القصة القديمة (المتعجلة والقلقة):",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = WarmTerracotta)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = "«$oldStory»", style = MaterialTheme.typography.bodySmall.copy(color = TextDark))
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = SageLight.copy(alpha = 0.6f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "القصة الجديدة (الأكثر رفقاً وحكمة):",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = DarkGreen)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = newStory,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = DeepForest,
                                lineHeight = 22.sp
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Practice step
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = WarmBeige.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "خطوة تدريب لطيفة:",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = DeepForest)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = practice,
                            style = MaterialTheme.typography.bodySmall.copy(color = TextDark, lineHeight = 20.sp)
                        )
                    }
                }
            }
        }

        // Actions: Save to Journey & Restart
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = onSaveToJourney,
                enabled = !hasSaved,
                colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.weight(1f)
            ) {
                Icon(imageVector = Icons.Default.BookmarkAdd, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (hasSaved) "تم الحفظ في رحلتي ✓" else "حفظ في «رحلتي»",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            OutlinedButton(
                onClick = onRestart,
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(imageVector = Icons.Default.Refresh, contentDescription = null, tint = DarkGreen)
                Spacer(modifier = Modifier.width(4.dp))
                Text("حكاية جديدة", color = DarkGreen)
            }
        }
    }
}
