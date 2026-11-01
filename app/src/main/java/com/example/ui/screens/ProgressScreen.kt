package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.data.CourseRepository
import com.example.ui.MainViewModel
import com.example.ui.theme.*

@Composable
fun ProgressScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val completedChapters by viewModel.progressManager.completedChapters.collectAsState()
    val streakDays by viewModel.progressManager.streakDays.collectAsState()
    val solvedProblems by viewModel.progressManager.solvedProblems.collectAsState()

    val totalChapters = 30
    val progressPercent = (completedChapters.size * 100) / totalChapters

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("progress_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Main Progress Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = PythonBlueDark),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "STUDENT ACADEMIC DASHBOARD",
                        color = PythonYellow,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "$progressPercent%",
                        color = Color.White,
                        fontSize = 44.sp,
                        fontWeight = FontWeight.Black
                    )

                    Text(
                        text = "Total Syllabus Completed (${completedChapters.size} / $totalChapters)",
                        color = Color(0xFFCBD5E1),
                        fontSize = 13.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    LinearProgressIndicator(
                        progress = { progressPercent / 100f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(CircleShape),
                        color = PythonYellow,
                        trackColor = Color(0xFF334155)
                    )
                }
            }
        }

        // Metrics Grid
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("🔥 Learning Streak", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("$streakDays Days", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = PythonYellowDark)
                    }
                }

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("💻 Coding Problems", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("${solvedProblems.size} / 10 Solved", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = AccentEmerald)
                    }
                }
            }
        }

        // Achievement Badges
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Honor Badges & Achievements",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        BadgeItem(
                            icon = "🌱",
                            name = "Python Novice",
                            desc = "Completed first 3 chapters",
                            isUnlocked = completedChapters.size >= 3
                        )
                        BadgeItem(
                            icon = "🔄",
                            name = "Loop Master",
                            desc = "Completed Chapters 10 to 15",
                            isUnlocked = completedChapters.contains(12) && completedChapters.contains(13)
                        )
                        BadgeItem(
                            icon = "📦",
                            name = "Data Structure Guru",
                            desc = "Mastered Lists, Tuples, Sets, Dicts",
                            isUnlocked = completedChapters.contains(16) && completedChapters.contains(19)
                        )
                        BadgeItem(
                            icon = "🏛️",
                            name = "OOPs Champion",
                            desc = "Mastered Classes, Objects & Inheritance",
                            isUnlocked = completedChapters.contains(25) && completedChapters.contains(26)
                        )
                        BadgeItem(
                            icon = "👑",
                            name = "MD Lukman Python AI Graduate",
                            desc = "Finished all 30 Chapters!",
                            isUnlocked = completedChapters.size == 30
                        )
                    }
                }
            }
        }

        // Chapters Completed List
        item {
            Text(
                text = "Course Chapter Breakdown",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        items(CourseRepository.chapters) { chapter ->
            val isDone = completedChapters.contains(chapter.id)
            val (score, total) = viewModel.progressManager.getQuizScore(chapter.id)

            Surface(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(if (isDone) AccentEmerald else Color(0xFF94A3B8).copy(alpha = 0.3f)),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isDone) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                            } else {
                                Text("${chapter.id}", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Text(
                                text = chapter.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = chapter.hindiTitle,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    if (score >= 0) {
                        Surface(
                            color = PythonYellow.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "Score: $score/$total",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = PythonYellowDark,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BadgeItem(
    icon: String,
    name: String,
    desc: String,
    isUnlocked: Boolean
) {
    Surface(
        color = if (isUnlocked) PythonBlue.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = icon, fontSize = 22.sp)
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = if (isUnlocked) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = desc,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (isUnlocked) {
                Surface(color = AccentEmerald, shape = RoundedCornerShape(6.dp)) {
                    Text(
                        text = "UNLOCKED",
                        color = Color.White,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}
