package com.ledge.ui.add

import com.ledge.core.model.TransactionType

data class AddTransactionUiState(

    val type: TransactionType =
        TransactionType.DEBIT,

    val amount: String = "",

    val amountError: String? = null,

    val category: String = "",

    val paymentInstrument: String = "",

    val note: String = "",

    val selectedDate: Long =
        System.currentTimeMillis(),

    val isSaving: Boolean = false,

    val isSaved: Boolean = false,

    val error: String? = null
)