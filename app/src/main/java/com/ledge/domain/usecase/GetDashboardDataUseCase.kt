package com.ledge.domain.usecase

import com.ledge.core.extensions.toRowData
import com.ledge.core.model.PeriodFilter

import com.ledge.data.preferences.SettingsPreferences
import com.ledge.data.repository.TransactionRepository

import com.ledge.domain.model.DashboardData
import com.ledge.domain.utils.PeriodDateResolver

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

import javax.inject.Inject

class GetDashboardDataUseCase @Inject constructor(

    private val repository: TransactionRepository,

    private val settingsPreferences:
    SettingsPreferences

) {

    operator fun invoke(

        filter: PeriodFilter

    ): Flow<DashboardData> {

        val (startDate, endDate) =

            PeriodDateResolver.resolve(
                filter
            )

        return combine(

            repository
                .observeDashboardSummaryForPeriod(

                    startDate = startDate,

                    endDate = endDate
                ),

            repository
                .getRecentTransactionsForPeriod(

                    startDate = startDate,

                    endDate = endDate
                ),

            repository
                .getCategoryTotalsForPeriod(

                    startDate = startDate,

                    endDate = endDate
                ),

            settingsPreferences
                .includeDebtInBalance,

            settingsPreferences
                .currency

        ) {

                summary,

                recentTransactions,

                categoryTotals,

                includeDebtInBalance,

                currency ->

            val displayedBalance =

                if (includeDebtInBalance) {

                    summary.balance -
                            summary.outstandingDebt

                } else {

                    summary.balance
                }

            DashboardData(

                totalIncome =
                    summary.income,

                totalExpense =
                    summary.expense,

                totalBalance =
                    displayedBalance,

                outstandingDebt =
                    summary.outstandingDebt,

                totalLent =
                    summary.lent,

                totalRecovered =
                    summary.lentRepaid,

                outstandingLent =
                    summary.outstandingLent,

                recentTransactions =

                    recentTransactions.map {

                        it.toRowData(
                            currency
                        )
                    },

                categoryTotals =
                    categoryTotals
            )
        }
    }
}