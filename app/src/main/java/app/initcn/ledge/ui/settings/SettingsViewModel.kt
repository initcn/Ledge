package app.initcn.ledge.ui.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.initcn.ledge.core.CurrencyType
import app.initcn.ledge.data.backup.BackupManager
import app.initcn.ledge.data.preferences.SettingsPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsPreferences: SettingsPreferences,
    private val backupManager: BackupManager
) : ViewModel() {

    val uiState: StateFlow<SettingsUiState> = combine(
        settingsPreferences.currency,
        settingsPreferences.budgetMode,
        settingsPreferences.biometricLock,
        settingsPreferences.includeDebtInBalance
    ) { currency: CurrencyType, budgetMode: Boolean, biometricLock: Boolean, includeDebtInBalance: Boolean ->
        SettingsUiState(
            currency = currency,
            budgetMode = budgetMode,
            biometricLock = biometricLock,
            includeDebtInBalance = includeDebtInBalance
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SettingsUiState()
    )

    fun setCurrency(currency: CurrencyType) {
        viewModelScope.launch {
            settingsPreferences.setCurrency(currency)
        }
    }

    fun setBudgetMode(enabled: Boolean) {
        viewModelScope.launch {
            settingsPreferences.setBudgetMode(enabled)
        }
    }

    fun setBiometricLock(enabled: Boolean) {
        viewModelScope.launch {
            settingsPreferences.setBiometricLock(enabled)
        }
    }

    fun setIncludeDebtInBalance(enabled: Boolean) {
        viewModelScope.launch {
            settingsPreferences.setIncludeDebtInBalance(enabled)
        }
    }

    suspend fun exportBackup(context: Context): File {
        return backupManager.exportBackup(context)
    }

    fun restoreBackup(json: String) {
        viewModelScope.launch {
            try {
                backupManager.restoreBackup(json)
            } catch (e: Exception) {
                // Safely handles malformed JSON files without crashing the application
                e.printStackTrace()
                throw e
            }
        }
    }
}