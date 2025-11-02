package com.ke.patientapp.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext


private val DarkColorScheme = darkColorScheme(
    primary            = White,
    onPrimary          = Black,
    primaryContainer   = LightGray,
    onPrimaryContainer = White,
    secondary          = White.copy(alpha = 0.88f),
    onSecondary        = Black,
    secondaryContainer = Gray,
    onSecondaryContainer = White,
    tertiary           = White.copy(alpha = 0.72f),
    onTertiary         = Black,
    tertiaryContainer  = LightGray,
    onTertiaryContainer = White,
    background         = Black,
    onBackground       = White,
    surface            = Gray,
    onSurface          = White,
    surfaceVariant     = DarkGray,
    onSurfaceVariant   = White.copy(alpha = 0.80f),
    outline            = White.copy(alpha = 0.24f),
    outlineVariant     = White.copy(alpha = 0.12f),
    error              = Color(0xFFFFB4AB),
    onError            = Color(0xFF690005),
    errorContainer     = Color(0xFF93000A),
    onErrorContainer   = Color(0xFFFFDAD6),
    surfaceDim         = Black,
    surfaceBright      = Gray,
    surfaceContainerLowest = DarkGray,
    surfaceContainerLow    = Gray,
    surfaceContainer       = Gray,
    surfaceContainerHigh   = LightGray,
    surfaceContainerHighest= LightGray
)


private val LightColorScheme = lightColorScheme(
    primary = Black,
    onPrimary = White,
    primaryContainer = White,
    onPrimaryContainer = Black,
    secondary = Black.copy(alpha = 0.72f),
    onSecondary = White,
    secondaryContainer = White,
    onSecondaryContainer = Black,
    tertiary = Black.copy(alpha = 0.60f),
    onTertiary = White,
    tertiaryContainer = White,
    onTertiaryContainer = Black,
    background = White,
    onBackground = Black,
    surface = White,
    onSurface = Black,
    surfaceVariant = White,
    onSurfaceVariant = Black.copy(alpha = 0.70f),
    outline = Black.copy(alpha = 0.18f),
    outlineVariant = Black.copy(alpha = 0.10f),
    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    surfaceDim = White,
    surfaceBright = White,
    surfaceContainerLowest = White,
    surfaceContainerLow = White,
    surfaceContainer = White,
    surfaceContainerHigh = White,
    surfaceContainerHighest = White
)


@Composable
fun PatientappTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}