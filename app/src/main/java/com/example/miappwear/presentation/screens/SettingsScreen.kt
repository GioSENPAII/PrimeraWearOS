package com.example.miappwear.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.example.miappwear.viewmodel.HealthViewModel

@Composable
fun SettingsScreen(
    viewModel: HealthViewModel,
    onBackPressed: () -> Unit
) {
    val healthMetrics by viewModel.healthMetrics.collectAsStateWithLifecycle()
    var showStepGoalDialog by remember { mutableStateOf(false) }
    var showHydrationGoalDialog by remember { mutableStateOf(false) }
    var showInfoDialog by remember { mutableStateOf(false) }

    if (showStepGoalDialog) {
        SettingsStepGoalDialog(
            currentGoal = healthMetrics.stepData.goal,
            onGoalSet = { newGoal ->
                viewModel.setStepGoal(newGoal)
                showStepGoalDialog = false
            },
            onDismiss = { showStepGoalDialog = false }
        )
    } else if (showHydrationGoalDialog) {
        SettingsHydrationGoalDialog(
            currentGoal = healthMetrics.hydrationData.dailyGoal,
            onGoalSet = { newGoal ->
                viewModel.setHydrationGoal(newGoal)
                showHydrationGoalDialog = false
            },
            onDismiss = { showHydrationGoalDialog = false }
        )
    } else if (showInfoDialog) {
        SettingsAppInfoDialog(
            onDismiss = { showInfoDialog = false }
        )
    } else {
        ScalingLazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Header
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Configuración",
                        style = MaterialTheme.typography.title2.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colors.onBackground
                    )
                    Text(
                        text = "Personaliza tu experiencia",
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onBackground.copy(alpha = 0.7f)
                    )
                }
            }

            // Meta de pasos
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colors.surface)
                        .clickable { showStepGoalDialog = true }
                        .padding(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Text(
                                text = "🚶 Meta de Pasos",
                                style = MaterialTheme.typography.body2.copy(
                                    fontWeight = FontWeight.Medium
                                ),
                                color = MaterialTheme.colors.onSurface
                            )
                            Text(
                                text = "${healthMetrics.stepData.goal} pasos",
                                style = MaterialTheme.typography.body2,
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                            )
                        }
                        Text(
                            text = ">",
                            style = MaterialTheme.typography.body1,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
                        )
                    }
                }
            }

            // Meta de hidratación
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colors.surface)
                        .clickable { showHydrationGoalDialog = true }
                        .padding(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Text(
                                text = "💧 Meta de Hidratación",
                                style = MaterialTheme.typography.body2.copy(
                                    fontWeight = FontWeight.Medium
                                ),
                                color = MaterialTheme.colors.onSurface
                            )
                            Text(
                                text = "${healthMetrics.hydrationData.dailyGoal} vasos",
                                style = MaterialTheme.typography.body2,
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                            )
                        }
                        Text(
                            text = ">",
                            style = MaterialTheme.typography.body1,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
                        )
                    }
                }
            }

            // Toggle de recordatorios
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colors.surface)
                        .padding(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Text(
                                text = "🔔 Recordatorios",
                                style = MaterialTheme.typography.body2.copy(
                                    fontWeight = FontWeight.Medium
                                ),
                                color = MaterialTheme.colors.onSurface
                            )
                            Text(
                                text = if (healthMetrics.activityReminder.isReminderEnabled)
                                    "Activado"
                                else
                                    "Desactivado",
                                style = MaterialTheme.typography.body2,
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                            )
                        }
                        Switch(
                            checked = healthMetrics.activityReminder.isReminderEnabled,
                            onCheckedChange = { enabled ->
                                viewModel.toggleReminders(enabled)
                            }
                        )
                    }
                }
            }

            // Estadísticas de hoy
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
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "📈 Progreso de Hoy",
                            style = MaterialTheme.typography.body2.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = MaterialTheme.colors.onSurface
                        )

                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "${healthMetrics.stepData.steps}",
                                    style = MaterialTheme.typography.body2.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = Color(0xFF4CAF50)
                                )
                                Text(
                                    text = "pasos",
                                    style = MaterialTheme.typography.body2.copy(fontSize = 10.sp),
                                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                                )
                            }

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "${healthMetrics.hydrationData.glassesConsumed}",
                                    style = MaterialTheme.typography.body2.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = Color(0xFF2196F3)
                                )
                                Text(
                                    text = "vasos",
                                    style = MaterialTheme.typography.body2.copy(fontSize = 10.sp),
                                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                                )
                            }

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "${((healthMetrics.stepData.progress + healthMetrics.hydrationData.progress) / 2 * 100).toInt()}%",
                                    style = MaterialTheme.typography.body2.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = MaterialTheme.colors.primary
                                )
                                Text(
                                    text = "promedio",
                                    style = MaterialTheme.typography.body2.copy(fontSize = 10.sp),
                                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }
                }
            }

            // Información de la aplicación
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colors.surface)
                        .clickable { showInfoDialog = true }
                        .padding(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Text(
                                text = "ℹ️ Acerca de la App",
                                style = MaterialTheme.typography.body2.copy(
                                    fontWeight = FontWeight.Medium
                                ),
                                color = MaterialTheme.colors.onSurface
                            )
                            Text(
                                text = "Versión 1.0",
                                style = MaterialTheme.typography.body2,
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                            )
                        }
                        Text(
                            text = ">",
                            style = MaterialTheme.typography.body1,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
                        )
                    }
                }
            }

            // Botón de volver
            item {
                Button(
                    onClick = onBackPressed,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("← Volver")
                }
            }
        }
    }
}

@Composable
fun SettingsStepGoalDialog(
    currentGoal: Int,
    onGoalSet: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    val goalOptions = listOf(5000, 8000, 10000, 12000, 15000, 20000)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.8f)),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colors.surface)
                .padding(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Meta de Pasos",
                    style = MaterialTheme.typography.title3.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center
                )

                goalOptions.forEach { goal ->
                    Button(
                        onClick = { onGoalSet(goal) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = if (goal == currentGoal)
                                MaterialTheme.colors.primary
                            else
                                MaterialTheme.colors.surface
                        )
                    ) {
                        Text("$goal pasos")
                    }
                }

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.error
                    )
                ) {
                    Text("Cancelar")
                }
            }
        }
    }
}

@Composable
fun SettingsHydrationGoalDialog(
    currentGoal: Int,
    onGoalSet: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    val goalOptions = listOf(4, 6, 8, 10, 12, 15)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.8f)),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colors.surface)
                .padding(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Meta de Hidratación",
                    style = MaterialTheme.typography.title3.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center
                )

                goalOptions.forEach { goal ->
                    Button(
                        onClick = { onGoalSet(goal) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = if (goal == currentGoal)
                                MaterialTheme.colors.primary
                            else
                                MaterialTheme.colors.surface
                        )
                    ) {
                        Text("$goal vasos")
                    }
                }

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.error
                    )
                ) {
                    Text("Cancelar")
                }
            }
        }
    }
}

@Composable
fun SettingsAppInfoDialog(
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.8f)),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colors.surface)
                .padding(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "🏋️ Mi Salud Wear",
                    style = MaterialTheme.typography.title3.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Aplicación de seguimiento de salud y bienestar para Wear OS",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.body2
                )

                Text(
                    text = "Funcionalidades:",
                    style = MaterialTheme.typography.body2.copy(
                        fontWeight = FontWeight.Medium
                    )
                )

                Column {
                    Text("• Seguimiento de pasos diarios", style = MaterialTheme.typography.body2)
                    Text("• Control de hidratación", style = MaterialTheme.typography.body2)
                    Text("• Recordatorios de actividad", style = MaterialTheme.typography.body2)
                    Text("• Interfaz optimizada para Wear OS", style = MaterialTheme.typography.body2)
                }

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cerrar")
                }
            }
        }
    }
}