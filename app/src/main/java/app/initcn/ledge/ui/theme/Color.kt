package app.initcn.ledge.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color

// FINANCIAL COLORS
val ColorScheme.income: Color
    get() = primary

val ColorScheme.expense: Color
    get() = error

val ColorScheme.debt: Color
    get() = tertiary

val ColorScheme.success: Color
    get() = primary

val ColorScheme.warning: Color
    get() = tertiary

// SURFACES
val ColorScheme.cardBackground: Color
    get() = surfaceContainer

val ColorScheme.cardBackgroundElevated: Color
    get() = surfaceContainerHigh

val ColorScheme.inputBackground: Color
    get() = surfaceContainerHighest


// TEXT
val ColorScheme.textPrimary: Color
    get() = onSurface

val ColorScheme.textSecondary: Color
    get() = onSurfaceVariant


// BORDERS
val ColorScheme.borderSubtle: Color
    get() = outlineVariant


// CHARTS
val ColorScheme.chartPositive: Color
    get() = primary

val ColorScheme.chartNegative: Color
    get() = error

val ColorScheme.chartNeutral: Color
    get() = secondary