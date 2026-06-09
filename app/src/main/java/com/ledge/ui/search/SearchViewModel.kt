package com.ledge.ui.search

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ledge.data.entity.TransactionEntity
import com.ledge.data.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@Immutable
data class SearchUiState(
    val selectedTransaction: TransactionEntity? = null,
    val transactionPendingDelete: TransactionEntity? = null,
    val showDeleteDialog: Boolean = false
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: TransactionRepository
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState = _uiState.asStateFlow()

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val transactionsStream: StateFlow<PagingData<TransactionEntity>> = _query
        .debounce(300.milliseconds)
        .flatMapLatest { query ->
            if (query.isBlank()) {
                repository.getPagedTransactions()
            } else {
                repository.searchPagedTransactions(query)
            }
        }
        .cachedIn(viewModelScope)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PagingData.empty())

    fun updateQuery(newQuery: String) {
        _query.value = newQuery
    }

    fun selectTransaction(transaction: TransactionEntity) {
        _uiState.value = _uiState.value.copy(selectedTransaction = transaction)
    }

    fun clearSelectedTransaction() {
        _uiState.value = _uiState.value.copy(selectedTransaction = null)
    }

    fun showDeleteDialog(transaction: TransactionEntity) {
        _uiState.value = _uiState.value.copy(
            transactionPendingDelete = transaction,
            showDeleteDialog = true
        )
    }

    fun dismissDeleteDialog() {
        _uiState.value = _uiState.value.copy(
            transactionPendingDelete = null,
            showDeleteDialog = false
        )
    }

    fun deleteSelectedTransaction() {
        val transaction = _uiState.value.transactionPendingDelete ?: return
        viewModelScope.launch {
            repository.deleteTransaction(transaction.id)
            dismissDeleteDialog()
        }
    }

    fun updateTransaction(transaction: TransactionEntity) {
        viewModelScope.launch {
            repository.updateTransaction(transaction)
        }
    }
}