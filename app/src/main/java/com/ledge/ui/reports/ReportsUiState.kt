package com.ledge.ui.reports

import com.ledge.core.model.PeriodFilter
import com.ledge.core.model.TransactionType

import com.ledge.data.entity.TransactionEntity



data class ReportsUiState(

    val selectedType: TransactionType? = null,

    val selectedCategories: Set<String> = emptySet(),

    val periodFilter: PeriodFilter = PeriodFilter(),

    val transactions: List<TransactionEntity> = emptyList(),

    val totalCredit: Long = 0L,

    val totalDebit: Long = 0L,

    val balance: Long = 0L,

    val outstandingDebt: Long = 0L
)