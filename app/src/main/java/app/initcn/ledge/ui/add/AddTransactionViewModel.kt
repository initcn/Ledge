package app.initcn.ledge.ui.add

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.initcn.ledge.core.TransactionType
import app.initcn.ledge.data.entity.CategoriesModel
import app.initcn.ledge.data.repository.TransactionRepository
import app.initcn.ledge.domain.AmountValidator
import app.initcn.ledge.domain.CategoryProvider
import app.initcn.ledge.domain.TransactionFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@Immutable
data class AddTransactionUiState(
    val type: TransactionType = TransactionType.DEBIT,
    val amount: String = "",
    val amountError: String? = null,
    val category: String = "",
    val paymentInstrument: String = "",
    val note: String = "",
    val selectedDate: Long = System.currentTimeMillis(),
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class AddTransactionViewModel @Inject constructor(
    private val repository: TransactionRepository,
    private val categoryProvider: CategoryProvider,
    private val transactionFactory: TransactionFactory,
    private val amountValidator: AmountValidator
) : ViewModel() {

    val categories: CategoriesModel = categoryProvider.getCategories()

    private val _uiState = MutableStateFlow(AddTransactionUiState())
    val uiState = _uiState.asStateFlow()

    init {
        _uiState.update {
            it.copy(
                category = defaultCategory(it.type),
                paymentInstrument = defaultPaymentInstrument()
            )
        }
    }

    private fun defaultCategory(type: TransactionType): String {
        return when (type) {
            TransactionType.DEBIT -> categories.debit.firstOrNull() ?: ""
            TransactionType.CREDIT -> categories.credit.firstOrNull() ?: ""
        }
    }

    private fun defaultPaymentInstrument(): String {
        return categories.paymentInstrument.firstOrNull() ?: ""
    }

    fun updateType(type: TransactionType) {
        _uiState.update {
            it.copy(
                type = type,
                category = defaultCategory(type)
            )
        }
    }

    fun updateAmount(value: String) {
        val result = amountValidator.validate(value)
        _uiState.update {
            it.copy(
                amount = result.value,
                amountError = result.error
            )
        }
    }

    fun updateCategory(category: String) {
        val validCategories = when (_uiState.value.type) {
            TransactionType.DEBIT -> categories.debit
            TransactionType.CREDIT -> categories.credit
        }
        if (category !in validCategories) return
        _uiState.update { it.copy(category = category) }
    }

    fun updatePaymentInstrument(paymentInstrument: String) {
        if (paymentInstrument !in categories.paymentInstrument) return
        _uiState.update { it.copy(paymentInstrument = paymentInstrument) }
    }

    fun updateNote(note: String) {
        _uiState.update { it.copy(note = note) }
    }

    fun updateDate(date: Long) {
        _uiState.update { it.copy(selectedDate = date) }
    }

    fun saveTransaction() {
        val state = _uiState.value
        val transaction = transactionFactory.createOrNull(
            type = state.type,
            amount = state.amount,
            category = state.category,
            paymentInstrument = state.paymentInstrument,
            note = state.note,
            createdAt = state.selectedDate
        ) ?: run {
            _uiState.update { it.copy(amountError = "Invalid amount") }
            return
        }

        viewModelScope.launch {
            _uiState.value = state.copy(isSaving = true, error = null)
            try {
                repository.insertTransaction(transaction)
                _uiState.value = AddTransactionUiState(
                    category = defaultCategory(state.type),
                    paymentInstrument = defaultPaymentInstrument(),
                    isSaved = true
                )
            } catch (e: Exception) {
                _uiState.value = state.copy(
                    isSaving = false,
                    error = e.message ?: "Failed to save transaction"
                )
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun resetSaveState() {
        _uiState.update { it.copy(isSaved = false) }
    }
}