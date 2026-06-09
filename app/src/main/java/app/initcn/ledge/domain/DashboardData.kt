package app.initcn.ledge.domain

import androidx.compose.runtime.Immutable
import app.initcn.ledge.data.entity.CategoryTotal
import app.initcn.ledge.ui.components.model.TransactionRowData

@Immutable
data class DashboardData(
    val totalIncome: Long,
    val totalExpense: Long,
    val totalBalance: Long,
    val outstandingDebt: Long,
    val totalLent: Long,
    val totalRecovered: Long,
    val outstandingLent: Long,
    val recentTransactions: List<TransactionRowData>,
    val categoryTotals: List<CategoryTotal>
)