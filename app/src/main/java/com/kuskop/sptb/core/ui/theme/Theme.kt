package com.kuskop.sptb.core.ui.theme

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

val LightColors = lightColorScheme(
    primary = Color(0xFF2563EB),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFDBEAFE),
    secondary = Color(0xFF059669),
    onSecondary = Color.White,
    tertiary = Color(0xFF7C3AED),
    error = Color(0xFFDC2626),
    background = Color(0xFFF8FAFC),
    surface = Color.White,
    surfaceVariant = Color(0xFFF1F5F9),
)

val DarkColors = darkColorScheme(
    primary = Color(0xFF60A5FA),
    onPrimary = Color(0xFF1E3A5F),
    primaryContainer = Color(0xFF1E40AF),
    secondary = Color(0xFF34D399),
    onSecondary = Color(0xFF064E3B),
    tertiary = Color(0xFFA78BFA),
    error = Color(0xFFF87171),
    background = Color(0xFF0F172A),
    surface = Color(0xFF1E293B),
    surfaceVariant = Color(0xFF334155),
)

val statusColors = StatusColors(
    sokong = Color(0xFF10B981),
    tidakDisokong = Color(0xFFEF4444),
    siasat = Color(0xFFF59E0B),
    lulus = Color(0xFF22C55E),
    tolak = Color(0xFFEF4444),
    pending = Color(0xFFF59E0B),
    proses = Color(0xFF3B82F6),
    incomplete = Color(0xFFF97316),
)

data class StatusColors(
    val sokong: Color,
    val tidakDisokong: Color,
    val siasat: Color,
    val lulus: Color,
    val tolak: Color,
    val pending: Color,
    val proses: Color,
    val incomplete: Color,
)

@Composable
fun SptbTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColors
        else -> LightColors
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
