package com.ledge.ui.components.transaction

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.ledge.ui.components.core.LedgeCard
import com.ledge.ui.components.model.TransactionRowData
import com.ledge.ui.theme.expense
import com.ledge.ui.theme.income

@Composable
fun TransactionRow(
    transaction: TransactionRowData
) {
    LedgeCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // LEFT SIDE INFO
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transaction.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (transaction.note.isNotBlank()) {
                    Text(
                        text = transaction.note,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Text(
                    text = transaction.formattedDate,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            // RIGHT SIDE AMOUNT SIGNALS
            Text(
                text = transaction.formattedAmount,
                style = MaterialTheme.typography.titleMedium,
                color = if (transaction.isExpense) {
                    MaterialTheme.colorScheme.expense
                } else {
                    MaterialTheme.colorScheme.income
                }
            )
        }
    }
}