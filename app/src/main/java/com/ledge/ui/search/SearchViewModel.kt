package com.ledge.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import androidx.paging.PagingData
import androidx.paging.cachedIn

import com.ledge.data.entity.TransactionEntity
import com.ledge.data.repository.TransactionRepository

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(

    private val repository:
    TransactionRepository

) : ViewModel() {

    /*
    ---------------------------------------------------
    QUERY
    ---------------------------------------------------
    */

    private val _query =

        MutableStateFlow("")

    val query:
            StateFlow<String> =
        _query.asStateFlow()

    /*
    ---------------------------------------------------
    RESULTS
    ---------------------------------------------------
    */

    @OptIn(

        FlowPreview::class,
        ExperimentalCoroutinesApi::class

    )
    val transactions =

        query

            .debounce(300)

            .flatMapLatest { query ->

                if (

                    query.isBlank()

                ) {

                    repository
                        .getPagedTransactions()

                } else {

                    repository
                        .searchPagedTransactions(query)
                }
            }

            .cachedIn(viewModelScope)

    /*
    ---------------------------------------------------
    UPDATE
    ---------------------------------------------------
    */

    fun updateQuery(

        query: String
    ) {

        _query.value = query
    }
}