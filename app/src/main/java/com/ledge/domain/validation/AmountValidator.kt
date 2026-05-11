package com.ledge.domain.validation

import javax.inject.Inject
import java.math.BigDecimal

class AmountValidator @Inject constructor() {

    fun validate(
        input: String
    ): ValidationResult {

        val filtered =
            input.filter { char ->
                char.isDigit() || char == '.'
            }

        /*
        ---------------------------------------------------
        MULTIPLE DECIMALS CHECK
        ---------------------------------------------------
        */

        if (filtered.count { it == '.' } > 1) {
            return ValidationResult(
                value = filtered,
                error = "Invalid amount"
            )
        }

        /*
        ---------------------------------------------------
        EMPTY CHECK
        ---------------------------------------------------
        */

        if (filtered.isBlank()) {
            return ValidationResult(
                value = filtered,
                error = "Amount is required"
            )
        }

        /*
        ---------------------------------------------------
        CONVERT TO LONG (CENTS/PENNIES)
        ---------------------------------------------------
        */

        val amountInLong = try {
            filtered.toBigDecimal()
                .movePointRight(2)
                .longValueExact()
        } catch (e: Exception) {
            null
        }

        /*
        ---------------------------------------------------
        VALIDATION
        ---------------------------------------------------
        */

        val error = when {

            amountInLong == null -> {
                "Invalid amount"
            }

            amountInLong <= 0L -> {
                "Amount must be greater than 0"
            }

            else -> null
        }

        return ValidationResult(
            value = filtered,
            error = error
        )
    }
}