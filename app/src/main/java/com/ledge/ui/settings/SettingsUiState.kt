package com.ledge.ui.settings

import androidx.compose.runtime.Immutable
import com.ledge.core.CurrencyType

@Immutable
data class SettingsUiState(
    val currency: CurrencyType = CurrencyType.INR,
    val budgetMode: Boolean = false,
    val biometricLock: Boolean = false,
    val includeDebtInBalance: Boolean = false
)