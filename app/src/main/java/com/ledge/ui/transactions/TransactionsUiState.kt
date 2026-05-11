package com.ledge.ui.transactions

import com.ledge.data.entity.TransactionEntity

data class TransactionsUiState(

    val transactions:
    List<TransactionEntity> = emptyList(),

    val selectedTransaction:
    TransactionEntity? = null,

    val transactionPendingDelete:
    TransactionEntity? = null,

    val showDeleteDialog:
    Boolean = false
)