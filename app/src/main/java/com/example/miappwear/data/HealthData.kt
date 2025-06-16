package com.example.miappwear.data

import java.util.Date

data class StepData(
    val steps: Int = 0,
    val goal: Int = 10000,
    val timestamp: Long = System.currentTimeMillis()
) {
    val progress: Float
        get() = if (goal > 0) (steps.toFloat() / goal.toFloat()).coerceAtMost(1f) else 0f
}

data class HydrationData(
    val glassesConsumed: Int = 0,
    val dailyGoal: Int = 8,
    val lastDrinkTime: Long = 0,
    val timestamp: Long = System.currentTimeMillis()
) {
    val progress: Float
        get() = if (dailyGoal > 0) (glassesConsumed.toFloat() / dailyGoal.toFloat()).coerceAtMost(1f) else 0f
}

data class ActivityReminderData(
    val lastActivityTime: Long = System.currentTimeMillis(),
    val reminderInterval: Long = 60 * 60 * 1000, // 1 hora en milisegundos
    val isReminderEnabled: Boolean = true
) {
    fun shouldShowReminder(): Boolean {
        return isReminderEnabled && (System.currentTimeMillis() - lastActivityTime) >= reminderInterval
    }
}

data class HealthMetrics(
    val stepData: StepData = StepData(),
    val hydrationData: HydrationData = HydrationData(),
    val activityReminder: ActivityReminderData = ActivityReminderData()
)