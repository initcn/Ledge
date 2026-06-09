package app.initcn.ledge.ui.components.core

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import app.initcn.ledge.ui.theme.cardBackgroundElevated

enum class LedgeCardVariant {
    DASHBOARD_CONTAINER, // Resolves to extraLarge (30.dp) shapes for high structural layout weight
    LIST_ITEM           // Resolves to large/medium shapes for contextual lists and settings rows
}

@Composable
fun LedgeCard(
    modifier: Modifier = Modifier,
    variant: LedgeCardVariant = LedgeCardVariant.LIST_ITEM,
    containerColor: Color = MaterialTheme.colorScheme.cardBackgroundElevated,
    elevation: Dp = 0.dp,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    // Automatically resolves shape tokens out of the master theme configuration context
    val assignedShape: Shape = when (variant) {
        LedgeCardVariant.DASHBOARD_CONTAINER -> MaterialTheme.shapes.extraLarge
        LedgeCardVariant.LIST_ITEM -> MaterialTheme.shapes.large
    }

    Card(
        modifier = modifier,
        shape = assignedShape,
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation)
    ) {
        Column(
            modifier = Modifier.padding(contentPadding),
            content = content
        )
    }
}