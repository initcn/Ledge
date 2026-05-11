package com.ledge.ui.reports.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth

import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme

import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

import com.ledge.core.format.CurrencyFormatter
import com.ledge.core.model.CurrencyType
import com.ledge.core.utils.date.DateFormatter

import com.ledge.ui.components.core.LedgeCard

import com.ledge.ui.theme.expense
import com.ledge.ui.theme.income
import com.ledge.ui.theme.textSecondary

@Composable
fun ReportsSummary(

    transactionCount: Int,

    totalCredit: Long,
    totalDebit: Long,
    balance: Long,

    fromDate: Long,
    toDate: Long,

    currency: CurrencyType
) {

    LedgeCard(
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(
                text = "Report Summary",
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = "Transactions: $transactionCount"
            )

            Text(
                text =
                    "Period: " +
                            DateFormatter.format(fromDate) +
                            " → " +
                            DateFormatter.format(toDate),
                color = MaterialTheme.colorScheme.textSecondary
            )

            Text(
                text =
                    "Total Credit: " +
                            CurrencyFormatter.format(
                                amount = totalCredit,
                                currency = currency
                            ),
                color = MaterialTheme.colorScheme.income
            )

            Text(
                text =
                    "Total Debit: " +
                            CurrencyFormatter.format(
                                amount = totalDebit,
                                currency = currency
                            ),
                color = MaterialTheme.colorScheme.expense
            )

            Text(
                text =
                    "Balance: " +
                            CurrencyFormatter.format(
                                amount = balance,
                                currency = currency
                            ),
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}