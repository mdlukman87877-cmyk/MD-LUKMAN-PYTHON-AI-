package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.model.Chapter
import com.example.model.QuizQuestion

@Entity(tableName = "cached_chapters")
data class ChapterEntity(
    @PrimaryKey val id: Int,
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
    val testQuestions: List<QuizQuestion>,
    val cachedTimestamp: Long = System.currentTimeMillis()
)

fun ChapterEntity.toChapter(): Chapter {
    return Chapter(
        id = id,
        title = title,
        hindiTitle = hindiTitle,
        category = category,
        summaryHindi = summaryHindi,
        introduction = introduction,
        definition = definition,
        detailedHindiExplanation = detailedHindiExplanation,
        importantConcepts = importantConcepts,
        realLifeExample = realLifeExample,
        pythonCodeExample = pythonCodeExample,
        lineByLineExplanation = lineByLineExplanation,
        commonMistakes = commonMistakes,
        practiceExercises = practiceExercises,
        testQuestions = testQuestions
    )
}

fun Chapter.toEntity(): ChapterEntity {
    return ChapterEntity(
        id = id,
        title = title,
        hindiTitle = hindiTitle,
        category = category,
        summaryHindi = summaryHindi,
        introduction = introduction,
        definition = definition,
        detailedHindiExplanation = detailedHindiExplanation,
        importantConcepts = importantConcepts,
        realLifeExample = realLifeExample,
        pythonCodeExample = pythonCodeExample,
        lineByLineExplanation = lineByLineExplanation,
        commonMistakes = commonMistakes,
        practiceExercises = practiceExercises,
        testQuestions = testQuestions
    )
}
