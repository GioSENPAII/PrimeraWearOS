package com.example.miappwear.services

import android.app.Service
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import com.example.miappwear.data.HealthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class StepCounterService : Service(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var stepCounterSensor: Sensor? = null
    private var stepDetectorSensor: Sensor? = null
    private lateinit var healthRepository: HealthRepository
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private var initialStepCount = -1
    private var currentStepCount = 0
    private var dailySteps = 0

    override fun onCreate() {
        super.onCreate()
        healthRepository = HealthRepository(this)
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        // Intentar usar el sensor TYPE_STEP_COUNTER primero
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        // Si no está disponible, usar TYPE_STEP_DETECTOR
        if (stepCounterSensor == null) {
            stepDetectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startStepCounting()
        return START_STICKY
    }

    private fun startStepCounting() {
        stepCounterSensor?.let { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI)
        } ?: run {
            stepDetectorSensor?.let { sensor ->
                sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI)
            }
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let { sensorEvent ->
            when (sensorEvent.sensor.type) {
                Sensor.TYPE_STEP_COUNTER -> {
                    // Este sensor cuenta pasos desde el boot del dispositivo
                    if (initialStepCount == -1) {
                        initialStepCount = sensorEvent.values[0].toInt()
                    } else {
                        currentStepCount = sensorEvent.values[0].toInt()
                        dailySteps = currentStepCount - initialStepCount
                        updateStepCount(dailySteps)
                    }
                }
                Sensor.TYPE_STEP_DETECTOR -> {
                    // Este sensor se activa con cada paso detectado
                    dailySteps++
                    updateStepCount(dailySteps)
                }
            }
        }
    }

    private fun updateStepCount(steps: Int) {
        serviceScope.launch {
            healthRepository.updateSteps(steps)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // No se requiere implementación para este caso
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
        serviceScope.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}