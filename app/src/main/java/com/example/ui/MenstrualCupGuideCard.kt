package com.example.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MenstrualCupGuideCard(
    modifier: Modifier = Modifier
) {
    var activeTab by remember { mutableStateOf(0) } // 0 = Step-by-Step Wizards, 1 = Fold Explorer, 2 = Hygiene & Checklist, 3 = Safety Quiz

    // Checklist states
    var checkedHands by remember { mutableStateOf(false) }
    var checkedBoiled by remember { mutableStateOf(false) }
    var checkedTime by remember { mutableStateOf(false) }
    val isReady = checkedHands && checkedBoiled && checkedTime

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("menstrual_cup_guide_card"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            
            // Premium Gradient Header
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
                                contentDescription = "Menstrual Cup Icon",
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
                        text = "Your comprehensive, clinically-backed visual safety companion.",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
            }

            // Material 3 Sub-Tabs
            ScrollableTabRow(
                selectedTabIndex = activeTab,
                edgePadding = 12.dp,
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f),
                contentColor = MaterialTheme.colorScheme.primary,
                divider = {}
            ) {
                listOf(
                    "🎓 Walkthroughs",
                    "📐 Folding Guide",
                    "🧼 Clean & Setup",
                    "🧠 Safety Quiz",
                    "❓ FAQs & Tips"
                ).forEachIndexed { index, title ->
                    Tab(
                        selected = activeTab == index,
                        onClick = { activeTab = index },
                        text = {
                            Text(
                                text = title,
                                fontSize = 13.sp,
                                fontWeight = if (activeTab == index) FontWeight.Bold else FontWeight.Medium
                            )
                        },
                        modifier = Modifier.testTag("cup_tab_$index")
                    )
                }
            }

            // Tab Content container with layout spring animations
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(animationSpec = spring())
                    .padding(16.dp)
            ) {
                when (activeTab) {
                    0 -> WizardSection()
                    1 -> FoldExplorerSection()
                    2 -> ChecklistAndCleaningSection(
                        checkedHands = checkedHands,
                        onCheckedHandsChanged = { checkedHands = it },
                        checkedBoiled = checkedBoiled,
                        onCheckedBoiledChanged = { checkedBoiled = it },
                        checkedTime = checkedTime,
                        onCheckedTimeChanged = { checkedTime = it },
                        isReady = isReady
                    )
                    3 -> CupQuizSection()
                    4 -> CupFaqSection()
                }
            }
        }
    }
}

// ==========================================
// 1. STEP-BY-STEP INTERACTIVE WIZARDS
// ==========================================
@Composable
fun WizardSection() {
    var activeWizardFlow by remember { mutableStateOf<String?>("NONE") } // "NONE", "INSERTION", "REMOVAL", "CLEANING"
    var currentStepIndex by remember { mutableStateOf(0) }
    var isStepCompleted by remember { mutableStateOf(false) }

    val insertionSteps = listOf(
        WizardStep(
            title = "1. Hand Hygiene First",
            icon = Icons.Default.Soap,
            description = "Wash your hands perfectly for at least 20 seconds using fragrance-free, oil-free soap. Ensure your nails are clean too.",
            tip = "Clinical Fact: Dirty hands or standard alcohol-based sanitizers transfer bacteria and alter your natural vaginal flora, leading to pH imbalances."
        ),
        WizardStep(
            title = "2. Fold the Cup",
            icon = Icons.Default.Layers,
            description = "Select a folding style (like C-Fold or Punch-Down) to reduce the insertion diameter. Squeeze the rim tightly to maintain the fold.",
            tip = "Clinical Fact: The Punch-Down fold offers the smallest entry point and is most recommended for first-time users."
        ),
        WizardStep(
            title = "3. Comfortable Position",
            icon = Icons.Default.Accessibility,
            description = "Consciously relax your pelvic floor muscles. Sit on the toilet, squat down, or lift one leg up onto the bathtub.",
            tip = "Clinical Fact: Tension makes insertion harder. If you are struggling, relax your jaw! A relaxed jaw naturally triggers relaxation in the pelvic floor."
        ),
        WizardStep(
            title = "4. Angle Towards Tailbone",
            icon = Icons.Default.ArrowForward,
            description = "Slowly slide the folded cup into your vagina, directing it backward toward your tailbone (spine) at a 45-degree angle, not straight up.",
            tip = "Clinical Fact: Menstrual cups rest lower in the vaginal canal than tampons, sitting just past the pubic bone."
        ),
        WizardStep(
            title = "5. Verify Vacuum Seal",
            icon = Icons.Default.CheckCircle,
            description = "Let the cup pop open. Slide a clean finger around the base. It must feel round with no creases. Give the stem a gentle pull—you should feel suction resistance.",
            tip = "Clinical Fact: Suction resistance confirms a complete airtight seal, guaranteeing a fully leak-proof experience."
        )
    )

    val removalSteps = listOf(
        WizardStep(
            title = "1. Clean Setup",
            icon = Icons.Default.Soap,
            description = "Wash your hands thoroughly with unscented soap. Assume a squatting position on the toilet to naturally shorten your vaginal canal.",
            tip = "Clinical Fact: Squatting pushes your cervix lower, making it significantly easier to reach the base of your cup."
        ),
        WizardStep(
            title = "2. Bear Down Gently",
            icon = Icons.Default.Search,
            description = "Inhale, then use your pelvic muscles to gently push (bear down) on the cup until you can feel the ribbed base with your thumb and index finger.",
            tip = "Clinical Fact: Bearing down is safe. Menstrual cups cannot get lost inside your body because the cervix seals off the canal."
        ),
        WizardStep(
            title = "3. Break the Vacuum Seal",
            icon = Icons.Default.DoNotTouch,
            description = "Locate the base of the cup (not the stem). Squeeze the bottom firmly between your fingers. You will feel the airtight suction seal release.",
            tip = "Clinical Fact: Crucial step! Never yank the stem directly without breaking the seal first. Doing so creates pain and cervix irritation."
        ),
        WizardStep(
            title = "4. Slide & Empty",
            icon = Icons.Default.ArrowDownward,
            description = "Slowly walk the cup side-to-side while pulling down. Keep it upright to avoid spills. Empty the contents directly into the toilet.",
            tip = "Clinical Fact: Rinse with cold water immediately to avoid permanent blood stains and odor buildup."
        )
    )

    val cleaningSteps = listOf(
        WizardStep(
            title = "1. Cold Water First",
            icon = Icons.Default.WaterDrop,
            description = "Always rinse the cup under COLD running tap water first, before using warm water or soap.",
            tip = "Clinical Fact: Hot water cooks and binds the organic iron proteins in blood directly to the silicone, locking in bad odors and stains."
        ),
        WizardStep(
            title = "2. Warm Wash & Air Holes",
            icon = Icons.Default.Soap,
            description = "Wash with warm water and oil-free, perfume-free mild soap. Stretch the tiny suction holes under the rim to clear any residual fluids.",
            tip = "Clinical Fact: Clogged suction air holes prevent the cup from expanding and forming an airtight leak-proof seal during wear."
        ),
        WizardStep(
            title = "3. Cycle Boiling",
            icon = Icons.Default.Whatshot,
            description = "Before your cycle starts and after it ends, boil the cup in a saucepan for 5 to 7 minutes. Keep it floating so it doesn't touch the bottom.",
            tip = "Clinical Fact: Use a metal kitchen whisk! Put the cup inside the whisk to keep it safely suspended from the hot metal bottom of the pot."
        ),
        WizardStep(
            title = "4. Air-Dry & Store",
            icon = Icons.Default.CheckCircle,
            description = "Let the cup air dry completely. Store it in a clean, breathable cotton drawstring bag. Never store it in an airtight plastic box.",
            tip = "Clinical Fact: Silicone is medical-grade and requires natural air ventilation. Airtight storage locks in humidity and promotes harmful mold growth."
        )
    )

    val currentSteps = when (activeWizardFlow) {
        "INSERTION" -> insertionSteps
        "REMOVAL" -> removalSteps
        "CLEANING" -> cleaningSteps
        else -> emptyList()
    }

    if (activeWizardFlow == "NONE") {
        // Main Selection Screen
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Select an Interactive Clinical Walkthrough:",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            listOf(
                WizardSelectionItem("INSERTION", "📥 Step-by-Step Insertion Guide", "Learn hand hygiene, proper folding, entry angles, and suction verification.", MutedRosePrimary),
                WizardSelectionItem("REMOVAL", "📤 Step-by-Step Removal Guide", "Master pelvic relaxation, bearing down, and breaking the vacuum seal painlessly.", LavenderSecondary),
                WizardSelectionItem("CLEANING", "🧼 Cleaning & Boiling Protocol", "Protect your silicone cup from stains, odors, and bacteria between cycles.", SuccessGreen)
            ).forEach { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            activeWizardFlow = item.id
                            currentStepIndex = 0
                            isStepCompleted = false
                        }
                        .testTag("select_wizard_${item.id.lowercase()}"),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f)),
                    border = BorderStroke(1.dp, item.color.copy(alpha = 0.15f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(item.color.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (item.id == "INSERTION") Icons.Default.ArrowDownward else if (item.id == "REMOVAL") Icons.Default.ArrowUpward else Icons.Default.WaterDrop,
                                contentDescription = null,
                                tint = item.color,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.title,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = item.subtitle,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "Begin Walkthrough",
                            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                        )
                    }
                }
            }
        }
    } else {
        // Active Wizard Flow View
        val step = currentSteps[currentStepIndex]
        val progress = (currentStepIndex + 1).toFloat() / currentSteps.size

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Flow Header & Exit Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = when (activeWizardFlow) {
                        "INSERTION" -> "Menstrual Cup Insertion"
                        "REMOVAL" -> "Menstrual Cup Removal"
                        else -> "Menstrual Cup Cleaning"
                    },
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                        shape = RoundedCornerShape(6.dp)
                    ).padding(horizontal = 8.dp, vertical = 4.dp)
                )

                IconButton(
                    onClick = { activeWizardFlow = "NONE" },
                    modifier = Modifier.size(24.dp).testTag("exit_wizard_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Exit Walkthrough",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Visual Progress bar
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Step ${currentStepIndex + 1} of ${currentSteps.size}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${(progress * 100).toInt()}% Done",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                )
            }

            // Main Step content Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.15f)),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.08f))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
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
                                imageVector = step.icon,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Text(
                            text = step.title,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Text(
                        text = step.description,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 19.sp
                    )

                    // clinical safety tip callout box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f))
                            .padding(10.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "Clinical Tip",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = step.tip,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                lineHeight = 15.sp
                            )
                        }
                    }
                }
            }

            // Checklist completion switch for interactive reinforcement
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { isStepCompleted = !isStepCompleted }
                    .background(
                        if (isStepCompleted) SuccessGreen.copy(alpha = 0.08f)
                        else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
                    )
                    .border(
                        1.dp,
                        if (isStepCompleted) SuccessGreen.copy(alpha = 0.3f)
                        else MaterialTheme.colorScheme.outline.copy(alpha = 0.05f),
                        RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = if (isStepCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                        contentDescription = null,
                        tint = if (isStepCompleted) SuccessGreen else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "I understand and verified this step!",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isStepCompleted) SuccessGreen else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Navigation Buttons (Prev, Next, Finish)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = {
                        if (currentStepIndex > 0) {
                            currentStepIndex--
                            isStepCompleted = false
                        } else {
                            activeWizardFlow = "NONE"
                        }
                    },
                    modifier = Modifier.testTag("wizard_prev_btn")
                ) {
                    Icon(Icons.Default.ChevronLeft, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Back")
                }

                Button(
                    onClick = {
                        if (currentStepIndex < currentSteps.size - 1) {
                            currentStepIndex++
                            isStepCompleted = false
                        } else {
                            // Completed flow, return to menu
                            activeWizardFlow = "NONE"
                        }
                    },
                    modifier = Modifier.testTag("wizard_next_btn"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (currentStepIndex == currentSteps.size - 1) SuccessGreen else MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = if (currentStepIndex == currentSteps.size - 1) "Finish 🎉" else "Next Step",
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = if (currentStepIndex == currentSteps.size - 1) Icons.Default.Check else Icons.Default.ChevronRight,
                        contentDescription = null
                    )
                }
            }
        }
    }
}

// ==========================================
// 2. INTERACTIVE FOLD EXPLORER SECTION
// ==========================================
@Composable
fun FoldExplorerSection() {
    var selectedFold by remember { mutableStateOf("C-FOLD") } // "C-FOLD", "PUNCH", "7-FOLD"
    var isPoppedOpen by remember { mutableStateOf(false) }

    val folds = listOf(
        FoldType(
            id = "C-FOLD",
            title = "C-Fold (U-Fold)",
            emoji = "∪",
            difficulty = "Easy 🟢",
            pros = "Very straightforward to execute, easy to understand.",
            cons = "Keeps the entry point slightly wider than other folds.",
            instructions = "Press the cup flat, then fold it in half length-wise to form a C or U shape. Squeeze firmly near the rim before inserting."
        ),
        FoldType(
            id = "PUNCH",
            title = "Punch-Down Fold",
            emoji = "👇",
            difficulty = "Beginner-Friendly ✨",
            pros = "Creates a very narrow insertion point, significantly reducing entry resistance.",
            cons = "Sometimes requires a gentle rotation at the base to pop open fully.",
            instructions = "Place a finger on the cup rim and push it straight down into the center of the base. Squeeze the sides together tightly."
        ),
        FoldType(
            id = "7-FOLD",
            title = "7-Fold (Triangle)",
            emoji = "◸",
            difficulty = "Intermediate 🟡",
            pros = "Low insertion profile, opens extremely easily inside the canal.",
            cons = "Can be slightly tricky to hold firmly without releasing too early.",
            instructions = "Press the cup flat, then fold one of the upper corners diagonally down across the body of the cup to form a '7' shape."
        )
    )

    val currentFold = folds.first { it.id == selectedFold }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "Menstrual Cup Folding Techniques",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        // Horizontal Selection Chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            folds.forEach { fold ->
                val isSelected = selectedFold == fold.id
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                            else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                        )
                        .border(
                            width = if (isSelected) 1.5.dp else 1.dp,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable {
                            selectedFold = fold.id
                            isPoppedOpen = false
                        }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(fold.emoji, fontSize = 20.sp)
                        Text(
                            text = fold.title.split(" ")[0],
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Fold Details Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.08f))
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = currentFold.title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Difficulty: ${currentFold.difficulty}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Text(
                    text = currentFold.instructions,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 17.sp
                )

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))

                // Pros & Cons
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text("✅", fontSize = 11.sp)
                        Text(
                            text = "Pro: ${currentFold.pros}",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 15.sp
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text("⚠️", fontSize = 11.sp)
                        Text(
                            text = "Con: ${currentFold.cons}",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 15.sp
                        )
                    }
                }
            }
        }

        // Interactive "Pop-Open" Simulator!
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (isPoppedOpen) SuccessGreen.copy(alpha = 0.08f)
                else MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.15f)
            ),
            border = BorderStroke(
                width = 1.dp,
                color = if (isPoppedOpen) SuccessGreen.copy(alpha = 0.3f)
                else MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Vacuum Pop-Open Simulator",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isPoppedOpen) SuccessGreen else MaterialTheme.colorScheme.primary
                )

                // Animated cup depiction
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .clip(CircleShape)
                        .background(
                            if (isPoppedOpen) SuccessGreen.copy(alpha = 0.12f)
                            else MaterialTheme.colorScheme.primary.copy(alpha = 0.06f)
                        )
                        .border(
                            width = 1.dp,
                            color = if (isPoppedOpen) SuccessGreen.copy(alpha = 0.3f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = if (isPoppedOpen) "🏆" else currentFold.emoji,
                            fontSize = if (isPoppedOpen) 36.sp else 30.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isPoppedOpen) "SEAL ACTIVE 🎯" else "FOLDED 🔒",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isPoppedOpen) SuccessGreen else MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Button(
                    onClick = { isPoppedOpen = !isPoppedOpen },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(38.dp)
                        .testTag("simulate_pop_btn"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isPoppedOpen) SuccessGreen else MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = if (isPoppedOpen) "Reset Fold 🔒" else "Simulate Pop-Open! 💥",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                AnimatedVisibility(
                    visible = isPoppedOpen,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    Text(
                        text = "Suction Success! The cup has fully expanded, displacing vaginal canal air to form an airtight leak-proof vacuum lock. Perfect insertion form! ✨",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = SuccessGreen,
                        textAlign = TextAlign.Center,
                        lineHeight = 15.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}

// ==========================================
// 3. HYGIENE & CLEANING CHECKLIST SECTION
// ==========================================
@Composable
fun ChecklistAndCleaningSection(
    checkedHands: Boolean,
    onCheckedHandsChanged: (Boolean) -> Unit,
    checkedBoiled: Boolean,
    onCheckedBoiledChanged: (Boolean) -> Unit,
    checkedTime: Boolean,
    onCheckedTimeChanged: (Boolean) -> Unit,
    isReady: Boolean
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Sterile Wear Preparation",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "Verify these three critical safety checks before inserting your menstrual cup:",
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Checklist Items
        ChecklistItemRow(
            checked = checkedHands,
            onCheckedChange = onCheckedHandsChanged,
            title = "My hands are washed cleanly",
            description = "Cleaned for at least 20 seconds with unscented, oil-free soap.",
            testTag = "chk_hands"
        )

        ChecklistItemRow(
            checked = checkedBoiled,
            onCheckedChange = onCheckedBoiledChanged,
            title = "My cup is sterilized & cooled down",
            description = "Boiled for 5-7 minutes at cycle inception, and rinsed cleanly during active wear.",
            testTag = "chk_boiled"
        )

        ChecklistItemRow(
            checked = checkedTime,
            onCheckedChange = onCheckedTimeChanged,
            title = "I will empty the cup within 12 hours",
            description = "Critical window to eliminate any bacterial growth or TSS risks completely.",
            testTag = "chk_time"
        )

        // Celebratory validation feedback
        AnimatedVisibility(
            visible = isReady,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SuccessGreen.copy(alpha = 0.12f))
                    .border(1.2.dp, SuccessGreen.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Sterile verified",
                        tint = SuccessGreen,
                        modifier = Modifier.size(24.dp)
                    )
                    Column {
                        Text(
                            text = "Sterile Habits Confirmed! ✨",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = SuccessGreen
                        )
                        Text(
                            text = "Excellent clinical compliance. You are protecting your reproductive health with elite sanitization habits.",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 15.sp
                        )
                    }
                }
            }
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f), modifier = Modifier.padding(vertical = 4.dp))

        // Basic Dos and Don'ts table
        Text(
            text = "Silicone Cleaner Compatibility Check:",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = SuccessGreen.copy(alpha = 0.05f)),
                border = BorderStroke(1.dp, SuccessGreen.copy(alpha = 0.15f))
            ) {
                Column(
                    modifier = Modifier.padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(Icons.Default.Check, null, tint = SuccessGreen, modifier = Modifier.size(14.dp))
                        Text("SAFE TO USE", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = SuccessGreen)
                    }
                    Text(
                        text = "• Cold tap water\n• Fragrance-free soap\n• pH-balanced cup wash\n• Plain boiling water",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 14.sp
                    )
                }
            }

            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = AlertRed.copy(alpha = 0.04f)),
                border = BorderStroke(1.dp, AlertRed.copy(alpha = 0.12f))
            ) {
                Column(
                    modifier = Modifier.padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(Icons.Default.Close, null, tint = AlertRed, modifier = Modifier.size(14.dp))
                        Text("NEVER USE", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = AlertRed)
                    }
                    Text(
                        text = "• Dish soap/rubbing alcohol\n• Vinegar or essential oils\n• Bleach or sanitizers\n• Sealed plastic containers",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun ChecklistItemRow(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    title: String,
    description: String,
    testTag: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .clickable { onCheckedChange(!checked) }
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.testTag(testTag)
        )
        Column {
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = description,
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// ==========================================
// 4. INTERACTIVE CUP SAFETY QUIZ SYSTEM
// ==========================================
@Composable
fun CupQuizSection() {
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var selectedAnswerIndex by remember { mutableStateOf<Int?>(null) }
    var isSubmitted by remember { mutableStateOf(false) }
    var score by remember { mutableStateOf(0) }
    var quizFinished by remember { mutableStateOf(false) }

    val questions = listOf(
        QuizQuestion(
            question = "What is the leading cause of pH imbalance and vaginal irritation when using a menstrual cup?",
            options = listOf(
                "Inserting the cup while standing upright.",
                "Inadequate hand hygiene (dirty hands/nails) before handling the cup.",
                "Washing the cup in normal tap water.",
                "Using the cup on the heaviest day of bleeding."
            ),
            correctIndex = 1,
            explanation = "Unwashed hands transfer microscopic bacteria directly to your cup and vaginal lining, triggering bacterial vaginosis or pH issues. Hand hygiene is mandatory."
        ),
        QuizQuestion(
            question = "How should you break the airtight vacuum seal before removing the cup?",
            options = listOf(
                "Yank the bottom stem firmly until the cup exits.",
                "Pinch the ribbed base of the cup firmly between your thumb and index finger.",
                "Wait until the cup overflows so the suction releases naturally.",
                "Use standard tweezers to grab the rim."
            ),
            correctIndex = 1,
            explanation = "Pinching the base of the cup instantly breaks the vacuum seal, allowing comfortable and safe removal without pulling cervix tissues."
        ),
        QuizQuestion(
            question = "What is the absolute maximum safe consecutive wear time for a menstrual cup?",
            options = listOf(
                "4 to 6 hours.",
                "8 to 12 hours.",
                "24 hours continuous.",
                "48 hours without removing."
            ),
            correctIndex = 1,
            explanation = "Although cups hold a high capacity, they must be emptied and sanitized every 12 hours max to minimize Toxic Shock Syndrome (TSS) risks."
        ),
        QuizQuestion(
            question = "Why must you rinse your menstrual cup with COLD water first when emptying it?",
            options = listOf(
                "To harden the silicone rim.",
                "Cold water is proven to kill more bacteria than warm soap.",
                "To prevent permanent blood staining and strong organic odor bonding.",
                "To prevent the cup from melting."
            ),
            correctIndex = 2,
            explanation = "Warm or hot water denatures and cooks iron proteins in blood, bonding them permanently to the silicone. Cold water washes blood clean away without stain or smell."
        )
    )

    if (quizFinished) {
        // Quiz Results & Certificate
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(SuccessGreen.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.WorkspacePremium,
                    contentDescription = "Success Trophy",
                    tint = SuccessGreen,
                    modifier = Modifier.size(36.dp)
                )
            }

            Text(
                text = "Quiz Completed!",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            // Certificate Box
            Card(
                modifier = Modifier.fillMaxWidth().testTag("quiz_certificate_card"),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)),
                border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "LunaCare Safety Certificate 🏆",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Menstrual Cup Safety Mastery",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f), modifier = Modifier.padding(vertical = 4.dp))
                    Text(
                        text = "This verifies that you have successfully completed the clinic-validated safety quiz with a score of:",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "$score / ${questions.size} Correct (${(score.toFloat() / questions.size * 100).toInt()}%)",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (score >= questions.size / 2) SuccessGreen else AlertRed
                    )
                    Text(
                        text = "You are fully equipped with safe, sterile, and healthy menstrual hygiene habits!",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        lineHeight = 14.sp
                    )
                }
            }

            Button(
                onClick = {
                    currentQuestionIndex = 0
                    selectedAnswerIndex = null
                    isSubmitted = false
                    score = 0
                    quizFinished = false
                },
                modifier = Modifier.fillMaxWidth().height(42.dp).testTag("quiz_retry_btn")
            ) {
                Text("Retry Quiz 🔄", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    } else {
        // Question View
        val q = questions[currentQuestionIndex]

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Menstrual Cup Safety Quiz",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Q: ${currentQuestionIndex + 1} of ${questions.size}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Progress bar
            LinearProgressIndicator(
                progress = { (currentQuestionIndex + 1).toFloat() / questions.size },
                modifier = Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(2.dp)),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            )

            // Question Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.15f))
            ) {
                Text(
                    text = q.question,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(14.dp),
                    lineHeight = 18.sp
                )
            }

            // Options List
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                q.options.forEachIndexed { optIdx, option ->
                    val isSelected = selectedAnswerIndex == optIdx
                    val optionBorderColor = when {
                        isSubmitted && optIdx == q.correctIndex -> SuccessGreen
                        isSubmitted && isSelected && selectedAnswerIndex != q.correctIndex -> AlertRed
                        isSelected -> MaterialTheme.colorScheme.primary
                        else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)
                    }
                    val optionBgColor = when {
                        isSubmitted && optIdx == q.correctIndex -> SuccessGreen.copy(alpha = 0.08f)
                        isSubmitted && isSelected && selectedAnswerIndex != q.correctIndex -> AlertRed.copy(alpha = 0.05f)
                        isSelected -> MaterialTheme.colorScheme.primary.copy(alpha = 0.06f)
                        else -> MaterialTheme.colorScheme.surface
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(optionBgColor)
                            .border(
                                width = if (isSelected || isSubmitted) 1.5.dp else 1.dp,
                                color = optionBorderColor,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable(enabled = !isSubmitted) { selectedAnswerIndex = optIdx }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(
                                    when {
                                        isSubmitted && optIdx == q.correctIndex -> SuccessGreen
                                        isSubmitted && isSelected && selectedAnswerIndex != q.correctIndex -> AlertRed
                                        isSelected -> MaterialTheme.colorScheme.primary
                                        else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isSubmitted && optIdx == q.correctIndex) {
                                Icon(Icons.Default.Check, null, tint = Color.White, modifier = Modifier.size(14.dp))
                            } else if (isSubmitted && isSelected && selectedAnswerIndex != q.correctIndex) {
                                Icon(Icons.Default.Close, null, tint = Color.White, modifier = Modifier.size(14.dp))
                            } else {
                                Text(
                                    text = ('A'.code + optIdx).toChar().toString(),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Text(
                            text = option,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            // Submit / Next Buttons and explanation
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (isSubmitted) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                if (selectedAnswerIndex == q.correctIndex) SuccessGreen.copy(alpha = 0.08f)
                                else AlertRed.copy(alpha = 0.04f)
                            )
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "💡 Clinical Rationale: " + q.explanation,
                            fontSize = 11.sp,
                            color = if (selectedAnswerIndex == q.correctIndex) SuccessGreen else AlertRed,
                            lineHeight = 15.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Button(
                        onClick = {
                            if (currentQuestionIndex < questions.size - 1) {
                                currentQuestionIndex++
                                selectedAnswerIndex = null
                                isSubmitted = false
                            } else {
                                quizFinished = true
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(42.dp).testTag("quiz_next_btn")
                    ) {
                        Text(
                            text = if (currentQuestionIndex == questions.size - 1) "View Results 🏁" else "Next Question",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                } else {
                    Button(
                        onClick = {
                            if (selectedAnswerIndex != null) {
                                isSubmitted = true
                                if (selectedAnswerIndex == q.correctIndex) {
                                    score++
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(42.dp).testTag("quiz_submit_btn"),
                        enabled = selectedAnswerIndex != null
                    ) {
                        Text("Submit Answer 🚀", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

// Data Classes
data class WizardStep(
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val description: String,
    val tip: String
)

data class WizardSelectionItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val color: Color
)

data class FoldType(
    val id: String,
    val title: String,
    val emoji: String,
    val difficulty: String,
    val pros: String,
    val cons: String,
    val instructions: String
)

data class QuizQuestion(
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)

// ==========================================
// 5. EDUCATIONAL FAQ & INSTRUCTIONAL TIPS SECTION
// ==========================================
data class FaqItemData(
    val question: String,
    val answer: String,
    val category: String,
    val keywords: List<String>
)

data class InstructionalTip(
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val description: String,
    val color: Color
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CupFaqSection() {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFaqCategory by remember { mutableStateOf("All") }
    
    // State of expanded FAQ items: holds the questions that are currently expanded
    val expandedStates = remember { mutableStateMapOf<String, Boolean>() }

    val faqs = listOf(
        FaqItemData(
            question = "How do I know if my menstrual cup is fully open?",
            category = "Usage & Fit",
            answer = "Slide a clean finger around the base of the cup after insertion. If you feel any folds, creases, or a flat side, the cup hasn't popped open fully. You can gently grab the base of the cup (not the stem) and rotate it 360 degrees, or do a few light squats to encourage it to expand.",
            keywords = listOf("open", "seal", "suction", "flat", "leak")
        ),
        FaqItemData(
            question = "Can a menstrual cup get lost inside me?",
            category = "Safety & Hygiene",
            answer = "No. The vagina is a closed canal, and the cervix acts as a physical barrier at the top. The cup cannot go past the cervix. If the cup rises too high to reach, stay calm, take a deep breath, squat down deeply on the toilet, and gently bear down with your pelvic muscles to push it lower.",
            keywords = listOf("lost", "cervix", "disappear", "high")
        ),
        FaqItemData(
            question = "How often do I need to empty the menstrual cup?",
            category = "Safety & Hygiene",
            answer = "Most manufacturers recommend emptying the cup every 8 to 12 hours. Even if you have a light flow, do not exceed 12 hours of continuous wear to prevent bacterial growth and toxic shock syndrome (TSS) risk.",
            keywords = listOf("hours", "empty", "frequency", "time", "tss")
        ),
        FaqItemData(
            question = "Can I use a menstrual cup if I have an IUD?",
            category = "Safety & Hygiene",
            answer = "Yes, but with extra caution! Always break the vacuum seal completely before pulling the cup down to avoid dislodging the IUD strings. You can also ask your gynecologist to trim the IUD strings slightly shorter so they do not get caught under the rim.",
            keywords = listOf("iud", "strings", "birth control", "dislodge")
        ),
        FaqItemData(
            question = "Does inserting the cup hurt?",
            category = "Usage & Fit",
            answer = "It shouldn't hurt, but there may be minor discomfort when you are first learning. Using a small drop of water or water-based lubricant on the rim of the folded cup can make insertion incredibly smooth. Remember to breathe and relax your pelvic muscles.",
            keywords = listOf("pain", "hurt", "lubricant", "discomfort", "tight")
        ),
        FaqItemData(
            question = "How do I clean my cup in a public restroom?",
            category = "Safety & Hygiene",
            answer = "Wash your hands first before entering the stall. Take a small water bottle inside the stall with you. Remove the cup, empty it into the toilet, and rinse it over the toilet bowl using the water bottle. Alternatively, simply wipe the cup clean with a sterile, fragrance-free wet wipe and reinsert, then wash it thoroughly when you get home.",
            keywords = listOf("public", "restroom", "toilet", "rinse", "travel")
        ),
        FaqItemData(
            question = "What should I do if my cup is leaking?",
            category = "Troubleshooting",
            answer = "Leaks usually mean the cup didn't seal properly, is positioned incorrectly, or is full. Double check that the cup has popped open fully, and try aiming it slightly backward towards your tailbone instead of straight up. Also, make sure the tiny air holes near the rim are completely clear and clean.",
            keywords = listOf("leak", "spill", "spotting", "holes")
        )
    )

    val proTips = listOf(
        InstructionalTip(
            title = "Cup Sizing Advice",
            icon = Icons.Default.Straighten,
            description = "Small cups are typically recommended for users under 30 who haven't given birth vaginally. Large cups are for users over 30 or who have given birth vaginally. Cervix height also matters: if your cervix is very low or high, search for custom short-body or long-body cup designs.",
            color = MutedRosePrimary
        ),
        InstructionalTip(
            title = "Trimming the Stem",
            icon = Icons.Default.ContentCut,
            description = "If the silicone stem of the cup is poking you or sticking out, causing localized skin irritation, you can safely trim it! Remove the cup first, use sanitized scissors to cut a small section of the stem, and reinsert. Never trim it while inside you.",
            color = LavenderSecondary
        ),
        InstructionalTip(
            title = "The Metal Whisk Boil",
            icon = Icons.Default.Whatshot,
            description = "When boiling your cup to sterilize, place it inside a clean metal kitchen whisk. This suspends the cup in the boiling water and prevents the silicone from touching the hot metal bottom of the pot, which can warp or melt the silicone.",
            color = SuccessGreen
        )
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Section Header
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = "Clinical Menstrual Cup FAQ & Tips 💬",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Learn best practices, troubleshoot leaks, and master cup comfort with medical-grade guidance.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search FAQ (e.g. leak, lost, IUD, pain...)") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(Icons.Default.Close, contentDescription = "Clear search")
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("faq_search_input"),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
            )
        )

        // FAQ Category Filter Row
        val categories = listOf("All", "Usage & Fit", "Safety & Hygiene", "Troubleshooting")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            categories.forEach { category ->
                val isSelected = selectedFaqCategory == category
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                            else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                        )
                        .border(
                            width = if (isSelected) 1.2.dp else 1.dp,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable { selectedFaqCategory = category }
                        .padding(vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = category.split(" ")[0], // Use short title
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Toggleable FAQ List
        val filteredFaqs = faqs.filter { faq ->
            val matchesCategory = selectedFaqCategory == "All" || faq.category == selectedFaqCategory
            val matchesSearch = searchQuery.isBlank() || 
                    faq.question.contains(searchQuery, ignoreCase = true) || 
                    faq.answer.contains(searchQuery, ignoreCase = true) ||
                    faq.keywords.any { it.contains(searchQuery, ignoreCase = true) }
            matchesCategory && matchesSearch
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Toggle FAQs:",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (filteredFaqs.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.15f))
                ) {
                    Box(modifier = Modifier.padding(16.dp), contentAlignment = Alignment.Center) {
                        Text(
                            text = "No matching FAQ items found.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                filteredFaqs.forEach { faq ->
                    val isExpanded = expandedStates[faq.question] == true
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expandedStates[faq.question] = !isExpanded }
                            .testTag("faq_card_${faq.question.hashCode()}"),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isExpanded) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.15f)
                            else MaterialTheme.colorScheme.surface
                        ),
                        border = BorderStroke(
                            width = 1.dp,
                            color = if (isExpanded) MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)
                            else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                        )
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    modifier = Modifier.weight(0.9f),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(20.dp)
                                            .clip(CircleShape)
                                            .background(
                                                if (isExpanded) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                                                else MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "?",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isExpanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSecondaryContainer
                                        )
                                    }
                                    Text(
                                        text = faq.question,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isExpanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                Icon(
                                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            AnimatedVisibility(
                                visible = isExpanded,
                                enter = expandVertically() + fadeIn(),
                                exit = shrinkVertically() + fadeOut()
                            ) {
                                Column(modifier = Modifier.padding(top = 10.dp)) {
                                    HorizontalDivider(
                                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    )
                                    Text(
                                        text = faq.answer,
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        lineHeight = 17.sp
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Box(
                                        modifier = Modifier
                                            .background(
                                                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.15f),
                                                shape = RoundedCornerShape(4.dp)
                                            )
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = faq.category,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.secondary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))

        // Instructional Pro Tips
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Clinical Instructional Tips:",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            proTips.forEach { tip ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = tip.color.copy(alpha = 0.04f)),
                    border = BorderStroke(1.dp, tip.color.copy(alpha = 0.15f))
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(tip.color.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = tip.icon,
                                contentDescription = null,
                                tint = tip.color,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                text = tip.title,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = tip.color
                            )
                            Text(
                                text = tip.description,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 15.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

