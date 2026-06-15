package com.hkm.dictionary.ui.component

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ShimmerLoading(
    modifier: Modifier = Modifier
) {
    val shimmerColors = listOf(
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
    )

    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_translate"
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnim.value, y = translateAnim.value)
    )

    Column(modifier = modifier.padding(16.dp)) {
        // Header shimmer
        ShimmerBlock(
            brush = brush,
            width = 200.dp,
            height = 36.dp,
            mb = 8.dp
        )
        ShimmerBlock(
            brush = brush,
            width = 140.dp,
            height = 20.dp,
            mb = 24.dp
        )

        // Meaning cards shimmer
        repeat(3) {
            ShimmerBlock(
                brush = brush,
                width = 80.dp,
                height = 24.dp,
                mb = 12.dp
            )
            ShimmerBlock(
                brush = brush,
                width = Dp.Unspecified,
                height = 100.dp,
                mb = 16.dp
            )
        }
    }
}

@Composable
private fun ShimmerBlock(
    brush: Brush,
    width: Dp,
    height: Dp,
    mb: Dp
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = mb)
            .then(
                if (width != Dp.Unspecified) Modifier.width(width)
                else Modifier
            )
            .height(height)
            .clip(RoundedCornerShape(12.dp))
            .background(brush)
    )
}
