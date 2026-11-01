package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Chapter
import com.example.ui.MainViewModel
import com.example.ui.Screen
import com.example.ui.components.CodeBlockView
import com.example.ui.theme.*

@Composable
fun ChapterDetailScreen(
    chapter: Chapter,
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler {
        viewModel.navigateBack()
    }

    val completedChapters by viewModel.progressManager.completedChapters.collectAsState()
    val isCompleted = completedChapters.contains(chapter.id)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("chapter_detail_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Chapter Header Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = PythonBlueDark),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = PythonYellow.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "CHAPTER ${chapter.id}",
                                color = PythonYellow,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        IconButton(
                            onClick = { viewModel.progressManager.markChapterCompleted(chapter.id) }
                        ) {
                            Icon(
                                imageVector = if (isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                                contentDescription = "Toggle Complete",
                                tint = if (isCompleted) AccentEmerald else Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = chapter.title.substringAfter(":").trim(),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Text(
                        text = chapter.hindiTitle,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                        color = PythonYellowLight
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { viewModel.startQuiz(chapter) },
                            colors = ButtonDefaults.buttonColors(containerColor = PythonYellowDark),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Quiz, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Take Test", fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = {
                                viewModel.navigateTo(Screen.AI_TUTOR)
                                viewModel.askAi("Mujhe '${chapter.title}' simple Hindi mein aur detail se samjhaiye.")
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.5f)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.SmartToy, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Ask AI Tutor")
                        }
                    }
                }
            }
        }

        // Section 1: Introduction (परिचय)
        item {
            ContentSectionCard(
                sectionNumber = 1,
                titleEnglish = "Introduction",
                titleHindi = "परिचय",
                icon = Icons.Default.Info,
                accentColor = PythonBlue
            ) {
                Text(
                    text = chapter.introduction,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 22.sp
                )
            }
        }

        // Section 2: Definition (परिभाषा)
        item {
            ContentSectionCard(
                sectionNumber = 2,
                titleEnglish = "Definition",
                titleHindi = "तकनीकी परिभाषा",
                icon = Icons.Default.MenuBook,
                accentColor = AccentPurple
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                ) {
                    Text(
                        text = chapter.definition,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(12.dp),
                        lineHeight = 22.sp
                    )
                }
            }
        }

        // Section 3: Detailed Hindi Explanation (विस्तृत हिंदी व्याख्या)
        item {
            ContentSectionCard(
                sectionNumber = 3,
                titleEnglish = "Detailed Hindi Explanation",
                titleHindi = "विस्तृत हिंदी व्याख्या",
                icon = Icons.Default.Description,
                accentColor = PythonYellowDark
            ) {
                Text(
                    text = chapter.detailedHindiExplanation,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 24.sp
                )
            }
        }

        // Section 4: Important Concepts (मुख्य बातें)
        item {
            ContentSectionCard(
                sectionNumber = 4,
                titleEnglish = "Important Concepts",
                titleHindi = "मुख्य सिद्धांत और बातें",
                icon = Icons.Default.Lightbulb,
                accentColor = AccentEmerald
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    chapter.importantConcepts.forEach { concept ->
                        Row(verticalAlignment = Alignment.Top) {
                            Box(
                                modifier = Modifier
                                    .padding(top = 6.dp)
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(AccentEmerald)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = concept,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                lineHeight = 20.sp
                            )
                        }
                    }
                }
            }
        }

        // Section 5: Real-Life Examples (दैनिक जीवन का उदाहरण)
        item {
            ContentSectionCard(
                sectionNumber = 5,
                titleEnglish = "Real-Life Example",
                titleHindi = "दैनिक जीवन का वास्तविक उदाहरण",
                icon = Icons.Default.EmojiObjects,
                accentColor = AccentCyan
            ) {
                Surface(
                    color = AccentCyan.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, AccentCyan.copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(text = "💡", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = chapter.realLifeExample,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 22.sp
                        )
                    }
                }
            }
        }

        // Section 6: Python Code Examples (कोड उदाहरण)
        item {
            ContentSectionCard(
                sectionNumber = 6,
                titleEnglish = "Python Code Example",
                titleHindi = "पायथन कोड उदाहरण",
                icon = Icons.Default.Terminal,
                accentColor = PythonBlue
            ) {
                Column {
                    CodeBlockView(
                        code = chapter.pythonCodeExample,
                        onTryInEditor = { viewModel.openCodeInEditor(chapter.pythonCodeExample) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { viewModel.openCodeInEditor(chapter.pythonCodeExample) },
                        colors = ButtonDefaults.buttonColors(containerColor = PythonBlue),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Open & Run in Code Lab", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Section 7: Line-by-Line Code Explanation (हर लाइन का अर्थ)
        item {
            ContentSectionCard(
                sectionNumber = 7,
                titleEnglish = "Line-by-Line Code Explanation",
                titleHindi = "कोड की हर लाइन का आसान मतलब",
                icon = Icons.Default.FormatListNumbered,
                accentColor = AccentPurple
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    chapter.lineByLineExplanation.forEach { explanation ->
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = explanation,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(10.dp),
                                lineHeight = 20.sp
                            )
                        }
                    }
                }
            }
        }

        // Section 8: Common Mistakes (अक्सर होने वाली गलतियां)
        item {
            ContentSectionCard(
                sectionNumber = 8,
                titleEnglish = "Common Mistakes",
                titleHindi = "छात्रों द्वारा अक्सर होने वाली गलतियां",
                icon = Icons.Default.WarningAmber,
                accentColor = AccentCoral
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    chapter.commonMistakes.forEach { mistake ->
                        Row(verticalAlignment = Alignment.Top) {
                            Text(text = "❌", fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = mistake,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                lineHeight = 20.sp
                            )
                        }
                    }
                }
            }
        }

        // Section 9: Practice Exercises (अभ्यास प्रश्न)
        item {
            ContentSectionCard(
                sectionNumber = 9,
                titleEnglish = "Practice Exercises",
                titleHindi = "स्वयं अभ्यास के लिए प्रश्न",
                icon = Icons.Default.EditNote,
                accentColor = AccentEmerald
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    chapter.practiceExercises.forEachIndexed { index, exercise ->
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(AccentEmerald.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${index + 1}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = AccentEmerald
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = exercise,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    lineHeight = 20.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // Section 10: Chapter Test (प्रत्येक अध्याय का टेस्ट)
        item {
            ContentSectionCard(
                sectionNumber = 10,
                titleEnglish = "Chapter Examination",
                titleHindi = "अध्याय परीक्षा एवं मूल्यांकन",
                icon = Icons.Default.FactCheck,
                accentColor = PythonYellowDark
            ) {
                Column {
                    Text(
                        text = "इस अध्याय में सीखी गई बातों का मूल्यांकन करने के लिए 10 प्रश्नों की ऑनलाइन परीक्षा दें। स्वचालित अंक गणना एवं विस्तृत हिंदी व्याख्या उपलब्ध है।",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 22.sp
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Button(
                        onClick = { viewModel.startQuiz(chapter) },
                        colors = ButtonDefaults.buttonColors(containerColor = PythonYellowDark),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("start_chapter_quiz_button")
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Start Chapter Test (${chapter.testQuestions.size} Questions)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ContentSectionCard(
    sectionNumber: Int,
    titleEnglish: String,
    titleHindi: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accentColor: Color,
    content: @Composable () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(accentColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = titleEnglish,
                        tint = accentColor,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = "$sectionNumber. $titleEnglish",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 15.sp
                    )
                    Text(
                        text = titleHindi,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            content()
        }
    }
}
