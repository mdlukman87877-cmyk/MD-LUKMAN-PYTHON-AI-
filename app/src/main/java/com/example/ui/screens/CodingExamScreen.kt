package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CodingExamRepository
import com.example.model.CodingProblem
import com.example.ui.MainViewModel
import com.example.ui.theme.*

@Composable
fun CodingExamScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val solvedProblems by viewModel.progressManager.solvedProblems.collectAsState()
    val problems = CodingExamRepository.problems

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("coding_exam_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = PythonBlueDark),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Coding Examination",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color.White
                        )
                        Surface(
                            color = PythonYellow,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "${solvedProblems.size}/10 Solved",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "हल करें वास्तविक प्रोग्रामिंग चुनौतियाँ। कोड लिखें, टेस्ट केस पास करें और अपनी कोडिंग स्किल्स साबित करें!",
                        fontSize = 13.sp,
                        color = Color(0xFFCBD5E1),
                        lineHeight = 18.sp
                    )
                }
            }
        }

        items(problems, key = { it.id }) { problem ->
            val isSolved = solvedProblems.contains(problem.id)

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isSolved) AccentEmerald.copy(alpha = 0.6f) else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.openCodingProblem(problem) }
                    .testTag("coding_problem_card_${problem.id}")
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                color = when (problem.difficulty) {
                                    "Easy" -> AccentEmerald.copy(alpha = 0.15f)
                                    "Medium" -> PythonYellow.copy(alpha = 0.15f)
                                    else -> AccentCoral.copy(alpha = 0.15f)
                                },
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = problem.difficulty,
                                    color = when (problem.difficulty) {
                                        "Easy" -> AccentEmerald
                                        "Medium" -> PythonYellowDark
                                        else -> AccentCoral
                                    },
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = problem.category,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "${problem.id}. ${problem.title}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Text(
                            text = problem.descriptionHindi,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.secondary,
                            maxLines = 2,
                            lineHeight = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    if (isSolved) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(AccentEmerald),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Solved",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    } else {
                        Button(
                            onClick = { viewModel.openCodingProblem(problem) },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PythonBlue),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text("Solve", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CodingProblemDetailScreen(
    problem: CodingProblem,
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler {
        viewModel.navigateBack()
    }

    val userCode by viewModel.problemUserCode.collectAsState()
    val testResults by viewModel.testCaseResults.collectAsState()
    val testOutputLog by viewModel.problemTestOutput.collectAsState()
    val isTesting by viewModel.isTestingProblem.collectAsState()
    val solvedProblems by viewModel.progressManager.solvedProblems.collectAsState()
    val isSolved = solvedProblems.contains(problem.id)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("coding_problem_detail_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Problem Header & Description
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = PythonBlue.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Problem #${problem.id}",
                                color = PythonBlue,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        if (isSolved) {
                            Surface(
                                color = AccentEmerald.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "✓ PASSED & SOLVED",
                                    color = AccentEmerald,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = problem.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = problem.descriptionHindi,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = problem.descriptionEnglish,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text("Input Format:", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                Text(problem.inputFormat, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface)
                            }
                        }

                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text("Output Format:", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                Text(problem.outputFormat, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface)
                            }
                        }
                    }
                }
            }
        }

        // Code Editor Box
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CodeEditorBg),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 200.dp, max = 320.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "solution.py",
                            color = Color(0xFF94A3B8),
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace
                        )
                        IconButton(
                            onClick = { viewModel.updateProblemUserCode(problem.starterCode) },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.RestartAlt,
                                contentDescription = "Reset",
                                tint = Color(0xFF94A3B8),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    BasicTextField(
                        value = userCode,
                        onValueChange = { viewModel.updateProblemUserCode(it) },
                        textStyle = TextStyle(
                            color = CodeEditorText,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 13.sp,
                            lineHeight = 20.sp
                        ),
                        cursorBrush = SolidColor(PythonYellow),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .testTag("coding_problem_editor")
                    )
                }
            }
        }

        // Run Test Cases Action Button
        item {
            Button(
                onClick = { viewModel.runProblemTests() },
                enabled = !isTesting,
                colors = ButtonDefaults.buttonColors(containerColor = PythonBlue),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("run_test_cases_btn")
            ) {
                if (isTesting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Executing Test Cases...", fontWeight = FontWeight.Bold)
                } else {
                    Icon(Icons.Default.PlayCircle, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Run Test Cases & Submit", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }
        }

        // Test Case Results Card
        if (testOutputLog.isNotEmpty()) {
            val allPassed = testResults.isNotEmpty() && testResults.all { it }

            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (allPassed) AccentEmerald.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        1.5.dp,
                        if (allPassed) AccentEmerald else MaterialTheme.colorScheme.outlineVariant
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (allPassed) Icons.Default.CheckCircle else Icons.Default.Info,
                                contentDescription = null,
                                tint = if (allPassed) AccentEmerald else PythonBlue
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (allPassed) "🎉 All Test Cases Passed!" else "Test Case Verification",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = if (allPassed) AccentEmerald else MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Surface(
                            color = CodeEditorBg,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = testOutputLog,
                                color = CodeEditorText,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 12.sp,
                                lineHeight = 18.sp,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
