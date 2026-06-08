package com.ledge.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

import com.ledge.data.entity.BudgetEntity

import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {

    @Insert(

        onConflict =
            OnConflictStrategy.REPLACE
    )
    suspend fun insertBudget(

        budget: BudgetEntity
    )

    @Update
    suspend fun updateBudget(

        budget: BudgetEntity
    )

    @Delete
    suspend fun deleteBudget(

        budget: BudgetEntity
    )

    @Query(

        """
        SELECT * FROM budgets
        WHERE month = :month
        AND year = :year
        ORDER BY category ASC
        """
    )
    fun getBudgetsForMonth(

        month: Int,

        year: Int
    ): Flow<List<BudgetEntity>>

    @Query(

        """
        SELECT * FROM budgets
        WHERE category = :category
        AND month = :month
        AND year = :year
        LIMIT 1
        """
    )
    suspend fun getBudgetForCategory(

        category: String,

        month: Int,

        year: Int
    ): BudgetEntity?

    @Query(
        "SELECT * FROM budgets"
    )
    suspend fun getAllBudgets():
            List<BudgetEntity>

    @Insert(
        onConflict =
            OnConflictStrategy.REPLACE
    )
    suspend fun insertBudgets(

        budgets:
        List<BudgetEntity>
    )

    @Query(
        "DELETE FROM budgets"
    )
    suspend fun clearBudgets()
}