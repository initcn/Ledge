package com.ledge.ui.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ledge.core.DashboardPeriod
import com.ledge.core.DateUtils
import com.ledge.core.LedgeTextFormatter
import com.ledge.domain.PeriodFilter
import com.ledge.ui.budget.BudgetViewModel
import com.ledge.ui.components.core.LedgeScaffold
import com.ledge.ui.settings.SettingsViewModel

@Composable
fun DashboardScreen(
    paddingValues: PaddingValues,
    viewModel: DashboardViewModel = hiltViewModel(),
    budgetViewModel: BudgetViewModel = hiltViewModel(),
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    val dashboardData by viewModel.dashboardData.collectAsStateWithLifecycle()
    val periodFilter by viewModel.periodFilter.collectAsStateWithLifecycle()
    val budgets by budgetViewModel.budgets.collectAsStateWithLifecycle()
    val settingsUiState by settingsViewModel.uiState.collectAsStateWithLifecycle()

    val budgetMode = settingsUiState.budgetMode
    val currency = settingsUiState.currency

    val periodText = remember(periodFilter) {
        buildDashboardPeriodText(periodFilter)
    }

    // CENTRALIZED SCAFFOLDING - Standardized screen container
    LedgeScaffold { _ ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                DashboardHeader(
                    periodFilter = periodFilter,
                    onPeriodSelected = viewModel::setPeriodFilter
                )
            }

            periodText?.let { text ->
                item {
                    Text(
                        text = text,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // SUMMARY CARDS SECTION
            dashboardSummarySection(
                dashboardData = dashboardData,
                budgets = budgets,
                budgetMode = budgetMode,
                currency = currency
            )

            // RECENT TRANSACTIONS SECTION
            recentTransactionsSection(
                transactions = dashboardData?.recentTransactions ?: emptyList()
            )
        }
    }
}


// DEFRAGMENTED INLINE HELPERS
private fun buildDashboardPeriodText(periodFilter: PeriodFilter): String? {
    return when (periodFilter.period) {
        DashboardPeriod.ALL -> null
        DashboardPeriod.DAY -> {
            "Today • ${LedgeTextFormatter.formatAbsoluteDate(System.currentTimeMillis())}"
        }

        DashboardPeriod.WEEK -> {
            val start = DateUtils.startOfWeek()
            val end = System.currentTimeMillis()
            "Week • ${LedgeTextFormatter.formatAbsoluteDate(start)} → ${
                LedgeTextFormatter.formatAbsoluteDate(
                    end
                )
            }"
        }

        DashboardPeriod.MONTH -> {
            val start = DateUtils.startOfMonth()
            val end = System.currentTimeMillis()
            "Month • ${LedgeTextFormatter.formatAbsoluteDate(start)} → ${
                LedgeTextFormatter.formatAbsoluteDate(
                    end
                )
            }"
        }

        DashboardPeriod.CUSTOM -> {
            val from = periodFilter.from ?: 0L
            val to = periodFilter.to ?: 0L
            "Custom • ${LedgeTextFormatter.formatAbsoluteDate(from)} → ${
                LedgeTextFormatter.formatAbsoluteDate(
                    to
                )
            }"
        }
    }
}