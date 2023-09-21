package com.tnmd.learningenglishapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Shapes
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat

private val DarkColorPalette =
    darkColors(primary = BrightOrange, primaryVariant = MediumOrange, secondary = DarkOrange)

private val LightColorPalette =
    lightColors(primary = BrightOrange, primaryVariant = MediumOrange, secondary = DarkOrange)


@Composable
fun LearningEnglishAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit
) {
    val colors = if (darkTheme) DarkColorPalette else LightColorPalette

    MaterialTheme(colors = colors, typography = Typography, shapes = Shapes, content = content)
}

