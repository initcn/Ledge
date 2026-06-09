package com.ledge.ui.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.ledge.ui.components.core.LedgeScaffold
import com.ledge.ui.components.core.LedgeScreenTitle
import com.ledge.ui.components.input.LedgeTextField
import com.ledge.ui.components.transaction.EditTransactionSheet
import com.ledge.ui.components.transaction.SwipeTransactionItem
import com.ledge.ui.settings.SettingsViewModel

@Composable
fun SearchScreen(
    paddingValues: PaddingValues,
    viewModel: SearchViewModel = hiltViewModel(),
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    val query by viewModel.query.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val settingsUiState by settingsViewModel.uiState.collectAsStateWithLifecycle()

    val pagingItems = viewModel.transactionsStream.collectAsLazyPagingItems()
    val currency = settingsUiState.currency

    LedgeScaffold { _ ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LedgeScreenTitle(title = "Search & History")

            LedgeTextField(
                value = query,
                onValueChange = viewModel::updateQuery,
                label = "Search transactions..."
            )

            Box(modifier = Modifier.weight(1f)) {
                when {
                    pagingItems.loadState.refresh is LoadState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    pagingItems.itemCount == 0 && pagingItems.loadState.refresh is LoadState.NotLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (query.isBlank()) "No transactions found." else "No matching results found."
                            )
                        }
                    }

                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(bottom = 16.dp)
                        ) {
                            items(
                                count = pagingItems.itemCount,
                                key = pagingItems.itemKey { it.id }
                            ) { index ->
                                val transaction = pagingItems[index]
                                transaction?.let { item ->
                                    SwipeTransactionItem(
                                        transaction = item,
                                        onDelete = { viewModel.showDeleteDialog(item) },
                                        onEdit = { viewModel.selectTransaction(item) },
                                        currency = currency
                                    )
                                }
                            }

                            if (pagingItems.loadState.append is LoadState.Loading) {
                                item {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Modal Sheet Overlay for Editing Transactions
    uiState.selectedTransaction?.let { transaction ->
        EditTransactionSheet(
            transaction = transaction,
            onDismiss = { viewModel.clearSelectedTransaction() },
            onSave = { updated ->
                viewModel.updateTransaction(updated)
                viewModel.clearSelectedTransaction()
            }
        )
    }

    // Confirmation Alert Dialog for Deleting Transactions
    if (uiState.showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissDeleteDialog() },
            title = { Text(text = "Delete Transaction") },
            text = { Text(text = "Are you sure you want to permanently delete this transaction record?") },
            confirmButton = {
                Button(onClick = { viewModel.deleteSelectedTransaction() }) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.dismissDeleteDialog() }) {
                    Text("Cancel")
                }
            }
        )
    }
}