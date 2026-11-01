package com.example.model

enum class QuestionType {
    MCQ,
    TRUE_FALSE,
    OUTPUT_FINDING,
    ERROR_FINDING,
    FILL_BLANK
}

data class QuizQuestion(
    val id: Int,
    val question: String,
    val hindiQuestion: String,
    val codeSnippet: String? = null,
    val options: List<String>,
    val correctOptionIndex: Int,
    val hindiExplanation: String,
    val questionType: QuestionType = QuestionType.MCQ
)

data class Chapter(
    val id: Int,
    val title: String,
    val hindiTitle: String,
    val category: String,
    val summaryHindi: String,
    val introduction: String,
    val definition: String,
    val detailedHindiExplanation: String,
    val importantConcepts: List<String>,
    val realLifeExample: String,
    val pythonCodeExample: String,
    val lineByLineExplanation: List<String>,
    val commonMistakes: List<String>,
    val practiceExercises: List<String>,
    val testQuestions: List<QuizQuestion>
)
