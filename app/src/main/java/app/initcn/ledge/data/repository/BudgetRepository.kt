package app.initcn.ledge.data.repository

import app.initcn.ledge.core.DateUtils
import app.initcn.ledge.data.dao.BudgetDao
import app.initcn.ledge.data.entity.BudgetEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject
import javax.inject.Singleton

data class BudgetProgress(
    val category: String,
    val budgetAmount: Long,
    val spentAmount: Long
) {
    val remainingAmount: Long
        get() = budgetAmount - spentAmount

    val progress: Float
        get() = if (budgetAmount <= 0L) 0f else (spentAmount.toFloat() / budgetAmount.toFloat())

    val isOverBudget: Boolean
        get() = spentAmount > budgetAmount
}

@Singleton
class BudgetRepository @Inject constructor(
    private val budgetDao: BudgetDao,
    private val transactionRepository: TransactionRepository
) {

    suspend fun saveBudget(
        category: String,
        limitAmount: Long
    ) {
        val month = DateUtils.currentMonth()
        val year = DateUtils.currentYear() // <-- FIXED CALLED SPECIFICATION

        val existingBudget = budgetDao.getBudgetForCategory(
            category = category,
            month = month,
            year = year
        )

        if (existingBudget != null) {
            budgetDao.updateBudget(
                existingBudget.copy(limitAmount = limitAmount)
            )
        } else {
            budgetDao.insertBudget(
                BudgetEntity(
                    category = category,
                    limitAmount = limitAmount,
                    month = month,
                    year = year
                )
            )
        }
    }

    suspend fun deleteBudget(budget: BudgetEntity) {
        budgetDao.deleteBudget(budget)
    }

    fun getCurrentMonthBudgets(): Flow<List<BudgetEntity>> {
        return budgetDao.getBudgetsForMonth(
            month = DateUtils.currentMonth(),
            year = DateUtils.currentYear() // <-- FIXED CALLED SPECIFICATION
        )
    }

    fun getBudgetProgress(): Flow<List<BudgetProgress>> {
        val startOfMonth = DateUtils.startOfMonth()
        val endOfMonth = DateUtils.endOfMonth()

        return combine(
            getCurrentMonthBudgets(),
            transactionRepository.getExpenseByCategoryForPeriod(
                startDate = startOfMonth,
                endDate = endOfMonth
            )
        ) { budgets, expenses ->
            budgets.map { budget ->
                val spent = expenses.find { it.category == budget.category }?.total ?: 0L
                BudgetProgress(
                    category = budget.category,
                    budgetAmount = budget.limitAmount,
                    spentAmount = spent
                )
            }
        }
    }
}