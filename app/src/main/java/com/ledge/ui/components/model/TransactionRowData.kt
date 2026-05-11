package com.ledge.ui.components.model

import androidx.compose.runtime.Immutable

@Immutable
data class TransactionRowData(

    val title: String,

    val subtitle: String,

    val formattedAmount: String,

    val formattedDate: String,

    val isExpense: Boolean,

    val note: String
)