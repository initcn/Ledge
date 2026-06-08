package com.ledge.domain // <-- FLAT ROOT PACKAGE HEADER

import androidx.compose.runtime.Immutable
import com.ledge.data.entity.TransactionEntity

@Immutable
data class ReportsData(
    val transactions: List<TransactionEntity>,
    val totalCredit: Long,
    val totalDebit: Long,
    val balance: Long
)