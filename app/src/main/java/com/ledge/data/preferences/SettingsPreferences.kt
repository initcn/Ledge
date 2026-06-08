package com.ledge.data.preferences

import android.content.Context

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

import com.ledge.core.CurrencyType

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(

    name = "settings"
)

class SettingsPreferences(

    private val context: Context
) {

    companion object {

        private val BIOMETRIC_LOCK =

            booleanPreferencesKey(
                "biometric_lock"
            )

        private val BUDGET_MODE =

            booleanPreferencesKey(
                "budget_mode"
            )

        private val CURRENCY_KEY =

            stringPreferencesKey(
                "currency"
            )

        private val INCLUDE_DEBT_IN_BALANCE =

            booleanPreferencesKey(
                "include_debt_in_balance"
            )
    }

    val budgetMode:
            Flow<Boolean> =

        context.dataStore.data.map {

                preferences ->

            preferences[BUDGET_MODE]
                ?: false
        }

    val currency:
            Flow<CurrencyType> =

        context.dataStore.data.map {

                preferences ->

            CurrencyType.valueOf(

                preferences[CURRENCY_KEY]

                    ?: CurrencyType.INR.name
            )
        }

    val biometricLock:
            Flow<Boolean> =

        context.dataStore.data.map {

                preferences ->

            preferences[BIOMETRIC_LOCK]
                ?: false
        }


    val includeDebtInBalance:
            Flow<Boolean> =

        context.dataStore.data.map {

                preferences ->

            preferences[
                INCLUDE_DEBT_IN_BALANCE
            ] ?: false
        }

    suspend fun setBudgetMode(

        enabled: Boolean
    ) {

        context.dataStore.edit {

                preferences ->

            preferences[BUDGET_MODE] =
                enabled
        }
    }

    suspend fun setCurrency(

        currency: CurrencyType
    ) {

        context.dataStore.edit {

                preferences ->

            preferences[CURRENCY_KEY] =
                currency.name
        }
    }

    suspend fun setBiometricLock(

        enabled: Boolean
    ) {

        context.dataStore.edit {

                preferences ->

            preferences[BIOMETRIC_LOCK] =
                enabled
        }
    }

    suspend fun setIncludeDebtInBalance(

        enabled: Boolean
    ) {

        context.dataStore.edit {

                preferences ->

            preferences[
                INCLUDE_DEBT_IN_BALANCE
            ] = enabled
        }
    }
}