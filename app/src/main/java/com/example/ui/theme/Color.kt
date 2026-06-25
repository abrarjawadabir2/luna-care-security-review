package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// Design Tokens (Light Theme Palette)
val SurfaceColor = Color(0xFFFFF8F7)
val SurfaceDim = Color(0xFFE4D7D6)
val SurfaceBright = Color(0xFFFFF8F7)
val SurfaceContainerLowest = Color(0xFFFFFFFF)
val SurfaceContainerLow = Color(0xFFFEF1F0)
val SurfaceContainer = Color(0xFFF8EBEA)
val SurfaceContainerHigh = Color(0xFFF2E5E4)
val SurfaceContainerHighest = Color(0xFFECE0DF)
val OnSurface = Color(0xFF201A1A)
val OnSurfaceVariant = Color(0xFF524343)
val InverseSurface = Color(0xFF362F2F)
val InverseOnSurface = Color(0xFFFBEEED)
val OutlineColor = Color(0xFF857372)
val OutlineVariant = Color(0xFFD7C2C1)
val SurfaceTint = Color(0xFF8A4D4E)
val PrimaryColor = Color(0xFF8A4D4E)
val OnPrimary = Color(0xFFFFFFFF)
val PrimaryContainerColor = Color(0xFFD48C8C)
val OnPrimaryContainer = Color(0xFF592628)
val InversePrimary = Color(0xFFFFB3B3)
val SecondaryColor = Color(0xFF5D5A84)
val OnSecondary = Color(0xFFFFFFFF)
val SecondaryContainerColor = Color(0xFFD1CCFE)
val OnSecondaryContainer = Color(0xFF58557F)
val TertiaryColor = Color(0xFF8D4D39)
val OnTertiary = Color(0xFFFFFFFF)
val TertiaryContainerColor = Color(0xFFD88C73)
val OnTertiaryContainer = Color(0xFF5B2615)
val ErrorColor = Color(0xFFBA1A1A)
val OnError = Color(0xFFFFFFFF)
val ErrorContainer = Color(0xFFFFDAD6)
val OnErrorContainer = Color(0xFF93000A)
val PrimaryFixed = Color(0xFFFFDAD9)
val PrimaryFixedDim = Color(0xFFFFB3B3)
val OnPrimaryFixed = Color(0xFF380C0F)
val OnPrimaryFixedVariant = Color(0xFF6E3637)
val SecondaryFixed = Color(0xFFE3DFFF)
val SecondaryFixedDim = Color(0xFFC6C1F2)
val OnSecondaryFixed = Color(0xFF19163D)
val OnSecondaryFixedVariant = Color(0xFF45426B)
val TertiaryFixed = Color(0xFFFFDBD0)
val TertiaryFixedDim = Color(0xFFFFB59E)
val OnTertiaryFixed = Color(0xFF380C01)
val OnTertiaryFixedVariant = Color(0xFF703623)
val BackgroundColor = Color(0xFFFFF8F7)
val OnBackgroundColor = Color(0xFF201A1A)
val SurfaceVariantColor = Color(0xFFECE0DF)

// Backward Compatibility Variables
val SoftCreamBackground = BackgroundColor
val SoftCardSurface = SurfaceContainer
val MutedRosePrimary = PrimaryColor
val LavenderSecondary = SecondaryColor
val WarmCoralTertiary = TertiaryColor
val DarkPlumText = OnSurface
val TextMuted = OnSurfaceVariant
val PrimaryContainer = PrimaryContainerColor
val SecondaryContainer = SecondaryContainerColor
val TertiaryContainer = TertiaryContainerColor
val SurfaceContainerLowVal = SurfaceContainerLow
val SurfaceContainerHighVal = SurfaceContainerHigh
val SurfaceContainerHighestVal = SurfaceContainerHighest

// Dark theme palette adjusted to preserve design tokens elegantly
val DarkPlumBackground = Color(0xFF201A1A)  // Deep rich dark plum background
val DarkCardSurface = Color(0xFF2D2323)     // Dark card surface
val MutedRoseLight = Color(0xFFD48C8C)      // Soft lightened rose for dark theme
val LavenderLight = Color(0xFFD1CCFE)       // Soft lightened lavender
val WarmCoralLight = Color(0xFFD88C73)      // Soft lightened coral

// Functional indicators
val SuccessGreen = Color(0xFF7EA180)        // Calm green
val WarningAmber = Color(0xFFE0A96D)        // Balanced orange-amber
val AlertRed = ErrorColor
val AlertRedContainer = ErrorContainer
