package com.ledge.ui.dashboard

import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import com.ledge.ui.components.cards.TransactionCard
import com.ledge.ui.components.model.TransactionRowData

import com.ledge.ui.theme.textSecondary

fun LazyListScope.recentTransactionsSection(

    transactions:
    List<TransactionRowData>

) {

    item {

        Text(

            text =
                "Recent Transactions",

            modifier = Modifier
                .padding(bottom = 8.dp),

            style =
                MaterialTheme.typography
                    .headlineSmall
        )
    }

    if (transactions.isEmpty()) {

        item {

            Text(

                text =
                    "No transactions yet.",

                modifier = Modifier
                    .padding(vertical = 12.dp),

                style =
                    MaterialTheme.typography
                        .bodyMedium,

                color =

                    MaterialTheme
                        .colorScheme
                        .textSecondary
            )
        }

        return
    }

    items(

        items = transactions,

        key = { transaction ->

            transaction.hashCode()
        }

    ) { transaction ->

        TransactionCard(

            transaction =
                transaction
        )
    }
}