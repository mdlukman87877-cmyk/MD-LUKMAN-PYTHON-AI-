package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.MainViewModel
import com.example.ui.theme.*

@Composable
fun CodeEditorScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val code by viewModel.editorCode.collectAsState()
    val output by viewModel.editorOutput.collectAsState()
    val error by viewModel.editorError.collectAsState()
    val isExecuting by viewModel.isCodeExecuting.collectAsState()
    val executionTimeMs by viewModel.executionTimeMs.collectAsState()

    val clipboardManager = LocalClipboardManager.current

    val codeShortcuts = listOf("(", ")", ":", "=", "\"", "'", "[", "]", "print()", "def ", "for ", "if ", "in ", "range(", "True", "False")

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("code_editor_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Toolbar with Controls
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Button(
                            onClick = { viewModel.runCode() },
                            enabled = !isExecuting,
                            colors = ButtonDefaults.buttonColors(containerColor = AccentEmerald),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("run_code_button")
                        ) {
                            if (isExecuting) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Running...", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            } else {
                                Icon(Icons.Default.PlayArrow, contentDescription = "Run", modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Run", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        OutlinedButton(
                            onClick = { viewModel.clearEditorOutput() },
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                            modifier = Modifier.testTag("clear_console_button")
                        ) {
                            Icon(Icons.Default.ClearAll, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Clear", fontSize = 12.sp)
                        }
                    }

                    Row {
                        IconButton(
                            onClick = { clipboardManager.setText(AnnotatedString(code)) },
                            modifier = Modifier.testTag("copy_code_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copy Code",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        IconButton(
                            onClick = { viewModel.resetEditorCode() },
                            modifier = Modifier.testTag("reset_code_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.RestartAlt,
                                contentDescription = "Reset Code",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        // Quick Symbol Toolbar
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                codeShortcuts.forEach { symbol ->
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .clickable { viewModel.insertEditorCodeSnippet(symbol) }
                    ) {
                        Text(
                            text = symbol,
                            color = MaterialTheme.colorScheme.primary,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        // Code Input Editor Box
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CodeEditorBg),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 220.dp, max = 340.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(AccentCoral))
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(PythonYellow))
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(AccentEmerald))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "main.py",
                                color = Color(0xFF94A3B8),
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        Text(
                            text = "${code.lines().size} lines",
                            color = Color(0xFF64748B),
                            fontSize = 11.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    BasicTextField(
                        value = code,
                        onValueChange = { viewModel.setEditorCode(it) },
                        textStyle = TextStyle(
                            color = CodeEditorText,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 14.sp,
                            lineHeight = 22.sp
                        ),
                        cursorBrush = SolidColor(PythonYellow),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .testTag("code_input_field")
                    )
                }
            }
        }

        // AI Error Solver Card (Appears automatically when error occurs!)
        if (error != null) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = AccentCoral.copy(alpha = 0.1f)),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, AccentCoral.copy(alpha = 0.6f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("ai_error_card")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.ErrorOutline,
                                contentDescription = "Error",
                                tint = AccentCoral
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Python Execution Error Detected",
                                fontWeight = FontWeight.Bold,
                                color = AccentCoral,
                                fontSize = 15.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = error ?: "",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
                                .padding(10.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = { viewModel.solveErrorWithAi() },
                            colors = ButtonDefaults.buttonColors(containerColor = PythonBlue),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth().testTag("solve_error_ai_btn")
                        ) {
                            Icon(Icons.Default.AutoFixHigh, contentDescription = null, tint = PythonYellow)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "🤖 AI Error Solver: Fix My Code in Hindi",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }

        // Output Console Display
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("output_console_card")
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Terminal,
                                contentDescription = "Output",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Output Console",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        if (executionTimeMs > 0) {
                            Surface(
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "${executionTimeMs}ms",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        color = CodeEditorBg,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 100.dp)
                    ) {
                        Text(
                            text = if (output.isNotEmpty()) output else "Click 'Run' to execute Python code...",
                            color = if (output.isNotEmpty()) AccentEmeraldLight else Color(0xFF64748B),
                            fontFamily = FontFamily.Monospace,
                            fontSize = 13.sp,
                            lineHeight = 20.sp,
                            modifier = Modifier
                                .padding(12.dp)
                                .horizontalScroll(rememberScrollState())
                        )
                    }
                }
            }
        }
    }
}
