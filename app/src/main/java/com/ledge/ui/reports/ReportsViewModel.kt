package com.ledge.ui.reports

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ledge.domain.PeriodFilter
import com.ledge.core.TransactionType
import com.ledge.data.entity.TransactionEntity
import com.ledge.domain.CategoryProvider
import com.ledge.domain.GetReportsDataUseCase
import com.ledge.domain.ReportsData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@Immutable
data class ReportsUiState(
    val selectedType: TransactionType? = null,
    val selectedCategories: Set<String> = emptySet(),
    val periodFilter: PeriodFilter = PeriodFilter(),
    val transactions: List<TransactionEntity> = emptyList(),
    val totalCredit: Long = 0L,
    val totalDebit: Long = 0L,
    val balance: Long = 0L,
    val outstandingDebt: Long = 0L
)

@HiltViewModel
class ReportsViewModel @Inject constructor(
    private val getReportsDataUseCase: GetReportsDataUseCase,
    categoryProvider: CategoryProvider
) : ViewModel() {
    val categories = categoryProvider.getCategories()

    private val _uiState = MutableStateFlow(ReportsUiState())

    fun setType(type: TransactionType?) {
        _uiState.value = _uiState.value.copy(selectedType = type)
    }

    fun toggleCategory(category: String) {
        val currentCategories = _uiState.value.selectedCategories
        val updatedCategories = if (category in currentCategories) {
            currentCategories - category
        } else {
            currentCategories + category
        }
        _uiState.value = _uiState.value.copy(selectedCategories = updatedCategories)
    }

    fun setPeriodFilter(filter: PeriodFilter) {
        _uiState.value = _uiState.value.copy(periodFilter = filter)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val reportsDataFlow = _uiState.flatMapLatest { state ->
        getReportsDataUseCase(
            selectedType = state.selectedType,
            selectedCategories = state.selectedCategories,
            periodFilter = state.periodFilter
        )
    }

    val uiState: StateFlow<ReportsUiState> = combine(
        reportsDataFlow,
        _uiState
    ) { reportsData: ReportsData, state: ReportsUiState ->
        state.copy(
            transactions = reportsData.transactions,
            totalCredit = reportsData.totalCredit,
            totalDebit = reportsData.totalDebit,
            balance = reportsData.balance
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ReportsUiState()
    )
}