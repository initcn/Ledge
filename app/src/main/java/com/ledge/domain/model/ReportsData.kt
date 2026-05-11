package com.ledge.domain.model

import com.ledge.data.entity.TransactionEntity
import androidx.compose.runtime.Immutable

@Immutable
data class ReportsData(

    val transactions: List<TransactionEntity>,

    val totalCredit: Long,
    val totalDebit: Long,

    val balance: Long
)