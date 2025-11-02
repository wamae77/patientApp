package com.ke.patientapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = BluePrimaryDark,
    onPrimary = OnPrimaryDarkInk,
    primaryContainer = BlueContainerDk,
    onPrimaryContainer = White,
    secondary = Color(0xFF38BDF8),
    onSecondary = OnPrimaryDarkInk,
    secondaryContainer = Color(0xFF0B315A),
    onSecondaryContainer = White,
    tertiary = Color(0xFF34D399),
    onTertiary = OnPrimaryDarkInk,
    tertiaryContainer = Color(0xFF0D3A2A),
    onTertiaryContainer = White,
    background = Black,
    onBackground = White,
    surface = SurfaceDk,
    onSurface = White,
    surfaceVariant = DarkGray,
    onSurfaceVariant = White.copy(alpha = 0.80f),
    outline = White.copy(alpha = 0.22f),
    outlineVariant = White.copy(alpha = 0.12f),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    surfaceDim = Black,
    surfaceBright = SurfaceDk,
    surfaceContainerLowest = DarkGray,
    surfaceContainerLow = SurfaceDk,
    surfaceContainer = SurfaceDk,
    surfaceContainerHigh = SurfaceHi,
    surfaceContainerHighest = SurfaceHi
)

private val LightColorScheme = lightColorScheme(
    primary = BluePrimaryLight,
    onPrimary = Color.White,
    primaryContainer = BlueContainerLt,
    onPrimaryContainer = Color(0xFF0B2960),
    secondary = Color(0xFF0EA5E9),
    onSecondary = Color.White,     // sky-500
    secondaryContainer = Color(0xFFCFF3FF),
    onSecondaryContainer = Color(0xFF003247),
    tertiary = Color(0xFF10B981),
    onTertiary = Color.White,      // emerald-500
    tertiaryContainer = Color(0xFFC5F6E6),
    onTertiaryContainer = Color(0xFF063826),
    background = White,
    onBackground = Black,
    surface = Color(0xFFFFFFFF),
    onSurface = Black,
    surfaceVariant = Color(0xFFF2F4F7),
    onSurfaceVariant = Color(0xFF2B2B2B),
    outline = Color(0x22000000),
    outlineVariant = Color(0x14000000),
    error = Color(0xFFBA1A1A),
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    surfaceDim = Color(0xFFF7F7F7),
    surfaceBright = Color(0xFFFFFFFF),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFFFFFFF),
    surfaceContainer = Color(0xFFFFFFFF),
    surfaceContainerHigh = Color(0xFFFFFFFF),
    surfaceContainerHighest = Color(0xFFFFFFFF)
)


@Composable
fun PatientappTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true, content: @Composable () -> Unit
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
        colorScheme = colorScheme, typography = Typography, content = content
    )
}