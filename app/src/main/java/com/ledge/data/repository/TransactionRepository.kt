package com.ledge.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.ledge.core.model.TransactionType
import com.ledge.data.dao.TransactionDao
import com.ledge.data.entity.CategoryTotal
import com.ledge.data.entity.TransactionEntity
import com.ledge.data.model.ReportsTotals
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

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

    /*
    ---------------------------------------------------
    TRANSACTIONS
    ---------------------------------------------------
    */

    fun getAllTransactions():
            Flow<List<TransactionEntity>> {

        return transactionDao
            .getAllTransactions()
    }

    fun getTransactionsForPeriod(

        startDate: Long,

        endDate: Long

    ): Flow<List<TransactionEntity>> {

        return transactionDao
            .getTransactionsForPeriod(

                startDate = startDate,

                endDate = endDate
            )
    }

    fun getFilteredTransactionsForPeriodAndCategories(

        startDate: Long,

        endDate: Long,

        type: TransactionType?,

        categories: List<String>

    ): Flow<List<TransactionEntity>> {

        return transactionDao
            .getFilteredTransactionsForPeriodAndCategories(

                startDate = startDate,

                endDate = endDate,

                type = type,

                categories = categories,

                categoriesSize = categories.size
            )
    }

    /*
    ---------------------------------------------------
    RECENT TRANSACTIONS
    ---------------------------------------------------
    */

    fun getRecentTransactions():
            Flow<List<TransactionEntity>> {

        return transactionDao
            .getRecentTransactions()
    }

    fun getRecentTransactionsForPeriod(

        startDate: Long,

        endDate: Long

    ): Flow<List<TransactionEntity>> {

        return transactionDao
            .getRecentTransactionsForPeriod(

                startDate = startDate,

                endDate = endDate
            )
    }

    /*
    ---------------------------------------------------
    CATEGORY ANALYTICS
    ---------------------------------------------------
    */

    fun getExpenseByCategory():
            Flow<List<CategoryTotal>> {

        return transactionDao
            .observeExpenseByCategory()
    }

    fun getExpenseByCategoryForPeriod(

        startDate: Long,

        endDate: Long

    ): Flow<List<CategoryTotal>> {

        return transactionDao
            .observeExpenseByCategoryForPeriod(

                startDate = startDate,

                endDate = endDate
            )
    }

    fun getCategoryTotalsForPeriod(

        startDate: Long,

        endDate: Long

    ): Flow<List<CategoryTotal>> {

        return getExpenseByCategoryForPeriod(

            startDate = startDate,

            endDate = endDate
        )
    }

    /*
    ---------------------------------------------------
    DASHBOARD
    ---------------------------------------------------
    */

    fun observeDashboardSummary():
            Flow<DashboardSummary> {

        return transactionDao
            .observeDashboardTotals()

            .map { totals ->

                val outstandingDebt =

                    (
                            totals.borrowed -
                                    totals.debtPaid
                            ).coerceAtLeast(0L)

                val outstandingLent =

                    (
                            totals.lent -
                                    totals.lentRecovered
                            ).coerceAtLeast(0L)

                val balance =
                    totals.income - totals.expense

                val netBalance =

                    balance -
                            outstandingDebt +
                            outstandingLent

                DashboardSummary(

                    income =
                        totals.income,

                    expense =
                        totals.expense,

                    borrowed =
                        totals.borrowed,

                    debtPaid =
                        totals.debtPaid,

                    outstandingDebt =
                        outstandingDebt,

                    lent =
                        totals.lent,

                    lentRepaid =
                        totals.lentRecovered,

                    outstandingLent =
                        outstandingLent,

                    balance =
                        balance,

                    netBalance =
                        netBalance
                )
            }
    }

    fun observeDashboardSummaryForPeriod(

        startDate: Long,

        endDate: Long

    ): Flow<DashboardSummary> {

        return transactionDao

            .observeDashboardTotalsForPeriod(

                startDate = startDate,

                endDate = endDate
            )

            .map { totals ->

                val outstandingDebt =

                    (
                            totals.borrowed -
                                    totals.debtPaid
                            ).coerceAtLeast(0L)

                val outstandingLent =

                    (
                            totals.lent -
                                    totals.lentRecovered
                            ).coerceAtLeast(0L)

                val balance =
                    totals.income - totals.expense

                val netBalance =

                    balance -
                            outstandingDebt +
                            outstandingLent

                DashboardSummary(

                    income =
                        totals.income,

                    expense =
                        totals.expense,

                    borrowed =
                        totals.borrowed,

                    debtPaid =
                        totals.debtPaid,

                    outstandingDebt =
                        outstandingDebt,

                    lent =
                        totals.lent,

                    lentRepaid =
                        totals.lentRecovered,

                    outstandingLent =
                        outstandingLent,

                    balance =
                        balance,

                    netBalance =
                        netBalance
                )
            }
    }

    /*
    ---------------------------------------------------
    REPORTS
    ---------------------------------------------------
    */

    fun observeReportsTotals(

        startDate: Long,

        endDate: Long,

        type: TransactionType?,

        categories: List<String>

    ): Flow<ReportsTotals> {

        return transactionDao
            .observeReportsTotals(

                startDate = startDate,

                endDate = endDate,

                type = type,

                categories = categories,

                categoriesSize = categories.size
            )
    }

    /*
    ---------------------------------------------------
    DEBT ANALYTICS
    ---------------------------------------------------
    */

    fun observeBorrowedAmountForPeriod(

        startDate: Long,

        endDate: Long

    ): Flow<Long> {

        return transactionDao
            .observeBorrowedAmountForPeriod(

                startDate = startDate,

                endDate = endDate
            )
    }

    fun observeDebtPaymentsForPeriod(

        startDate: Long,

        endDate: Long

    ): Flow<Long> {

        return transactionDao
            .observeDebtPaymentsForPeriod(

                startDate = startDate,

                endDate = endDate
            )
    }

    fun observeOutstandingDebtForPeriod(

        startDate: Long,

        endDate: Long

    ): Flow<Long> {

        return transactionDao
            .observeOutstandingDebtForPeriod(

                startDate = startDate,

                endDate = endDate
            )
    }

    /*
    ---------------------------------------------------
    CRUD
    ---------------------------------------------------
    */

    suspend fun insertTransaction(

        transaction: TransactionEntity

    ) {

        transactionDao.insertTransaction(
            transaction
        )
    }

    suspend fun updateTransaction(

        transaction: TransactionEntity

    ) {

        transactionDao.updateTransaction(
            transaction
        )
    }

    suspend fun deleteTransaction(

        transactionId: Int

    ) {

        transactionDao.deleteTransaction(
            transactionId
        )
    }

    /*
    ---------------------------------------------------
    BACKUP
    ---------------------------------------------------
    */

    suspend fun getAllTransactionsList():
            List<TransactionEntity> {

        return transactionDao
            .getAllTransactionsList()
    }

    suspend fun insertTransactions(

        transactions:
        List<TransactionEntity>

    ) {

        transactionDao.insertTransactions(
            transactions
        )
    }

    suspend fun clearTransactions() {

        transactionDao.clearTransactions()
    }

    /*
    ---------------------------------------------------
    PAGING
    ---------------------------------------------------
    */

    fun getPagedTransactions():
            Flow<PagingData<TransactionEntity>> {

        return Pager(

            config = PagingConfig(

                pageSize = 20,

                prefetchDistance = 5,

                enablePlaceholders = false
            ),

            pagingSourceFactory = {

                transactionDao
                    .getPagedTransactions()
            }

        ).flow
    }

    /*
    ---------------------------------------------------
    SEARCH
    ---------------------------------------------------
    */

    fun searchPagedTransactions(

        query: String

    ): Flow<PagingData<TransactionEntity>> {

        return Pager(

            config = PagingConfig(

                pageSize = 20,

                prefetchDistance = 5,

                enablePlaceholders = false
            ),

            pagingSourceFactory = {

                transactionDao
                    .searchPagedTransactions(query)
            }

        ).flow
    }
}