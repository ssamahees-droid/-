package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.CreamBackground
import com.example.ui.theme.DarkGreen
import com.example.ui.theme.DeepForest
import com.example.ui.theme.SageGreen
import com.example.ui.theme.SageLight
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.WarmBeige

@Composable
fun OnboardingScreen(
    onComplete: (saveChoices: Boolean, selectedGoals: List<String>) -> Unit,
    modifier: Modifier = Modifier
) {
    var step by remember { mutableIntStateOf(0) }
    val selectedInterests = remember { mutableStateListOf<String>() }

    val explorationOptions = listOf(
        "تنظيم يومي بهدوء" to "تحديد خطوات صغيرة قابلة للتحقيق بلا إرهاق",
        "التنفس والتخفيف من التوتر" to "تمارين منتظمة لتهدئة الجهاز العصبي",
        "تسجيل المشاعر" to "مساحة يومية خاصة لفهم ما يدور بداخلك",
        "الرفق بالنفس والتأمل" to "قراءات وأسئلة تساعدك على التروي"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CreamBackground)
    ) {
        if (step == 0) {
            // STEP 1: Welcome & Scope Definition
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(210.dp)
                        .clip(RoundedCornerShape(24.dp))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.hero_nasmat_hayat_1790892087751),
                        contentDescription = "نسمة حياة",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "نسمة حياة",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = DarkGreen
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "مساحة عربية هادئة للتوعية العامة والرفق بالنفس",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = DeepForest,
                        fontWeight = FontWeight.Medium
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(14.dp))

                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = WarmBeige.copy(alpha = 0.55f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "هنا لا توجد سلاسل إنجاز قسرية أو درجات تشخيصية. «نسمة حياة» رفيق يومي يساعدك على إدارة مهامك بهدوء، والتنفس، وملاحظة مشاعرك بالكامل على هذا الجهاز.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = TextDark,
                            lineHeight = 22.sp
                        ),
                        modifier = Modifier.padding(18.dp),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                Button(
                    onClick = { step = 1 },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("onboarding_start_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "ابدأ بهدوء ❯",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(
                    onClick = { onComplete(false, emptyList()) },
                    modifier = Modifier.testTag("onboarding_skip_button")
                ) {
                    Text("تخطي للرئيسية مباشرة", color = TextMuted)
                }

                Spacer(modifier = Modifier.height(14.dp))
            }
        } else {
            // STEP 2: Multi-choice Exploration & Consent
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "ما الذي ترغب في استكشافه؟",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = DeepForest
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "اختر ما يناسبك الآن. تبقى هذه الاختيارات مؤقتة حتى تؤكد حفظها على جهازك.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextMuted,
                        textAlign = TextAlign.Center
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                explorationOptions.forEach { (title, subtitle) ->
                    val isSelected = selectedInterests.contains(title)
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) SageLight.copy(alpha = 0.5f) else Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .clickable {
                                if (isSelected) {
                                    selectedInterests.remove(title)
                                } else {
                                    selectedInterests.add(title)
                                }
                            }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = isSelected,
                                onCheckedChange = {
                                    if (isSelected) selectedInterests.remove(title) else selectedInterests.add(title)
                                },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = DarkGreen,
                                    checkmarkColor = Color.White
                                )
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            Column {
                                Text(
                                    text = title,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = DeepForest
                                    )
                                )
                                Text(
                                    text = subtitle,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TextMuted,
                                        fontSize = 12.sp
                                    )
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        onComplete(true, selectedInterests.toList())
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("save_preferences_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "حفظ اختياراتي على هذا الجهاز",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(
                    onClick = {
                        onComplete(false, emptyList())
                    },
                    modifier = Modifier.testTag("skip_preferences_button")
                ) {
                    Text(
                        text = "المتابعة دون حفظ",
                        color = TextMuted,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}
