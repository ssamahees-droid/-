package com.example.data.model

data class PurposefulVideo(
    val id: String,
    val title: String,
    val speaker: String,
    val speakerRole: String,
    val durationText: String,
    val category: String,
    val description: String,
    val keyTakeaways: List<String>,
    val practicalExerciseTitle: String,
    val practicalExerciseSteps: List<String>,
    val soundtrackId: String,
    val iconEmoji: String,
    val quote: String
)
