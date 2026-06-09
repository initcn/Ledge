package com.ledge.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf

val LocalLedgeSurfaces =

    staticCompositionLocalOf<LedgeSurfaces> {

        error("No LedgeSurfaces provided")
    }

object LedgeTheme {

    val colors: ColorScheme
        @Composable
        get() = MaterialTheme.colorScheme

    val surfaces: LedgeSurfaces
        @Composable
        get() = LocalLedgeSurfaces.current
}