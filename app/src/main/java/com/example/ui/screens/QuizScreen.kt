package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Chapter
import com.example.ui.MainViewModel
import com.example.ui.Screen
import com.example.ui.theme.*

@Composable
fun QuizScreen(
    chapter: Chapter,
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler {
        viewModel.navigateBack()
    }

    val currentQIndex by viewModel.currentQuestionIndex.collectAsState()
    val userAnswers by viewModel.quizSelectedAnswers.collectAsState()
    val isSubmitted by viewModel.quizSubmitted.collectAsState()
    val score by viewModel.quizScore.collectAsState()
    val timeRemaining by viewModel.quizTimeRemainingSeconds.collectAsState()

    val questions = chapter.testQuestions
    val total = questions.size

    val minutes = timeRemaining / 60
    val seconds = timeRemaining % 60
    val timeFormatted = String.format("%02d:%02d", minutes, seconds)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("quiz_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Quiz Header / Status Bar
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Chapter ${chapter.id} Examination",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = if (!isSubmitted) "Question ${currentQIndex + 1} of $total" else "Exam Results",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    if (!isSubmitted) {
                        Surface(
                            color = if (timeRemaining < 60) AccentCoral.copy(alpha = 0.15f) else PythonYellow.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Timer,
                                    contentDescription = "Timer",
                                    tint = if (timeRemaining < 60) AccentCoral else PythonYellowDark,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = timeFormatted,
                                    fontWeight = FontWeight.Bold,
                                    color = if (timeRemaining < 60) AccentCoral else PythonYellowDark,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        if (!isSubmitted && questions.isNotEmpty() && currentQIndex < questions.size) {
            val q = questions[currentQIndex]
            val selectedOption = userAnswers[currentQIndex]

            // Question Card
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "Q${currentQIndex + 1}. ${q.question}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 22.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = q.hindiQuestion,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary,
                            lineHeight = 20.sp
                        )

                        if (q.codeSnippet != null) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Surface(
                                color = CodeEditorBg,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = q.codeSnippet,
                                    color = CodeEditorText,
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 13.sp,
                                    modifier = Modifier.padding(10.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // Options
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            q.options.forEachIndexed { optIndex, optionText ->
                                val isChecked = selectedOption == optIndex
                                Surface(
                                    color = if (isChecked) PythonBlue.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                    shape = RoundedCornerShape(12.dp),
                                    border = androidx.compose.foundation.BorderStroke(
                                        width = if (isChecked) 1.5.dp else 1.dp,
                                        color = if (isChecked) PythonBlue else Color.Transparent
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { viewModel.selectQuizAnswer(currentQIndex, optIndex) }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(14.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        RadioButton(
                                            selected = isChecked,
                                            onClick = { viewModel.selectQuizAnswer(currentQIndex, optIndex) },
                                            colors = RadioButtonDefaults.colors(selectedColor = PythonBlue)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = optionText,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = if (isChecked) FontWeight.Bold else FontWeight.Normal,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Navigation Controls
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedButton(
                        onClick = { viewModel.prevQuestion() },
                        enabled = currentQIndex > 0,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Previous")
                    }

                    if (currentQIndex < total - 1) {
                        Button(
                            onClick = { viewModel.nextQuestion() },
                            colors = ButtonDefaults.buttonColors(containerColor = PythonBlue),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Next")
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                        }
                    } else {
                        Button(
                            onClick = { viewModel.submitQuiz() },
                            colors = ButtonDefaults.buttonColors(containerColor = AccentEmerald),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("submit_exam_btn")
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Submit Exam", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Exam Result & Review View (When Submitted)
        if (isSubmitted) {
            val percentage = if (total > 0) (score * 100) / total else 0
            val isPassed = percentage >= 60

            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isPassed) AccentEmerald.copy(alpha = 0.12f) else AccentCoral.copy(alpha = 0.12f)
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        1.5.dp,
                        if (isPassed) AccentEmerald else AccentCoral
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (isPassed) "🎉 EXAM PASSED! बधाई हो!" else "📚 PRACTICE NEEDED - दोबारा प्रयास करें!",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp,
                            color = if (isPassed) AccentEmerald else AccentCoral
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "$score / $total",
                            fontWeight = FontWeight.Black,
                            fontSize = 36.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Text(
                            text = "Score: $percentage%",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { viewModel.startQuiz(chapter) },
                                colors = ButtonDefaults.buttonColors(containerColor = PythonBlue),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Retake Test")
                            }

                            OutlinedButton(
                                onClick = { viewModel.navigateTo(Screen.COURSE) },
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Back to Course")
                            }
                        }
                    }
                }
            }

            item {
                Text(
                    text = "Review Answers & Hindi Explanations",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            itemsIndexed(questions) { index, q ->
                val userChoice = userAnswers[index]
                val isCorrect = userChoice == q.correctOptionIndex

                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isCorrect) AccentEmerald.copy(alpha = 0.5f) else AccentCoral.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Question ${index + 1}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Surface(
                                color = if (isCorrect) AccentEmerald.copy(alpha = 0.15f) else AccentCoral.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = if (isCorrect) "✓ Correct" else "✗ Incorrect",
                                    color = if (isCorrect) AccentEmerald else AccentCoral,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = q.question,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Text(
                            text = q.hindiQuestion,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Correct Answer: ${q.options[q.correctOptionIndex]}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = AccentEmerald
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "💡 व्याख्या: ${q.hindiExplanation}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(8.dp),
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
