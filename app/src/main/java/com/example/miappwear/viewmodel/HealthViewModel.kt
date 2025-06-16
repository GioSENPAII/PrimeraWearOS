package com.example.miappwear.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.miappwear.data.HealthMetrics
import com.example.miappwear.data.HealthRepository
import com.example.miappwear.services.StepCounterService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HealthViewModel(application: Application) : AndroidViewModel(application) {

    private val healthRepository = HealthRepository(application)
    private val vibrator = application.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    private val _healthMetrics = MutableStateFlow(HealthMetrics())
    val healthMetrics: StateFlow<HealthMetrics> = _healthMetrics.asStateFlow()

    private val _showCelebration = MutableStateFlow(false)
    val showCelebration: StateFlow<Boolean> = _showCelebration.asStateFlow()

    init {
        startStepCounterService()
        observeHealthMetrics()
    }

    private fun startStepCounterService() {
        val intent = Intent(getApplication(), StepCounterService::class.java)
        getApplication<Application>().startService(intent)
    }

    private fun observeHealthMetrics() {
        viewModelScope.launch {
            healthRepository.healthMetrics.collect { metrics ->
                val previousSteps = _healthMetrics.value.stepData.steps
                _healthMetrics.value = metrics

                // Verificar si se alcanzó la meta de pasos
                if (previousSteps < metrics.stepData.goal && metrics.stepData.steps >= metrics.stepData.goal) {
                    celebrateGoalAchievement()
                }
            }
        }
    }

    fun addWaterGlass() {
        viewModelScope.launch {
            healthRepository.addWaterGlass()
            vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))

            // Verificar si se alcanzó la meta de hidratación
            val current = _healthMetrics.value.hydrationData.glassesConsumed + 1
            val goal = _healthMetrics.value.hydrationData.dailyGoal
            if (current >= goal) {
                celebrateGoalAchievement()
            }
        }
    }

    fun recordActivity() {
        viewModelScope.launch {
            healthRepository.updateLastActivityTime()
            vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
        }
    }

    fun setStepGoal(goal: Int) {
        viewModelScope.launch {
            healthRepository.setStepGoal(goal)
        }
    }

    fun setHydrationGoal(goal: Int) {
        viewModelScope.launch {
            healthRepository.setHydrationGoal(goal)
        }
    }

    fun toggleReminders(enabled: Boolean) {
        viewModelScope.launch {
            healthRepository.setReminderEnabled(enabled)
        }
    }

    private fun celebrateGoalAchievement() {
        _showCelebration.value = true
        vibrate(VibrationEffect.createWaveform(longArrayOf(0, 200, 100, 200, 100, 200), -1))

        // Ocultar celebración después de 3 segundos
        viewModelScope.launch {
            kotlinx.coroutines.delay(3000)
            _showCelebration.value = false
        }
    }

    private fun vibrate(effect: VibrationEffect) {
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(effect)
        }
    }

    fun dismissCelebration() {
        _showCelebration.value = false
    }
}