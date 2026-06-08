package com.ledge.ui.components.core

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import com.ledge.ui.theme.LedgeTheme

@Composable
fun LedgeScaffold(
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        containerColor = LedgeTheme.surfaces.background,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        content = content
    )
}