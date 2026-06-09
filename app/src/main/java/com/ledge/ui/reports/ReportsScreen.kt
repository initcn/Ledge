package com.ledge.ui.reports

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ledge.core.DashboardPeriod
import com.ledge.core.TransactionType
import com.ledge.core.buildReportHtml
import com.ledge.core.printReport
import com.ledge.core.toRowData
import com.ledge.ui.components.core.LedgeScaffold
import com.ledge.ui.components.core.LedgeScreenTitle
import com.ledge.ui.settings.SettingsViewModel

/**
 * ReportsScreen renders an analytical history view of transactions.
 * It provides multi-select filtering logic across categories, timespans, and transaction types,
 * alongside printing components to export generated HTML tables natively.
 *
 * @param paddingValues Safe window padding boundaries passed down from the AppNavigation Scaffold shell.
 */
@Composable
fun ReportsScreen(
    paddingValues: PaddingValues,
    viewModel: ReportsViewModel = hiltViewModel(),
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    // Observe flows reactively wrapped to tie directly to the lifecycle states
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val settingsUiState by settingsViewModel.uiState.collectAsStateWithLifecycle()
    val currency = settingsUiState.currency

    // NOTE: Dynamic Filter Resolution. Merges categories conditionally based on selected transaction type.
    val currentCategories = when (uiState.selectedType) {
        TransactionType.DEBIT -> viewModel.categories.debit
        TransactionType.CREDIT -> viewModel.categories.credit
        else -> viewModel.categories.debit + viewModel.categories.credit
    }

    // NOTE: Date bounds calculations. Evaluates historical transaction timelines to safely format printing bounds.
    val reportFromDate = if (uiState.periodFilter.period == DashboardPeriod.CUSTOM) {
        uiState.periodFilter.from ?: System.currentTimeMillis()
    } else {
        uiState.transactions.minOfOrNull { it.createdAt } ?: System.currentTimeMillis()
    }

    val reportToDate = if (uiState.periodFilter.period == DashboardPeriod.CUSTOM) {
        uiState.periodFilter.to ?: System.currentTimeMillis()
    } else {
        uiState.transactions.maxOfOrNull { it.createdAt } ?: System.currentTimeMillis()
    }

    // Performance Optimization: Cache converted presentation rows to eliminate computational sorting re-renders
    val transactionRows = remember(uiState.transactions, currency) {
        uiState.transactions.map { it.toRowData(currency) }
    }

    LedgeScaffold { _ ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Prevents content clipping under the top status/action bar layers
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // SECTION 1: Screen Header
            item {
                LedgeScreenTitle(title = "Reports")
            }

            // SECTION 2: Dynamic Granular Category, Type, and Time Filter Trays
            item {
                ReportsFilters(
                    selectedType = uiState.selectedType,
                    selectedCategories = uiState.selectedCategories,
                    currentCategories = currentCategories,
                    periodFilter = uiState.periodFilter,
                    onTypeSelected = viewModel::setType,
                    onCategoryToggle = viewModel::toggleCategory,
                    onPeriodSelected = viewModel::setPeriodFilter
                )
            }

            // SECTION 3: Summary Metric Card (Credit, Debit, and Balance totals)
            item {
                ReportsSummary(
                    transactionCount = uiState.transactions.size,
                    totalCredit = uiState.totalCredit,
                    totalDebit = uiState.totalDebit,
                    balance = uiState.balance,
                    fromDate = reportFromDate,
                    toDate = reportToDate,
                    currency = currency
                )
            }

            // SECTION 4: Native Android Printing Export Trigger
            item {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        // Triggers raw HTML configuration engine strings passing down target filters and transaction items
                        val html = buildReportHtml(
                            transactions = uiState.transactions,
                            totalDebit = uiState.totalDebit,
                            totalCredit = uiState.totalCredit,
                            balance = uiState.balance,
                            fromDate = reportFromDate,
                            toDate = reportToDate
                        )
                        printReport(context, html)
                    }
                ) {
                    Text("Print Report")
                }
            }

            // SECTION 5: Filtered Transaction History Rows Feed
            reportsTransactionList(transactions = transactionRows)
        }
    }
}