package app.initcn.ledge.ui.settings

import androidx.compose.runtime.Immutable
import app.initcn.ledge.core.CurrencyType

@Immutable
data class SettingsUiState(
    val currency: CurrencyType = CurrencyType.INR,
    val budgetMode: Boolean = false,
    val biometricLock: Boolean = false,
    val includeDebtInBalance: Boolean = false
)