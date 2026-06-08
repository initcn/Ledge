package com.ledge.domain // <-- FLAT ROOT PACKAGE HEADER

import com.ledge.core.TransactionType
import com.ledge.data.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetReportsDataUseCase @Inject constructor(
    private val repository: TransactionRepository
) {

    operator fun invoke(
        selectedType: TransactionType?,
        selectedCategories: Set<String>,
        periodFilter: PeriodFilter
    ): Flow<ReportsData> {
        val (startDate, endDate) = PeriodDateResolver.resolve(periodFilter)
        val categories = selectedCategories.toList()

        return combine(
            repository.getFilteredTransactionsForPeriodAndCategories(
                startDate = startDate,
                endDate = endDate,
                type = selectedType,
                categories = categories
            ),
            repository.observeReportsTotals(
                startDate = startDate,
                endDate = endDate,
                type = selectedType,
                categories = categories
            )
        ) { transactions, totals ->
            ReportsData(
                transactions = transactions,
                totalCredit = totals.totalCredit,
                totalDebit = totals.totalDebit,
                balance = totals.totalCredit - totals.totalDebit
            )
        }
    }
}