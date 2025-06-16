package com.example.miappwear.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.*

@Composable
fun CircularProgressIndicator(
    progress: Float,
    modifier: Modifier = Modifier,
    strokeWidth: Float = 8f,
    color: Color = MaterialTheme.colors.primary,
    backgroundColor: Color = MaterialTheme.colors.onSurface.copy(alpha = 0.3f)
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 1000),
        label = "progress"
    )

    Canvas(
        modifier = modifier.size(120.dp)
    ) {
        val strokeWidthPx = strokeWidth * density
        val radius = (size.minDimension - strokeWidthPx) / 2
        val center = androidx.compose.ui.geometry.Offset(size.width / 2, size.height / 2)

        // Círculo de fondo
        drawCircle(
            color = backgroundColor,
            radius = radius,
            center = center,
            style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round)
        )

        // Círculo de progreso
        drawArc(
            color = color,
            startAngle = -90f,
            sweepAngle = 360f * animatedProgress,
            useCenter = false,
            style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round)
        )
    }
}

@Composable
fun MetricCard(
    title: String,
    value: String,
    subtitle: String,
    icon: String, // Cambiado a String para usar emojis
    progress: Float,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    progressColor: Color = MaterialTheme.colors.primary
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colors.surface)
            .clickable { onClick() }
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            progress = progress,
            color = progressColor,
            modifier = Modifier.size(80.dp)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = icon,
                fontSize = 16.sp
            )

            Text(
                text = value,
                style = MaterialTheme.typography.body2.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                ),
                color = MaterialTheme.colors.onSurface,
                textAlign = TextAlign.Center
            )

            Text(
                text = subtitle,
                style = MaterialTheme.typography.body2.copy(fontSize = 8.sp),
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colors.primary
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = backgroundColor)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.button.copy(fontSize = 12.sp)
        )
    }
}

@Composable
fun QuickActionChip(
    text: String,
    icon: String, // Cambiado a String para usar emojis
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = if (isSelected)
                MaterialTheme.colors.primary
            else
                MaterialTheme.colors.surface
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = icon,
                fontSize = 12.sp
            )
            Text(
                text = text,
                style = MaterialTheme.typography.body2.copy(fontSize = 10.sp),
                color = if (isSelected)
                    MaterialTheme.colors.onPrimary
                else
                    MaterialTheme.colors.onSurface
            )
        }
    }
}

@Composable
fun CelebrationOverlay(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    message: String = "¡Meta alcanzada!"
) {
    if (isVisible) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.8f))
                .clickable { onDismiss() },
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colors.primary)
                    .padding(16.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "⭐",
                        fontSize = 32.sp
                    )

                    Text(
                        text = message,
                        style = MaterialTheme.typography.body1.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colors.onPrimary,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "Toca para continuar",
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onPrimary.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}