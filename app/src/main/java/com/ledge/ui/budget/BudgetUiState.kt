package com.ledge.ui.budget

data class BudgetUiState(

    val category: String = "",

    val amount: String = "",

    val amountError: String? = null,

    val isSaving: Boolean = false,

    val isSaved: Boolean = false,

    val error: String? = null
)