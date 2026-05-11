package com.ledge.ui.settings

import android.content.Context

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.ledge.core.model.CurrencyType

import com.ledge.data.backup.BackupManager
import com.ledge.data.preferences.SettingsPreferences

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

    private val settingsPreferences:
    SettingsPreferences,

    private val backupManager:
    BackupManager

) : ViewModel() {

    val uiState:
            StateFlow<SettingsUiState> =

        combine(

            settingsPreferences.currency,

            settingsPreferences.budgetMode,

            settingsPreferences.biometricLock,

            settingsPreferences.includeDebtInBalance

        ) {

                currency,
                budgetMode,
                biometricLock,
                includeDebtInBalance ->

            SettingsUiState(

                currency = currency,

                budgetMode = budgetMode,

                biometricLock = biometricLock,

                includeDebtInBalance =
                    includeDebtInBalance
            )

        }.stateIn(

            scope =
                viewModelScope,

            started =

                SharingStarted
                    .WhileSubscribed(5000),

            initialValue =
                SettingsUiState()
        )

    fun setCurrency(

        currency: CurrencyType
    ) {

        viewModelScope.launch {

            settingsPreferences
                .setCurrency(currency)
        }
    }

    fun setBudgetMode(

        enabled: Boolean
    ) {

        viewModelScope.launch {

            settingsPreferences
                .setBudgetMode(enabled)
        }
    }

    fun setBiometricLock(

        enabled: Boolean
    ) {

        viewModelScope.launch {

            settingsPreferences
                .setBiometricLock(enabled)
        }
    }

    fun setIncludeDebtInBalance(

        enabled: Boolean
    ) {

        viewModelScope.launch {

            settingsPreferences
                .setIncludeDebtInBalance(
                    enabled
                )
        }
    }

    suspend fun exportBackup(

        context: Context

    ): File {

        return backupManager
            .exportBackup(context)
    }

    fun restoreBackup(

        json: String
    ) {

        viewModelScope.launch {

            backupManager
                .restoreBackup(json)
        }
    }
}