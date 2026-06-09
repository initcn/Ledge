package app.initcn.ledge.ui.components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.initcn.ledge.core.LedgeTextFormatter
import app.initcn.ledge.data.entity.CategoryTotal
import app.initcn.ledge.data.repository.BudgetProgress
import app.initcn.ledge.ui.components.core.LedgeCard
import app.initcn.ledge.ui.components.core.LedgeCardVariant
import app.initcn.ledge.ui.theme.LedgeTheme
import app.initcn.ledge.ui.theme.expense
import app.initcn.ledge.ui.theme.income
import app.initcn.ledge.ui.theme.textSecondary

@Composable
fun BalanceCard(
    totalBalance: String,
    totalIncome: String,
    totalExpense: String
) {
    LedgeCard(
        modifier = Modifier.fillMaxWidth(),
        variant = LedgeCardVariant.DASHBOARD_CONTAINER,
        containerColor = LedgeTheme.surfaces.surfaceHighest,
        elevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "Total Balance",
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = totalBalance,
                style = MaterialTheme.typography.headlineLarge
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(text = "Income")
                    Text(
                        text = totalIncome,
                        color = MaterialTheme.colorScheme.income
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(text = "Expense")
                    Text(
                        text = totalExpense,
                        color = MaterialTheme.colorScheme.expense
                    )
                }
            }
        }
    }
}

@Composable
fun BudgetOverviewCard(
    budgets: List<BudgetProgress>
) {
    LedgeCard(
        modifier = Modifier.fillMaxWidth(),
        variant = LedgeCardVariant.DASHBOARD_CONTAINER,
        containerColor = LedgeTheme.surfaces.surfaceHigh,
        elevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "Budget Overview",
                style = MaterialTheme.typography.titleLarge
            )

            budgets.take(5).forEach { budget ->
                val colorScheme = MaterialTheme.colorScheme
                val progressColor = when {
                    budget.isOverBudget -> colorScheme.expense
                    budget.progress >= 0.8f -> colorScheme.tertiary
                    else -> colorScheme.income
                }

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = budget.category)
                        Text(
                            text = "${LedgeTextFormatter.formatCurrency(budget.spentAmount)} / ${
                                LedgeTextFormatter.formatCurrency(
                                    budget.budgetAmount
                                )
                            }",
                            color = progressColor
                        )
                    }

                    LinearProgressIndicator(
                        progress = { budget.progress.coerceAtMost(1f) },
                        modifier = Modifier.fillMaxWidth(),
                        color = progressColor,
                        trackColor = LedgeTheme.surfaces.surfaceHighest,
                        strokeCap = ProgressIndicatorDefaults.LinearStrokeCap
                    )

                    Text(
                        text = if (budget.isOverBudget) {
                            "Overspent by ${LedgeTextFormatter.formatCurrency(budget.remainingAmount * -1)}"
                        } else {
                            "Remaining ${LedgeTextFormatter.formatCurrency(budget.remainingAmount)}"
                        },
                        color = progressColor
                    )
                }
            }
        }
    }
}

@Composable
fun CategorySpendingCard(
    categoryData: List<CategoryTotal>
) {
    val maxAmount: Long = categoryData.maxOfOrNull { it.total } ?: 1L

    LedgeCard(
        modifier = Modifier.fillMaxWidth(),
        variant = LedgeCardVariant.DASHBOARD_CONTAINER,
        containerColor = LedgeTheme.surfaces.surfaceHigh,
        elevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "Category Spending",
                style = MaterialTheme.typography.titleLarge
            )

            categoryData.forEach { item ->
                val progress = if (maxAmount == 0L) {
                    0f
                } else {
                    item.total.toFloat() / maxAmount.toFloat()
                }

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = item.category,
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Text(
                            text = LedgeTextFormatter.formatCurrency(item.total),
                            color = MaterialTheme.colorScheme.expense
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .background(
                                LedgeTheme.surfaces.surfaceHighest,
                                RoundedCornerShape(50)
                            )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(progress.coerceIn(0f, 1f))
                                .height(10.dp)
                                .background(
                                    MaterialTheme.colorScheme.expense,
                                    RoundedCornerShape(50)
                                )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DebtCard(
    outstandingDebt: String
) {
    LedgeCard(
        modifier = Modifier.fillMaxWidth(),
        variant = LedgeCardVariant.DASHBOARD_CONTAINER,
        containerColor = LedgeTheme.surfaces.surfaceHigh,
        elevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Outstanding Debt",
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = outstandingDebt,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.expense
            )

            Text(
                text = "Borrowed + credit purchases - repayments",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.textSecondary
            )
        }
    }
}

@Composable
fun LendingCard(
    outstandingLent: String
) {
    LedgeCard(
        modifier = Modifier.fillMaxWidth(),
        variant = LedgeCardVariant.DASHBOARD_CONTAINER,
        containerColor = LedgeTheme.surfaces.surfaceHigh,
        elevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Outstanding Lent",
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = outstandingLent,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.income
            )

            Text(
                text = "Money lent but not yet recovered",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.textSecondary
            )
        }
    }
}