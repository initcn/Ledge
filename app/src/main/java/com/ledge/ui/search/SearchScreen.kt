package com.ledge.ui.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.lazy.LazyColumn

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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

import com.ledge.core.extensions.toRowData
import com.ledge.core.model.CurrencyType

import com.ledge.ui.components.core.LedgeScreenTitle
import com.ledge.ui.components.transaction.TransactionRow

import com.ledge.ui.theme.LedgeTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(

    viewModel: SearchViewModel =
        hiltViewModel()

) {

    val query by viewModel
        .query
        .collectAsStateWithLifecycle()

    val transactions =

        viewModel.transactions
            .collectAsLazyPagingItems()

    Scaffold(

        containerColor =

            LedgeTheme.surfaces
                .background

    ) { paddingValues ->

        Column(

            modifier = Modifier

                .fillMaxSize()

                .padding(paddingValues)

                .padding(16.dp),

            verticalArrangement =
                Arrangement.spacedBy(16.dp)
        ) {

            /*
            ---------------------------------------------------
            TITLE
            ---------------------------------------------------
            */

            LedgeScreenTitle(
                title = "Search"
            )

            /*
            ---------------------------------------------------
            SEARCH FIELD
            ---------------------------------------------------
            */

            OutlinedTextField(

                value = query,

                onValueChange =
                    viewModel::updateQuery,

                modifier = Modifier
                    .fillMaxWidth(),

                label = {

                    Text("Search Transactions")
                },

                singleLine = true
            )

            /*
            ---------------------------------------------------
            CONTENT
            ---------------------------------------------------
            */

            when {

                /*
                ---------------------------------------------------
                LOADING
                ---------------------------------------------------
                */

                transactions.loadState.refresh
                        is LoadState.Loading -> {

                    Box(

                        modifier = Modifier
                            .fillMaxSize(),

                        contentAlignment =
                            Alignment.Center

                    ) {

                        CircularProgressIndicator()
                    }
                }

                /*
                ---------------------------------------------------
                EMPTY
                ---------------------------------------------------
                */

                transactions.itemCount == 0 -> {

                    Box(

                        modifier = Modifier
                            .fillMaxSize(),

                        contentAlignment =
                            Alignment.Center

                    ) {

                        Text(

                            text =

                                if (

                                    query.isBlank()

                                ) {

                                    "No transactions yet"

                                } else {

                                    "No matching transactions"
                                }
                        )
                    }
                }

                /*
                ---------------------------------------------------
                RESULTS
                ---------------------------------------------------
                */

                else -> {

                    LazyColumn(

                        modifier = Modifier
                            .fillMaxSize(),

                        verticalArrangement =
                            Arrangement.spacedBy(12.dp),

                        contentPadding =
                            PaddingValues(bottom = 24.dp)

                    ) {

                        items(

                            count =
                                transactions.itemCount,

                            key =
                                transactions.itemKey {
                                    it.id
                                }

                        ) { index ->

                            val transaction =
                                transactions[index]

                            transaction?.let {

                                TransactionRow(

                                    transaction =

                                        it.toRowData(
                                            CurrencyType.INR
                                        )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}