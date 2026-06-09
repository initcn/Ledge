package app.initcn.ledge.ui.transactions

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import app.initcn.ledge.data.entity.TransactionEntity
import app.initcn.ledge.data.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@Immutable
data class TransactionsUiState(
    val selectedTransaction: TransactionEntity? = null,
    val transactionPendingDelete: TransactionEntity? = null,
    val showDeleteDialog: Boolean = false
)

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val repository: TransactionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TransactionsUiState())
    val uiState = _uiState.asStateFlow()

    // 🔥 Added backing search state
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // 🔥 Dynamic search switcher stream
    @OptIn(ExperimentalCoroutinesApi::class)
    val pagedTransactions: StateFlow<PagingData<TransactionEntity>> = _searchQuery
        .flatMapLatest { query ->
            if (query.isBlank()) {
                repository.getPagedTransactions()
            } else {
                repository.searchPagedTransactions(query)
            }
        }
        .cachedIn(viewModelScope)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PagingData.empty())

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
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
            _uiState.value = _uiState.value.copy(
                transactionPendingDelete = null,
                showDeleteDialog = false
            )
        }
    }

    fun updateTransaction(transaction: TransactionEntity) {
        viewModelScope.launch {
            repository.updateTransaction(transaction)
        }
    }
}