package app.initcn.ledge.ui.components.core

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.initcn.ledge.ui.theme.LedgeTheme

@Composable
fun LedgeScaffold(
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        containerColor = LedgeTheme.surfaces.background,
        contentWindowInsets = WindowInsets.navigationBars,
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.padding(bottom = 96.dp)
            )
        }
    ) { innerPadding ->
        content(innerPadding)
    }
}