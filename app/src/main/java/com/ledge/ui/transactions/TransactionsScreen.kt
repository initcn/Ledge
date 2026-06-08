package com.ledge.ui.transactions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.ledge.ui.components.core.LedgeScaffold
import com.ledge.ui.components.core.LedgeScreenTitle
import com.ledge.ui.components.transaction.EditTransactionSheet
import com.ledge.ui.components.transaction.SwipeTransactionItem
import com.ledge.ui.settings.SettingsViewModel

@Composable
fun TransactionsScreen(
    viewModel: TransactionsViewModel = hiltViewModel(),
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val settingsUiState by settingsViewModel.uiState.collectAsStateWithLifecycle()

    val pagingItems = viewModel.pagedTransactions.collectAsLazyPagingItems()
    val currency = settingsUiState.currency

    // CENTRALIZED SCAFFOLDING - Enforcing unified background and snackbar tracking
    LedgeScaffold { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                LedgeScreenTitle(title = "Transactions")
            }

            // EMPTY STATE LAYER
            if (pagingItems.itemCount == 0 && pagingItems.loadState.refresh is LoadState.NotLoading) {
                item {
                    Text(text = "No transactions found.")
                }
            }

            // PAGED TRANSACTION ITEMS
            items(
                count = pagingItems.itemCount,
                key = pagingItems.itemKey { it.id }
            ) { index ->
                val transaction = pagingItems[index]
                transaction?.let {
                    SwipeTransactionItem(
                        transaction = transaction,
                        onDelete = { viewModel.showDeleteDialog(transaction) },
                        onEdit = { viewModel.selectTransaction(transaction) },
                        currency = currency
                    )
                }
            }

            // BOTTOM APPEND PAGING LOADING INDICATOR
            if (pagingItems.loadState.append is LoadState.Loading) {
                item {
                    CircularProgressIndicator()
                }
            }
        }
    }

    /*
    ---------------------------------------------------
    MODAL INTERACTION OVERLAYS (SHEETS & DIALOGS)
    ---------------------------------------------------
    */

    // TRANSACTION EDIT SHEET
    uiState.selectedTransaction?.let { transaction ->
        EditTransactionSheet(
            transaction = transaction,
            onDismiss = { viewModel.clearSelectedTransaction() },
            onSave = { updatedTransaction ->
                viewModel.updateTransaction(updatedTransaction)
                viewModel.clearSelectedTransaction()
            }
        )
    }

    // CONFIRMATION DELETE DIALOG
    if (uiState.showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissDeleteDialog() },
            title = { Text(text = "Delete Transaction") },
            text = { Text(text = "Are you sure you want to delete this transaction?") },
            confirmButton = {
                Button(
                    onClick = { viewModel.deleteSelectedTransaction() }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { viewModel.dismissDeleteDialog() }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}