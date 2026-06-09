package app.initcn.ledge.core

import app.initcn.ledge.data.entity.TransactionEntity
import app.initcn.ledge.ui.components.model.TransactionRowData

fun TransactionEntity.toRowData(currency: CurrencyType): TransactionRowData {
    return TransactionRowData(
        title = this.category,
        subtitle = this.mode,
        formattedAmount = LedgeTextFormatter.formatTransactionAmount(
            type = this.type, amount = this.amount, currency = currency
        ),
        formattedDate = LedgeTextFormatter.formatRelativeDate(this.createdAt),
        isExpense = this.type == TransactionType.DEBIT,
        note = this.note
    )
}