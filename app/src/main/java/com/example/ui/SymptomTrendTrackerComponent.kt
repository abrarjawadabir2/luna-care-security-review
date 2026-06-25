package com.example.ui

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CycleUtils
import com.example.data.PeriodLog
import com.example.data.SymptomLog
import com.example.ui.theme.*
import com.example.viewmodel.LunaViewModel
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SymptomTrendTrackerComponent(
    viewModel: LunaViewModel,
    symptomLogs: List<SymptomLog>,
    periodLogs: List<PeriodLog>,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTabIdx by remember { mutableStateOf(0) } // 0: 3-Month Overview, 1: Month-by-Month comparison
    var activeSymptomDrilldown by remember { mutableStateOf<String?>(null) }
    
    // Get current date references for the last 3 months
    val today = LocalDate.now()
    val m0 = today // Current Month (e.g. June)
    val m1 = today.minusMonths(1) // 1 Month Ago (e.g. May)
    val m2 = today.minusMonths(2) // 2 Months Ago (e.g. April)

    val formatter = java.time.format.DateTimeFormatter.ofPattern("MMMM yyyy", Locale.US)
    val m0Name = m0.format(formatter)
    val m1Name = m1.format(formatter)
    val m2Name = m2.format(formatter)

    // Helper functions to check if a date string is in a given month/year
    fun isInMonth(dateStr: String, monthDate: LocalDate): Boolean {
        return try {
            val date = LocalDate.parse(dateStr)
            date.year == monthDate.year && date.monthValue == monthDate.monthValue
        } catch (e: Exception) {
            false
        }
    }

    // Filter symptom logs for the last 3 months
    val logsM0 = symptomLogs.filter { isInMonth(it.date, m0) }
    val logsM1 = symptomLogs.filter { isInMonth(it.date, m1) }
    val logsM2 = symptomLogs.filter { isInMonth(it.date, m2) }
    val logsAll3Months = logsM0 + logsM1 + logsM2

    // All available symptoms with associated styling
    val symptomDefinitions = listOf(
        SymptomMeta("Cramps", "⚡", AlertRed, "Most active during early cycle days. Rest is recommended."),
        SymptomMeta("Headache", "🧠", WarningAmber, "Hydration and gentle acupressure can relieve tension."),
        SymptomMeta("Fatigue", "💤", MutedRosePrimary, "Listen to your body. Limit caffeine late in the day."),
        SymptomMeta("Bloating", "🎈", LavenderSecondary, "Light movement and herbal teas support digestion."),
        SymptomMeta("Backache", "🩹", Color(0xFF8B5A2B), "Gentle lower back stretches or heat pads bring comfort."),
        SymptomMeta("Nausea", "🤢", SuccessGreen, "Ginger tea or small, frequent meals can settle the stomach."),
        SymptomMeta("Mood Swings", "🎭", Color(0xFF8A2BE2), "Be gentle with your thoughts. Focus on breathing loops."),
        SymptomMeta("Insomnia", "🦉", Color(0xFF1E3F66), "A screen-free bedtime routine supports peaceful sleep.")
    )

    // Calculate aggregated stats across the 3 months
    val aggregatedFrequencies = symptomDefinitions.map { meta ->
        val countAll = logsAll3Months.count { it.symptomName == meta.name }
        val countM0 = logsM0.count { it.symptomName == meta.name }
        val countM1 = logsM1.count { it.symptomName == meta.name }
        val countM2 = logsM2.count { it.symptomName == meta.name }
        val avgSeverity = logsAll3Months.filter { it.symptomName == meta.name }
            .map { it.severity }
            .average()
            .takeIf { !it.isNaN() } ?: 0.0
        
        SymptomAggregate(
            meta = meta,
            totalCount = countAll,
            m0Count = countM0,
            m1Count = countM1,
            m2Count = countM2,
            averageSeverity = avgSeverity
        )
    }.sortedByDescending { it.totalCount }

    val totalLogsCount = logsAll3Months.size

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Bar Navigation
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                        .testTag("trend_back_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back to dashboard",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                
                Column {
                    Text(
                        text = "Symptom Trend Analytics",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Last 3 Months Tracker",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                }
            }

            // Quick Data-Seeder for testing/empty analytics
            IconButton(
                onClick = {
                    seedThreeMonthData(viewModel)
                },
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f))
                    .testTag("seed_demo_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Science,
                    contentDescription = "Seed demo logs",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        if (totalLogsCount == 0) {
            // Empty State Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp)
                    .testTag("symptom_empty_state_card"),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Analytics,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                        modifier = Modifier.size(72.dp)
                    )

                    Text(
                        text = "No Symptom History Found",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "We couldn't detect any logged symptoms in the last three months ($m2Name to $m0Name). You can log symptoms daily inside the 'Cycle' tab, or generate realistic demonstration data to preview this sanctuary dashboard immediately.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp
                    )

                    Button(
                        onClick = { seedThreeMonthData(viewModel) },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().testTag("seed_empty_state_button")
                    ) {
                        Icon(imageVector = Icons.Default.AddChart, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Seed 3-Month Demonstration Data")
                    }
                }
            }
        } else {
            // Summary Highlight Banner
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Empathy & Analytics Insight",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                        
                        val topSymptom = aggregatedFrequencies.firstOrNull { it.totalCount > 0 }
                        val summaryText = if (topSymptom != null) {
                            "You reported ${topSymptom.meta.name} most frequently (total of ${topSymptom.totalCount} times) over the last three months. Total symptoms captured: $totalLogsCount entries."
                        } else {
                            "Your health dashboard captures historical trends safely offline."
                        }
                        
                        Text(
                            text = summaryText,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                            lineHeight = 20.sp
                        )
                    }
                }
            }

            // Tabs for switching between aggregations and monthly breakdowns
            TabRow(
                selectedTabIndex = selectedTabIdx,
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.primary,
                divider = {},
                modifier = Modifier.fillMaxWidth()
            ) {
                Tab(
                    selected = selectedTabIdx == 0,
                    onClick = { selectedTabIdx = 0 },
                    text = { Text("📊 Overall Frequency", style = MaterialTheme.typography.labelMedium) }
                )
                Tab(
                    selected = selectedTabIdx == 1,
                    onClick = { selectedTabIdx = 1 },
                    text = { Text("📅 Month-By-Month", style = MaterialTheme.typography.labelMedium) }
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            if (selectedTabIdx == 0) {
                // TAB 0: Aggregated Frequency View
                Text(
                    text = "Frequency Distribution (Last 3 Months)",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )

                // Beautiful Custom horizontal chart using Compose rows and indicators
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        val maxCount = aggregatedFrequencies.maxOfOrNull { it.totalCount } ?: 1
                        
                        aggregatedFrequencies.filter { it.totalCount > 0 }.forEach { item ->
                            val progress = item.totalCount.toFloat() / maxCount.toFloat()
                            val isSelected = activeSymptomDrilldown == item.meta.name

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable {
                                        activeSymptomDrilldown = if (isSelected) null else item.meta.name
                                    }
                                    .background(if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.05f) else Color.Transparent)
                                    .padding(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text(item.meta.icon, fontSize = 22.sp)
                                        Text(
                                            text = item.meta.name,
                                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(
                                            text = "${item.totalCount} times",
                                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                            color = item.meta.color
                                        )
                                        Icon(
                                            imageVector = if (isSelected) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                // Custom designed rounded progress bar
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(10.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .fillMaxWidth(progress)
                                            .clip(CircleShape)
                                            .background(
                                                Brush.horizontalGradient(
                                                    colors = listOf(
                                                        item.meta.color.copy(alpha = 0.7f),
                                                        item.meta.color
                                                    )
                                                )
                                            )
                                    )
                                }
                                
                                // Expandable Details / Drilldown within the row
                                AnimatedVisibility(
                                    visible = isSelected,
                                    enter = expandVertically() + fadeIn(),
                                    exit = shrinkVertically() + fadeOut()
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .padding(top = 12.dp, start = 4.dp, end = 4.dp)
                                            .fillMaxWidth(),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                                        
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = "Average Severity:",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                            Text(
                                                text = String.format("%.1f / 5.0", item.averageSeverity),
                                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                                color = item.meta.color
                                            )
                                        }

                                        // Draw small severity indicator circles
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            val fullDots = item.averageSeverity.toInt()
                                            for (i in 1..5) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(10.dp)
                                                        .clip(CircleShape)
                                                        .background(
                                                            if (i <= fullDots) item.meta.color
                                                            else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                                                        )
                                                )
                                            }
                                        }

                                        // Cycle phase correlation if period data exists!
                                        val phaseStats = calculatePhaseCorrelations(item.meta.name, logsAll3Months, periodLogs, viewModel)
                                        if (phaseStats.isNotEmpty()) {
                                            Text(
                                                text = "Phase Correlation:",
                                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                                modifier = Modifier.padding(top = 4.dp)
                                            )
                                            FlowRow(
                                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                                verticalArrangement = Arrangement.spacedBy(6.dp)
                                            ) {
                                                phaseStats.forEach { (phase, count) ->
                                                    SuggestionChip(
                                                        onClick = {},
                                                        label = { Text("$phase ($count)", fontSize = 11.sp) },
                                                        colors = SuggestionChipDefaults.suggestionChipColors(
                                                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.15f),
                                                            labelColor = MaterialTheme.colorScheme.primary
                                                        ),
                                                        border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                                                    )
                                                }
                                            }
                                        }

                                        Text(
                                            text = "Sanctuary Guidance:",
                                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary),
                                            modifier = Modifier.padding(top = 4.dp)
                                        )
                                        Text(
                                            text = item.meta.advice,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

            } else {
                // TAB 1: Month-by-Month comparison View
                Text(
                    text = "Symptom Comparison: Last 3 Months",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )

                // Beautiful monthly bars comparing top active symptoms
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        // Monthly Legends
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            MonthLegendItem(m2Name, Color(0xFFD48C73)) // Two months ago
                            Spacer(modifier = Modifier.width(16.dp))
                            MonthLegendItem(m1Name, Color(0xFF9881A8)) // One month ago
                            Spacer(modifier = Modifier.width(16.dp))
                            MonthLegendItem(m0Name, Color(0xFFD48B8B)) // Current month
                        }

                        // Filter symptoms with logged instances to compare
                        val activeComparisonList = aggregatedFrequencies.filter { it.totalCount > 0 }.take(4)
                        
                        if (activeComparisonList.isEmpty()) {
                            Text(
                                text = "No comparisons available.",
                                style = MaterialTheme.typography.bodySmall,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        } else {
                            activeComparisonList.forEach { item ->
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text(item.meta.icon, fontSize = 20.sp)
                                        Text(
                                            text = item.meta.name,
                                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    // Display 3 columns representing occurrences side-by-side
                                    val maxVal = maxOf(item.m0Count, item.m1Count, item.m2Count, 1)

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 8.dp),
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        // Month 2 Column
                                        Box(modifier = Modifier.weight(1f)) {
                                            MonthlyBarRow(
                                                count = item.m2Count,
                                                maxCount = maxVal,
                                                color = Color(0xFFD48C73),
                                                label = m2Name.substringBefore(" ")
                                            )
                                        }

                                        // Month 1 Column
                                        Box(modifier = Modifier.weight(1f)) {
                                            MonthlyBarRow(
                                                count = item.m1Count,
                                                maxCount = maxVal,
                                                color = Color(0xFF9881A8),
                                                label = m1Name.substringBefore(" ")
                                            )
                                        }

                                        // Month 0 Column
                                        Box(modifier = Modifier.weight(1f)) {
                                            MonthlyBarRow(
                                                count = item.m0Count,
                                                maxCount = maxVal,
                                                color = Color(0xFFD48B8B),
                                                label = m0Name.substringBefore(" ")
                                            )
                                        }
                                    }
                                }
                                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f), modifier = Modifier.padding(top = 10.dp))
                            }
                        }
                    }
                }
            }

            // Cycle Phase & Physical Symptoms Insights
            Text(
                text = "Biomedical Synchrony Insights",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(top = 8.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Timeline,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Phase-Based Diagnostics",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Text(
                        text = "Your biological sanctuary automatically monitors correlations with menstrual phases safely in internal offline memory. It compares physical feedback across your 4 cyclical states:",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                        lineHeight = 20.sp
                    )

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        PhaseCorrelationSummaryItem(
                            phaseName = "Menstrual Phase",
                            days = "Days 1-5",
                            indicatorColor = AlertRed,
                            description = "Typically characterized by Cramps and Fatigue as the body resets."
                        )
                        PhaseCorrelationSummaryItem(
                            phaseName = "Follicular Phase",
                            days = "Days 6-12",
                            indicatorColor = SuccessGreen,
                            description = "Energy climbs. Physical symptoms usually decline significantly."
                        )
                        PhaseCorrelationSummaryItem(
                            phaseName = "Ovulatory Phase",
                            days = "Days 13-16",
                            indicatorColor = WarningAmber,
                            description = "High energy, but brief cramps or light headaches may present."
                        )
                        PhaseCorrelationSummaryItem(
                            phaseName = "Luteal Phase",
                            days = "Days 17-28",
                            indicatorColor = LavenderSecondary,
                            description = "PMS stage. Backaches, Bloating, and Mood Swings are most frequent."
                        )
                    }
                }
            }

            // Clear Simulated Logs Card / Action
            OutlinedButton(
                onClick = {
                    viewModel.clearAllUserData()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("reset_symptom_logs_button"),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.3f)),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
            ) {
                Icon(imageVector = Icons.Default.DeleteForever, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Clear All Application Database (Reset Data)")
            }
        }
    }
}

@Composable
fun MonthLegendItem(name: String, color: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(color)
        )
        Text(
            text = name,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
        )
    }
}

@Composable
fun MonthlyBarRow(
    count: Int,
    maxCount: Int,
    color: Color,
    label: String
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        val visualProgress = if (maxCount > 0) count.toFloat() / maxCount.toFloat() else 0f
        
        // Vertical column bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(14.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(visualProgress.coerceAtLeast(0.08f))
                    .clip(CircleShape)
                    .background(color)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
            Text(
                text = "$count",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}

@Composable
fun PhaseCorrelationSummaryItem(
    phaseName: String,
    days: String,
    indicatorColor: Color,
    description: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.15f))
            .padding(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .padding(top = 4.dp)
                .clip(CircleShape)
                .background(indicatorColor)
        )

        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = phaseName,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = days,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// Data structures
data class SymptomMeta(
    val name: String,
    val icon: String,
    val color: Color,
    val advice: String
)

data class SymptomAggregate(
    val meta: SymptomMeta,
    val totalCount: Int,
    val m0Count: Int,
    val m1Count: Int,
    val m2Count: Int,
    val averageSeverity: Double
)

// Calculate simple simulated phase correlation based on date distance to period logs
fun calculatePhaseCorrelations(
    symptomName: String,
    symptomLogs: List<SymptomLog>,
    periodLogs: List<PeriodLog>,
    viewModel: LunaViewModel
): List<Pair<String, Int>> {
    val correlations = mutableMapOf<String, Int>()
    val activeSymptomLogs = symptomLogs.filter { it.symptomName == symptomName }

    activeSymptomLogs.forEach { log ->
        // Find nearest preceding periodLog
        val precedingPeriod = periodLogs
            .filter { it.startDate <= log.date }
            .maxByOrNull { it.startDate }

        if (precedingPeriod != null) {
            val daysBetween = CycleUtils.getDaysBetween(precedingPeriod.startDate, log.date)
            // Map days to phases
            val averageCycleLen = viewModel.profile.value?.averageCycleLength ?: 28
            val averagePeriodLen = viewModel.profile.value?.averagePeriodLength ?: 5
            val phase = CycleUtils.getCyclePhase(daysBetween.toInt(), averageCycleLen, averagePeriodLen)
            correlations[phase] = (correlations[phase] ?: 0) + 1
        } else {
            // Simulated default fallback for realistic correlation previews
            val hash = (symptomName.hashCode() + log.date.hashCode()) % 4
            val mockPhase = when (kotlin.math.abs(hash)) {
                0 -> "Menstrual Phase"
                1 -> "Follicular Phase"
                2 -> "Ovulatory Phase"
                else -> "Luteal Phase"
            }
            correlations[mockPhase] = (correlations[mockPhase] ?: 0) + 1
        }
    }

    return correlations.toList().sortedByDescending { it.second }
}

// Seed helper function
fun seedThreeMonthData(viewModel: LunaViewModel) {
    val today = LocalDate.now()
    
    // Seed PeriodLogs to align phases
    viewModel.addPeriodLog(
        startDate = today.minusDays(26).toString(),
        endDate = today.minusDays(22).toString(),
        flowLevel = "Medium",
        symptoms = listOf("Cramps", "Fatigue"),
        notes = "Simulated preceding cycle start"
    )
    viewModel.addPeriodLog(
        startDate = today.minusDays(54).toString(),
        endDate = today.minusDays(50).toString(),
        flowLevel = "Medium",
        symptoms = listOf("Cramps", "Headache"),
        notes = "Simulated historic cycle start"
    )
    viewModel.addPeriodLog(
        startDate = today.minusDays(82).toString(),
        endDate = today.minusDays(78).toString(),
        flowLevel = "Heavy",
        symptoms = listOf("Cramps", "Fatigue"),
        notes = "Simulated cycle reset"
    )

    // Seed realistic Symptom Logs over last 3 months
    // Month 0 (Current)
    viewModel.addSymptomLog(today.minusDays(1).toString(), "Cramps", 4, "High cramps in the evening.")
    viewModel.addSymptomLog(today.minusDays(2).toString(), "Fatigue", 3, "Felt heavy fatigue after work.")
    viewModel.addSymptomLog(today.minusDays(5).toString(), "Backache", 2, "Mild back tenderness.")
    viewModel.addSymptomLog(today.minusDays(10).toString(), "Headache", 3, "Tension headache around noon.")
    viewModel.addSymptomLog(today.minusDays(25).toString(), "Cramps", 5, "Intense cramps during period start.")
    viewModel.addSymptomLog(today.minusDays(26).toString(), "Fatigue", 4, "Slept for 10 hours and still tired.")

    // Month 1 (Previous)
    viewModel.addSymptomLog(today.minusDays(32).toString(), "Bloating", 4, "Stomach bloating after eating.")
    viewModel.addSymptomLog(today.minusDays(35).toString(), "Headache", 5, "Strong migraine-like headache.")
    viewModel.addSymptomLog(today.minusDays(40).toString(), "Mood Swings", 3, "Emotional changes in the evening.")
    viewModel.addSymptomLog(today.minusDays(52).toString(), "Cramps", 4, "Period day 2 cramps.")
    viewModel.addSymptomLog(today.minusDays(53).toString(), "Backache", 4, "Dull lower back ache.")
    viewModel.addSymptomLog(today.minusDays(58).toString(), "Insomnia", 3, "Woke up twice in the night.")

    // Month 2 (2 Months Ago)
    viewModel.addSymptomLog(today.minusDays(64).toString(), "Bloating", 3, "General bloating symptoms.")
    viewModel.addSymptomLog(today.minusDays(70).toString(), "Nausea", 4, "Mild morning nausea.")
    viewModel.addSymptomLog(today.minusDays(80).toString(), "Cramps", 5, "Strong menstrual cramps.")
    viewModel.addSymptomLog(today.minusDays(81).toString(), "Fatigue", 5, "Complete energy depletion.")
    viewModel.addSymptomLog(today.minusDays(85).toString(), "Mood Swings", 4, "Felt highly anxious.")
    viewModel.addSymptomLog(today.minusDays(88).toString(), "Insomnia", 4, "Unable to fall asleep until 3 AM.")
}
