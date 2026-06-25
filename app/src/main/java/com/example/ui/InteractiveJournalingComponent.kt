package com.example.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CycleUtils
import com.example.data.JournalEntry
import com.example.ui.theme.*
import com.example.viewmodel.LunaViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun InteractiveJournalingComponent(
    viewModel: LunaViewModel,
    journalEntries: List<JournalEntry>,
    modifier: Modifier = Modifier
) {
    // Current input state
    var journalTitle by remember { mutableStateOf("") }
    var journalBody by remember { mutableStateOf("") }
    var selectedMood by remember { mutableStateOf<MoodOption?>(null) }
    var searchKeyword by remember { mutableStateOf("") }
    var selectedMoodFilter by remember { mutableStateOf<String>("All") }
    
    // UI Expand/Collapse states
    var isWritingActive by remember { mutableStateOf(false) }
    var showSuccessBanner by remember { mutableStateOf(false) }
    
    val moods = listOf(
        MoodOption("Radiant", "💖", MutedRosePrimary, PrimaryContainer.copy(alpha = 0.25f)),
        MoodOption("Peaceful", "🌸", LavenderSecondary, SecondaryContainer.copy(alpha = 0.25f)),
        MoodOption("Calm", "🍵", SuccessGreen, SuccessGreen.copy(alpha = 0.25f)),
        MoodOption("Tired", "💤", WarningAmber, WarningAmber.copy(alpha = 0.25f)),
        MoodOption("Low", "💧", Color(0xFF4A90E2), Color(0xFF4A90E2).copy(alpha = 0.2f)),
        MoodOption("Stressed", "⚡", AlertRed, AlertRed.copy(alpha = 0.15f))
    )

    val reflectionPrompts = listOf(
        "What is one small thing you are truly grateful for today?",
        "How does your physical body feel right now?",
        "Describe a kind message you want to tell yourself today.",
        "What helped you feel calm, present, or supported today?",
        "Is there any tension you want to release today?"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("interactive_journal_component"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        
        // 1. Gorgeous offline privacy card with a shield icon
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.12f)
            ),
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Privacy Secure",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Your Privacy is Fully Protected",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Reflections are kept strictly in a sandboxed SQLite Room database on this physical phone. Zero server syncing, zero tracking, 100% offline security.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.9f),
                        lineHeight = 15.sp
                    )
                }
            }
        }

        // 2. Writing section (collapsible/expandable for clean space management)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            border = BorderStroke(
                width = 1.5.dp,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Expandable Writing Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isWritingActive = !isWritingActive }
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.EditNote,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "How are you feeling today?",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            if (!isWritingActive) {
                                Text(
                                    text = "Tap to record your mood & write down reflections",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }
                    
                    IconButton(
                        onClick = { isWritingActive = !isWritingActive },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = if (isWritingActive) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = if (isWritingActive) "Collapse Writer" else "Expand Writer",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                AnimatedVisibility(
                    visible = isWritingActive,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))

                        // A. Mood Picker
                        Text(
                            text = "1. Log Your Current Mood",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            moods.forEach { option ->
                                val isSelected = selectedMood == option
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (isSelected) option.selectedColor else Color.Transparent)
                                        .border(
                                            width = 1.dp,
                                            color = if (isSelected) option.primaryColor else MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .clickable { selectedMood = if (isSelected) null else option }
                                        .padding(vertical = 10.dp, horizontal = 4.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = option.emoji,
                                        fontSize = 24.sp,
                                        textAlign = TextAlign.Center
                                    )
                                    Text(
                                        text = option.name,
                                        fontSize = 10.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) option.primaryColor else MaterialTheme.colorScheme.onSurfaceVariant,
                                        textAlign = TextAlign.Center,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }

                        // B. Prompt suggestions Carousel
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "💡 Interactive Reflection Prompts",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Select a prompt below to inspire your private diary writing:",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                        )
                        
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(reflectionPrompts) { prompt ->
                                val isChosen = journalBody.contains(prompt)
                                Card(
                                    modifier = Modifier
                                        .width(220.dp)
                                        .clickable {
                                            if (!isChosen) {
                                                journalBody = if (journalBody.isBlank()) {
                                                    "Prompt: $prompt\n\n"
                                                } else {
                                                    "$journalBody\n\nPrompt: $prompt\n\n"
                                                }
                                                if (journalTitle.isBlank()) {
                                                    journalTitle = "Daily Reflection"
                                                }
                                            }
                                        },
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isChosen) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                                        else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                                    ),
                                    border = BorderStroke(
                                        width = 1.dp,
                                        color = if (isChosen) MaterialTheme.colorScheme.primary else Color.Transparent
                                    )
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Text(
                                            text = prompt,
                                            fontSize = 11.sp,
                                            lineHeight = 15.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            maxLines = 3,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }
                        }

                        // C. Entry Inputs
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "2. Compose Reflection",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        OutlinedTextField(
                            value = journalTitle,
                            onValueChange = { journalTitle = it },
                            label = { Text("Title (e.g. Grateful Morning, Quiet Evening)", fontSize = 12.sp) },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            textStyle = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.fillMaxWidth().testTag("journal_title_input")
                        )

                        OutlinedTextField(
                            value = journalBody,
                            onValueChange = { journalBody = it },
                            label = { Text("How was your day? What are you holding onto or letting go of?", fontSize = 12.sp) },
                            shape = RoundedCornerShape(12.dp),
                            textStyle = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(130.dp)
                                .testTag("journal_body_input")
                        )

                        // Characters and Mood warning validation
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Chars: ${journalBody.length}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                            )
                            if (selectedMood == null) {
                                Text(
                                    text = "⚠️ Please pick a mood above",
                                    fontSize = 11.sp,
                                    color = WarningAmber,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Save Button
                        Button(
                            onClick = {
                                if (journalBody.isNotBlank()) {
                                    val finalTitle = if (journalTitle.isBlank()) {
                                        selectedMood?.let { "${it.emoji} ${it.name} Reflections" } ?: "Daily Reflections"
                                    } else {
                                        journalTitle
                                    }
                                    
                                    viewModel.addJournalEntry(
                                        title = finalTitle,
                                        body = journalBody,
                                        moodTag = selectedMood?.name,
                                        cyclePhase = null
                                    )
                                    
                                    // Reset fields
                                    journalTitle = ""
                                    journalBody = ""
                                    selectedMood = null
                                    isWritingActive = false
                                    showSuccessBanner = true
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("save_journal_entry_btn"),
                            enabled = journalBody.isNotBlank() && selectedMood != null,
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Save Private Entry", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                    }
                }
            }
        }

        // 3. Save Confirmation banner
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
                    .padding(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Success",
                        tint = SuccessGreen,
                        modifier = Modifier.size(24.dp)
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Reflection Logged Securely! 🎉",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = SuccessGreen
                        )
                        Text(
                            text = "Your private thoughts have been stored locally. No cloud sync requested.",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(
                        onClick = { showSuccessBanner = false },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Banner",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                LaunchedEffect(showSuccessBanner) {
                    if (showSuccessBanner) {
                        kotlinx.coroutines.delay(4000)
                        showSuccessBanner = false
                    }
                }
            }
        }

        // 4. Search and Filter row
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "My Private Reflection History",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            // Search Bar
            OutlinedTextField(
                value = searchKeyword,
                onValueChange = { searchKeyword = it },
                placeholder = { Text("Search title, body, or thoughts...", fontSize = 13.sp) },
                leadingIcon = { Icon(Icons.Default.Search, null, modifier = Modifier.size(20.dp)) },
                trailingIcon = if (searchKeyword.isNotEmpty()) {
                    {
                        IconButton(onClick = { searchKeyword = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear", modifier = Modifier.size(18.dp))
                        }
                    }
                } else null,
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                textStyle = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth().testTag("journal_history_search")
            )

            // Filter chips row
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                item {
                    FilterChip(
                        selected = selectedMoodFilter == "All",
                        onClick = { selectedMoodFilter = "All" },
                        label = { Text("All Moods", fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                }

                items(moods) { option ->
                    val isChecked = selectedMoodFilter == option.name
                    FilterChip(
                        selected = isChecked,
                        onClick = { selectedMoodFilter = if (isChecked) "All" else option.name },
                        label = { Text("${option.emoji} ${option.name}", fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = option.selectedColor,
                            selectedLabelColor = option.primaryColor
                        )
                    )
                }
            }
        }

        // 5. Entries List
        val filteredEntries = journalEntries.filter { entry ->
            val matchKeyword = searchKeyword.isBlank() || 
                    entry.title.contains(searchKeyword, ignoreCase = true) ||
                    entry.body.contains(searchKeyword, ignoreCase = true)
            
            val matchMood = selectedMoodFilter == "All" || entry.moodTag == selectedMoodFilter
            
            matchKeyword && matchMood
        }.sortedByDescending { it.date }

        if (filteredEntries.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.08f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                        modifier = Modifier.size(36.dp)
                    )
                    Text(
                        text = if (searchKeyword.isNotEmpty() || selectedMoodFilter != "All") "No matches found" else "No reflections saved yet",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = if (searchKeyword.isNotEmpty() || selectedMoodFilter != "All") 
                            "Try modifying your filter or keyword to locate entries."
                            else "Your logged moods and private thoughts will be safely listed here.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                filteredEntries.forEach { entry ->
                    val matchingMoodOption = moods.find { it.name == entry.moodTag }
                    
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("journal_history_item_${entry.id}"),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        border = BorderStroke(
                            width = 1.dp,
                            color = matchingMoodOption?.primaryColor?.copy(alpha = 0.2f) 
                                ?: MaterialTheme.colorScheme.outline.copy(alpha = 0.08f)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = entry.title,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    
                                    Text(
                                        text = CycleUtils.formatDisplayDate(entry.date),
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                                
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    // Mood Tag Badge
                                    if (matchingMoodOption != null) {
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(matchingMoodOption.selectedColor)
                                                .padding(horizontal = 8.dp, vertical = 4.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                                            ) {
                                                Text(matchingMoodOption.emoji, fontSize = 12.sp)
                                                Text(
                                                    text = matchingMoodOption.name,
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = matchingMoodOption.primaryColor
                                                )
                                            }
                                        }
                                    }
                                    
                                    IconButton(
                                        onClick = { viewModel.deleteJournalEntry(entry.id) },
                                        modifier = Modifier.size(28.dp).testTag("delete_journal_${entry.id}")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete Entry",
                                            tint = MaterialTheme.colorScheme.error.copy(alpha = 0.8f),
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                            
                            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.15f))
                            
                            Text(
                                text = entry.body,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 17.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

private data class MoodOption(
    val name: String,
    val emoji: String,
    val primaryColor: Color,
    val selectedColor: Color
)
