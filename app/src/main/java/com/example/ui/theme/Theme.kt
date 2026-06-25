package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = MutedRoseLight,
    secondary = LavenderLight,
    tertiary = WarmCoralLight,
    background = DarkPlumBackground,
    surface = DarkCardSurface,
    onPrimary = DarkPlumText,
    onSecondary = DarkPlumText,
    onTertiary = DarkPlumText,
    onBackground = SoftCreamBackground,
    onSurface = SoftCreamBackground,
    error = AlertRed
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryColor,
    onPrimary = OnPrimary,
    primaryContainer = PrimaryContainerColor,
    onPrimaryContainer = OnPrimaryContainer,
    inversePrimary = InversePrimary,
    secondary = SecondaryColor,
    onSecondary = OnSecondary,
    secondaryContainer = SecondaryContainerColor,
    onSecondaryContainer = OnSecondaryContainer,
    tertiary = TertiaryColor,
    onTertiary = OnTertiary,
    tertiaryContainer = TertiaryContainerColor,
    onTertiaryContainer = OnTertiaryContainer,
    background = BackgroundColor,
    onBackground = OnBackgroundColor,
    surface = SurfaceColor,
    onSurface = OnSurface,
    surfaceVariant = SurfaceVariantColor,
    onSurfaceVariant = OnSurfaceVariant,
    outline = OutlineColor,
    outlineVariant = OutlineVariant,
    error = ErrorColor,
    onError = OnError,
    errorContainer = ErrorContainer,
    onErrorContainer = OnErrorContainer,
    inverseSurface = InverseSurface,
    inverseOnSurface = InverseOnSurface,
    surfaceTint = SurfaceTint
)

@Composable
fun LunaCareTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit,
) {
    LunaCareTheme(darkTheme = darkTheme, content = content)
}
