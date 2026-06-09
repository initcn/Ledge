package com.ledge.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ledge.data.entity.CategoryTotal
import com.ledge.data.entity.TransactionEntity
import com.ledge.domain.GetDashboardDataUseCase
import com.ledge.domain.PeriodFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class DashboardUiState(
    val totalIncome: Long = 0L,
    val totalExpense: Long = 0L,
    val totalBalance: Long = 0L,
    val outstandingDebt: Long = 0L,
    val totalLent: Long = 0L,
    val totalRecovered: Long = 0L,
    val outstandingLent: Long = 0L,
    val recentTransactions: List<TransactionEntity> = emptyList(),
    val categorySpending: List<CategoryTotal> = emptyList(),
    val periodFilter: PeriodFilter = PeriodFilter()
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getDashboardDataUseCase: GetDashboardDataUseCase
) : ViewModel() {

    private val _periodFilter = MutableStateFlow(PeriodFilter())
    val periodFilter: StateFlow<PeriodFilter> = _periodFilter

    fun setPeriodFilter(filter: PeriodFilter) {
        _periodFilter.value = filter
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val dashboardData = _periodFilter
        .flatMapLatest { filter ->
            getDashboardDataUseCase(filter)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
}