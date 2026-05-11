package com.ledge.ui.reports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.ledge.core.model.PeriodFilter
import com.ledge.core.model.TransactionType

import com.ledge.domain.category.CategoryProvider

import com.ledge.domain.model.ReportsData
import com.ledge.domain.usecase.GetReportsDataUseCase

import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.ExperimentalCoroutinesApi

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow

import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

import javax.inject.Inject

@HiltViewModel
class ReportsViewModel @Inject constructor(

    private val getReportsDataUseCase:
    GetReportsDataUseCase,

    private val categoryProvider:
    CategoryProvider

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
            ReportsUiState()
        )

    /*
    ---------------------------------------------------
    FILTERS
    ---------------------------------------------------
    */

    fun setType(

        type: TransactionType?
    ) {

        _uiState.value =

            _uiState.value.copy(

                selectedType = type
            )
    }

    fun toggleCategory(

        category: String
    ) {

        val currentCategories =

            _uiState.value
                .selectedCategories

        val updatedCategories =

            if (
                category in currentCategories
            ) {

                currentCategories - category

            } else {

                currentCategories + category
            }

        _uiState.value =

            _uiState.value.copy(

                selectedCategories =
                    updatedCategories
            )
    }

    fun setPeriodFilter(

        filter: PeriodFilter
    ) {

        _uiState.value =

            _uiState.value.copy(

                periodFilter = filter
            )
    }

    /*
    ---------------------------------------------------
    REPORTS DATA
    ---------------------------------------------------
    */

    @OptIn(ExperimentalCoroutinesApi::class)
    private val reportsDataFlow =

        _uiState.flatMapLatest { state ->

            getReportsDataUseCase(

                selectedType =
                    state.selectedType,

                selectedCategories =
                    state.selectedCategories,

                periodFilter =
                    state.periodFilter
            )
        }

    /*
    ---------------------------------------------------
    EXPOSED UI STATE
    ---------------------------------------------------
    */

    val uiState:
            StateFlow<ReportsUiState> =

        combine(

            reportsDataFlow,

            _uiState

        ) {

                reportsData:
                ReportsData,

                state:
                ReportsUiState ->

            state.copy(

                transactions =

                    reportsData
                        .transactions,

                totalCredit =

                    reportsData
                        .totalCredit,

                totalDebit =

                    reportsData
                        .totalDebit,

                balance =

                    reportsData
                        .balance
            )

        }.stateIn(

            scope =
                viewModelScope,

            started =

                SharingStarted
                    .WhileSubscribed(5000),

            initialValue =
                ReportsUiState()
        )
}