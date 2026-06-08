package com.ledge.ui.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
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
import com.ledge.core.toRowData
import com.ledge.core.CurrencyType
import com.ledge.ui.components.core.LedgeScaffold
import com.ledge.ui.components.core.LedgeScreenTitle
import com.ledge.ui.components.input.LedgeTextField
import com.ledge.ui.components.transaction.TransactionRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel()
) {
    val query by viewModel.query.collectAsStateWithLifecycle()
    val transactions = viewModel.transactions.collectAsLazyPagingItems()

    // CENTRALIZED SCAFFOLDING - Enforcing global background styling
    LedgeScaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LedgeScreenTitle(title = "Search")

            // CENTRALIZED TEXT FIELD - Implements uniform shapes and properties automatically
            LedgeTextField(
                value = query,
                onValueChange = viewModel::updateQuery,
                label = "Search Transactions"
            )

            /*
            ---------------------------------------------------
            REACTIVE CONTENT STATES (LOADING / EMPTY / RESULTS)
            ---------------------------------------------------
            */
            when {
                // REFRESH INITIAL LOADING STATE
                transactions.loadState.refresh is LoadState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                // NO RESULTS / EMPTY MATCH FOUND
                transactions.itemCount == 0 -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (query.isBlank()) {
                                "No transactions yet"
                            } else {
                                "No matching transactions"
                            }
                        )
                    }
                }

                // MATCHED SEARCH RESULT ITEMS
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 24.dp)
                    ) {
                        items(
                            count = transactions.itemCount,
                            key = transactions.itemKey { it.id }
                        ) { index ->
                            val transaction = transactions[index]
                            transaction?.let {
                                TransactionRow(
                                    transaction = it.toRowData(CurrencyType.INR)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}