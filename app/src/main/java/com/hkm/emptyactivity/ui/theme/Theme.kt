package com.hkm.emptyactivity.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF4A6741),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFCBE8C0),
    onPrimaryContainer = Color(0xFF082104),
    secondary = Color(0xFF53634E),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFD6E8CD),
    onSecondaryContainer = Color(0xFF111F0F),
    tertiary = Color(0xFF386667),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFBCEBEC),
    onTertiaryContainer = Color(0xFF002020),
    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    background = Color(0xFFF8FAF0),
    onBackground = Color(0xFF1A1C18),
    surface = Color(0xFFF8FAF0),
    onSurface = Color(0xFF1A1C18),
    surfaceVariant = Color(0xFFDFE4D7),
    onSurfaceVariant = Color(0xFF43483E),
    outline = Color(0xFF73796D),
    outlineVariant = Color(0xFFC3C8BB),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFF2F4EA),
    surfaceContainer = Color(0xFFECEEE4),
    surfaceContainerHigh = Color(0xFFE6E9DF),
    surfaceContainerHighest = Color(0xFFE1E3D9),
    inverseSurface = Color(0xFF2F312D),
    inverseOnSurface = Color(0xFFEFF1E7),
    inversePrimary = Color(0xFFB0CC9E),
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFB0CC9E),
    onPrimary = Color(0xFF1D3714),
    primaryContainer = Color(0xFF334E2B),
    onPrimaryContainer = Color(0xFFCBE8C0),
    secondary = Color(0xFFBBCCB2),
    onSecondary = Color(0xFF263422),
    secondaryContainer = Color(0xFF3C4B37),
    onSecondaryContainer = Color(0xFFD6E8CD),
    tertiary = Color(0xFFA0CFD0),
    onTertiary = Color(0xFF003738),
    tertiaryContainer = Color(0xFF1F4E4F),
    onTertiaryContainer = Color(0xFFBCEBEC),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    background = Color(0xFF1A1C18),
    onBackground = Color(0xFFE1E3D9),
    surface = Color(0xFF1A1C18),
    onSurface = Color(0xFFE1E3D9),
    surfaceVariant = Color(0xFF43483E),
    onSurfaceVariant = Color(0xFFC3C8BB),
    outline = Color(0xFF8D9286),
    outlineVariant = Color(0xFF43483E),
    surfaceContainerLowest = Color(0xFF0F1210),
    surfaceContainerLow = Color(0xFF1A1C18),
    surfaceContainer = Color(0xFF1E201C),
    surfaceContainerHigh = Color(0xFF282B26),
    surfaceContainerHighest = Color(0xFF333631),
    inverseSurface = Color(0xFFE1E3D9),
    inverseOnSurface = Color(0xFF2F312D),
    inversePrimary = Color(0xFF4A6741),
)

@Composable
fun TodoAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
