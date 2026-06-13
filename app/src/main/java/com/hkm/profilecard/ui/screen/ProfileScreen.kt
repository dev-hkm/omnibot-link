package com.hkm.profilecard.ui.screen

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun ProfileScreen() {
    val context = LocalContext.current
    val interactionSource = remember { MutableInteractionSource() }
    
    // Tap animation
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )
    
    // Floating orbs animation
    val infiniteTransition = rememberInfiniteTransition(label = "floating")
    val orbFloat1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "orb1"
    )
    val orbFloat2 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "orb2"
    )
    val orbFloat3 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "orb3"
    )
    
    // Rotate animation for decorative circles
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )
    
    // Glow pulse animation
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )
    
    val primaryColor = MaterialTheme.colorScheme.primary
    val tertiaryColor = MaterialTheme.colorScheme.tertiary
    val secondaryColor = MaterialTheme.colorScheme.secondary
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surface,
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Decorative floating orbs - background layer
        Canvas(modifier = Modifier.fillMaxSize()) {
            val centerX = size.width / 2
            val centerY = size.height / 2
            
            // Large background gradient circles
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        primaryColor.copy(alpha = 0.08f),
                        Color.Transparent
                    ),
                    center = Offset(centerX, centerY),
                    radius = size.minDimension * 0.8f
                ),
                radius = size.minDimension * 0.8f,
                center = Offset(centerX, centerY)
            )
            
            // Rotating decorative rings
            for (i in 0..2) {
                val ringRadius = 180.dp.toPx() + i * 60.dp.toPx()
                val ringRotation = rotation + i * 30f
                val segments = 8
                for (j in 0 until segments) {
                    val angle = Math.toRadians((ringRotation + j * (360.0 / segments)).toDouble())
                    val dotX = centerX + cos(angle).toFloat() * ringRadius
                    val dotY = centerY + sin(angle).toFloat() * ringRadius
                    drawCircle(
                        color = when (i) {
                            0 -> primaryColor.copy(alpha = 0.15f)
                            1 -> tertiaryColor.copy(alpha = 0.12f)
                            else -> secondaryColor.copy(alpha = 0.1f)
                        },
                        radius = 4.dp.toPx(),
                        center = Offset(dotX, dotY)
                    )
                }
            }
            
            // Floating orbs
            val orb1X = centerX + (orbFloat1 - 0.5f) * 200
            val orb1Y = centerY - 200 + orbFloat1 * 80
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(primaryColor.copy(alpha = 0.3f), Color.Transparent),
                    center = Offset(orb1X, orb1Y),
                    radius = 60.dp.toPx()
                ),
                radius = 60.dp.toPx(),
                center = Offset(orb1X, orb1Y)
            )
            
            val orb2X = centerX - 150 + (orbFloat2 - 0.5f) * 120
            val orb2Y = centerY + 150 - orbFloat2 * 60
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(tertiaryColor.copy(alpha = 0.25f), Color.Transparent),
                    center = Offset(orb2X, orb2Y),
                    radius = 50.dp.toPx()
                ),
                radius = 50.dp.toPx(),
                center = Offset(orb2X, orb2Y)
            )
            
            val orb3X = centerX + 180 + (orbFloat3 - 0.5f) * 80
            val orb3Y = centerY + 100 + orbFloat3 * 100
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(secondaryColor.copy(alpha = 0.2f), Color.Transparent),
                    center = Offset(orb3X, orb3Y),
                    radius = 45.dp.toPx()
                ),
                radius = 45.dp.toPx(),
                center = Offset(orb3X, orb3Y)
            )
            
            // Center glow
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        primaryColor.copy(alpha = glowAlpha * 0.15f),
                        Color.Transparent
                    ),
                    center = Offset(centerX, centerY),
                    radius = 150.dp.toPx()
                ),
                radius = 150.dp.toPx(),
                center = Offset(centerX, centerY)
            )
        }
        
        // Main name text with tap handler
        Column(
            modifier = Modifier
                .padding(32.dp)
                .scale(scale)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    isPressed = true
                    // Open browser
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse("https://hoangkhanhminh.pages.dev")
                    }
                    context.startActivity(intent)
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Glow effect behind name
            Box(
                modifier = Modifier
                    .scale(1.05f + glowAlpha * 0.02f)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                primaryColor.copy(alpha = 0.2f),
                                Color.Transparent
                            )
                        ),
                        shape = CircleShape
                    )
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                Text(
                    text = "Trần Hoàng Khánh Minh",
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Subtitle with tap hint
            Text(
                text = "Tap to visit my page →",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
        
        // Small decorative dots at corners
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Corner accents
            val dotPositions = listOf(
                Offset(40.dp.toPx(), 60.dp.toPx()),
                Offset(size.width - 40.dp.toPx(), 80.dp.toPx()),
                Offset(60.dp.toPx(), size.height - 80.dp.toPx()),
                Offset(size.width - 60.dp.toPx(), size.height - 60.dp.toPx())
            )
            dotPositions.forEachIndexed { index, pos ->
                drawCircle(
                    color = when (index % 3) {
                        0 -> primaryColor.copy(alpha = 0.3f)
                        1 -> tertiaryColor.copy(alpha = 0.25f)
                        else -> secondaryColor.copy(alpha = 0.2f)
                    },
                    radius = 6.dp.toPx(),
                    center = pos
                )
            }
        }
    }
    
    // Reset press state
    LaunchedEffect(isPressed) {
        if (isPressed) {
            kotlinx.coroutines.delay(150)
            isPressed = false
        }
    }
}