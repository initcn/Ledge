package com.ledge.core.format

import com.ledge.core.model.CurrencyType
import com.ledge.core.model.TransactionType



object TransactionAmountFormatter {

    fun format(

        type: TransactionType,

        amount: Long,

        currency: CurrencyType

    ): String {

        val formattedAmount =

            CurrencyFormatter.format(

                amount = amount,

                currency = currency
            )

        return if (

            type ==
            TransactionType.DEBIT

        ) {

            "-$formattedAmount"

        } else {

            "+$formattedAmount"
        }
    }
}