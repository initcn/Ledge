package app.initcn.ledge.domain

object FinancialRules {
    val debtPaymentCategories = setOf("Debt Repayment", "Loan Payment", "EMI")
    val borrowedMoneyCategories = setOf("Credit & Debt", "Borrowed Money")
    val lendingCategories = setOf("Money Lent")
    val repaymentCategories = setOf("Repayment Received")
    val creditDebtModes = setOf("Credit Line", "Credit Card")

    fun isDebtPayment(category: String): Boolean = category in debtPaymentCategories
    fun isBorrowedMoney(category: String): Boolean = category in borrowedMoneyCategories
    fun isLending(category: String): Boolean = category in lendingCategories
    fun isRepaymentReceived(category: String): Boolean = category in repaymentCategories
    fun isCreditMode(mode: String): Boolean = mode in creditDebtModes
    fun shouldIncludeInExpense(category: String): Boolean = !isDebtPayment(category)
}