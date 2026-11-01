package com.example.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.concurrent.TimeUnit

class ProgressManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("md_lukman_python_ai_prefs", Context.MODE_PRIVATE)

    private val _completedChapters = MutableStateFlow<Set<Int>>(emptySet())
    val completedChapters: StateFlow<Set<Int>> = _completedChapters.asStateFlow()

    private val _streakDays = MutableStateFlow(1)
    val streakDays: StateFlow<Int> = _streakDays.asStateFlow()

    private val _isDarkMode = MutableStateFlow(true)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    private val _solvedProblems = MutableStateFlow<Set<Int>>(emptySet())
    val solvedProblems: StateFlow<Set<Int>> = _solvedProblems.asStateFlow()

    init {
        loadData()
        updateStreak()
    }

    private fun loadData() {
        val chapterStrings = prefs.getStringSet("completed_chapters", emptySet()) ?: emptySet()
        _completedChapters.value = chapterStrings.mapNotNull { it.toIntOrNull() }.toSet()

        val problemStrings = prefs.getStringSet("solved_problems", emptySet()) ?: emptySet()
        _solvedProblems.value = problemStrings.mapNotNull { it.toIntOrNull() }.toSet()

        _streakDays.value = prefs.getInt("streak_days", 1)
        _isDarkMode.value = prefs.getBoolean("is_dark_mode", true)
    }

    private fun updateStreak() {
        val currentDay = TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis())
        val lastDay = prefs.getLong("last_active_day", 0L)

        if (lastDay == 0L) {
            prefs.edit().putLong("last_active_day", currentDay).putInt("streak_days", 1).apply()
            _streakDays.value = 1
        } else if (currentDay == lastDay + 1) {
            val newStreak = prefs.getInt("streak_days", 1) + 1
            prefs.edit().putLong("last_active_day", currentDay).putInt("streak_days", newStreak).apply()
            _streakDays.value = newStreak
        } else if (currentDay > lastDay + 1) {
            // Missed a day
            prefs.edit().putLong("last_active_day", currentDay).putInt("streak_days", 1).apply()
            _streakDays.value = 1
        }
    }

    fun markChapterCompleted(chapterId: Int) {
        val current = _completedChapters.value.toMutableSet()
        current.add(chapterId)
        _completedChapters.value = current
        prefs.edit().putStringSet("completed_chapters", current.map { it.toString() }.toSet()).apply()
    }

    fun isChapterCompleted(chapterId: Int): Boolean {
        return _completedChapters.value.contains(chapterId)
    }

    fun getCompletedChaptersCount(): Int {
        return _completedChapters.value.size
    }

    fun markProblemSolved(problemId: Int) {
        val current = _solvedProblems.value.toMutableSet()
        current.add(problemId)
        _solvedProblems.value = current
        prefs.edit().putStringSet("solved_problems", current.map { it.toString() }.toSet()).apply()
    }

    fun saveQuizScore(chapterId: Int, score: Int, total: Int) {
        prefs.edit().putInt("quiz_score_$chapterId", score).putInt("quiz_total_$chapterId", total).apply()
        if (score.toFloat() / total.toFloat() >= 0.6f) {
            markChapterCompleted(chapterId)
        }
    }

    fun getQuizScore(chapterId: Int): Pair<Int, Int> {
        val score = prefs.getInt("quiz_score_$chapterId", -1)
        val total = prefs.getInt("quiz_total_$chapterId", 0)
        return Pair(score, total)
    }

    fun setDarkMode(enabled: Boolean) {
        _isDarkMode.value = enabled
        prefs.edit().putBoolean("is_dark_mode", enabled).apply()
    }

    fun getOverallProgressPercentage(totalChapters: Int = 30): Int {
        if (totalChapters == 0) return 0
        val percentage = (_completedChapters.value.size * 100) / totalChapters
        return percentage.coerceIn(0, 100)
    }

    fun resetAllProgress() {
        prefs.edit().clear().apply()
        _completedChapters.value = emptySet()
        _solvedProblems.value = emptySet()
        _streakDays.value = 1
        updateStreak()
    }
}
