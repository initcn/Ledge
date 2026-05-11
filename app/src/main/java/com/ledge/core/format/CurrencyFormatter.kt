package com.ledge.core.format

import com.ledge.core.model.CurrencyType


import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

object CurrencyFormatter {

    fun format(

        amount: Long,

        currency: CurrencyType =
            CurrencyType.INR

    ): String {

        val locale =

            when (currency) {

                CurrencyType.INR -> {

                    Locale.Builder()

                        .setLanguage("en")

                        .setRegion("IN")

                        .build()
                }

                CurrencyType.USD -> {

                    Locale.US
                }
            }

        val formatter =

            NumberFormat
                .getCurrencyInstance(
                    locale
                )

        formatter.currency =

            Currency.getInstance(
                currency.name
            )

        return formatter.format(
            amount / 100.0
        )
    }
}