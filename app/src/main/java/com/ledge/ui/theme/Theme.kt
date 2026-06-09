package com.ledge.ui.theme

import android.os.Build

import androidx.compose.foundation.isSystemInDarkTheme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

import androidx.compose.ui.platform.LocalContext

private val LightColors =
    lightColorScheme()

private val DarkColors =
    darkColorScheme()

@Composable
fun LedgeTheme(

    content: @Composable () -> Unit

) {

    val context =
        LocalContext.current

    val darkTheme =
        isSystemInDarkTheme()

    val colorScheme =

        when {

            Build.VERSION.SDK_INT >=
                    Build.VERSION_CODES.S -> {

                if (darkTheme) {

                    dynamicDarkColorScheme(
                        context
                    )

                } else {

                    dynamicLightColorScheme(
                        context
                    )
                }
            }

            darkTheme -> {

                DarkColors
            }

            else -> {

                LightColors
            }
        }

    val surfaces =

        LedgeSurfaces(

            background =
                colorScheme.background,

            surfaceLow =
                colorScheme.surface,

            surface =
                colorScheme.surfaceContainerLow,

            surfaceHigh =
                colorScheme.surfaceContainer,

            surfaceHighest =
                colorScheme.surfaceContainerHigh,

            input =
                colorScheme.surfaceContainerHighest
        )

    CompositionLocalProvider(

        LocalLedgeSurfaces provides
                surfaces

    ) {

        MaterialTheme(

            colorScheme = colorScheme,
            typography = Typography,
            shapes = LedgeShapes,
            content = content
        )
    }
}