package app.initcn.ledge.domain

import androidx.compose.runtime.Immutable
import app.initcn.ledge.data.entity.TransactionEntity

@Immutable
data class ReportsData(
    val transactions: List<TransactionEntity>,
    val totalCredit: Long,
    val totalDebit: Long,
    val balance: Long
)