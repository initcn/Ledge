package com.ledge.ui.components.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ledge.ui.components.core.LedgeCard
import com.ledge.ui.components.model.TransactionRowData
import com.ledge.ui.theme.LedgeTheme
import com.ledge.ui.theme.expense
import com.ledge.ui.theme.income

@Composable
fun TransactionCard(
    transaction: TransactionRowData
) {
    val amountColor = if (transaction.isExpense) {
        MaterialTheme.colorScheme.expense
    } else {
        MaterialTheme.colorScheme.income
    }

    LedgeCard(
        modifier = Modifier.fillMaxWidth(),
        containerColor = LedgeTheme.surfaces.surfaceHigh, // <-- UPDATED TO THE MODERN HIGHER SURFACE SPEC
        shape = RoundedCornerShape(30.dp), // <-- ALIGNED WITH BUDGETOVERVIEW 30DP CORNERS
        elevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            /*
            ---------------------------------------------------
            LEFT SIDE INFO - TYPOGRAPHY SPEC SYNC
            ---------------------------------------------------
            */
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = transaction.title,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = transaction.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant // <-- REMOVED STALE CUSTOM EXTENSION Fallback
                )

                Text(
                    text = transaction.formattedDate,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant // <-- REPLACED WITH SYSTEM TOKEN
                )

                if (transaction.note.isNotBlank()) {
                    Text(
                        text = transaction.note,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            /*
            ---------------------------------------------------
            RIGHT SIDE AMOUNT DISPLAY
            ---------------------------------------------------
            */
            Text(
                text = transaction.formattedAmount,
                style = MaterialTheme.typography.titleMedium,
                color = amountColor
            )
        }
    }
}