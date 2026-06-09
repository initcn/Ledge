package com.ledge.core

import java.text.NumberFormat
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Currency
import java.util.Locale

object LedgeTextFormatter {

    private val zoneId = ZoneId.systemDefault()
    private val absoluteDateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")


    // RAW CURRENCY FORMATTING
    fun formatCurrency(
        amount: Long, currency: CurrencyType = CurrencyType.INR
    ): String {
        val locale = when (currency) {
            CurrencyType.INR -> Locale.Builder().setLanguage("en").setRegion("IN").build()
            CurrencyType.USD -> Locale.US
        }

        val formatter = NumberFormat.getCurrencyInstance(locale)
        formatter.currency = Currency.getInstance(currency.name)

        return formatter.format(amount / 100.0)
    }


    // TRANSACTION AMOUNT WITH PREFIX SIGN SIGNALS
    fun formatTransactionAmount(
        type: TransactionType, amount: Long, currency: CurrencyType
    ): String {
        val formattedAmount = formatCurrency(amount = amount, currency = currency)
        return if (type == TransactionType.DEBIT) {
            "-$formattedAmount"
        } else {
            "+$formattedAmount"
        }
    }

    // RELATIVE & ABSOLUTE TIMELINE FORMATTING
    fun formatRelativeDate(timestamp: Long): String {
        val now = Instant.now()
        val instant = Instant.ofEpochMilli(timestamp)
        val duration = Duration.between(instant, now)

        return when {
            duration.toMinutes() < 1 -> "Just now"
            duration.toHours() < 1 -> "${duration.toMinutes()} min ago"
            duration.toDays() < 1 -> "${duration.toHours()} hr ago"
            duration.toDays() < 2 -> "Yesterday"
            else -> instant.atZone(zoneId).toLocalDate().format(absoluteDateFormatter)
        }
    }

    fun formatAbsoluteDate(timestamp: Long): String {
        return Instant.ofEpochMilli(timestamp).atZone(zoneId).toLocalDate()
            .format(absoluteDateFormatter)
    }
}