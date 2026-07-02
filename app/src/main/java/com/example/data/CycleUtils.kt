package com.example.data

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

object CycleUtils {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private val displayFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.US)
    private val shortDisplayFormatter = DateTimeFormatter.ofPattern("MMM d", Locale.US)

    fun getTodayString(): String {
        return LocalDate.now().format(formatter)
    }

    fun parseDate(dateStr: String): LocalDate {
        return try {
            LocalDate.parse(dateStr, formatter)
        } catch (e: Exception) {
            LocalDate.now()
        }
    }

    fun formatDate(date: LocalDate): String {
        return date.format(formatter)
    }

    fun formatDisplayDate(dateStr: String): String {
        return try {
            val date = LocalDate.parse(dateStr, formatter)
            date.format(displayFormatter)
        } catch (e: Exception) {
            dateStr
        }
    }

    fun formatShortDate(dateStr: String): String {
        return try {
            val date = LocalDate.parse(dateStr, formatter)
            date.format(shortDisplayFormatter)
        } catch (e: Exception) {
            dateStr
        }
    }

    fun getDaysBetween(startStr: String, endStr: String): Long {
        return try {
            val start = LocalDate.parse(startStr, formatter)
            val end = LocalDate.parse(endStr, formatter)
            ChronoUnit.DAYS.between(start, end)
        } catch (e: Exception) {
            0
        }
    }

    fun addDays(dateStr: String, days: Int): String {
        return try {
            val date = LocalDate.parse(dateStr, formatter)
            date.plusDays(days.toLong()).format(formatter)
        } catch (e: Exception) {
            dateStr
        }
    }

    fun getCurrentCycleDay(lastPeriodStart: String, todayStr: String = getTodayString()): Int {
        val days = getDaysBetween(lastPeriodStart, todayStr)
        return (days + 1).toInt()
    }

    fun getPredictedNextPeriod(lastPeriodStart: String, cycleLength: Int): String {
        return addDays(lastPeriodStart, cycleLength)
    }

    fun getCyclePhase(cycleDay: Int, cycleLength: Int, periodLength: Int): String {
        return when {
            cycleDay in 1..periodLength -> "Menstrual Phase"
            cycleDay in (periodLength + 1)..(cycleLength / 2 - 2) -> "Follicular Phase"
            cycleDay in (cycleLength / 2 - 1)..(cycleLength / 2 + 1) -> "Ovulatory Phase"
            else -> "Luteal Phase"
        }
    }

    fun getPhaseDescription(phase: String): String {
        return when (phase) {
            "Menstrual Phase" -> "Your body is shedding the uterine lining. Rest and gentle heat are your best companions."
            "Follicular Phase" -> "Estrogen rises, energy builds up. A perfect phase for learning, socializing, and creative ideas."
            "Ovulatory Phase" -> "Peak hormonal phase. You may feel highly confident, communicative, and physically active."
            "Luteal Phase" -> "Progesterone takes over, soothing the body. PMS may develop. Focus on warm comforts and private calm."
            else -> "A peaceful phase of your cycle."
        }
    }

    data class CycleInfoResult(
        val currentCycleDay: Int?,
        val nextPeriodDate: String?,
        val message: String
    )

    fun calculateCycleInfo(lastPeriodStartDate: String?, averageCycleLength: Int?): CycleInfoResult {
        if (lastPeriodStartDate.isNullOrEmpty() || averageCycleLength == null || averageCycleLength < 20 || averageCycleLength > 45) {
            return CycleInfoResult(
                currentCycleDay = null,
                nextPeriodDate = null,
                message = "Add valid cycle information to see predictions."
            )
        }

        return try {
            val start = java.time.LocalDate.parse(lastPeriodStartDate, formatter)
            val today = java.time.LocalDate.now()

            val daysSinceStart = java.time.temporal.ChronoUnit.DAYS.between(start, today)

            if (daysSinceStart < 0) {
                return CycleInfoResult(
                    currentCycleDay = null,
                    nextPeriodDate = null,
                    message = "Last period date cannot be in the future."
                )
            }

            val cyclesPassed = (daysSinceStart / averageCycleLength).toInt()
            val currentCycleDay = (daysSinceStart % averageCycleLength).toInt() + 1

            val nextDate = start.plusDays(((cyclesPassed + 1) * averageCycleLength).toLong())

            CycleInfoResult(
                currentCycleDay = currentCycleDay,
                nextPeriodDate = nextDate.format(formatter),
                message = "Prediction is an estimate."
            )
        } catch (e: Exception) {
            CycleInfoResult(
                currentCycleDay = null,
                nextPeriodDate = null,
                message = "Invalid period start date."
            )
        }
    }

    data class PredictionResult(
        val nextPeriodDate: String?,
        val predictedCycleLength: Int,
        val isBasedOnHistory: Boolean,
        val historicalCycleCount: Int,
        val cycleLengthsUsed: List<Int>,
        val currentCycleDay: Int?,
        val daysUntil: Long?,
        val phase: String,
        val isOverdue: Boolean,
        val overdueDays: Long
    )

    fun predictNextPeriod(
        periodLogs: List<PeriodLog>,
        profileAverageLength: Int,
        profilePeriodLength: Int,
        today: LocalDate = LocalDate.now()
    ): PredictionResult {
        val sortedDates = periodLogs
            .mapNotNull { 
                try { LocalDate.parse(it.startDate, formatter) } catch (e: Exception) { null } 
            }
            .distinct()
            .sorted()

        val cycleStarts = mutableListOf<LocalDate>()
        for (date in sortedDates) {
            if (cycleStarts.isEmpty() || ChronoUnit.DAYS.between(cycleStarts.last(), date) >= 14) {
                cycleStarts.add(date)
            }
        }

        val cycleLengths = mutableListOf<Int>()
        for (i in 0 until cycleStarts.size - 1) {
            val length = ChronoUnit.DAYS.between(cycleStarts[i], cycleStarts[i + 1]).toInt()
            if (length in 18..45) { // Allow realistic physiological window
                cycleLengths.add(length)
            }
        }

        val isBasedOnHistory = cycleLengths.isNotEmpty()
        val predictedCycleLength = if (isBasedOnHistory) {
            cycleLengths.average().let { if (it.isNaN()) profileAverageLength else kotlin.math.round(it).toInt() }
        } else {
            profileAverageLength.coerceIn(21, 45)
        }

        val latestStart = cycleStarts.lastOrNull()
        if (latestStart == null) {
            return PredictionResult(
                nextPeriodDate = null,
                predictedCycleLength = predictedCycleLength,
                isBasedOnHistory = false,
                historicalCycleCount = 0,
                cycleLengthsUsed = emptyList(),
                currentCycleDay = null,
                daysUntil = null,
                phase = "Unknown",
                isOverdue = false,
                overdueDays = 0L
            )
        }

        val immediateNext = latestStart.plusDays(predictedCycleLength.toLong())
        val isOverdue = today.isAfter(immediateNext)
        val overdueDays = if (isOverdue) ChronoUnit.DAYS.between(immediateNext, today) else 0L
        val currentCycleDay = ChronoUnit.DAYS.between(latestStart, today).toInt() + 1
        val daysUntil = ChronoUnit.DAYS.between(today, immediateNext)

        val cycleDayInLength = if (currentCycleDay > predictedCycleLength) {
            predictedCycleLength
        } else {
            currentCycleDay
        }
        val phase = getCyclePhase(cycleDayInLength, predictedCycleLength, profilePeriodLength)

        return PredictionResult(
            nextPeriodDate = immediateNext.format(formatter),
            predictedCycleLength = predictedCycleLength,
            isBasedOnHistory = isBasedOnHistory,
            historicalCycleCount = cycleStarts.size,
            cycleLengthsUsed = cycleLengths,
            currentCycleDay = currentCycleDay,
            daysUntil = daysUntil,
            phase = phase,
            isOverdue = isOverdue,
            overdueDays = overdueDays
        )
    }

    val crisisKeywords = listOf(
        "suicide",
        "kill myself",
        "hurt myself",
        "don't want to live",
        "dont want to live",
        "end my life",
        "self harm"
    )

    fun detectCrisisText(text: String): Boolean {
        val lower = text.lowercase()
        return crisisKeywords.any { lower.contains(it) }
    }

    fun buildGoogleMapsSearchUrl(query: String, lat: Double? = null, lng: Double? = null): String {
        val encodedQuery = java.net.URLEncoder.encode(query, "UTF-8")
        if (lat != null && lng != null) {
            return "https://www.google.com/maps/search/$encodedQuery/@$lat,$lng,14z"
        }
        return "https://www.google.com/maps/search/$encodedQuery"
    }

    data class FeatureAccess(
        val canTrackCycle: Boolean,
        val canUseMedicalJournal: Boolean,
        val canUseMedicineReminder: Boolean,
        val canUseSupportGuide: Boolean,
        val canUseCareShop: Boolean,
        val canUseHealthEducation: Boolean,
        val canUseMentalHealthTools: Boolean
    )

    fun getFeatureAccess(profile: Profile?): FeatureAccess {
        if (profile == null) {
            return FeatureAccess(
                canTrackCycle = true,
                canUseMedicalJournal = true,
                canUseMedicineReminder = true,
                canUseSupportGuide = false,
                canUseCareShop = true,
                canUseHealthEducation = true,
                canUseMentalHealthTools = true
            )
        }

        val canTrackCycle = profile.userMode == "SELF_TRACKING" && profile.bodyRelevantMode == "MENSTRUATES"
        val canUseMedicalJournal = profile.userMode == "SELF_TRACKING" || profile.sharedTrackingConsent
        val canUseMedicineReminder = profile.userMode == "SELF_TRACKING" || profile.sharedTrackingConsent
        val canUseSupportGuide = profile.userMode == "SUPPORT_MODE" || profile.genderMode == "MALE" || profile.userMode == "EDUCATION_ONLY"
        val canUseCareShop = true
        val canUseHealthEducation = true
        val canUseMentalHealthTools = true

        return FeatureAccess(
            canTrackCycle = canTrackCycle,
            canUseMedicalJournal = canUseMedicalJournal,
            canUseMedicineReminder = canUseMedicineReminder,
            canUseSupportGuide = canUseSupportGuide,
            canUseCareShop = canUseCareShop,
            canUseHealthEducation = canUseHealthEducation,
            canUseMentalHealthTools = canUseMentalHealthTools
        )
    }

    fun calculateBehaviourFlags(entry: BehaviourLog): List<String> {
        val flags = mutableListOf<String>()

        if (entry.stressLevel >= 8) {
            flags.add("HIGH_STRESS")
        }

        if (entry.anxietyLevel >= 8) {
            flags.add("HIGH_ANXIETY")
        }

        if (entry.sleepHours in 0.01..5.0) {
            flags.add("LOW_SLEEP")
        }

        if (entry.painLevel >= 8) {
            flags.add("SEVERE_PAIN")
        }

        if (entry.mood == "VERY_LOW" || entry.mood == "Very low") {
            flags.add("VERY_LOW_MOOD")
        }

        val notesLower = entry.notes?.lowercase() ?: ""
        if (entry.flowLevel.equals("heavy", ignoreCase = true)) {
            if (notesLower.contains("dizzy") || notesLower.contains("dizziness")) {
                flags.add("HEAVY_BLEEDING_WITH_DIZZINESS")
            }
        }

        if (detectCrisisText(notesLower)) {
            flags.add("CRISIS_SUPPORT")
        }

        return flags
    }

    data class BehaviourInsight(
        val type: String, // urgent, warning, support, neutral
        val title: String,
        val message: String,
        val action: String
    )

    fun generateBehaviourInsight(flags: List<String>, userMode: String): BehaviourInsight {
        if (flags.contains("CRISIS_SUPPORT")) {
            return BehaviourInsight(
                type = "urgent",
                title = "You deserve support right now",
                message = "Please contact emergency services, a crisis hotline, or someone you trust immediately.",
                action = "OPEN_CRISIS_SUPPORT"
            )
        }

        if (flags.contains("SEVERE_PAIN")) {
            return BehaviourInsight(
                type = "warning",
                title = "Severe pain logged",
                message = "Severe or unusual pain should be checked by a qualified healthcare professional.",
                action = "OPEN_WARNING_SIGNS"
            )
        }

        if (flags.contains("HEAVY_BLEEDING_WITH_DIZZINESS")) {
            return BehaviourInsight(
                type = "warning",
                title = "Heavy bleeding with dizziness",
                message = "Heavy bleeding with dizziness or weakness may need urgent medical attention.",
                action = "OPEN_WARNING_SIGNS"
            )
        }

        if (flags.contains("HIGH_STRESS") || flags.contains("HIGH_ANXIETY")) {
            return BehaviourInsight(
                type = "support",
                title = "Stress support",
                message = "Try a short grounding exercise, slow breathing, or writing down what feels heavy today.",
                action = "OPEN_SELF_CARE"
            )
        }

        if (flags.contains("LOW_SLEEP")) {
            return BehaviourInsight(
                type = "support",
                title = "Sleep care",
                message = "Low sleep can affect mood and energy. A calm bedtime routine may help tonight.",
                action = "OPEN_SLEEP_CARE"
            )
        }

        if (userMode == "SUPPORT_MODE") {
            return BehaviourInsight(
                type = "support",
                title = "Support with respect",
                message = "Ask what she needs, listen without judging, and offer practical help like water, food, rest, or buying supplies.",
                action = "OPEN_SUPPORT_GUIDE"
            )
        }

        return BehaviourInsight(
            type = "neutral",
            title = "Today’s gentle check-in",
            message = "Track your mood, body symptoms, sleep, and stress to understand your patterns over time.",
            action = "OPEN_CHECK_IN"
        )
    }

    fun generateWeeklyPatterns(logs: List<BehaviourLog>, periodLogs: List<PeriodLog>): List<String> {
        val patterns = mutableListOf<String>()
        if (logs.isEmpty()) {
            return listOf("Start logging daily symptoms to unlock personalized behaviour patterns.")
        }

        val highStressLogs = logs.filter { it.stressLevel >= 7 }
        val lowSleepLogs = logs.filter { it.sleepHours > 0 && it.sleepHours < 6 }
        val highStressAndLowSleep = logs.filter { it.stressLevel >= 7 && it.sleepHours > 0 && it.sleepHours < 6 }

        if (highStressAndLowSleep.isNotEmpty() || (highStressLogs.size >= 2 && lowSleepLogs.size >= 2)) {
            patterns.add("You logged higher stress levels on days with fewer sleep hours. A steady calming wind-down routine might assist in breaking this pattern.")
        }

        val highPainLogs = logs.filter { it.painLevel >= 6 }
        if (highPainLogs.isNotEmpty() && periodLogs.isNotEmpty()) {
            patterns.add("You logged higher physical discomfort/pain around your period days. This pattern may be worth discussing with a healthcare professional.")
        }

        val lowMoodLogs = logs.filter { it.mood == "LOW" || it.mood == "VERY_LOW" || it.mood == "Low" || it.mood == "Very low" }
        if (lowMoodLogs.size >= 2) {
            patterns.add("You logged lower emotional vibes before or during menstruating days. This pattern is natural, but feel free to record your personal notes for reference.")
        }

        val helpfulDays = logs.filter { (it.hydrationLevel.lowercase().contains("adequate") || it.hydrationLevel.lowercase().contains("high") || it.hydrationLevel.replace("[^\\d]".toRegex(), "").toIntOrNull() ?: 0 >= 6) && it.energyLevel >= 6 }
        if (helpfulDays.isNotEmpty()) {
            patterns.add("You may notice you often log better energy and mood levels on days with more hydration or restful cycles.")
        }

        if (patterns.isEmpty()) {
            patterns.add("Consistent logging helps us analyze cycle connections. Keep tracking daily stress, sleeping hours, and discomfort.")
        }

        return patterns
    }

    fun getSupportSuggestions(relationship: String, selectedFocuses: List<String>): List<String> {
        val base = listOf(
            "Ask what she needs instead of assuming.",
            "Respect privacy. Do not pressure her to share symptoms.",
            "Offer practical help such as water, food, rest, heat pad, or buying supplies.",
            "Avoid jokes, shame, or comments about mood.",
            "Encourage professional help for severe pain, heavy bleeding, fever, or unusual symptoms."
        )

        val relationshipTips = mapOf(
            "WIFE" to listOf(
                "Share household tasks when she is tired or in pain.",
                "Support doctor visits if she wants company."
            ),
            "MOTHER" to listOf(
                "Speak respectfully and offer help without making her feel weak.",
                "Ask before buying products or medicines."
            ),
            "DAUGHTER" to listOf(
                "Use calm, age-appropriate language.",
                "Respect her privacy and help her access safe products."
            ),
            "GIRLFRIEND" to listOf(
                "Be patient and avoid taking mood changes personally.",
                "Offer comfort without forcing advice."
            ),
            "FEMALE_PARTNER" to listOf(
                "Ask how she prefers to be supported.",
                "Respect boundaries and privacy."
            ),
            "SISTER" to listOf(
                "Offer practical help and avoid teasing."
            ),
            "FRIEND" to listOf(
                "Listen and support without asking intrusive questions."
            ),
            "OTHER" to listOf(
                "Keep support respectful, private, and consent-based."
            )
        )

        val relationSpecific = relationshipTips[relationship.uppercase()] ?: relationshipTips["OTHER"]!!
        return base + relationSpecific
    }

    data class Microcopy(
        val cycleTitle: String,
        val moodTitle: String,
        val journalTitle: String,
        val careTitle: String
    )

    fun getMicrocopy(userMode: String): Microcopy {
        if (userMode == "SELF_TRACKING") {
            return Microcopy(
                cycleTitle = "Your cycle",
                moodTitle = "Your mood check-in",
                journalTitle = "Your private journal",
                careTitle = "Care suggestions for you"
            )
        }

        if (userMode == "SUPPORT_MODE") {
            return Microcopy(
                cycleTitle = "Period health education",
                moodTitle = "Emotional support guide",
                journalTitle = "Your support notes",
                careTitle = "Helpful care items"
            )
        }

        return Microcopy(
            cycleTitle = "Period health basics",
            moodTitle = "Mental wellbeing basics",
            journalTitle = "Learning notes",
            careTitle = "Care product guide"
        )
    }
}
