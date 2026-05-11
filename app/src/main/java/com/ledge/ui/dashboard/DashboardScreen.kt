package com.ledge.ui.dashboard

import androidx.compose.foundation.layout.Arrangement
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

import com.ledge.core.extensions.toRowData

import com.ledge.ui.budget.BudgetViewModel

import com.ledge.ui.dashboard.components.DashboardHeader
import com.ledge.ui.dashboard.components.buildDashboardPeriodText
import com.ledge.ui.dashboard.components.dashboardSummarySection
import com.ledge.ui.dashboard.components.recentTransactionsSection

import com.ledge.ui.settings.SettingsViewModel

@Composable
fun DashboardScreen(

    viewModel: DashboardViewModel =
        hiltViewModel(),

    budgetViewModel: BudgetViewModel =
        hiltViewModel(),

    settingsViewModel: SettingsViewModel =
        hiltViewModel()

) {

    val dashboardData by viewModel
        .dashboardData
        .collectAsStateWithLifecycle()

    val periodFilter by viewModel
        .periodFilter
        .collectAsStateWithLifecycle()

    val budgets by budgetViewModel
        .budgets
        .collectAsStateWithLifecycle()

    val settingsUiState by settingsViewModel
        .uiState
        .collectAsStateWithLifecycle()

    val budgetMode =
        settingsUiState.budgetMode

    val currency =
        settingsUiState.currency

    val periodText = remember(
        periodFilter
    ) {

        buildDashboardPeriodText(
            periodFilter
        )
    }



    LazyColumn(

        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),

        verticalArrangement =
            Arrangement.spacedBy(20.dp)
    ) {

        item {

            DashboardHeader(

                periodFilter =
                    periodFilter,

                onPeriodSelected =
                    viewModel::setPeriodFilter
            )
        }

        periodText?.let { text ->

            item {

                Text(

                    text = text,

                    style =
                        MaterialTheme.typography
                            .bodyMedium,

                    color =
                        MaterialTheme
                            .colorScheme
                            .primary
                )
            }
        }

        dashboardSummarySection(

            dashboardData =
                dashboardData,

            budgets =
                budgets,

            budgetMode =
                budgetMode,

            currency =
                currency
        )

        recentTransactionsSection(

            transactions =

                dashboardData
                    ?.recentTransactions
                    ?: emptyList()
        )
    }
}