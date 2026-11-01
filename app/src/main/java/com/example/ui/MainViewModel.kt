package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.CodingExamRepository
import com.example.data.CourseRepository
import com.example.data.GeminiTutorService
import com.example.data.ProgressManager
import com.example.data.PythonExecutionEngine
import com.example.model.ChatMessage
import com.example.model.Chapter
import com.example.model.CodingProblem
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class Screen {
    HOME,
    COURSE,
    CHAPTER_DETAIL,
    CODE_LAB,
    AI_TUTOR,
    QUIZ,
    CODING_EXAMS,
    CODING_PROBLEM_DETAIL,
    PROGRESS,
    SETTINGS
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val progressManager = ProgressManager(application)
    private val pythonEngine = PythonExecutionEngine(application)
    private val tutorService = GeminiTutorService()

    // Room Database Cached Course Chapters Flow
    val cachedChapters: StateFlow<List<Chapter>> = CourseRepository.getCachedChaptersFlow(application)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CourseRepository.chapters
        )

    private val _isOfflineCacheReady = MutableStateFlow(true)
    val isOfflineCacheReady: StateFlow<Boolean> = _isOfflineCacheReady.asStateFlow()

    init {
        viewModelScope.launch {
            CourseRepository.ensureCached(application)
            _isOfflineCacheReady.value = true
        }
    }

    fun refreshOfflineCourseCache() {
        viewModelScope.launch {
            _isOfflineCacheReady.value = false
            CourseRepository.forceRefreshCache(getApplication())
            _isOfflineCacheReady.value = true
        }
    }

    // Navigation state
    private val _currentScreen = MutableStateFlow(Screen.HOME)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val screenStack = mutableListOf<Screen>()

    // Chapter Detail State
    private val _selectedChapter = MutableStateFlow<Chapter?>(null)
    val selectedChapter: StateFlow<Chapter?> = _selectedChapter.asStateFlow()

    // Course Search & Filter
    private val _courseSearchQuery = MutableStateFlow("")
    val courseSearchQuery: StateFlow<String> = _courseSearchQuery.asStateFlow()

    private val _selectedCategoryFilter = MutableStateFlow("All")
    val selectedCategoryFilter: StateFlow<String> = _selectedCategoryFilter.asStateFlow()

    // Code Editor State
    private val _editorCode = MutableStateFlow(
        """# MD LUKMAN PYTHON AI - Code Lab
# Yahan apna Python code likhein aur 'Run' dabayein!

name = "Lukman"
print("Namaste,", name)
print("Welcome to Python Programming!")

# Loop Example:
for i in range(1, 6):
    print("Step", i, ": Code running smoothly!")
""".trimIndent()
    )
    val editorCode: StateFlow<String> = _editorCode.asStateFlow()

    private val _editorOutput = MutableStateFlow("")
    val editorOutput: StateFlow<String> = _editorOutput.asStateFlow()

    private val _editorError = MutableStateFlow<String?>(null)
    val editorError: StateFlow<String?> = _editorError.asStateFlow()

    private val _isCodeExecuting = MutableStateFlow(false)
    val isCodeExecuting: StateFlow<Boolean> = _isCodeExecuting.asStateFlow()

    private val _executionTimeMs = MutableStateFlow(0L)
    val executionTimeMs: StateFlow<Long> = _executionTimeMs.asStateFlow()

    // AI Tutor Chat State
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                text = "नमस्ते! मैं हूँ आपका Personal AI Python Teacher (MD LUKMAN PYTHON AI) 🤖 जिसे MD Lukman ने बनाया है।\n\nआप मुझसे Python का कोई भी सवाल, कोड की हर लाइन का मतलब, या कोई भी एरर पूछ सकते हैं। आप हिंदी, हिंग्लिश या इंग्लिश में पूछें, मैं आसान भाषा में समझाऊंगा!",
                isUser = false
            )
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _isAiThinking = MutableStateFlow(false)
    val isAiThinking: StateFlow<Boolean> = _isAiThinking.asStateFlow()

    // Quiz State
    private val _quizChapter = MutableStateFlow<Chapter?>(null)
    val quizChapter: StateFlow<Chapter?> = _quizChapter.asStateFlow()

    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex: StateFlow<Int> = _currentQuestionIndex.asStateFlow()

    private val _quizSelectedAnswers = MutableStateFlow<Map<Int, Int>>(emptyMap())
    val quizSelectedAnswers: StateFlow<Map<Int, Int>> = _quizSelectedAnswers.asStateFlow()

    private val _quizSubmitted = MutableStateFlow(false)
    val quizSubmitted: StateFlow<Boolean> = _quizSubmitted.asStateFlow()

    private val _quizScore = MutableStateFlow(0)
    val quizScore: StateFlow<Int> = _quizScore.asStateFlow()

    private val _quizTimeRemainingSeconds = MutableStateFlow(600) // 10 minutes
    val quizTimeRemainingSeconds: StateFlow<Int> = _quizTimeRemainingSeconds.asStateFlow()
    private var quizTimerJob: Job? = null

    // Coding Exam State
    private val _selectedProblem = MutableStateFlow<CodingProblem?>(null)
    val selectedProblem: StateFlow<CodingProblem?> = _selectedProblem.asStateFlow()

    private val _problemUserCode = MutableStateFlow("")
    val problemUserCode: StateFlow<String> = _problemUserCode.asStateFlow()

    private val _testCaseResults = MutableStateFlow<List<Boolean>>(emptyList())
    val testCaseResults: StateFlow<List<Boolean>> = _testCaseResults.asStateFlow()

    private val _isTestingProblem = MutableStateFlow(false)
    val isTestingProblem: StateFlow<Boolean> = _isTestingProblem.asStateFlow()

    private val _problemTestOutput = MutableStateFlow("")
    val problemTestOutput: StateFlow<String> = _problemTestOutput.asStateFlow()

    // Navigation Methods
    fun navigateTo(screen: Screen) {
        if (_currentScreen.value != screen) {
            screenStack.add(_currentScreen.value)
            _currentScreen.value = screen
        }
    }

    fun navigateBack(): Boolean {
        return if (screenStack.isNotEmpty()) {
            _currentScreen.value = screenStack.removeAt(screenStack.size - 1)
            true
        } else {
            if (_currentScreen.value != Screen.HOME) {
                _currentScreen.value = Screen.HOME
                true
            } else {
                false
            }
        }
    }

    fun openChapter(chapter: Chapter) {
        _selectedChapter.value = chapter
        navigateTo(Screen.CHAPTER_DETAIL)
    }

    fun openCodeInEditor(code: String) {
        _editorCode.value = code
        _editorOutput.value = ""
        _editorError.value = null
        navigateTo(Screen.CODE_LAB)
    }

    fun setCourseSearchQuery(query: String) {
        _courseSearchQuery.value = query
    }

    fun setCategoryFilter(category: String) {
        _selectedCategoryFilter.value = category
    }

    fun setEditorCode(newCode: String) {
        _editorCode.value = newCode
    }

    fun insertEditorCodeSnippet(snippet: String) {
        _editorCode.value = _editorCode.value + snippet
    }

    fun clearEditorOutput() {
        _editorOutput.value = ""
        _editorError.value = null
    }

    fun resetEditorCode() {
        _editorCode.value = """# Reset Code
name = "MD Lukman"
print(f"Hello from {name} Python AI!")
for x in range(3):
    print("Python is fun!", x + 1)
"""
        _editorOutput.value = ""
        _editorError.value = null
    }

    fun runCode() {
        viewModelScope.launch {
            _isCodeExecuting.value = true
            _editorOutput.value = ""
            _editorError.value = null

            val result = pythonEngine.execute(_editorCode.value)
            _editorOutput.value = result.output
            _editorError.value = result.error
            _executionTimeMs.value = result.executionTimeMs
            _isCodeExecuting.value = false
        }
    }

    fun solveErrorWithAi() {
        val errorText = _editorError.value ?: "Error occurred"
        val codeText = _editorCode.value
        val question = "Mere is Python code mein error aa raha hai. Kripya ise Hindi mein samjhayein aur sahi code dein:\nError: $errorText"

        navigateTo(Screen.AI_TUTOR)
        askAi(question, codeText)
    }

    fun askAi(question: String, codeSnippet: String? = null) {
        if (question.isBlank()) return

        val userMsg = ChatMessage(text = question, isUser = true, codeSnippet = codeSnippet)
        _chatMessages.value = _chatMessages.value + userMsg

        viewModelScope.launch {
            _isAiThinking.value = true
            val responseText = tutorService.askTutor(question, codeSnippet)
            val aiMsg = ChatMessage(text = responseText, isUser = false)
            _chatMessages.value = _chatMessages.value + aiMsg
            _isAiThinking.value = false
        }
    }

    // Quiz Methods
    fun startQuiz(chapter: Chapter) {
        _quizChapter.value = chapter
        _currentQuestionIndex.value = 0
        _quizSelectedAnswers.value = emptyMap()
        _quizSubmitted.value = false
        _quizScore.value = 0
        _quizTimeRemainingSeconds.value = 600

        quizTimerJob?.cancel()
        quizTimerJob = viewModelScope.launch {
            while (_quizTimeRemainingSeconds.value > 0 && !_quizSubmitted.value) {
                delay(1000L)
                _quizTimeRemainingSeconds.value -= 1
            }
            if (!_quizSubmitted.value && _quizTimeRemainingSeconds.value <= 0) {
                submitQuiz()
            }
        }

        navigateTo(Screen.QUIZ)
    }

    fun selectQuizAnswer(questionIndex: Int, optionIndex: Int) {
        if (_quizSubmitted.value) return
        val current = _quizSelectedAnswers.value.toMutableMap()
        current[questionIndex] = optionIndex
        _quizSelectedAnswers.value = current
    }

    fun nextQuestion() {
        val questions = _quizChapter.value?.testQuestions ?: return
        if (_currentQuestionIndex.value < questions.size - 1) {
            _currentQuestionIndex.value += 1
        }
    }

    fun prevQuestion() {
        if (_currentQuestionIndex.value > 0) {
            _currentQuestionIndex.value -= 1
        }
    }

    fun submitQuiz() {
        quizTimerJob?.cancel()
        val chapter = _quizChapter.value ?: return
        val questions = chapter.testQuestions
        var correctCount = 0

        for ((idx, question) in questions.withIndex()) {
            val userChoice = _quizSelectedAnswers.value[idx]
            if (userChoice == question.correctOptionIndex) {
                correctCount++
            }
        }

        _quizScore.value = correctCount
        _quizSubmitted.value = true

        progressManager.saveQuizScore(chapter.id, correctCount, questions.size)
    }

    // Coding Exam Methods
    fun openCodingProblem(problem: CodingProblem) {
        _selectedProblem.value = problem
        _problemUserCode.value = problem.starterCode
        _testCaseResults.value = emptyList()
        _problemTestOutput.value = ""
        navigateTo(Screen.CODING_PROBLEM_DETAIL)
    }

    fun updateProblemUserCode(code: String) {
        _problemUserCode.value = code
    }

    fun runProblemTests() {
        val problem = _selectedProblem.value ?: return
        viewModelScope.launch {
            _isTestingProblem.value = true
            val results = mutableListOf<Boolean>()
            val outputLog = StringBuilder()

            for ((i, tc) in problem.testCases.withIndex()) {
                val res = pythonEngine.execute(_problemUserCode.value, tc.input)
                val cleanActual = res.output.trim()
                val cleanExpected = tc.expectedOutput.trim()

                val passed = res.isSuccess && (cleanActual == cleanExpected)
                results.add(passed)

                outputLog.append("Test Case #${i + 1}: ${if (passed) "✅ PASSED" else "❌ FAILED"}\n")
                outputLog.append("  Input: ${tc.input.replace("\n", " ")}\n")
                outputLog.append("  Expected: $cleanExpected\n")
                outputLog.append("  Actual: $cleanActual\n")
                if (res.error != null) {
                    outputLog.append("  Error: ${res.error}\n")
                }
                outputLog.append("\n")
            }

            _testCaseResults.value = results
            _problemTestOutput.value = outputLog.toString()
            _isTestingProblem.value = false

            if (results.all { it }) {
                progressManager.markProblemSolved(problem.id)
            }
        }
    }
}
