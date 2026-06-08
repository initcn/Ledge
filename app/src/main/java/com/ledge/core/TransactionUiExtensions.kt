package com.ledge.core

import com.ledge.data.entity.TransactionEntity
import com.ledge.ui.components.model.TransactionRowData

fun TransactionEntity.toRowData(currency: CurrencyType): TransactionRowData {
    return TransactionRowData(
        title = category,
        subtitle = mode,
        note = note,
        formattedAmount = LedgeTextFormatter.formatTransactionAmount(
            type = type,
            amount = amount,
            currency = currency
        ),
        formattedDate = LedgeTextFormatter.formatRelativeDate(createdAt),
        isExpense = type == TransactionType.DEBIT
    )
}