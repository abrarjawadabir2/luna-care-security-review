package com.example.ui

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CycleUtils
import com.example.data.SymptomLog
import com.example.ui.theme.*
import com.example.viewmodel.LunaViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SymptomLoggerComponent(
    viewModel: LunaViewModel,
    symptomLogs: List<SymptomLog>,
    selectedDate: java.time.LocalDate,
    modifier: Modifier = Modifier
) {
    val dateString = selectedDate.toString() // YYYY-MM-DD
    
    // Logging input states
    var activeSymptom by remember { mutableStateOf<String?>("Cramps") }
    var severity by remember { mutableStateOf(3) } // 1-5 scale
    var logNotes by remember { mutableStateOf("") }
    var showSuccessBanner by remember { mutableStateOf(false) }

    val symptomOptions = listOf(
        SymptomItem("Cramps", "⚡", AlertRed),
        SymptomItem("Headache", "🧠", WarningAmber),
        SymptomItem("Fatigue", "💤", MutedRosePrimary),
        SymptomItem("Bloating", "🎈", LavenderSecondary),
        SymptomItem("Backache", "🩹", Color(0xFF8B5A2B)),
        SymptomItem("Nausea", "🤢", SuccessGreen),
        SymptomItem("Mood Swings", "🎭", Color(0xFF8A2BE2)),
        SymptomItem("Insomnia", "🦉", Color(0xFF1E3F66))
    )

    // Filter logs for the selected date
    val logsOnSelectedDate = symptomLogs.filter { it.date == dateString }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("symptom_logger_card"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.MedicalServices,
                            contentDescription = "Physical Symptoms",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "Log Physical Symptoms",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Linked to: ${CycleUtils.formatDisplayDate(dateString)}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))

            // 1. Symptom Selector grid/row
            Text(
                text = "1. Select Symptom Type",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            // Dynamic grid layout for symptom items using FlowRow
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                maxItemsInEachRow = 4
            ) {
                symptomOptions.forEach { symptom ->
                    val isSelected = activeSymptom == symptom.name
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (isSelected) symptom.color.copy(alpha = 0.15f)
                                else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                            )
                            .border(
                                width = if (isSelected) 1.5.dp else 1.dp,
                                color = if (isSelected) symptom.color else MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable { activeSymptom = symptom.name }
                            .padding(vertical = 10.dp, horizontal = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(symptom.emoji, fontSize = 16.sp)
                            Text(
                                text = symptom.name,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) symptom.color else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // 2. Severity Slider
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "2. Severity Level (1 to 5)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = when (severity) {
                            1 -> "Mild 🟢"
                            2 -> "Moderate 🟡"
                            3 -> "Noticeable 🟠"
                            4 -> "Severe 🔴"
                            5 -> "Intense 🛑"
                            else -> ""
                        },
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = when (severity) {
                            1 -> SuccessGreen
                            2 -> WarningAmber
                            3 -> MutedRosePrimary
                            4 -> AlertRed
                            5 -> Color.Red
                            else -> MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                }

                Slider(
                    value = severity.toFloat(),
                    onValueChange = { severity = it.toInt() },
                    valueRange = 1f..5f,
                    steps = 3,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("symptom_severity_slider"),
                    colors = SliderDefaults.colors(
                        activeTrackColor = MaterialTheme.colorScheme.primary,
                        inactiveTrackColor = MaterialTheme.colorScheme.outlineVariant
                    )
                )
            }

            // 3. Notes Input
            OutlinedTextField(
                value = logNotes,
                onValueChange = { logNotes = it },
                label = { Text("Describe details or local patterns (optional)", fontSize = 12.sp) },
                shape = RoundedCornerShape(12.dp),
                textStyle = MaterialTheme.typography.bodyMedium,
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("symptom_notes_input")
            )

            // Save Button
            Button(
                onClick = {
                    activeSymptom?.let { symptomName ->
                        viewModel.addSymptomLog(
                            date = dateString,
                            symptomName = symptomName,
                            severity = severity,
                            notes = if (logNotes.isBlank()) null else logNotes
                        )
                        logNotes = ""
                        showSuccessBanner = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("save_symptom_log_btn"),
                enabled = activeSymptom != null,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Log Physical Symptom", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }

            // Success feedback banner
            AnimatedVisibility(
                visible = showSuccessBanner,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(SuccessGreen.copy(alpha = 0.15f))
                        .border(1.dp, SuccessGreen.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                        .padding(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Success",
                            tint = SuccessGreen,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "Symptom logged privately on this date! 🎉",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = SuccessGreen,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    LaunchedEffect(showSuccessBanner) {
                        if (showSuccessBanner) {
                            kotlinx.coroutines.delay(3000)
                            showSuccessBanner = false
                        }
                    }
                }
            }

            // Symptoms History List for this Selected Date
            if (logsOnSelectedDate.isNotEmpty()) {
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                Text(
                    text = "Symptom History on This Date:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    logsOnSelectedDate.forEach { log ->
                        val matchingOption = symptomOptions.find { it.name == log.symptomName }
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("symptom_history_item_${log.id}"),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
                            ),
                            border = BorderStroke(
                                width = 1.dp,
                                color = matchingOption?.color?.copy(alpha = 0.15f) ?: MaterialTheme.colorScheme.outline.copy(alpha = 0.05f)
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                              ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(matchingOption?.emoji ?: "🩹", fontSize = 20.sp)
                                    Column {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Text(
                                                text = log.symptomName,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .background(
                                                        (matchingOption?.color ?: MaterialTheme.colorScheme.error).copy(alpha = 0.15f)
                                                    )
                                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                                            ) {
                                                Text(
                                                    text = "Severity: ${log.severity}/5",
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = matchingOption?.color ?: MaterialTheme.colorScheme.error
                                                )
                                            }
                                        }
                                        if (!log.notes.isNullOrEmpty()) {
                                            Text(
                                                text = log.notes,
                                                fontSize = 11.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                }
                                IconButton(
                                    onClick = { viewModel.deleteSymptomLog(log.id) },
                                    modifier = Modifier.size(28.dp).testTag("delete_symptom_${log.id}")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete Log",
                                        tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private data class SymptomItem(
    val name: String,
    val emoji: String,
    val color: Color
)
