package com.ledge.core.extensions

import com.ledge.core.format.TransactionAmountFormatter

import com.ledge.core.model.CurrencyType
import com.ledge.core.model.TransactionType

import com.ledge.core.utils.date.RelativeDateFormatter

import com.ledge.data.entity.TransactionEntity

import com.ledge.ui.components.model.TransactionRowData

fun TransactionEntity.toRowData(

    currency: CurrencyType

): TransactionRowData {

    return TransactionRowData(

        title =
            category,

        subtitle =
            mode,

        note =
            note,

        formattedAmount =

            TransactionAmountFormatter
                .format(

                    type =
                        type,

                    amount =
                        amount,

                    currency =
                        currency
                ),

        formattedDate =

            RelativeDateFormatter
                .format(
                    createdAt
                ),

        isExpense =

            type ==
                    TransactionType.DEBIT
    )
}