package com.ledge.ui.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.ledge.core.model.TransactionType

import com.ledge.data.repository.TransactionRepository

import com.ledge.domain.category.CategoryProvider
import com.ledge.domain.factory.TransactionFactory

import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

import kotlinx.coroutines.launch

import javax.inject.Inject

@HiltViewModel
class AddTransactionViewModel @Inject constructor(

    private val repository:
    TransactionRepository,

    private val categoryProvider:
    CategoryProvider,

    private val transactionFactory:
    TransactionFactory

) : ViewModel() {

    /*
    ---------------------------------------------------
    CATEGORY DATA
    ---------------------------------------------------
    */

    val categories =
        categoryProvider
            .getCategories()

    /*
    ---------------------------------------------------
    UI STATE
    ---------------------------------------------------
    */

    private val _uiState =

        MutableStateFlow(
            AddTransactionUiState()
        )

    val uiState =
        _uiState.asStateFlow()

    /*
    ---------------------------------------------------
    INITIALIZATION
    ---------------------------------------------------
    */

    init {

        _uiState.update {

            it.copy(

                category =

                    defaultCategory(
                        it.type
                    ),

                paymentInstrument =

                    defaultPaymentInstrument()
            )
        }
    }

    /*
    ---------------------------------------------------
    DEFAULT VALUES
    ---------------------------------------------------
    */

    private fun defaultCategory(
        type: TransactionType
    ): String {

        return when (type) {

            TransactionType.DEBIT -> {

                categories.debit
                    .firstOrNull()

                    ?: ""
            }

            TransactionType.CREDIT -> {

                categories.credit
                    .firstOrNull()

                    ?: ""
            }
        }
    }

    private fun defaultPaymentInstrument():
            String {

        return categories
            .paymentInstrument
            .firstOrNull()

            ?: ""
    }

    /*
    ---------------------------------------------------
    TYPE
    ---------------------------------------------------
    */

    fun updateType(

        type: TransactionType
    ) {

        _uiState.update {

            it.copy(

                type = type,

                category =
                    defaultCategory(type)
            )
        }
    }

    /*
    ---------------------------------------------------
    AMOUNT
    ---------------------------------------------------
    */

    fun updateAmount(

        value: String
    ) {

        _uiState.update {

            it.copy(
                amount = value
            )
        }
    }

    /*
    ---------------------------------------------------
    CATEGORY
    ---------------------------------------------------
    */

    fun updateCategory(

        category: String
    ) {

        val validCategories =

            when (_uiState.value.type) {

                TransactionType.DEBIT -> {

                    categories.debit
                }

                TransactionType.CREDIT -> {

                    categories.credit
                }
            }

        if (
            category !in validCategories
        ) {
            return
        }

        _uiState.update {

            it.copy(
                category = category
            )
        }
    }

    /*
    ---------------------------------------------------
    PAYMENT INSTRUMENT
    ---------------------------------------------------
    */

    fun updatePaymentInstrument(

        paymentInstrument: String
    ) {

        if (
            paymentInstrument !in
            categories.paymentInstrument
        ) {
            return
        }

        _uiState.update {

            it.copy(

                paymentInstrument =
                    paymentInstrument
            )
        }
    }

    /*
    ---------------------------------------------------
    NOTE
    ---------------------------------------------------
    */

    fun updateNote(

        note: String
    ) {

        _uiState.update {

            it.copy(
                note = note
            )
        }
    }

    /*
    ---------------------------------------------------
    DATE
    ---------------------------------------------------
    */

    fun updateDate(

        date: Long
    ) {

        _uiState.update {

            it.copy(
                selectedDate = date
            )
        }
    }

    /*
    ---------------------------------------------------
    SAVE
    ---------------------------------------------------
    */

    fun saveTransaction() {

        val state =
            _uiState.value

        val transaction =

            transactionFactory
                .createOrNull(

                    type =
                        state.type,

                    amount =
                        state.amount,

                    category =
                        state.category,

                    paymentInstrument =
                        state.paymentInstrument,

                    note =
                        state.note,

                    createdAt =
                        state.selectedDate
                )

                ?: run {

                    _uiState.update {

                        it.copy(
                            amountError =
                                "Invalid amount"
                        )
                    }

                    return
                }

        viewModelScope.launch {

            _uiState.value =

                state.copy(

                    isSaving = true,

                    error = null
                )

            try {

                repository.insertTransaction(
                    transaction
                )

                _uiState.value =

                    AddTransactionUiState(

                        category =

                            defaultCategory(
                                state.type
                            ),

                        paymentInstrument =

                            defaultPaymentInstrument(),

                        isSaved = true
                    )

            } catch (e: Exception) {

                _uiState.value =

                    state.copy(

                        isSaving = false,

                        error =

                            e.message
                                ?: "Failed to save transaction"
                    )
            }
        }
    }

    /*
    ---------------------------------------------------
    ERROR
    ---------------------------------------------------
    */

    fun clearError() {

        _uiState.update {

            it.copy(
                error = null
            )
        }
    }

    /*
    ---------------------------------------------------
    SAVE STATE
    ---------------------------------------------------
    */

    fun resetSaveState() {

        _uiState.update {

            it.copy(
                isSaved = false
            )
        }
    }
}