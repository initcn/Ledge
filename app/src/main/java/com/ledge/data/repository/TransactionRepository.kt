package com.ledge.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.ledge.core.TransactionType
import com.ledge.data.dao.DbGroupTotal
import com.ledge.data.dao.DbReportsTotals
import com.ledge.data.dao.TransactionDao
import com.ledge.data.entity.CategoryTotal
import com.ledge.data.entity.TransactionEntity
import com.ledge.domain.FinancialRules
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

// Kept your existing clean UI response model intact
data class DashboardSummary(
    val income: Long,
    val expense: Long,
    val borrowed: Long,
    val debtPaid: Long,
    val outstandingDebt: Long,
    val lent: Long,
    val lentRepaid: Long,
    val outstandingLent: Long,
    val balance: Long,
    val netBalance: Long
)

@Singleton
class TransactionRepository @Inject constructor(
    private val transactionDao: TransactionDao
) {

    fun getAllTransactions(): Flow<List<TransactionEntity>> =
        transactionDao.getAllTransactions()

    fun getTransactionsForPeriod(startDate: Long, endDate: Long): Flow<List<TransactionEntity>> {
        return transactionDao.getTransactionsForPeriod(startDate, endDate)
    }

    fun getFilteredTransactionsForPeriodAndCategories(
        startDate: Long,
        endDate: Long,
        type: TransactionType?,
        categories: List<String>
    ): Flow<List<TransactionEntity>> {
        return transactionDao.getFilteredTransactionsForPeriodAndCategories(
            startDate, endDate, type, categories, categories.size
        )
    }

    fun getRecentTransactions(): Flow<List<TransactionEntity>> = transactionDao.getRecentTransactions()

    fun getRecentTransactionsForPeriod(startDate: Long, endDate: Long): Flow<List<TransactionEntity>> {
        return transactionDao.getRecentTransactionsForPeriod(startDate, endDate)
    }

    fun getExpenseByCategory(): Flow<List<CategoryTotal>> = transactionDao.observeExpenseByCategory()

    fun getExpenseByCategoryForPeriod(startDate: Long, endDate: Long): Flow<List<CategoryTotal>> {
        return transactionDao.observeExpenseByCategoryForPeriod(startDate, endDate)
    }

    fun getCategoryTotalsForPeriod(startDate: Long, endDate: Long): Flow<List<CategoryTotal>> {
        return getExpenseByCategoryForPeriod(startDate, endDate)
    }

    /*
    ---------------------------------------------------
    DEFRAGMENTED METRIC COMPUTATIONS VIA KOTLIN CORE
    ---------------------------------------------------
    */
    private fun calculateSummary(totalsList: List<DbGroupTotal>): DashboardSummary {
        var income = 0L
        var expense = 0L
        var borrowed = 0L
        var debtPaid = 0L
        var lent = 0L
        var lentRecovered = 0L

        totalsList.forEach { item ->
            val isCredit = item.type == TransactionType.CREDIT
            val isDebit = item.type == TransactionType.DEBIT

            // 1. Income Tracking
            if (isCredit) {
                income += item.totalAmount
            }

            // 2. Base Expense Tracking (Exclude Debt/EMI)
            if (isDebit && FinancialRules.shouldIncludeInExpense(item.category)) {
                expense += item.totalAmount
            }

            // 3. Borrowed Money Tracking
            if ((isCredit && FinancialRules.isBorrowedMoney(item.category)) ||
                (isDebit && FinancialRules.isCreditMode(item.category) && !FinancialRules.isDebtPayment(item.category))) {
                borrowed += item.totalAmount
            }

            // 4. Debt Repayments (EMI/Loans)
            if (isDebit && FinancialRules.isDebtPayment(item.category)) {
                debtPaid += item.totalAmount
            }

            // 5. Money Lent
            if (isDebit && FinancialRules.isLending(item.category)) {
                lent += item.totalAmount
            }

            // 6. Lending Recoveries
            if (isCredit && FinancialRules.isRepaymentReceived(item.category)) {
                lentRecovered += item.totalAmount
            }
        }

        val outstandingDebt = (borrowed - debtPaid).coerceAtLeast(0L)
        val outstandingLent = (lent - lentRecovered).coerceAtLeast(0L)
        val balance = income - expense
        val netBalance = balance - outstandingDebt + outstandingLent

        return DashboardSummary(
            income = income,
            expense = expense,
            borrowed = borrowed,
            debtPaid = debtPaid,
            outstandingDebt = outstandingDebt,
            lent = lent,
            lentRepaid = lentRecovered,
            outstandingLent = outstandingLent,
            balance = balance,
            netBalance = netBalance
        )
    }

    fun observeDashboardSummary(): Flow<DashboardSummary> {
        return transactionDao.observeRawGroupTotals().map { calculateSummary(it) }
    }

    fun observeDashboardSummaryForPeriod(startDate: Long, endDate: Long): Flow<DashboardSummary> {
        return transactionDao.observeRawGroupTotalsForPeriod(startDate, endDate).map { calculateSummary(it) }
    }

    fun observeReportsTotals(
        startDate: Long,
        endDate: Long,
        type: TransactionType?,
        categories: List<String>
    ): Flow<DbReportsTotals> {
        return transactionDao.observeReportsTotals(startDate, endDate, type, categories, categories.size)
    }

    fun observeBorrowedAmountForPeriod(startDate: Long, endDate: Long): Flow<Long> {
        return observeDashboardSummaryForPeriod(startDate, endDate).map { it.borrowed }
    }

    fun observeDebtPaymentsForPeriod(startDate: Long, endDate: Long): Flow<Long> {
        return observeDashboardSummaryForPeriod(startDate, endDate).map { it.debtPaid }
    }

    fun observeOutstandingDebtForPeriod(startDate: Long, endDate: Long): Flow<Long> {
        return observeDashboardSummaryForPeriod(startDate, endDate).map { it.outstandingDebt }
    }

    suspend fun insertTransaction(transaction: TransactionEntity) = transactionDao.insertTransaction(transaction)

    suspend fun updateTransaction(transaction: TransactionEntity) = transactionDao.updateTransaction(transaction)

    suspend fun deleteTransaction(transactionId: Int) = transactionDao.deleteTransaction(transactionId)

    suspend fun getAllTransactionsList(): List<TransactionEntity> = transactionDao.getAllTransactionsList()

    suspend fun insertTransactions(transactions: List<TransactionEntity>) = transactionDao.insertTransactions(transactions)

    suspend fun clearTransactions() = transactionDao.clearTransactions()

    fun getPagedTransactions(): Flow<PagingData<TransactionEntity>> {
        return Pager(
            config = PagingConfig(pageSize = 20, prefetchDistance = 5, enablePlaceholders = false),
            pagingSourceFactory = { transactionDao.getPagedTransactions() }
        ).flow
    }

    fun searchPagedTransactions(query: String): Flow<PagingData<TransactionEntity>> {
        return Pager(
            config = PagingConfig(pageSize = 20, prefetchDistance = 5, enablePlaceholders = false),
            pagingSourceFactory = { transactionDao.searchPagedTransactions(query) }
        ).flow
    }
}