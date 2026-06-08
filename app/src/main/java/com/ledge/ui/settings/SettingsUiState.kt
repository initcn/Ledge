package com.ledge.ui.settings

import com.ledge.core.CurrencyType

data class SettingsUiState(

    val currency: CurrencyType =
        CurrencyType.INR,

    val budgetMode: Boolean = false,

    val biometricLock: Boolean = false,

    val includeDebtInBalance: Boolean = false
)