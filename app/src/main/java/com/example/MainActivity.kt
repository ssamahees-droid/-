package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material.icons.outlined.Timeline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.BreathingScreen
import com.example.ui.screens.ContentScreen
import com.example.ui.screens.GwayaHekayaGame
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.JourneyScreen
import com.example.ui.screens.LightSpaceScreen
import com.example.ui.screens.OnboardingScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.SupportDraftsScreen
import com.example.ui.theme.DarkGreen
import com.example.ui.theme.DeepForest
import com.example.ui.theme.NasmatHayatTheme
import com.example.ui.theme.SageLight
import com.example.ui.theme.TextMuted
import com.example.ui.theme.WarmBeige
import com.example.ui.viewmodel.NasmatViewModel

enum class MainTab {
    HOME, CONTENT, LIGHT_SPACE, JOURNEY, PROFILE
}

enum class ActiveScreen {
    MAIN_TABS, BREATHING, SUPPORT_DRAFTS, GWAYA_HEKAYA
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NasmatHayatTheme {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    NasmatHayatApp()
                }
            }
        }
    }
}

@Composable
fun NasmatHayatApp(
    viewModel: NasmatViewModel = viewModel()
) {
    val onboardingCompleted by viewModel.onboardingCompleted.collectAsState()

    var activeScreen by remember { mutableStateOf(ActiveScreen.MAIN_TABS) }
    var currentTab by remember { mutableStateOf(MainTab.HOME) }

    if (!onboardingCompleted) {
        OnboardingScreen(
            onComplete = { saveChoices, selectedGoals ->
                viewModel.completeOnboarding(saveChoices, selectedGoals)
            }
        )
    } else {
        when (activeScreen) {
            ActiveScreen.BREATHING -> {
                BreathingScreen(
                    viewModel = viewModel,
                    onBack = { activeScreen = ActiveScreen.MAIN_TABS }
                )
            }
            ActiveScreen.SUPPORT_DRAFTS -> {
                SupportDraftsScreen(
                    viewModel = viewModel,
                    onBack = { activeScreen = ActiveScreen.MAIN_TABS }
                )
            }
            ActiveScreen.GWAYA_HEKAYA -> {
                GwayaHekayaGame(
                    viewModel = viewModel,
                    onBack = { activeScreen = ActiveScreen.MAIN_TABS }
                )
            }
            ActiveScreen.MAIN_TABS -> {
                Scaffold(
                    contentWindowInsets = WindowInsets.safeDrawing,
                    bottomBar = {
                        NavigationBar(
                            containerColor = WarmBeige.copy(alpha = 0.85f),
                            tonalElevation = 2.dp,
                            modifier = Modifier.testTag("main_bottom_nav")
                        ) {
                            NavigationBarItem(
                                selected = currentTab == MainTab.HOME,
                                onClick = { currentTab = MainTab.HOME },
                                icon = {
                                    Icon(
                                        imageVector = if (currentTab == MainTab.HOME) Icons.Filled.Spa else Icons.Outlined.Spa,
                                        contentDescription = "الرئيسية"
                                    )
                                },
                                label = { Text("الرئيسية", fontSize = 11.sp) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = DarkGreen,
                                    selectedTextColor = DarkGreen,
                                    indicatorColor = SageLight,
                                    unselectedIconColor = TextMuted,
                                    unselectedTextColor = TextMuted
                                ),
                                modifier = Modifier.testTag("nav_item_home")
                            )

                            NavigationBarItem(
                                selected = currentTab == MainTab.CONTENT,
                                onClick = { currentTab = MainTab.CONTENT },
                                icon = {
                                    Icon(
                                        imageVector = if (currentTab == MainTab.CONTENT) Icons.Filled.MenuBook else Icons.Outlined.MenuBook,
                                        contentDescription = "المحتوى"
                                    )
                                },
                                label = { Text("المحتوى", fontSize = 11.sp) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = DarkGreen,
                                    selectedTextColor = DarkGreen,
                                    indicatorColor = SageLight,
                                    unselectedIconColor = TextMuted,
                                    unselectedTextColor = TextMuted
                                ),
                                modifier = Modifier.testTag("nav_item_content")
                            )

                            NavigationBarItem(
                                selected = currentTab == MainTab.LIGHT_SPACE,
                                onClick = { currentTab = MainTab.LIGHT_SPACE },
                                icon = {
                                    Icon(
                                        imageVector = if (currentTab == MainTab.LIGHT_SPACE) Icons.Filled.AutoAwesome else Icons.Outlined.AutoAwesome,
                                        contentDescription = "مساحة خفيفة"
                                    )
                                },
                                label = { Text("مساحة خفيفة", fontSize = 11.sp) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = DarkGreen,
                                    selectedTextColor = DarkGreen,
                                    indicatorColor = SageLight,
                                    unselectedIconColor = TextMuted,
                                    unselectedTextColor = TextMuted
                                ),
                                modifier = Modifier.testTag("nav_item_light_space")
                            )

                            NavigationBarItem(
                                selected = currentTab == MainTab.JOURNEY,
                                onClick = { currentTab = MainTab.JOURNEY },
                                icon = {
                                    Icon(
                                        imageVector = if (currentTab == MainTab.JOURNEY) Icons.Filled.Timeline else Icons.Outlined.Timeline,
                                        contentDescription = "رحلتي"
                                    )
                                },
                                label = { Text("رحلتي", fontSize = 11.sp) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = DarkGreen,
                                    selectedTextColor = DarkGreen,
                                    indicatorColor = SageLight,
                                    unselectedIconColor = TextMuted,
                                    unselectedTextColor = TextMuted
                                ),
                                modifier = Modifier.testTag("nav_item_journey")
                            )

                            NavigationBarItem(
                                selected = currentTab == MainTab.PROFILE,
                                onClick = { currentTab = MainTab.PROFILE },
                                icon = {
                                    Icon(
                                        imageVector = if (currentTab == MainTab.PROFILE) Icons.Filled.Person else Icons.Outlined.Person,
                                        contentDescription = "الملف"
                                    )
                                },
                                label = { Text("الملف", fontSize = 11.sp) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = DarkGreen,
                                    selectedTextColor = DarkGreen,
                                    indicatorColor = SageLight,
                                    unselectedIconColor = TextMuted,
                                    unselectedTextColor = TextMuted
                                ),
                                modifier = Modifier.testTag("nav_item_profile")
                            )
                        }
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        Crossfade(targetState = currentTab, label = "tab_crossfade") { tab ->
                            when (tab) {
                                MainTab.HOME -> HomeScreen(
                                    viewModel = viewModel,
                                    onNavigateToBreathing = { activeScreen = ActiveScreen.BREATHING },
                                    onNavigateToLightSpace = { currentTab = MainTab.LIGHT_SPACE },
                                    onNavigateToDrafts = { activeScreen = ActiveScreen.SUPPORT_DRAFTS },
                                    onNavigateToGwayaHekaya = { activeScreen = ActiveScreen.GWAYA_HEKAYA }
                                )
                                MainTab.CONTENT -> ContentScreen(viewModel = viewModel)
                                MainTab.LIGHT_SPACE -> LightSpaceScreen(
                                    viewModel = viewModel,
                                    onLaunchGwayaHekaya = { activeScreen = ActiveScreen.GWAYA_HEKAYA }
                                )
                                MainTab.JOURNEY -> JourneyScreen(viewModel = viewModel)
                                MainTab.PROFILE -> ProfileScreen(
                                    viewModel = viewModel,
                                    onNavigateToDrafts = { activeScreen = ActiveScreen.SUPPORT_DRAFTS }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
