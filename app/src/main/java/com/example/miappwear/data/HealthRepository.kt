package com.example.miappwear.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.*

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "health_prefs")

class HealthRepository(private val context: Context) {

    companion object {
        val STEPS_KEY = intPreferencesKey("steps")
        val STEPS_GOAL_KEY = intPreferencesKey("steps_goal")
        val HYDRATION_GLASSES_KEY = intPreferencesKey("hydration_glasses")
        val HYDRATION_GOAL_KEY = intPreferencesKey("hydration_goal")
        val LAST_DRINK_TIME_KEY = longPreferencesKey("last_drink_time")
        val LAST_ACTIVITY_TIME_KEY = longPreferencesKey("last_activity_time")
        val REMINDER_ENABLED_KEY = booleanPreferencesKey("reminder_enabled")
        val LAST_RESET_DATE_KEY = longPreferencesKey("last_reset_date")
    }

    val healthMetrics: Flow<HealthMetrics> = context.dataStore.data.map { preferences ->
        val currentDate = getCurrentDateString()
        val lastResetDate = preferences[LAST_RESET_DATE_KEY] ?: 0
        val lastResetDateString = if (lastResetDate > 0)
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(lastResetDate))
        else ""

        // Resetear datos diarios si es un nuevo día
        val shouldReset = lastResetDateString != currentDate

        HealthMetrics(
            stepData = StepData(
                steps = if (shouldReset) 0 else (preferences[STEPS_KEY] ?: 0),
                goal = preferences[STEPS_GOAL_KEY] ?: 10000
            ),
            hydrationData = HydrationData(
                glassesConsumed = if (shouldReset) 0 else (preferences[HYDRATION_GLASSES_KEY] ?: 0),
                dailyGoal = preferences[HYDRATION_GOAL_KEY] ?: 8,
                lastDrinkTime = preferences[LAST_DRINK_TIME_KEY] ?: 0
            ),
            activityReminder = ActivityReminderData(
                lastActivityTime = preferences[LAST_ACTIVITY_TIME_KEY] ?: System.currentTimeMillis(),
                isReminderEnabled = preferences[REMINDER_ENABLED_KEY] ?: true
            )
        )
    }

    suspend fun updateSteps(steps: Int) {
        context.dataStore.edit { preferences ->
            checkAndResetDailyData(preferences)
            preferences[STEPS_KEY] = steps
        }
    }

    suspend fun setStepGoal(goal: Int) {
        context.dataStore.edit { preferences ->
            preferences[STEPS_GOAL_KEY] = goal
        }
    }

    suspend fun addWaterGlass() {
        context.dataStore.edit { preferences ->
            checkAndResetDailyData(preferences)
            val current = preferences[HYDRATION_GLASSES_KEY] ?: 0
            preferences[HYDRATION_GLASSES_KEY] = current + 1
            preferences[LAST_DRINK_TIME_KEY] = System.currentTimeMillis()
        }
    }

    suspend fun setHydrationGoal(goal: Int) {
        context.dataStore.edit { preferences ->
            preferences[HYDRATION_GOAL_KEY] = goal
        }
    }

    suspend fun updateLastActivityTime() {
        context.dataStore.edit { preferences ->
            preferences[LAST_ACTIVITY_TIME_KEY] = System.currentTimeMillis()
        }
    }

    suspend fun setReminderEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[REMINDER_ENABLED_KEY] = enabled
        }
    }

    private suspend fun checkAndResetDailyData(preferences: androidx.datastore.preferences.core.MutablePreferences) {
        val currentDate = getCurrentDateString()
        val lastResetDate = preferences[LAST_RESET_DATE_KEY] ?: 0
        val lastResetDateString = if (lastResetDate > 0)
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(lastResetDate))
        else ""

        if (lastResetDateString != currentDate) {
            // Nuevo día, resetear datos diarios
            preferences[STEPS_KEY] = 0
            preferences[HYDRATION_GLASSES_KEY] = 0
            preferences[LAST_RESET_DATE_KEY] = System.currentTimeMillis()
        }
    }

    private fun getCurrentDateString(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }
}