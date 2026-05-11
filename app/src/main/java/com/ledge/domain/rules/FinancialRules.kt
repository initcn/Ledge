package com.ledge.domain.rules

object FinancialRules {

    val debtPaymentCategories = setOf(

        "Debt Repayment",
        "Loan Payment",
        "EMI"
    )

    val borrowedMoneyCategories = setOf(

        "Credit & Debt",
        "Borrowed Money"
    )

    val lendingCategories = setOf(

        "Money Lent"
    )

    val repaymentCategories = setOf(

        "Repayment Received"
    )

    val creditDebtModes = setOf(

        "Credit Line",
        "Credit Card"
    )

    fun isDebtPayment(
        category: String
    ): Boolean {

        return category in
                debtPaymentCategories
    }

    fun isBorrowedMoney(
        category: String
    ): Boolean {

        return category in
                borrowedMoneyCategories
    }

    fun isLending(
        category: String
    ): Boolean {

        return category in
                lendingCategories
    }

    fun isRepaymentReceived(
        category: String
    ): Boolean {

        return category in
                repaymentCategories
    }

    fun isCreditMode(
        mode: String
    ): Boolean {

        return mode in
                creditDebtModes
    }

    fun shouldIncludeInExpense(
        category: String
    ): Boolean {

        return !isDebtPayment(
            category
        )
    }
}