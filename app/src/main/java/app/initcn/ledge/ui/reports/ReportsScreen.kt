package app.initcn.ledge.ui.reports

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Print
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.initcn.ledge.core.buildReportHtml
import app.initcn.ledge.core.printReport
import app.initcn.ledge.core.toRowData
import app.initcn.ledge.domain.PeriodDateResolver
import app.initcn.ledge.ui.components.core.LedgeScaffold
import app.initcn.ledge.ui.components.core.LedgeScreenTitle
import app.initcn.ledge.ui.settings.SettingsViewModel

@Composable
fun ReportsScreen(
    paddingValues: PaddingValues, // Dynamic scaffolding layout properties from the master container
    viewModel: ReportsViewModel = hiltViewModel(),
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val settingsUiState by settingsViewModel.uiState.collectAsStateWithLifecycle()
    val currency = settingsUiState.currency

    val currentCategories = remember(uiState.selectedType) {
        if (uiState.selectedType == app.initcn.ledge.core.TransactionType.CREDIT) {
            viewModel.categories.credit
        } else {
            viewModel.categories.debit
        }
    }

    val mappedRowData = remember(uiState.transactions, currency) {
        uiState.transactions.map { it.toRowData(currency) }
    }

    val dateRange = remember(uiState.periodFilter) {
        PeriodDateResolver.resolve(uiState.periodFilter)
    }

    LedgeScaffold { _ ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                // 🛠️ UNIFIED OPTION B: Removed parent .padding(paddingValues) to allow absolute infinite scroll canvas tracks
                .padding(horizontal = 16.dp),
            // 🛠️ UNIFIED OPTION B: Map scaffold top safe status bounds and bottom navigation offsets directly inside content padding
            contentPadding = PaddingValues(
                top = paddingValues.calculateTopPadding() + 12.dp,
                bottom = paddingValues.calculateBottomPadding() + 88.dp // Clean clearance spacing track beneath the floating bar panel
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        LedgeScreenTitle(title = "Financial Reports")
                    }

                    IconButton(
                        onClick = {
                            val htmlReport = buildReportHtml(
                                transactions = uiState.transactions,
                                totalDebit = uiState.totalDebit,
                                totalCredit = uiState.totalCredit,
                                balance = uiState.balance,
                                fromDate = dateRange.first,
                                toDate = dateRange.second,
                                isDarkTheme = false
                            )
                            printReport(context, htmlReport)
                        },
                        enabled = uiState.transactions.isNotEmpty()
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Print,
                            contentDescription = "Print financial spreadsheet document report"
                        )
                    }
                }
            }

            // 1. FILTERS MODULE
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

            // 2. SUMMARY METRICS MODULE
            item {
                ReportsSummary(
                    transactionCount = uiState.transactions.size,
                    totalCredit = uiState.totalCredit,
                    totalDebit = uiState.totalDebit,
                    balance = uiState.balance,
                    fromDate = dateRange.first,
                    toDate = dateRange.second,
                    currency = currency
                )
            }

            // 3. FILTERED TRANSACTIONS LIST MODULE
            reportsTransactionList(transactions = mappedRowData)
        }
    }
}