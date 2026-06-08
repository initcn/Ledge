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
import com.ledge.core.buildReportHtml
import com.ledge.core.printReport
import com.ledge.core.toRowData
import com.ledge.core.DashboardPeriod
import com.ledge.core.TransactionType
import com.ledge.ui.components.core.LedgeScaffold
import com.ledge.ui.components.core.LedgeScreenTitle
import com.ledge.ui.settings.SettingsViewModel

@Composable
fun ReportsScreen(
    viewModel: ReportsViewModel = hiltViewModel(),
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val settingsUiState by settingsViewModel.uiState.collectAsStateWithLifecycle()
    val currency = settingsUiState.currency

    // Resolve Category Filters dynamically based on Type State
    val currentCategories = when (uiState.selectedType) {
        TransactionType.DEBIT -> viewModel.categories.debit
        TransactionType.CREDIT -> viewModel.categories.credit
        else -> viewModel.categories.debit + viewModel.categories.credit
    }

    /*
    ---------------------------------------------------
    REPORT DATE RANGE CALCULATIONS
    ---------------------------------------------------
    */
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

    // Transform entities to formatted row displays
    val transactionRows = remember(uiState.transactions, currency) {
        uiState.transactions.map { it.toRowData(currency) }
    }

    /*
    ---------------------------------------------------
    UI LAYOUT
    ---------------------------------------------------
    */
    LedgeScaffold { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // SCREEN TITLE
            item {
                LedgeScreenTitle(title = "Reports")
            }

            // FILTER FLOW ROWS
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

            // SUMMARY CARD
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

            // PRINT ACTION BUTTON
            item {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
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

            // FILTERED TRANSACTION LIST
            reportsTransactionList(transactions = transactionRows)
        }
    }
}