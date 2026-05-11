package com.ledge.ui.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import androidx.paging.cachedIn

import com.ledge.data.entity.TransactionEntity
import com.ledge.data.repository.TransactionRepository

import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

import kotlinx.coroutines.launch

import javax.inject.Inject

@HiltViewModel
class TransactionsViewModel @Inject constructor(

    private val repository:
    TransactionRepository

) : ViewModel() {

    private val _uiState =

        MutableStateFlow(
            TransactionsUiState()
        )

    val uiState =
        _uiState.asStateFlow()

    val pagedTransactions =

        repository

            .getPagedTransactions()

            .cachedIn(
                viewModelScope
            )

    fun selectTransaction(

        transaction: TransactionEntity
    ) {

        _uiState.value =

            _uiState.value.copy(

                selectedTransaction =
                    transaction
            )
    }

    fun clearSelectedTransaction() {

        _uiState.value =

            _uiState.value.copy(

                selectedTransaction =
                    null
            )
    }

    fun showDeleteDialog(

        transaction: TransactionEntity
    ) {

        _uiState.value =

            _uiState.value.copy(

                transactionPendingDelete =
                    transaction,

                showDeleteDialog =
                    true
            )
    }

    fun dismissDeleteDialog() {

        _uiState.value =

            _uiState.value.copy(

                transactionPendingDelete =
                    null,

                showDeleteDialog =
                    false
            )
    }

    fun deleteSelectedTransaction() {

        val transaction =

            _uiState.value
                .transactionPendingDelete

                ?: return

        viewModelScope.launch {

            repository
                .deleteTransaction(
                    transaction.id
                )

            _uiState.value =

                _uiState.value.copy(

                    transactionPendingDelete =
                        null,

                    showDeleteDialog =
                        false
                )
        }
    }

    fun updateTransaction(

        transaction: TransactionEntity
    ) {

        viewModelScope.launch {

            repository
                .updateTransaction(
                    transaction
                )
        }
    }
}