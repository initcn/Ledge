package app.initcn.ledge.ui.dashboard

import androidx.compose.foundation.lazy.LazyListScope
import app.initcn.ledge.core.CurrencyType
import app.initcn.ledge.core.LedgeTextFormatter
import app.initcn.ledge.data.repository.BudgetProgress
import app.initcn.ledge.domain.DashboardData
import app.initcn.ledge.ui.components.cards.BalanceCard
import app.initcn.ledge.ui.components.cards.BudgetOverviewCard
import app.initcn.ledge.ui.components.cards.CategorySpendingCard
import app.initcn.ledge.ui.components.cards.DebtCard
import app.initcn.ledge.ui.components.cards.LendingCard

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