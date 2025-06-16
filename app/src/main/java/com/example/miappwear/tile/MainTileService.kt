package com.example.miappwear.tile

import android.content.Context
import androidx.wear.protolayout.ColorBuilders.argb
import androidx.wear.protolayout.LayoutElementBuilders
import androidx.wear.protolayout.ResourceBuilders
import androidx.wear.protolayout.TimelineBuilders
import androidx.wear.protolayout.material.Colors
import androidx.wear.protolayout.material.Text
import androidx.wear.protolayout.material.Typography
import androidx.wear.protolayout.material.layouts.PrimaryLayout
import androidx.wear.tiles.RequestBuilders
import androidx.wear.tiles.TileBuilders
import androidx.wear.tiles.tooling.preview.Preview
import androidx.wear.tiles.tooling.preview.TilePreviewData
import androidx.wear.tooling.preview.devices.WearDevices
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.tiles.SuspendingTileService
import com.example.miappwear.data.HealthRepository
import kotlinx.coroutines.flow.first

private const val RESOURCES_VERSION = "0"

/**
 * Tile que muestra un resumen de salud con pasos e hidratación
 */
@OptIn(ExperimentalHorologistApi::class)
class MainTileService : SuspendingTileService() {

    override suspend fun resourcesRequest(
        requestParams: RequestBuilders.ResourcesRequest
    ) = resources(requestParams)

    override suspend fun tileRequest(
        requestParams: RequestBuilders.TileRequest
    ) = tile(requestParams, this)
}

private fun resources(
    requestParams: RequestBuilders.ResourcesRequest
): ResourceBuilders.Resources {
    return ResourceBuilders.Resources.Builder()
        .setVersion(RESOURCES_VERSION)
        .build()
}

private suspend fun tile(
    requestParams: RequestBuilders.TileRequest,
    context: Context,
): TileBuilders.Tile {
    val singleTileTimeline = TimelineBuilders.Timeline.Builder()
        .addTimelineEntry(
            TimelineBuilders.TimelineEntry.Builder()
                .setLayout(
                    LayoutElementBuilders.Layout.Builder()
                        .setRoot(tileLayout(requestParams, context))
                        .build()
                )
                .build()
        )
        .build()

    return TileBuilders.Tile.Builder()
        .setResourcesVersion(RESOURCES_VERSION)
        .setTileTimeline(singleTileTimeline)
        .build()
}

private suspend fun tileLayout(
    requestParams: RequestBuilders.TileRequest,
    context: Context,
): LayoutElementBuilders.LayoutElement {
    // Obtener datos de salud
    val healthRepository = HealthRepository(context)
    val healthMetrics = healthRepository.healthMetrics.first()

    val steps = healthMetrics.stepData.steps
    val hydration = healthMetrics.hydrationData.glassesConsumed

    val stepsText = when {
        steps >= 1000 -> {
            val stepsInK = steps / 1000.0
            if (stepsInK % 1 == 0.0) {
                "${stepsInK.toInt()}K pasos"
            } else {
                "${String.format("%.1f", stepsInK)}K pasos"
            }
        }
        else -> "$steps pasos"
    }

    val hydrationText = "$hydration vasos"

    return PrimaryLayout.Builder(requestParams.deviceConfiguration)
        .setResponsiveContentInsetEnabled(true)
        .setPrimaryLabelTextContent(
            Text.Builder(context, "Mi Salud")
                .setColor(argb(Colors.DEFAULT.onSurface))
                .setTypography(Typography.TYPOGRAPHY_CAPTION2)
                .build()
        )
        .setContent(
            LayoutElementBuilders.Column.Builder()
                .addContent(
                    Text.Builder(context, stepsText)
                        .setColor(argb(0xFF4CAF50.toInt()))
                        .setTypography(Typography.TYPOGRAPHY_BODY1)
                        .build()
                )
                .addContent(
                    Text.Builder(context, hydrationText)
                        .setColor(argb(0xFF2196F3.toInt()))
                        .setTypography(Typography.TYPOGRAPHY_BODY2)
                        .build()
                )
                .build()
        )
        .build()
}

@Preview(device = WearDevices.SMALL_ROUND)
@Preview(device = WearDevices.LARGE_ROUND)
fun tilePreview(context: Context) = TilePreviewData(::resources) { requestParams ->
    // Para el preview, usamos datos estáticos
    val singleTileTimeline = TimelineBuilders.Timeline.Builder()
        .addTimelineEntry(
            TimelineBuilders.TimelineEntry.Builder()
                .setLayout(
                    LayoutElementBuilders.Layout.Builder()
                        .setRoot(staticTileLayout(requestParams, context))
                        .build()
                )
                .build()
        )
        .build()

    TileBuilders.Tile.Builder()
        .setResourcesVersion(RESOURCES_VERSION)
        .setTileTimeline(singleTileTimeline)
        .build()
}

private fun staticTileLayout(
    requestParams: RequestBuilders.TileRequest,
    context: Context,
): LayoutElementBuilders.LayoutElement {
    return PrimaryLayout.Builder(requestParams.deviceConfiguration)
        .setResponsiveContentInsetEnabled(true)
        .setPrimaryLabelTextContent(
            Text.Builder(context, "Mi Salud")
                .setColor(argb(Colors.DEFAULT.onSurface))
                .setTypography(Typography.TYPOGRAPHY_CAPTION2)
                .build()
        )
        .setContent(
            LayoutElementBuilders.Column.Builder()
                .addContent(
                    Text.Builder(context, "8.5K pasos")
                        .setColor(argb(0xFF4CAF50.toInt()))
                        .setTypography(Typography.TYPOGRAPHY_BODY1)
                        .build()
                )
                .addContent(
                    Text.Builder(context, "6 vasos")
                        .setColor(argb(0xFF2196F3.toInt()))
                        .setTypography(Typography.TYPOGRAPHY_BODY2)
                        .build()
                )
                .build()
        )
        .build()
}