package com.example.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun MenstrualCupGuideCard(
    modifier: Modifier = Modifier
) {
    var activeTab by remember { mutableStateOf(0) } // 0 = Safe Use, 1 = Safe Cleaning, 2 = Myths & Safety
    
    // Hygiene checklist state
    var checkedHands by remember { mutableStateOf(false) }
    var checkedBoiled by remember { mutableStateOf(false) }
    var checkedTime by remember { mutableStateOf(false) }
    val isReady = checkedHands && checkedBoiled && checkedTime

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("menstrual_cup_guide_card"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            width = 1.5.dp,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            
            // Header with gorgeous organic gradient background matching LunaCare theme
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                MutedRosePrimary,
                                PrimaryContainer.copy(alpha = 0.9f)
                            )
                        )
                    )
                    .padding(20.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.25f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Spa,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Text(
                            text = "Menstrual Cup Care Center",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.testTag("cup_guide_title")
                        )
                    }
                    Text(
                        text = "Your comprehensive, clinically backed clinical safety companion.",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
            }

            // Tab Selector Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                listOf("Safe Use", "Cleaning", "Myths & TSS").forEachIndexed { index, label ->
                    val selected = activeTab == index
                    val containerColor = if (selected) MaterialTheme.colorScheme.primary else Color.Transparent
                    val textColor = if (selected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                    val weightModifier = Modifier.weight(1f)
                    
                    Box(
                        modifier = weightModifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(containerColor)
                            .clickable { activeTab = index }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = label,
                            fontSize = 13.sp,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                            color = textColor,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // Tab Contents
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(animationSpec = spring())
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                when (activeTab) {
                    0 -> UseGuideSection()
                    1 -> CleaningGuideSection()
                    2 -> SafetyAndMythsSection()
                }
            }

            Divider(
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // Dynamic Hygiene Prep Checklist (Implements client state check for a supportive touch)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 20.dp, top = 4.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "✨ Interactive Safer-Wear Checklist",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Text(
                    text = "Verify these three simple rules before each menstrual cup insertion:",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                )

                // Checkbox 1
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { checkedHands = !checkedHands }
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Checkbox(
                        checked = checkedHands,
                        onCheckedChange = { checkedHands = it },
                        modifier = Modifier.testTag("chk_hands")
                    )
                    Column {
                        Text(
                            text = "My hands are washed cleanly",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Cleaned for 20s with perfume-free soap.",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Checkbox 2
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { checkedBoiled = !checkedBoiled }
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Checkbox(
                        checked = checkedBoiled,
                        onCheckedChange = { checkedBoiled = it },
                        modifier = Modifier.testTag("chk_boiled")
                    )
                    Column {
                        Text(
                            text = "My cup is sterilized & cooled down",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Boiled for 5-7 minutes at cycle inception, or rinsed properly during wear.",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Checkbox 3
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { checkedTime = !checkedTime }
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Checkbox(
                        checked = checkedTime,
                        onCheckedChange = { checkedTime = it },
                        modifier = Modifier.testTag("chk_time")
                    )
                    Column {
                        Text(
                            text = "I will empty the cup within 8-12 hours",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Crucial window to prevent bacteria growth and TSS.",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Spring-animated validation feedback
                AnimatedVisibility(
                    visible = isReady,
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
                                contentDescription = "Verified Ready",
                                tint = SuccessGreen,
                                modifier = Modifier.size(24.dp)
                            )
                            Column {
                                Text(
                                    text = "Ready & Sterile! ✨",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SuccessGreen
                                )
                                Text(
                                    text = "You are following strict clinical safety and health habits. Perfect job!",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// USE GUIDE SECTION
// ==========================================
@Composable
fun UseGuideSection() {
    var expandedStep by remember { mutableStateOf(-1) }
    
    val steps = listOf(
        StepItem(
            num = "1",
            title = "Strict Hand Hygiene",
            summary = "The absolute #1 rule before insertion.",
            details = "Always wash your hands thoroughly for at least 20 seconds using plain, unscented, oil-free soap. Any under-nail dirt or residual chemical soap can alter vaginal flora and cause yeast/bacterial infections.",
            icon = Icons.Default.Soap
        ),
        StepItem(
            num = "2",
            title = "A Perfect Fold",
            summary = "Choose C-Fold, Punch-down, or 7-fold.",
            details = "Fold the cup to make the leading insertion edge as small as a standard tampon. For beginners, the 'Punch-down' fold provides the narrowest entry angle, reducing resistance.",
            icon = Icons.Default.Layers
        ),
        StepItem(
            num = "3",
            title = "Relax & Insert Backward",
            summary = "Position the entry angle properly.",
            details = "Sit comfortably on the toilet or squat. Inhale and relax your pelvic floor muscles. Direct the folded cup back towards your tailbone/spine (never straight upwards!). A vertical placement blocks natural sealing.",
            icon = Icons.Default.WavingHand
        ),
        StepItem(
            num = "4",
            title = "Verify Vacuum Seal",
            summary = "Check for an airtight, secure fit.",
            details = "Once inserted, let the cup pop open. Run a clean finger around the cup base; it must feel fully round and inflated. Give the base a slight turn or pull the stem slightly—there must be noticeable suction resistance.",
            icon = Icons.Default.Verified
        ),
        StepItem(
            num = "5",
            title = "8-12 Hour Limit",
            summary = "Never exceed 12 hours of consecutive wear.",
            details = "A menstrual cup can safely hold up to 3 tampons' worth of content, but leaving it inside longer than 12 hours poses a rare but real risk of Toxic Shock Syndrome (TSS) due to lack of fresh ventilation.",
            icon = Icons.Default.Timer
        ),
        StepItem(
            num = "6",
            title = "Pinch to Remove Safely",
            summary = "Never pull the stem without breaking suction.",
            details = "To remove, relax your pelvic bottom. Locate the base of the cup (just above the stem). Squeeze/pinch the base firmly between your fingers to break the airtight seal, then walk the cup out safely.",
            icon = Icons.Default.DoNotTouch
        )
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Safe Cup Insertion & Removal Guide",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        
        Text(
            text = "Tap on any step to reveal crucial clinical explanations:",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        steps.forEachIndexed { index, step ->
            val expanded = expandedStep == index
            
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (expanded) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)
                        else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
                    )
                    .clickable { expandedStep = if (expanded) -1 else index }
                    .padding(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = step.num,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = step.title,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = step.summary,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    Icon(
                        imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = if (expanded) "Collapse" else "Expand",
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                        modifier = Modifier.size(20.dp)
                    )
                }
                
                AnimatedVisibility(
                    visible = expanded,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, start = 38.dp)
                    ) {
                        Text(
                            text = step.details,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.9f),
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }
    }
}

// ==========================================
// CLEANING GUIDE SECTION
// ==========================================
@Composable
fun CleaningGuideSection() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Sterilization & Cleaning Protocol",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        // 1. Between Cycles Boiling callout
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.15f)
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.15f))
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AcUnit, // represents sterilizing/clean environment
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "♨️ Between-Cycles: Deep Sterilize",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Before your period starts and after it fully ends, boil your cup in a saucepan of hot water for 5-7 minutes. Use a kitchen whisk or insert the cup inside tongs to ensure it does not touch the bottom or sides of the pot directly, which can cause the silicone to melt.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // 2. Active period washing
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            )
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.WaterDrop,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "💧 During Wear: Rinsing & Washing",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        text = "Always rinse with COLD water first (helps prevent permanent blood stains and strong organic odors), then wash with warm water and oil-free, perfume-free mild soap. Take extra care to clean the tiny suction holes beneath the cup rim—they allow the vacuum seal to release cleanly.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // Simple table of DO vs DON'T (highly interactive educational approach)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "🧼 Wash Ingredient Checklist",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // Left column: Clean list
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = SuccessGreen.copy(alpha = 0.08f)),
                    border = BorderStroke(1.dp, SuccessGreen.copy(alpha = 0.2f))
                ) {
                    Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Icon(Icons.Default.Check, null, tint = SuccessGreen, modifier = Modifier.size(14.dp))
                            Text("SAFE TO USE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SuccessGreen)
                        }
                        Text("• Water-based cleaners\n• Plain unscented soap\n• Dedicated cup wash\n• Clean tap water", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 15.sp)
                    }
                }

                // Right column: Bad list
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = AlertRed.copy(alpha = 0.05f)),
                    border = BorderStroke(1.dp, AlertRed.copy(alpha = 0.15f))
                ) {
                    Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Icon(Icons.Default.Close, null, tint = AlertRed, modifier = Modifier.size(14.dp))
                            Text("NEVER USE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AlertRed)
                        }
                        Text("• Dishwasher detergents\n• Rubbing Alcohol/Bleach\n• Vinegar or Scented oil\n• Airtight plastic boxes", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 15.sp)
                    }
                }
            }
        }
    }
}

// ==========================================
// MYTHS & TSS SAFETY SECTION
// ==========================================
@Composable
fun SafetyAndMythsSection() {
    var expandedMyth by remember { mutableStateOf(-1) }
    
    val myths = listOf(
        MythItem(
            myth = "Myth: A menstrual cup can get completely lost inside your body.",
            fact = "Fact: Your cervix acts as a physical barrier closing off the top of your vaginal canal. There is nowhere for the cup to escape! If a cup feels too high to reach, simply relax and gently bear down with your pelvic muscles to push it down.",
            icon = Icons.Default.PrivacyTip
        ),
        MythItem(
            myth = "Myth: You must remove the cup every single time you urinate.",
            fact = "Fact: The vagina, urethra, and rectum are entirely separate physical channels. Urination and bowel movements do not require removal of your cup! However, confirm your cup seal remains secure post physical strain.",
            icon = Icons.Default.Repeat
        ),
        MythItem(
            myth = "Myth: Menstrual cups are 100% immune from Toxic Shock Syndrome (TSS).",
            fact = "Fact: While far safer than tampons, cup wearers are NOT completely immune from TSS if safety protocols are broken. TSS occurs if bacteria (Staphylococcus aureus) grows on stale blood due to extended wear (>12h) or dirty finger insertion.",
            icon = Icons.Default.HealthAndSafety
        )
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Red TSS Warning Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(AlertRed.copy(alpha = 0.1f))
                .border(1.dp, AlertRed.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                .padding(12.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "TSS Danger Alert",
                    tint = AlertRed,
                    modifier = Modifier.size(20.dp)
                )
                Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    Text(
                        text = "Critical Clinical Warning: TSS Safety",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = AlertRed
                    )
                    Text(
                        text = "Toxic Shock Syndrome (TSS) is an ultra-rare but severe illness. Seek IMMEDIATE emergency care if you experience high sudden fever, blood pressure drop, nausea/diarrhea, extreme dizziness, or a red rash resembling sunburn during or shortly after your period.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        Text(
            text = "Common Myths Debunked",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(top = 4.dp)
        )
        
        Text(
            text = "Tap on any card to reveal scientific realities:",
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        myths.forEachIndexed { idx, item ->
            val expanded = expandedMyth == idx
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expandedMyth = if (expanded) -1 else idx },
                colors = CardDefaults.cardColors(
                    containerColor = if (expanded) MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.2f)
                    else MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = if (expanded) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = null,
                            tint = if (expanded) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = item.myth,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (expanded) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    AnimatedVisibility(
                        visible = expanded,
                        enter = expandVertically() + fadeIn(),
                        exit = shrinkVertically() + fadeOut()
                    ) {
                        Text(
                            text = item.fact,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.9f),
                            lineHeight = 16.sp,
                            modifier = Modifier.padding(start = 30.dp, top = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

// Data models helper
private data class StepItem(
    val num: String,
    val title: String,
    val summary: String,
    val details: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

private data class MythItem(
    val myth: String,
    val fact: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)
