package com.example.data.model

data class ContentArticle(
    val id: String,
    val title: String,
    val subtitle: String,
    val category: String,
    val readTimeMinutes: Int,
    val content: List<String>,
    val gentleTakeaway: String,
    val iconName: String = "article"
)
