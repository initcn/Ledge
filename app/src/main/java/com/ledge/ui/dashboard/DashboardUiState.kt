package com.ledge.ui.dashboard

import com.ledge.core.model.PeriodFilter
import com.ledge.data.entity.CategoryTotal
import com.ledge.data.entity.TransactionEntity

data class DashboardUiState(

    val totalIncome: Long = 0L,
    val totalExpense: Long = 0L,

    val totalBalance: Long = 0L,

    val outstandingDebt: Long = 0L,

    val totalLent: Long = 0L,
    val totalRecovered: Long = 0L,
    val outstandingLent: Long = 0L,

    val recentTransactions: List<TransactionEntity> = emptyList(),

    val categorySpending: List<CategoryTotal> = emptyList(),

    val periodFilter: PeriodFilter = PeriodFilter()
)