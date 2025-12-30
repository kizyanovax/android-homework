package com.example.hw_3.ui.theme

import android.app.Activity
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
    primary = DnDGold,
    secondary = DnDBronze,
    tertiary = DnDRed,
    background = DnDDarkBrown,
    surface = DnDDarkGray,
    onPrimary = DnDDarkBrown,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = DnDBeige,
    onSurface = DnDBeige
)

private val LightColorScheme = lightColorScheme(
    primary = DnDBlue,
    secondary = DnDPurple,
    tertiary = DnDGreen,
    background = DnDBeige,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = DnDDarkBrown,
    onSurface = DnDDarkBrown
)

@Composable
fun Hw3Theme(
    darkTheme: Boolean = true, // По умолчанию темная тема для D&D стиля
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Отключаем для использования кастомной темы
    content: @Composable () -> Unit
) {
    val colorScheme = when {
      // Используем кастомную тему D&D вместо динамических цветов
      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

    MaterialTheme(
      colorScheme = colorScheme,
      typography = Typography,
      content = content
    )
}