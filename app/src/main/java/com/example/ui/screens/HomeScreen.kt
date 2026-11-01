package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.CourseRepository
import com.example.ui.MainViewModel
import com.example.ui.Screen
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val completedChapters by viewModel.progressManager.completedChapters.collectAsState()
    val streakDays by viewModel.progressManager.streakDays.collectAsState()
    val solvedProblems by viewModel.progressManager.solvedProblems.collectAsState()
    val chapters by viewModel.cachedChapters.collectAsState()

    val totalChapters = 30
    val progressPercent = (completedChapters.size * 100) / totalChapters

    // Find next uncompleted chapter from Room local cache
    val nextChapter = chapters.firstOrNull { !completedChapters.contains(it.id) }
        ?: chapters.firstOrNull() ?: CourseRepository.chapters.first()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Card with Generated Illustration
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("hero_banner_card")
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.hero_banner),
                            contentDescription = "MD Lukman Python AI Hero",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(Color.Transparent, Color(0xCC0A0F1D)),
                                        startY = 100f
                                    )
                                )
                        )
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "Welcome to MD LUKMAN PYTHON AI",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                            Text(
                                text = "Your Personal AI Python Teacher • By MD Lukman",
                                color = PythonYellow,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }

        // Student Progress Overview Card
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Student Learning Progress",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "${completedChapters.size} of $totalChapters Chapters Completed",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            text = "$progressPercent%",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 22.sp,
                            color = PythonBlue
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LinearProgressIndicator(
                        progress = { progressPercent / 100f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(CircleShape),
                        color = PythonYellow,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        StatPill(
                            icon = "🔥",
                            title = "Daily Streak",
                            value = "$streakDays Days",
                            color = PythonYellowDark
                        )
                        StatPill(
                            icon = "✅",
                            title = "Chapters Done",
                            value = "${completedChapters.size}/30",
                            color = AccentEmerald
                        )
                        StatPill(
                            icon = "🏆",
                            title = "Exams Solved",
                            value = "${solvedProblems.size}/10",
                            color = PythonBlue
                        )
                    }
                }
            }
        }

        // Continue Learning Action Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = PythonBlueDark),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.openChapter(nextChapter) }
                    .testTag("continue_learning_button")
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(PythonBlue),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Continue",
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "CONTINUE LEARNING",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = PythonYellow
                            )
                            Text(
                                text = nextChapter.title,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = nextChapter.hindiTitle,
                                fontSize = 13.sp,
                                color = Color(0xFFCBD5E1)
                            )
                        }
                    }
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Go",
                        tint = PythonYellow
                    )
                }
            }
        }

        // Main Navigation Cards Grid
        item {
            Text(
                text = "Learning Modules & Tools",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ModuleCard(
                        title = "Python Course",
                        subtitle = "30 Complete Chapters",
                        icon = Icons.Default.School,
                        badgeText = "Hindi + Eng",
                        accentColor = PythonBlue,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.navigateTo(Screen.COURSE) },
                        testTag = "btn_python_course"
                    )

                    ModuleCard(
                        title = "AI Teacher",
                        subtitle = "Personal Hinglish Tutor",
                        icon = Icons.Default.SmartToy,
                        badgeText = "AI Powered",
                        accentColor = AccentPurple,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.navigateTo(Screen.AI_TUTOR) },
                        testTag = "btn_ai_teacher"
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ModuleCard(
                        title = "Coding Practice",
                        subtitle = "Python Code Editor",
                        icon = Icons.Default.Terminal,
                        badgeText = "Run Code",
                        accentColor = AccentEmerald,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.navigateTo(Screen.CODE_LAB) },
                        testTag = "btn_coding_practice"
                    )

                    ModuleCard(
                        title = "Online Test",
                        subtitle = "Chapter Quizzes & MCQs",
                        icon = Icons.Default.Quiz,
                        badgeText = "Exams",
                        accentColor = PythonYellowDark,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.navigateTo(Screen.COURSE) },
                        testTag = "btn_online_test"
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ModuleCard(
                        title = "Coding Exam",
                        subtitle = "10 Practical Problems",
                        icon = Icons.Default.Code,
                        badgeText = "Test Cases",
                        accentColor = AccentCyan,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.navigateTo(Screen.CODING_EXAMS) },
                        testTag = "btn_coding_exam"
                    )

                    ModuleCard(
                        title = "My Progress",
                        subtitle = "Analytics & Badges",
                        icon = Icons.Default.Leaderboard,
                        badgeText = "Stats",
                        accentColor = AccentCoral,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.navigateTo(Screen.PROGRESS) },
                        testTag = "btn_my_progress"
                    )
                }
            }
        }
    }
}

@Composable
private fun StatPill(
    icon: String,
    title: String,
    value: String,
    color: Color
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.widthIn(min = 90.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = icon, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = color
            )
            Text(
                text = title,
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ModuleCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    badgeText: String,
    accentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String = ""
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .testTag(testTag)
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(accentColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = accentColor,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Surface(
                    color = accentColor.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = badgeText,
                        color = accentColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 14.sp
            )
        }
    }
}
