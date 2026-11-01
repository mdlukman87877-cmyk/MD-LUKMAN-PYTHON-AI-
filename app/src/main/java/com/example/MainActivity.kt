package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.ui.MainViewModel
import com.example.ui.Screen
import com.example.ui.components.AppBottomNav
import com.example.ui.components.AppTopHeader
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val isDarkMode by viewModel.progressManager.isDarkMode.collectAsState()
            val currentScreen by viewModel.currentScreen.collectAsState()
            val streakDays by viewModel.progressManager.streakDays.collectAsState()

            val selectedChapter by viewModel.selectedChapter.collectAsState()
            val quizChapter by viewModel.quizChapter.collectAsState()
            val selectedProblem by viewModel.selectedProblem.collectAsState()

            // System Back Handling
            BackHandler(enabled = currentScreen != Screen.HOME) {
                viewModel.navigateBack()
            }

            MyApplicationTheme(darkTheme = isDarkMode) {
                val showBack = currentScreen != Screen.HOME

                Scaffold(
                    topBar = {
                        AppTopHeader(
                            title = when (currentScreen) {
                                Screen.HOME -> "MD LUKMAN PYTHON AI"
                                Screen.COURSE -> "Python Course (30 Chapters)"
                                Screen.CHAPTER_DETAIL -> selectedChapter?.title?.substringBefore(":") ?: "Lesson"
                                Screen.CODE_LAB -> "Python Code Lab"
                                Screen.AI_TUTOR -> "AI Python Teacher"
                                Screen.QUIZ -> "Chapter Examination"
                                Screen.CODING_EXAMS -> "Coding Examination"
                                Screen.CODING_PROBLEM_DETAIL -> selectedProblem?.title ?: "Problem"
                                Screen.PROGRESS -> "My Academic Progress"
                                Screen.SETTINGS -> "Settings & About"
                            },
                            streakDays = streakDays,
                            isDarkMode = isDarkMode,
                            onToggleDarkMode = { viewModel.progressManager.setDarkMode(!isDarkMode) },
                            onSettingsClick = { viewModel.navigateTo(Screen.SETTINGS) },
                            showBackButton = showBack,
                            onBackClick = { viewModel.navigateBack() }
                        )
                    },
                    bottomBar = {
                        val isSecondaryScreen = currentScreen == Screen.CHAPTER_DETAIL ||
                                currentScreen == Screen.QUIZ ||
                                currentScreen == Screen.CODING_PROBLEM_DETAIL ||
                                currentScreen == Screen.SETTINGS

                        if (!isSecondaryScreen) {
                            AppBottomNav(
                                currentScreen = currentScreen,
                                onNavigate = { viewModel.navigateTo(it) }
                            )
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        when (currentScreen) {
                            Screen.HOME -> HomeScreen(viewModel = viewModel)
                            Screen.COURSE -> CourseScreen(viewModel = viewModel)
                            Screen.CHAPTER_DETAIL -> {
                                selectedChapter?.let { chapter ->
                                    ChapterDetailScreen(chapter = chapter, viewModel = viewModel)
                                } ?: HomeScreen(viewModel = viewModel)
                            }
                            Screen.CODE_LAB -> CodeEditorScreen(viewModel = viewModel)
                            Screen.AI_TUTOR -> AiTeacherScreen(viewModel = viewModel)
                            Screen.QUIZ -> {
                                quizChapter?.let { chapter ->
                                    QuizScreen(chapter = chapter, viewModel = viewModel)
                                } ?: CourseScreen(viewModel = viewModel)
                            }
                            Screen.CODING_EXAMS -> CodingExamScreen(viewModel = viewModel)
                            Screen.CODING_PROBLEM_DETAIL -> {
                                selectedProblem?.let { problem ->
                                    CodingProblemDetailScreen(problem = problem, viewModel = viewModel)
                                } ?: CodingExamScreen(viewModel = viewModel)
                            }
                            Screen.PROGRESS -> ProgressScreen(viewModel = viewModel)
                            Screen.SETTINGS -> SettingsScreen(viewModel = viewModel)
                        }
                    }
                }
            }
        }
    }
}
