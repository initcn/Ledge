package app.initcn.ledge.domain

import javax.inject.Inject

data class ValidationResult(
    val value: String,
    val error: String?
)

class AmountValidator @Inject constructor() {

    fun validate(input: String): ValidationResult {
        val filtered = input.filter { char ->
            char.isDigit() || char == '.'
        }

        if (filtered.count { it == '.' } > 1) {
            return ValidationResult(
                value = filtered,
                error = "Invalid amount"
            )
        }

        if (filtered.isBlank()) {
            return ValidationResult(
                value = filtered,
                error = "Amount is required"
            )
        }

        val amountInLong = try {
            filtered.toBigDecimal()
                .movePointRight(2)
                .longValueExact()
        } catch (e: Exception) {
            null
        }

        val error = when {
            amountInLong == null -> "Invalid amount"
            amountInLong <= 0L -> "Amount must be greater than 0"
            else -> null
        }

        return ValidationResult(
            value = filtered,
            error = error
        )
    }
}