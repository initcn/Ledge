package com.ledge.ui.reports

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth

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

import com.ledge.core.analytics.buildReportHtml
import com.ledge.core.analytics.printReport

import com.ledge.core.extensions.toRowData

import com.ledge.core.model.DashboardPeriod
import com.ledge.core.model.TransactionType

import com.ledge.ui.components.core.LedgeScreenTitle

import com.ledge.ui.reports.components.ReportsFilters
import com.ledge.ui.reports.components.ReportsSummary
import com.ledge.ui.reports.components.reportsTransactionList

import com.ledge.ui.settings.SettingsViewModel

@Composable
fun ReportsScreen(

    viewModel: ReportsViewModel =
        hiltViewModel(),

    settingsViewModel:
    SettingsViewModel =
        hiltViewModel()

) {

    val context =
        LocalContext.current

    /*
    ---------------------------------------------------
    CATEGORY DATA
    ---------------------------------------------------
    */

    val categoryData =
        viewModel.categories

    /*
    ---------------------------------------------------
    UI STATE
    ---------------------------------------------------
    */

    val uiState by viewModel
        .uiState
        .collectAsStateWithLifecycle()

    val settingsUiState by settingsViewModel
        .uiState
        .collectAsStateWithLifecycle()

    val currency =
        settingsUiState.currency

    /*
    ---------------------------------------------------
    CURRENT CATEGORY FILTERS
    ---------------------------------------------------
    */

    val currentCategories =

        when (uiState.selectedType) {

            TransactionType.DEBIT -> {

                categoryData.debit
            }

            TransactionType.CREDIT -> {

                categoryData.credit
            }

            else -> {

                categoryData.debit +
                        categoryData.credit
            }
        }

    /*
    ---------------------------------------------------
    REPORT DATE RANGE
    ---------------------------------------------------
    */

    val reportFromDate =

        if (

            uiState
                .periodFilter
                .period == DashboardPeriod.CUSTOM

        ) {

            uiState
                .periodFilter
                .from

                ?: System.currentTimeMillis()

        } else {

            uiState.transactions
                .minOfOrNull {

                    it.createdAt
                }

                ?: System.currentTimeMillis()
        }

    val reportToDate =

        if (

            uiState
                .periodFilter
                .period == DashboardPeriod.CUSTOM

        ) {

            uiState
                .periodFilter
                .to

                ?: System.currentTimeMillis()

        } else {

            uiState.transactions
                .maxOfOrNull {

                    it.createdAt
                }

                ?: System.currentTimeMillis()
        }

    /*
    ---------------------------------------------------
    TABLE DATA
    ---------------------------------------------------
    */

    val transactionRows = remember(

        uiState.transactions,
        currency

    ) {

        uiState.transactions.map {

            it.toRowData(
                currency
            )
        }
    }

    /*
    ---------------------------------------------------
    UI
    ---------------------------------------------------
    */

    LazyColumn(

        modifier = Modifier
            .fillMaxSize(),

        contentPadding =
            PaddingValues(16.dp),

        verticalArrangement =
            Arrangement.spacedBy(20.dp)
    ) {

        /*
        ---------------------------------------------------
        TITLE
        ---------------------------------------------------
        */

        item {

            LedgeScreenTitle(
                title = "Reports"
            )
        }

        /*
        ---------------------------------------------------
        FILTERS
        ---------------------------------------------------
        */

        item {

            ReportsFilters(

                selectedType =
                    uiState.selectedType,

                selectedCategories =
                    uiState.selectedCategories,

                currentCategories =
                    currentCategories,

                periodFilter =
                    uiState.periodFilter,

                onTypeSelected =
                    viewModel::setType,

                onCategoryToggle =
                    viewModel::toggleCategory,

                onPeriodSelected =
                    viewModel::setPeriodFilter
            )
        }

        /*
        ---------------------------------------------------
        SUMMARY
        ---------------------------------------------------
        */

        item {

            ReportsSummary(

                transactionCount =
                    uiState.transactions.size,

                totalCredit =
                    uiState.totalCredit,

                totalDebit =
                    uiState.totalDebit,

                balance =
                    uiState.balance,

                fromDate =
                    reportFromDate,

                toDate =
                    reportToDate,

                currency =
                    currency
            )
        }

        /*
        ---------------------------------------------------
        PRINT
        ---------------------------------------------------
        */

        item {

            Button(

                modifier = Modifier
                    .fillMaxWidth(),

                onClick = {

                    val html =

                        buildReportHtml(

                            transactions =
                                uiState.transactions,

                            totalDebit =
                                uiState.totalDebit,

                            totalCredit =
                                uiState.totalCredit,

                            balance =
                                uiState.balance,

                            fromDate =
                                reportFromDate,

                            toDate =
                                reportToDate
                        )

                    printReport(
                        context,
                        html
                    )
                }
            ) {

                Text("Print Report")
            }
        }

        /*
        ---------------------------------------------------
        TRANSACTIONS
        ---------------------------------------------------
        */

        reportsTransactionList(

            transactions =
                transactionRows
        )
    }
}