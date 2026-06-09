package com.ledge.ui.dashboard

import androidx.compose.foundation.lazy.LazyListScope
import com.ledge.core.CurrencyType
import com.ledge.core.LedgeTextFormatter
import com.ledge.data.repository.BudgetProgress
import com.ledge.domain.DashboardData
import com.ledge.ui.components.cards.BalanceCard
import com.ledge.ui.components.cards.BudgetOverviewCard
import com.ledge.ui.components.cards.CategorySpendingCard
import com.ledge.ui.components.cards.DebtCard
import com.ledge.ui.components.cards.LendingCard

fun LazyListScope.dashboardSummarySection(
    dashboardData: DashboardData?,
    budgets: List<BudgetProgress>,
    budgetMode: Boolean,
    currency: CurrencyType
) {
    val data = dashboardData ?: return

    item {
        BalanceCard(
            totalBalance = LedgeTextFormatter.formatCurrency(
                amount = data.totalBalance,
                currency = currency
            ),
            totalIncome = LedgeTextFormatter.formatCurrency(
                amount = data.totalIncome,
                currency = currency
            ),
            totalExpense = LedgeTextFormatter.formatCurrency(
                amount = data.totalExpense,
                currency = currency
            )
        )
    }

    if (data.outstandingDebt > 0L) {
        item {
            DebtCard(
                outstandingDebt = LedgeTextFormatter.formatCurrency(
                    amount = data.outstandingDebt,
                    currency = currency
                )
            )
        }
    }

    if (data.outstandingLent > 0L) {
        item {
            LendingCard(
                outstandingLent = LedgeTextFormatter.formatCurrency(
                    amount = data.outstandingLent,
                    currency = currency
                )
            )
        }
    }

    if (budgetMode) {
        if (budgets.isNotEmpty()) {
            item {
                BudgetOverviewCard(budgets = budgets)
            }
        }
    } else {
        if (data.categoryTotals.isNotEmpty()) {
            item {
                CategorySpendingCard(categoryData = data.categoryTotals)
            }
        }
    }
}