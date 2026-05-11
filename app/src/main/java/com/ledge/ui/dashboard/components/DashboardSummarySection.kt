package com.ledge.ui.dashboard.components

import androidx.compose.foundation.lazy.LazyListScope

import com.ledge.core.format.CurrencyFormatter
import com.ledge.core.model.CurrencyType

import com.ledge.data.repository.BudgetProgress

import com.ledge.domain.model.DashboardData

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

    val data =
        dashboardData ?: return

    item {

        BalanceCard(

            totalBalance =

                CurrencyFormatter.format(

                    amount =
                        data.totalBalance,

                    currency =
                        currency
                ),

            totalIncome =

                CurrencyFormatter.format(

                    amount =
                        data.totalIncome,

                    currency =
                        currency
                ),

            totalExpense =

                CurrencyFormatter.format(

                    amount =
                        data.totalExpense,

                    currency =
                        currency
                )
        )
    }

    if (data.outstandingDebt > 0L) {

        item {

            DebtCard(

                outstandingDebt =

                    CurrencyFormatter.format(

                        amount =
                            data.outstandingDebt,

                        currency =
                            currency
                    )
            )
        }
    }

    if (data.outstandingLent > 0L) {

        item {

            LendingCard(

                outstandingLent =

                    CurrencyFormatter.format(

                        amount =
                            data.outstandingLent,

                        currency =
                            currency
                    )
            )
        }
    }

    if (budgetMode) {

        if (budgets.isNotEmpty()) {

            item {

                BudgetOverviewCard(

                    budgets = budgets
                )
            }
        }

    } else {

        if (
            data.categoryTotals
                .isNotEmpty()
        ) {

            item {

                CategorySpendingCard(

                    categoryData =
                        data.categoryTotals
                )
            }
        }
    }
}