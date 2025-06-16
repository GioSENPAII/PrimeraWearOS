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
import kotlin.math.roundToInt

@Composable
fun HydrationScreen(
    viewModel: HealthViewModel,
    onBackPressed: () -> Unit
) {
    val healthMetrics by viewModel.healthMetrics.collectAsStateWithLifecycle()
    val hydrationData = healthMetrics.hydrationData
    var showGoalDialog by remember { mutableStateOf(false) }

    // Cálculos adicionales
    val progressPercentage = (hydrationData.progress * 100).roundToInt()
    val remainingGlasses = (hydrationData.dailyGoal - hydrationData.glassesConsumed).coerceAtLeast(0)
    val totalLiters = (hydrationData.glassesConsumed * 0.25f) // Asumiendo 250ml por vaso
    val lastDrinkFormatted = if (hydrationData.lastDrinkTime > 0) {
        SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(hydrationData.lastDrinkTime))
    } else "No registrado"

    if (showGoalDialog) {
        HydrationGoalDialog(
            currentGoal = hydrationData.dailyGoal,
            onGoalSet = { newGoal ->
                viewModel.setHydrationGoal(newGoal)
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
                        text = "Hidratación",
                        style = MaterialTheme.typography.title2.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colors.onBackground
                    )
                    Text(
                        text = "Mantente hidratado",
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
                        progress = hydrationData.progress,
                        modifier = Modifier.size(120.dp),
                        strokeWidth = 12f,
                        color = Color(0xFF2196F3)
                    )

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "${hydrationData.glassesConsumed}",
                            style = MaterialTheme.typography.title1.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp
                            ),
                            color = MaterialTheme.colors.onSurface
                        )
                        Text(
                            text = "vasos",
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                        )
                        Text(
                            text = "$progressPercentage%",
                            style = MaterialTheme.typography.body2.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = Color(0xFF2196F3)
                        )
                    }
                }
            }

            // Botón grande para agregar agua
            item {
                Button(
                    onClick = { viewModel.addWaterGlass() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFF2196F3)
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "➕",
                            fontSize = 20.sp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Agregar Vaso",
                            style = MaterialTheme.typography.body1.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color.White
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
                            text = "${hydrationData.dailyGoal} vasos",
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
                        // Vasos restantes
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
                                    text = "💧",
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "$remainingGlasses",
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

                        // Litros consumidos
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
                                    text = "🌊",
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "${String.format("%.1f", totalLiters)}",
                                    style = MaterialTheme.typography.body2.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    ),
                                    color = MaterialTheme.colors.onSurface
                                )
                                Text(
                                    text = "litros",
                                    style = MaterialTheme.typography.body2.copy(fontSize = 8.sp),
                                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }

                    // Último vaso
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
                                text = "⏰",
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Último vaso: $lastDrinkFormatted",
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

            // Estado de hidratación
            if (hydrationData.progress >= 1.0f) {
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
                                text = "¡Bien hidratado!",
                                style = MaterialTheme.typography.body2.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "Has alcanzado tu meta diaria",
                                style = MaterialTheme.typography.body2,
                                color = Color.White.copy(alpha = 0.9f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            } else if (hydrationData.progress >= 0.7f) {
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
                                text = "¡Vas muy bien!",
                                style = MaterialTheme.typography.body2.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "Solo ${remainingGlasses} vasos más",
                                style = MaterialTheme.typography.body2.copy(fontSize = 10.sp),
                                color = Color.White.copy(alpha = 0.9f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            } else if (hydrationData.progress < 0.3f && hydrationData.lastDrinkTime > 0 &&
                (System.currentTimeMillis() - hydrationData.lastDrinkTime) > 2 * 60 * 60 * 1000) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFFF5722).copy(alpha = 0.8f))
                            .padding(12.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "⚠️",
                                fontSize = 16.sp
                            )
                            Text(
                                text = "¡Tiempo de hidratarse!",
                                style = MaterialTheme.typography.body2.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "No olvides beber agua regularmente",
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
                Button(
                    onClick = onBackPressed,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.surface
                    )
                ) {
                    Text("← Volver")
                }
            }
        }
    }
}

@Composable
fun HydrationGoalDialog(
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
                        Text("$goal vasos (${goal * 0.25f}L)")
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