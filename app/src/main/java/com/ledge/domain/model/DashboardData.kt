package com.ledge.domain.model

import androidx.compose.runtime.Immutable
import com.ledge.data.entity.CategoryTotal
import com.ledge.ui.components.model.TransactionRowData

@Immutable
data class DashboardData(

    val totalIncome: Long,
    val totalExpense: Long,

    val totalBalance: Long,

    val outstandingDebt: Long,

    val totalLent: Long,
    val totalRecovered: Long,
    val outstandingLent: Long,

    val recentTransactions:
        List<TransactionRowData>,
    val categoryTotals: List<CategoryTotal>
)