package com.ledge.data.dao

import androidx.paging.PagingSource

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

import com.ledge.core.model.TransactionType

import com.ledge.data.entity.CategoryTotal
import com.ledge.data.entity.TransactionEntity

import com.ledge.data.model.DashboardTotals
import com.ledge.data.model.PeriodDashboardTotals
import com.ledge.data.model.ReportsTotals

import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    /*
    ---------------------------------------------------
    CRUD
    ---------------------------------------------------
    */

    @Insert(
        onConflict =
            OnConflictStrategy.REPLACE
    )
    suspend fun insertTransaction(

        transaction: TransactionEntity
    )

    @Update
    suspend fun updateTransaction(

        transaction: TransactionEntity
    )

    @Query(
        "DELETE FROM transactions WHERE id = :transactionId"
    )
    suspend fun deleteTransaction(

        transactionId: Int
    )

    /*
    ---------------------------------------------------
    BASIC TRANSACTION QUERIES
    ---------------------------------------------------
    */

    @Query(
        """
        SELECT * FROM transactions
        ORDER BY createdAt DESC
        """
    )
    fun getAllTransactions():
            Flow<List<TransactionEntity>>

    @Query(
        """
        SELECT * FROM transactions

        WHERE createdAt
        BETWEEN :startDate AND :endDate

        ORDER BY createdAt DESC
        """
    )
    fun getTransactionsForPeriod(

        startDate: Long,

        endDate: Long

    ): Flow<List<TransactionEntity>>

    @Query(
        """
        SELECT * FROM transactions

        WHERE category = :category

        ORDER BY createdAt DESC
        """
    )
    fun getTransactionsByCategory(

        category: String

    ): Flow<List<TransactionEntity>>

    @Query(
        """
        SELECT * FROM transactions

        WHERE mode = :mode

        ORDER BY createdAt DESC
        """
    )
    fun getTransactionsByMode(

        mode: String

    ): Flow<List<TransactionEntity>>

    /*
    ---------------------------------------------------
    FILTERED QUERIES
    ---------------------------------------------------
    */

    @Query(
        """
        SELECT * FROM transactions

        WHERE createdAt
        BETWEEN :startDate AND :endDate

        AND (
            :type IS NULL
            OR type = :type
        )

        AND (
            :categoriesSize = 0
            OR category IN (:categories)
        )

        ORDER BY createdAt DESC
        """
    )
    fun getFilteredTransactionsForPeriodAndCategories(

        startDate: Long,

        endDate: Long,

        type: TransactionType?,

        categories: List<String>,

        categoriesSize: Int

    ): Flow<List<TransactionEntity>>

    /*
    ---------------------------------------------------
    RECENT TRANSACTIONS
    ---------------------------------------------------
    */

    @Query(
        """
        SELECT * FROM transactions

        ORDER BY createdAt DESC

        LIMIT 5
        """
    )
    fun getRecentTransactions():
            Flow<List<TransactionEntity>>

    @Query(
        """
        SELECT * FROM transactions

        WHERE createdAt
        BETWEEN :startDate AND :endDate

        ORDER BY createdAt DESC

        LIMIT 5
        """
    )
    fun getRecentTransactionsForPeriod(

        startDate: Long,

        endDate: Long

    ): Flow<List<TransactionEntity>>

    /*
    ---------------------------------------------------
    DASHBOARD TOTALS
    ---------------------------------------------------
    */

    @Query(
        """
        SELECT

            COALESCE(SUM(
                CASE
                    WHEN type = 'CREDIT'
                    THEN amount
                    ELSE 0
                END
            ), 0) AS income,

            COALESCE(SUM(
                CASE
                    WHEN type = 'DEBIT'
                    AND category NOT IN (
                        'Debt Repayment',
                        'Loan Payment',
                        'EMI'
                    )
                    THEN amount
                    ELSE 0
                END
            ), 0) AS expense,

            COALESCE(SUM(
                CASE
                    WHEN (
                        type = 'CREDIT'
                        AND category IN (
                            'Credit & Debt',
                            'Borrowed Money'
                        )
                    )
                    OR (
                        type = 'DEBIT'
                        AND mode IN (
                            'Credit Line',
                            'Credit Card'
                        )
                        AND category NOT IN (
                            'Debt Repayment',
                            'Loan Payment',
                            'EMI'
                        )
                    )
                    THEN amount
                    ELSE 0
                END
            ), 0) AS borrowed,

            COALESCE(SUM(
                CASE
                    WHEN type = 'DEBIT'
                    AND category IN (
                        'Debt Repayment',
                        'Loan Payment',
                        'EMI'
                    )
                    THEN amount
                    ELSE 0
                END
            ), 0) AS debtPaid,

            COALESCE(SUM(
                CASE
                    WHEN type = 'DEBIT'
                    AND category = 'Money Lent'
                    THEN amount
                    ELSE 0
                END
            ), 0) AS lent,

            COALESCE(SUM(
                CASE
                    WHEN type = 'CREDIT'
                    AND category = 'Repayment Received'
                    THEN amount
                    ELSE 0
                END
            ), 0) AS lentRecovered

        FROM transactions
        """
    )
    fun observeDashboardTotals():
            Flow<DashboardTotals>

    @Query(
        """
        SELECT

            COALESCE(SUM(
                CASE
                    WHEN type = 'CREDIT'
                    THEN amount
                    ELSE 0
                END
            ), 0) AS income,

            COALESCE(SUM(
                CASE
                    WHEN type = 'DEBIT'
                    AND category NOT IN (
                        'Debt Repayment',
                        'Loan Payment',
                        'EMI'
                    )
                    THEN amount
                    ELSE 0
                END
            ), 0) AS expense,

            COALESCE(SUM(
                CASE
                    WHEN (
                        type = 'CREDIT'
                        AND category IN (
                            'Credit & Debt',
                            'Borrowed Money'
                        )
                    )
                    OR (
                        type = 'DEBIT'
                        AND mode IN (
                            'Credit Line',
                            'Credit Card'
                        )
                        AND category NOT IN (
                            'Debt Repayment',
                            'Loan Payment',
                            'EMI'
                        )
                    )
                    THEN amount
                    ELSE 0
                END
            ), 0) AS borrowed,

            COALESCE(SUM(
                CASE
                    WHEN type = 'DEBIT'
                    AND category IN (
                        'Debt Repayment',
                        'Loan Payment',
                        'EMI'
                    )
                    THEN amount
                    ELSE 0
                END
            ), 0) AS debtPaid,

            COALESCE(SUM(
                CASE
                    WHEN type = 'DEBIT'
                    AND category = 'Money Lent'
                    THEN amount
                    ELSE 0
                END
            ), 0) AS lent,

            COALESCE(SUM(
                CASE
                    WHEN type = 'CREDIT'
                    AND category = 'Repayment Received'
                    THEN amount
                    ELSE 0
                END
            ), 0) AS lentRecovered

        FROM transactions

        WHERE createdAt
        BETWEEN :startDate AND :endDate
        """
    )
    fun observeDashboardTotalsForPeriod(

        startDate: Long,

        endDate: Long

    ): Flow<PeriodDashboardTotals>

    /*
    ---------------------------------------------------
    CATEGORY ANALYTICS
    ---------------------------------------------------
    */

    @Query(
        """
        SELECT

            category,

            SUM(amount) AS total

        FROM transactions

        WHERE type = 'DEBIT'

        AND category NOT IN (
            'Debt Repayment',
            'Loan Payment',
            'EMI'
        )

        GROUP BY category

        ORDER BY total DESC
        """
    )
    fun observeExpenseByCategory():
            Flow<List<CategoryTotal>>

    @Query(
        """
        SELECT

            category,

            SUM(amount) AS total

        FROM transactions

        WHERE type = 'DEBIT'

        AND createdAt
        BETWEEN :startDate AND :endDate

        AND category NOT IN (
            'Debt Repayment',
            'Loan Payment',
            'EMI'
        )

        GROUP BY category

        ORDER BY total DESC
        """
    )
    fun observeExpenseByCategoryForPeriod(

        startDate: Long,

        endDate: Long

    ): Flow<List<CategoryTotal>>

    /*
    ---------------------------------------------------
    REPORTS
    ---------------------------------------------------
    */

    @Query(
        """
        SELECT

            COALESCE(SUM(
                CASE
                    WHEN type = 'CREDIT'
                    THEN amount
                    ELSE 0
                END
            ), 0) AS totalCredit,

            COALESCE(SUM(
                CASE
                    WHEN type = 'DEBIT'
                    THEN amount
                    ELSE 0
                END
            ), 0) AS totalDebit

        FROM transactions

        WHERE createdAt
        BETWEEN :startDate AND :endDate

        AND (
            :type IS NULL
            OR type = :type
        )

        AND (
            :categoriesSize = 0
            OR category IN (:categories)
        )
        """
    )
    fun observeReportsTotals(

        startDate: Long,

        endDate: Long,

        type: TransactionType?,

        categories: List<String>,

        categoriesSize: Int

    ): Flow<ReportsTotals>

    /*
    ---------------------------------------------------
    DEBT ANALYTICS
    ---------------------------------------------------
    */

    @Query(
        """
        SELECT COALESCE(SUM(
            CASE
                WHEN (
                    type = 'CREDIT'
                    AND category IN (
                        'Credit & Debt',
                        'Borrowed Money'
                    )
                )
                OR (
                    type = 'DEBIT'
                    AND mode IN (
                        'Credit Line',
                        'Credit Card'
                    )
                    AND category NOT IN (
                        'Debt Repayment',
                        'Loan Payment',
                        'EMI'
                    )
                )
                THEN amount
                ELSE 0
            END
        ), 0)

        FROM transactions

        WHERE createdAt
        BETWEEN :startDate AND :endDate
        """
    )
    fun observeBorrowedAmountForPeriod(

        startDate: Long,

        endDate: Long

    ): Flow<Long>

    @Query(
        """
        SELECT COALESCE(SUM(amount), 0)

        FROM transactions

        WHERE type = 'DEBIT'

        AND category IN (
            'Debt Repayment',
            'Loan Payment',
            'EMI'
        )

        AND createdAt
        BETWEEN :startDate AND :endDate
        """
    )
    fun observeDebtPaymentsForPeriod(

        startDate: Long,

        endDate: Long

    ): Flow<Long>

    /*
    ---------------------------------------------------
    BACKUP
    ---------------------------------------------------
    */

    @Query(
        "SELECT * FROM transactions"
    )
    suspend fun getAllTransactionsList():
            List<TransactionEntity>

    @Insert(
        onConflict =
            OnConflictStrategy.REPLACE
    )
    suspend fun insertTransactions(

        transactions:
        List<TransactionEntity>
    )

    @Query(
        "DELETE FROM transactions"
    )
    suspend fun clearTransactions()

    /*
    ---------------------------------------------------
    PAGING
    ---------------------------------------------------
    */

    @Query(
        """
        SELECT * FROM transactions
        ORDER BY createdAt DESC
        """
    )
    fun getPagedTransactions():
            PagingSource<Int, TransactionEntity>


    /*
    ---------------------------------------------------
    SEARCH
    ---------------------------------------------------
    */

    @Query(
        """
    SELECT * FROM transactions

    WHERE

        (
            note LIKE '%' || :query || '%'
            OR category LIKE '%' || :query || '%'
            OR mode LIKE '%' || :query || '%'
        )

    ORDER BY createdAt DESC
    """
    )
    fun searchPagedTransactions(

        query: String

    ): PagingSource<Int, TransactionEntity>

    /*
    ---------------------------------------------------
    DEBT
    ---------------------------------------------------
    */

    @Query(
        """
    SELECT MAX(

        (

            COALESCE(SUM(
                CASE
                    WHEN (
                        type = 'CREDIT'
                        AND category IN (
                            'Credit & Debt',
                            'Borrowed Money'
                        )
                    )
                    OR (
                        type = 'DEBIT'
                        AND mode IN (
                            'Credit Line',
                            'Credit Card'
                        )
                        AND category NOT IN (
                            'Debt Repayment',
                            'Loan Payment',
                            'EMI'
                        )
                    )
                    THEN amount
                    ELSE 0
                END
            ), 0)

            -

            COALESCE(SUM(
                CASE
                    WHEN type = 'DEBIT'
                    AND category IN (
                        'Debt Repayment',
                        'Loan Payment',
                        'EMI'
                    )
                    THEN amount
                    ELSE 0
                END
            ), 0)

        ),

        0
    )

    FROM transactions

    WHERE createdAt
    BETWEEN :startDate AND :endDate
    """
    )
    fun observeOutstandingDebtForPeriod(

        startDate: Long,

        endDate: Long

    ): Flow<Long>



}