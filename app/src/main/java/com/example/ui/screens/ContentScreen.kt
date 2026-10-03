package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ContentArticle
import com.example.data.model.PurposefulVideo
import com.example.data.repository.SampleContentProvider
import com.example.ui.components.PurposefulVideoPlayerModal
import com.example.ui.components.PurposefulVideosTab
import com.example.ui.components.SoundscapeTab
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
fun ContentScreen(
    viewModel: NasmatViewModel,
    modifier: Modifier = Modifier
) {
    val articles by viewModel.filteredArticles.collectAsState()
    val searchQuery by viewModel.contentSearchQuery.collectAsState()
    val selectedCategory by viewModel.selectedContentCategory.collectAsState()
    val bookmarks by viewModel.allBookmarks.collectAsState()

    var activeArticle by remember { mutableStateOf<ContentArticle?>(null) }
    var activeSubTab by remember { mutableIntStateOf(0) } // 0: Articles, 1: Videos, 2: Soundscape
    var activeVideo by remember { mutableStateOf<PurposefulVideo?>(null) }

    val bookmarkedIds = remember(bookmarks) { bookmarks.map { it.articleId }.toSet() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CreamBackground)
            .padding(horizontal = 18.dp)
            .testTag("content_screen")
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        // Screen Title
        Text(
            text = "واحة المعرفة والسكينة",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = DarkGreen
            )
        )
        Text(
            text = "مقالات مطمئنة، فيديوهات هادفة، وألحان هادئة للاسترخاء",
            style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Sub-Tab Switcher
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val subTabs = listOf(
                "📚 مقالات التوعية",
                "🎬 فيديوهات هادفة",
                "🎵 أصوات وموسيقى"
            )
            subTabs.forEachIndexed { idx, title ->
                val isSel = activeSubTab == idx
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isSel) DarkGreen else WarmBeige.copy(alpha = 0.5f),
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { activeSubTab = idx }
                ) {
                    Text(
                        text = title,
                        modifier = Modifier.padding(vertical = 9.dp),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = if (isSel) Color.White else TextDark,
                            fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 11.sp
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        when (activeSubTab) {
            0 -> {
                // Articles Tab
                Column(modifier = Modifier.fillMaxSize()) {
                    // Search bar
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { viewModel.setContentSearchQuery(it) },
                        placeholder = { Text("ابحث في المقالات والأفكار...") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = TextMuted
                            )
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { viewModel.setContentSearchQuery("") }) {
                                    Icon(imageVector = Icons.Default.Close, contentDescription = "مسح", tint = TextMuted)
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("content_search_input"),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = DarkGreen,
                            unfocusedBorderColor = SageLight
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Category Chips
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(SampleContentProvider.categories) { category ->
                            val isSelected = selectedCategory == category
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) DarkGreen else WarmBeige.copy(alpha = 0.6f),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable { viewModel.setContentCategory(category) }
                            ) {
                                Text(
                                    text = category,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        color = if (isSelected) Color.White else TextDark,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Articles List
                    if (articles.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "لم نجد مقالات تطابق بحثك، جرّب كلمات أخرى",
                                style = MaterialTheme.typography.bodyMedium.copy(color = TextMuted)
                            )
                        }
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(articles, key = { it.id }) { article ->
                                val isBookmarked = bookmarkedIds.contains(article.id)
                                ArticleItemCard(
                                    article = article,
                                    isBookmarked = isBookmarked,
                                    onToggleBookmark = {
                                        viewModel.toggleBookmark(article, isBookmarked)
                                    },
                                    onClick = { activeArticle = article }
                                )
                            }
                            item {
                                Spacer(modifier = Modifier.height(80.dp))
                            }
                        }
                    }
                }
            }
            1 -> {
                // Purposeful Videos Tab
                PurposefulVideosTab(
                    onSelectVideo = { activeVideo = it }
                )
            }
            2 -> {
                // Soundscape & Music Tab
                SoundscapeTab()
            }
        }
    }

    // Purposeful Video Player Modal
    if (activeVideo != null) {
        PurposefulVideoPlayerModal(
            video = activeVideo!!,
            onDismiss = { activeVideo = null },
            onSaveToJourney = { title, summary ->
                viewModel.addReflection(
                    question = "ومضة مرئية: $title",
                    answer = summary
                )
            }
        )
    }

    // Article Reader BottomSheet
    if (activeArticle != null) {
        val article = activeArticle!!
        val isBookmarked = bookmarkedIds.contains(article.id)
        ArticleDetailSheet(
            article = article,
            isBookmarked = isBookmarked,
            onToggleBookmark = { viewModel.toggleBookmark(article, isBookmarked) },
            onDismiss = { activeArticle = null }
        )
    }
}

@Composable
fun ArticleItemCard(
    article: ContentArticle,
    isBookmarked: Boolean,
    onToggleBookmark: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("article_card_${article.id}"),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = SageLight.copy(alpha = 0.6f)
                ) {
                    Text(
                        text = article.category,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = DeepForest,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${article.readTimeMinutes} دقائق قراءة",
                        style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    IconButton(
                        onClick = onToggleBookmark,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                            contentDescription = if (isBookmarked) "إزالة من المفضلة" else "إضافة للمفضلة",
                            tint = if (isBookmarked) DarkGreen else TextMuted,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = article.title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = DeepForest
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = article.subtitle,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextMuted,
                    lineHeight = 18.sp
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleDetailSheet(
    article: ContentArticle,
    isBookmarked: Boolean,
    onToggleBookmark: () -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = CreamBackground,
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(22.dp)
                .verticalScroll(rememberScrollState())
                .testTag("article_detail_sheet")
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "إغلاق", tint = TextMuted)
                }

                IconButton(onClick = onToggleBookmark) {
                    Icon(
                        imageVector = if (isBookmarked) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                        contentDescription = "مفضلة",
                        tint = if (isBookmarked) DarkGreen else TextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = SageLight.copy(alpha = 0.6f)
            ) {
                Text(
                    text = article.category,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = DeepForest,
                        fontWeight = FontWeight.Medium
                    )
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = article.title,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = DeepForest
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = article.subtitle,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = TextMuted,
                    lineHeight = 22.sp
                )
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Article Paragraphs
            article.content.forEach { paragraph ->
                Text(
                    text = paragraph,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = TextDark,
                        lineHeight = 28.sp,
                        fontSize = 16.sp
                    )
                )
                Spacer(modifier = Modifier.height(14.dp))
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Gentle Takeaway Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = WarmBeige.copy(alpha = 0.6f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "خلاصة رقيقة لليوم:",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = DarkGreen
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = article.gentleTakeaway,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = DeepForest,
                            fontWeight = FontWeight.Medium,
                            lineHeight = 22.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}
