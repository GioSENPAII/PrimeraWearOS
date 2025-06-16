package com.example.miappwear.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.material.*
import com.example.miappwear.presentation.components.*
import com.example.miappwear.viewmodel.HealthViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HealthDashboardScreen(
    viewModel: HealthViewModel,
    onNavigateToSteps: () -> Unit,
    onNavigateToHydration: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val healthMetrics by viewModel.healthMetrics.collectAsStateWithLifecycle()
    val showCelebration by viewModel.showCelebration.collectAsStateWithLifecycle()
    val currentTime = remember { SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()) }

    Box(modifier = Modifier.fillMaxSize()) {
        ScalingLazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Header con hora
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    TimeText(
                        timeSource = TimeTextDefaults.timeSource(TimeTextDefaults.timeFormat())
                    )
                    Text(
                        text = "Mi Salud",
                        style = MaterialTheme.typography.title2.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        ),
                        color = MaterialTheme.colors.onBackground,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Resumen rápido de metas
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colors.surface)
                        .padding(12.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Progreso de Hoy",
                            style = MaterialTheme.typography.body2.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = MaterialTheme.colors.onSurface
                        )

                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Mini indicador de pasos
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CircularProgressIndicator(
                                    progress = healthMetrics.stepData.progress,
                                    modifier = Modifier.size(40.dp),
                                    strokeWidth = 4f,
                                    color = Color(0xFF4CAF50)
                                )
                                Text(
                                    text = "${healthMetrics.stepData.steps}",
                                    style = MaterialTheme.typography.body2.copy(fontSize = 8.sp),
                                    color = MaterialTheme.colors.onSurface
                                )
                                Text(
                                    text = "pasos",
                                    style = MaterialTheme.typography.body2.copy(fontSize = 6.sp),
                                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                                )
                            }

                            // Mini indicador de hidratación
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CircularProgressIndicator(
                                    progress = healthMetrics.hydrationData.progress,
                                    modifier = Modifier.size(40.dp),
                                    strokeWidth = 4f,
                                    color = Color(0xFF2196F3)
                                )
                                Text(
                                    text = "${healthMetrics.hydrationData.glassesConsumed}",
                                    style = MaterialTheme.typography.body2.copy(fontSize = 8.sp),
                                    color = MaterialTheme.colors.onSurface
                                )
                                Text(
                                    text = "vasos",
                                    style = MaterialTheme.typography.body2.copy(fontSize = 6.sp),
                                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }
                }
            }

            // Botón de pasos
            item {
                MetricCard(
                    title = "Pasos",
                    value = "${healthMetrics.stepData.steps}",
                    subtitle = "Meta: ${healthMetrics.stepData.goal}",
                    icon = "🚶",
                    progress = healthMetrics.stepData.progress,
                    progressColor = Color(0xFF4CAF50),
                    onClick = onNavigateToSteps
                )
            }

            // Botón de hidratación
            item {
                MetricCard(
                    title = "Hidratación",
                    value = "${healthMetrics.hydrationData.glassesConsumed}",
                    subtitle = "Meta: ${healthMetrics.hydrationData.dailyGoal} vasos",
                    icon = "💧",
                    progress = healthMetrics.hydrationData.progress,
                    progressColor = Color(0xFF2196F3),
                    onClick = onNavigateToHydration
                )
            }

            // Acciones rápidas
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Acciones Rápidas",
                        style = MaterialTheme.typography.body2.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = MaterialTheme.colors.onBackground,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        QuickActionChip(
                            text = "Agua",
                            icon = "➕",
                            onClick = { viewModel.addWaterGlass() },
                            modifier = Modifier.weight(1f)
                        )

                        QuickActionChip(
                            text = "Actividad",
                            icon = "🏋️",
                            onClick = { viewModel.recordActivity() },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Botón de configuración
            item {
                ActionButton(
                    text = "⚙️ Configuración",
                    onClick = onNavigateToSettings,
                    backgroundColor = MaterialTheme.colors.surface
                )
            }

            // Recordatorio de actividad si es necesario
            if (healthMetrics.activityReminder.shouldShowReminder()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFFF9800).copy(alpha = 0.8f))
                            .padding(12.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "🔔",
                                fontSize = 16.sp
                            )
                            Text(
                                text = "¡Hora de moverse!",
                                style = MaterialTheme.typography.body2.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "Toma un descanso activo",
                                style = MaterialTheme.typography.body2.copy(fontSize = 10.sp),
                                color = Color.White.copy(alpha = 0.9f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }

        // Overlay de celebración
        CelebrationOverlay(
            isVisible = showCelebration,
            onDismiss = { viewModel.dismissCelebration() },
            message = "¡Meta alcanzada! 🎉"
        )
    }
}