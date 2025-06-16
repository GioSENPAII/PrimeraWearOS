package com.example.miappwear.complication

import androidx.wear.watchface.complications.data.ComplicationData
import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.complications.data.PlainComplicationText
import androidx.wear.watchface.complications.data.ShortTextComplicationData
import androidx.wear.watchface.complications.datasource.ComplicationRequest
import androidx.wear.watchface.complications.datasource.SuspendingComplicationDataSourceService
import com.example.miappwear.data.HealthRepository
import kotlinx.coroutines.flow.first

/**
 * Complicación que muestra el conteo de pasos actual
 */
class MainComplicationService : SuspendingComplicationDataSourceService() {

    override fun getPreviewData(type: ComplicationType): ComplicationData? {
        if (type != ComplicationType.SHORT_TEXT) {
            return null
        }
        return createComplicationData("8.5K", "8,500 pasos hoy")
    }

    override suspend fun onComplicationRequest(request: ComplicationRequest): ComplicationData {
        val healthRepository = HealthRepository(this)
        val healthMetrics = healthRepository.healthMetrics.first()

        val steps = healthMetrics.stepData.steps
        val stepsText = when {
            steps >= 1000 -> "${(steps / 1000.0).let { if (it % 1 == 0.0) it.toInt().toString() else "%.1f".format(it) }}K"
            else -> steps.toString()
        }

        val contentDescription = "$steps pasos hoy"

        return createComplicationData(stepsText, contentDescription)
    }

    private fun createComplicationData(text: String, contentDescription: String) =
        ShortTextComplicationData.Builder(
            text = PlainComplicationText.Builder(text).build(),
            contentDescription = PlainComplicationText.Builder(contentDescription).build()
        ).build()
}