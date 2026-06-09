package app.initcn.ledge.ui.budget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.initcn.ledge.data.entity.BudgetEntity
import app.initcn.ledge.data.entity.CategoriesModel
import app.initcn.ledge.data.repository.BudgetProgress
import app.initcn.ledge.data.repository.BudgetRepository
import app.initcn.ledge.domain.AmountValidator
import app.initcn.ledge.domain.CategoryProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class BudgetUiState(
    val category: String = "",
    val amount: String = "",
    val amountError: String? = null,
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class BudgetViewModel @Inject constructor(
    private val repository: BudgetRepository,
    private val categoryProvider: CategoryProvider,
    private val amountValidator: AmountValidator
) : ViewModel() {

    private val _uiState = MutableStateFlow(BudgetUiState())
    val uiState: StateFlow<BudgetUiState> = _uiState.asStateFlow()

    val categories: CategoriesModel = categoryProvider.getCategories()

    val budgets: StateFlow<List<BudgetProgress>> = repository
        .getBudgetProgress()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun updateCategory(category: String) {
        _uiState.update { it.copy(category = category) }
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

    fun saveBudget() {
        val state = _uiState.value
        val amountLong: Long = state.amount
            .trim()
            .toBigDecimalOrNull()
            ?.movePointRight(2)
            ?.longValueExact()
            ?: run {
                _uiState.update { it.copy(amountError = "Invalid amount") }
                return
            }

        if (amountLong <= 0L || state.category.isBlank()) {
            _uiState.update { it.copy(amountError = "Invalid amount") }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(isSaving = true, error = null)
            }
            try {
                repository.saveBudget(
                    category = state.category.trim(),
                    limitAmount = amountLong
                )
                _uiState.value = BudgetUiState(isSaved = true)
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