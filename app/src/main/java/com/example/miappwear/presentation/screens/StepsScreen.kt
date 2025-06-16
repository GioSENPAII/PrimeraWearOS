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
import kotlin.math.roundToInt

@Composable
fun StepsScreen(
    viewModel: HealthViewModel,
    onBackPressed: () -> Unit
) {
    val healthMetrics by viewModel.healthMetrics.collectAsStateWithLifecycle()
    val stepData = healthMetrics.stepData
    var showGoalDialog by remember { mutableStateOf(false) }

    // Cálculos adicionales
    val progressPercentage = (stepData.progress * 100).roundToInt()
    val remainingSteps = (stepData.goal - stepData.steps).coerceAtLeast(0)
    val estimatedCalories = (stepData.steps * 0.04).roundToInt() // Estimación básica
    val estimatedDistance = (stepData.steps * 0.762).roundToInt() // Estimación en metros

    if (showGoalDialog) {
        StepGoalDialog(
            currentGoal = stepData.goal,
            onGoalSet = { newGoal ->
                viewModel.setStepGoal(newGoal)
                showGoalDialog = false
            },
            onDismiss = { showGoalDialog = false }
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
                        text = "Pasos",
                        style = MaterialTheme.typography.title2.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colors.onBackground
                    )
                    Text(
                        text = "Seguimiento diario",
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onBackground.copy(alpha = 0.7f)
                    )
                }
            }

            // Indicador circular principal
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colors.surface)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        progress = stepData.progress,
                        modifier = Modifier.size(120.dp),
                        strokeWidth = 12f,
                        color = Color(0xFF4CAF50)
                    )

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "${stepData.steps}",
                            style = MaterialTheme.typography.title1.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp
                            ),
                            color = MaterialTheme.colors.onSurface
                        )
                        Text(
                            text = "pasos",
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                        )
                        Text(
                            text = "$progressPercentage%",
                            style = MaterialTheme.typography.body2.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = Color(0xFF4CAF50)
                        )
                    }
                }
            }

            // Meta actual y botón para cambiar
            item {
                Button(
                    onClick = { showGoalDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.surface
                    )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Meta diaria",
                            style = MaterialTheme.typography.body2.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = MaterialTheme.colors.onSurface
                        )
                        Text(
                            text = "${stepData.goal} pasos",
                            style = MaterialTheme.typography.body2.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colors.primary
                        )
                    }
                }
            }

            // Estadísticas adicionales
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "Estadísticas",
                        style = MaterialTheme.typography.body2.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = MaterialTheme.colors.onBackground,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Pasos restantes
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colors.surface)
                                .padding(8.dp)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "🏁",
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "$remainingSteps",
                                    style = MaterialTheme.typography.body2.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    ),
                                    color = MaterialTheme.colors.onSurface
                                )
                                Text(
                                    text = "restantes",
                                    style = MaterialTheme.typography.body2.copy(fontSize = 8.sp),
                                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                                )
                            }
                        }

                        // Calorías estimadas
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colors.surface)
                                .padding(8.dp)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "🔥",
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "$estimatedCalories",
                                    style = MaterialTheme.typography.body2.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    ),
                                    color = MaterialTheme.colors.onSurface
                                )
                                Text(
                                    text = "kcal",
                                    style = MaterialTheme.typography.body2.copy(fontSize = 8.sp),
                                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }

                    // Distancia estimada
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colors.surface)
                            .padding(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "📏",
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Distancia: ${estimatedDistance}m",
                                style = MaterialTheme.typography.body2.copy(
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 10.sp
                                ),
                                color = MaterialTheme.colors.onSurface
                            )
                        }
                    }
                }
            }

            // Motivación
            if (stepData.progress >= 1.0f) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF4CAF50).copy(alpha = 0.8f))
                            .padding(12.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "✅",
                                fontSize = 20.sp
                            )
                            Text(
                                text = "¡Meta alcanzada!",
                                style = MaterialTheme.typography.body2.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "¡Excelente trabajo hoy!",
                                style = MaterialTheme.typography.body2,
                                color = Color.White.copy(alpha = 0.9f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            } else if (stepData.progress >= 0.8f) {
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
                                text = "📈",
                                fontSize = 16.sp
                            )
                            Text(
                                text = "¡Casi lo logras!",
                                style = MaterialTheme.typography.body2.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "Solo ${remainingSteps} pasos más",
                                style = MaterialTheme.typography.body2.copy(fontSize = 10.sp),
                                color = Color.White.copy(alpha = 0.9f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            // Botón de volver
            item {
                ActionButton(
                    text = "← Volver",
                    onClick = onBackPressed,
                    backgroundColor = MaterialTheme.colors.surface
                )
            }
        }
    }
}

@Composable
fun StepGoalDialog(
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