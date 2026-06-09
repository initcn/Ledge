package com.ledge.domain

import com.ledge.core.TransactionType
import com.ledge.data.entity.TransactionEntity
import java.math.BigDecimal
import javax.inject.Inject

class TransactionFactory @Inject constructor() {

    // CREATE
    fun create(
        type: TransactionType,
        amount: Long,
        category: String,
        paymentInstrument: String,
        note: String,
        createdAt: Long
    ): TransactionEntity {
        return TransactionEntity(
            type = type,
            amount = amount,
            category = category.trim(),
            mode = paymentInstrument.trim(),
            note = note.trim(),
            createdAt = createdAt
        )
    }


    // CREATE SAFE
    fun createOrNull(
        type: TransactionType,
        amount: String,
        category: String,
        paymentInstrument: String,
        note: String,
        createdAt: Long
    ): TransactionEntity? {
        val parsedAmount = try {
            BigDecimal(amount.trim())
                .movePointRight(2)
                .longValueExact()
        } catch (e: Exception) {
            return null
        }

        if (parsedAmount <= 0L) {
            return null
        }

        return create(
            type = type,
            amount = parsedAmount,
            category = category,
            paymentInstrument = paymentInstrument,
            note = note,
            createdAt = createdAt
        )
    }
}