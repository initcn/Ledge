package com.ledge.ui.budget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.ledge.data.entity.BudgetEntity
import com.ledge.data.repository.BudgetProgress
import com.ledge.data.repository.BudgetRepository

import com.ledge.domain.category.CategoryProvider
import com.ledge.domain.validation.AmountValidator

import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

import kotlinx.coroutines.launch

import javax.inject.Inject

@HiltViewModel
class BudgetViewModel @Inject constructor(

    private val repository: BudgetRepository,
    private val categoryProvider: CategoryProvider,
    private val amountValidator: AmountValidator

) : ViewModel() {

    private val _uiState =
        MutableStateFlow(BudgetUiState())

    val uiState: StateFlow<BudgetUiState> =
        _uiState.asStateFlow()

    val categories =
        categoryProvider.getCategories()

    val budgets: StateFlow<List<BudgetProgress>> =
        repository
            .getBudgetProgress()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    fun updateCategory(category: String) {
        _uiState.update {
            it.copy(category = category)
        }
    }

    fun updateAmount(value: String) {

        val result =
            amountValidator.validate(value)

        _uiState.update {
            it.copy(
                amount = result.value,
                amountError = result.error
            )
        }
    }

    fun saveBudget() {

        val state = _uiState.value

        val amountLong: Long =
            state.amount
                .trim()
                .toBigDecimalOrNull()
                ?.movePointRight(2)
                ?.longValueExact()
                ?: run {
                    _uiState.update {
                        it.copy(amountError = "Invalid amount")
                    }
                    return
                }

        if (
            amountLong <= 0L ||
            state.category.isBlank()
        ) {
            _uiState.update {
                it.copy(amountError = "Invalid amount")
            }
            return
        }

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isSaving = true,
                    error = null
                )
            }

            try {

                repository.saveBudget(
                    category = state.category.trim(),
                    limitAmount = amountLong
                )

                _uiState.value =
                    BudgetUiState(isSaved = true)

            } catch (e: Exception) {

                _uiState.update {
                    it.copy(
                        isSaving = false,
                        error = e.message ?: "Failed to save budget"
                    )
                }
            }
        }
    }

    fun resetSaveState() {
        _uiState.update { it.copy(isSaved = false) }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun deleteBudget(budget: BudgetEntity) {
        viewModelScope.launch {
            repository.deleteBudget(budget)
        }
    }
}