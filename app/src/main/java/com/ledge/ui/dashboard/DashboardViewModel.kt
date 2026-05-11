package com.ledge.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.ledge.core.model.PeriodFilter

import com.ledge.domain.usecase.GetDashboardDataUseCase

import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.ExperimentalCoroutinesApi

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(

    private val getDashboardDataUseCase:
    GetDashboardDataUseCase

) : ViewModel() {

    private val _periodFilter =

        MutableStateFlow(
            PeriodFilter()
        )

    val periodFilter:
            StateFlow<PeriodFilter> =

        _periodFilter

    fun setPeriodFilter(

        filter: PeriodFilter

    ) {

        _periodFilter.value =
            filter
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val dashboardData =

        _periodFilter

            .flatMapLatest { filter ->

                getDashboardDataUseCase(
                    filter
                )
            }

            .stateIn(

                scope =
                    viewModelScope,

                started =

                    SharingStarted
                        .WhileSubscribed(5000),

                initialValue = null
            )
}