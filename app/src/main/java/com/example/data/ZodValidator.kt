package com.example.data

object ZodValidator {
    data class ZodError(val field: String, val message: String)
    data class ZodResult<T>(val success: Boolean, val data: T? = null, val errors: List<ZodError> = emptyList()) {
        fun getFieldError(field: String): String? = errors.find { it.field == field }?.message
    }

    data class ReminderSchema(
        val title: String,
        val reminderType: String,
        val reminderTime: String,
        val repeatRule: String,
        val notes: String?
    )

    fun validateReminder(
        title: String,
        reminderType: String,
        reminderTime: String,
        repeatRule: String,
        notes: String?
    ): ZodResult<ReminderSchema> {
        val errors = mutableListOf<ZodError>()

        // 1. Title validation: minimum 3 chars, max 50 chars
        val trimmedTitle = title.trim()
        if (trimmedTitle.isEmpty()) {
            errors.add(ZodError("title", "Title schema: Required, cannot be blank"))
        } else if (trimmedTitle.length < 3) {
            errors.add(ZodError("title", "Title schema: Must be at least 3 characters"))
        } else if (trimmedTitle.length > 50) {
            errors.add(ZodError("title", "Title schema: Cannot exceed 50 characters"))
        }

        // 2. Type validation
        val validTypes = listOf("MEDICINE", "DOCTOR_APPOINTMENT", "WATER", "CUP_CLEANING", "PAD_CHANGE", "SELF_CARE")
        if (reminderType.trim().isEmpty()) {
            errors.add(ZodError("reminderType", "Type schema: Required"))
        } else if (!validTypes.contains(reminderType)) {
            errors.add(ZodError("reminderType", "Type schema: Invalid care type category selected"))
        }

        // 3. Time validation (HH:MM format, valid numbers)
        val timeTrim = reminderTime.trim()
        val timeRegex = Regex("^([01]?\\d|2[0-3]):[0-5]\\d$")
        if (timeTrim.isEmpty()) {
            errors.add(ZodError("reminderTime", "Time schema: Required"))
        } else if (!timeTrim.matches(timeRegex)) {
            errors.add(ZodError("reminderTime", "Time schema: Valid 24h format mandatory (HH:MM, e.g., 08:30 or 21:00)"))
        }

        // 4. Repeat rule validation
        val trimmedRule = repeatRule.trim()
        if (trimmedRule.isEmpty()) {
            errors.add(ZodError("repeatRule", "Repeat interval schema: Required"))
        } else if (trimmedRule.length < 3) {
            errors.add(ZodError("repeatRule", "Repeat interval schema: Must state valid recurring rule (e.g. Daily, Weekly)"))
        }

        if (errors.isNotEmpty()) {
            return ZodResult(success = false, errors = errors)
        }

        return ZodResult(
            success = true,
            data = ReminderSchema(
                title = trimmedTitle,
                reminderType = reminderType,
                reminderTime = timeTrim,
                repeatRule = trimmedRule,
                notes = notes?.trim()
            )
        )
    }
}
