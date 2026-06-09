package app.initcn.ledge.ui.reports

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

import app.initcn.ledge.ui.components.cards.TransactionCard

import app.initcn.ledge.ui.components.model.TransactionRowData

fun LazyListScope.reportsTransactionList(

    transactions:
    List<TransactionRowData>

) {

    item {

        Text(

            text =
                "Transactions",

            style =
                MaterialTheme.typography
                    .titleLarge
        )
    }

    if (transactions.isEmpty()) {

        item {

            Text(

                text =
                    "No transactions found."
            )
        }

        return
    }

    items(
        transactions
    ) { transaction ->

        TransactionCard(

            transaction =
                transaction
        )
    }
}