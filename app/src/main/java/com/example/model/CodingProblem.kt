package com.example.model

data class TestCase(
    val input: String,
    val expectedOutput: String,
    val isHidden: Boolean = false
)

data class CodingProblem(
    val id: Int,
    val title: String,
    val difficulty: String, // "Easy", "Medium", "Hard"
    val category: String,
    val descriptionHindi: String,
    val descriptionEnglish: String,
    val inputFormat: String,
    val outputFormat: String,
    val starterCode: String,
    val solutionCode: String,
    val testCases: List<TestCase>
)

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
    val codeSnippet: String? = null
)
