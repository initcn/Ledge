package app.initcn.ledge.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import app.initcn.ledge.core.TransactionType
import app.initcn.ledge.data.entity.CategoryTotal
import app.initcn.ledge.data.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

// Lightweight model to return raw groups to the repository
data class DbGroupTotal(
    val type: TransactionType,
    val category: String,
    val totalAmount: Long
)

// ADD THIS LIGHTWEIGHT RETURN MODEL TO REPLACE THE DELETED FILE
data class DbReportsTotals(
    val totalCredit: Long,
    val totalDebit: Long
)

@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity)

    @Update
    suspend fun updateTransaction(transaction: TransactionEntity)

    @Query("DELETE FROM transactions WHERE id = :transactionId")
    suspend fun deleteTransaction(transactionId: Int)

    @Query("SELECT * FROM transactions ORDER BY createdAt DESC")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE createdAt BETWEEN :startDate AND :endDate ORDER BY createdAt DESC")
    fun getTransactionsForPeriod(startDate: Long, endDate: Long): Flow<List<TransactionEntity>>

    @Query(
        """
        SELECT * FROM transactions
        WHERE createdAt BETWEEN :startDate AND :endDate
        AND (:type IS NULL OR type = :type)
        AND (:categoriesSize = 0 OR category IN (:categories))
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

    @Query("SELECT * FROM transactions ORDER BY createdAt DESC LIMIT 5")
    fun getRecentTransactions(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE createdAt BETWEEN :startDate AND :endDate ORDER BY createdAt DESC LIMIT 5")
    fun getRecentTransactionsForPeriod(
        startDate: Long,
        endDate: Long
    ): Flow<List<TransactionEntity>>

    @Query(
        """
        SELECT type, category, SUM(amount) as totalAmount 
        FROM transactions 
        GROUP BY type, category
    """
    )
    fun observeRawGroupTotals(): Flow<List<DbGroupTotal>>

    @Query(
        """
        SELECT type, category, SUM(amount) as totalAmount 
        FROM transactions 
        WHERE createdAt BETWEEN :startDate AND :endDate
        GROUP BY type, category
    """
    )
    fun observeRawGroupTotalsForPeriod(startDate: Long, endDate: Long): Flow<List<DbGroupTotal>>

    @Query(
        """
        SELECT category, SUM(amount) AS total 
        FROM transactions 
        WHERE type = 'DEBIT' 
        GROUP BY category 
        ORDER BY total DESC
    """
    )
    fun observeExpenseByCategory(): Flow<List<CategoryTotal>>

    @Query(
        """
        SELECT category, SUM(amount) AS total 
        FROM transactions 
        WHERE type = 'DEBIT' AND createdAt BETWEEN :startDate AND :endDate
        GROUP BY category 
        ORDER BY total DESC
    """
    )
    fun observeExpenseByCategoryForPeriod(startDate: Long, endDate: Long): Flow<List<CategoryTotal>>

    // UPDATED TO RETURN THE LOCAL DB DATA CLASS
    @Query(
        """
        SELECT 
            COALESCE(SUM(CASE WHEN type = 'CREDIT' THEN amount ELSE 0 END), 0) AS totalCredit,
            COALESCE(SUM(CASE WHEN type = 'DEBIT' THEN amount ELSE 0 END), 0) AS totalDebit
        FROM transactions
        WHERE createdAt BETWEEN :startDate AND :endDate
        AND (:type IS NULL OR type = :type)
        AND (:categoriesSize = 0 OR category IN (:categories))
    """
    )
    fun observeReportsTotals(
        startDate: Long,
        endDate: Long,
        type: TransactionType?,
        categories: List<String>,
        categoriesSize: Int
    ): Flow<DbReportsTotals> // <-- FIXED RETURN TYPE HERE

    @Query("SELECT * FROM transactions")
    suspend fun getAllTransactionsList(): List<TransactionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransactions(transactions: List<TransactionEntity>)

    @Query("DELETE FROM transactions")
    suspend fun clearTransactions()

    @Query("SELECT * FROM transactions ORDER BY createdAt DESC")
    fun getPagedTransactions(): PagingSource<Int, TransactionEntity>

    @Query(
        """
        SELECT * FROM transactions
        WHERE (note LIKE '%' || :query || '%' OR category LIKE '%' || :query || '%' OR mode LIKE '%' || :query || '%')
        ORDER BY createdAt DESC
    """
    )
    fun searchPagedTransactions(query: String): PagingSource<Int, TransactionEntity>
}