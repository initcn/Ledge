package com.ledge.ui.reports

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow

import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

import com.ledge.core.DashboardPeriod
import com.ledge.domain.PeriodFilter
import com.ledge.core.TransactionType

import com.ledge.core.DatePickerHelper

import com.ledge.ui.components.filter.LedgeFilterChip

import androidx.compose.material3.MaterialTheme

import com.ledge.ui.theme.expense
import com.ledge.ui.theme.income

@Composable
fun ReportsFilters(

    modifier: Modifier = Modifier,

    selectedType: TransactionType?,

    selectedCategories: Set<String>,

    currentCategories: List<String>,

    periodFilter: PeriodFilter,

    onTypeSelected: (TransactionType?) -> Unit,

    onCategoryToggle: (String) -> Unit,

    onPeriodSelected: (PeriodFilter) -> Unit
) {

    val context =
        LocalContext.current

    FlowRow(

        modifier = modifier,

        horizontalArrangement =
            Arrangement.spacedBy(10.dp),

        verticalArrangement =
            Arrangement.spacedBy(10.dp)
    ) {

        LedgeFilterChip(

            text =
                TransactionType
                    .DEBIT
                    .name,

            selected =
                selectedType ==
                        TransactionType.DEBIT,

            selectedColor =
                MaterialTheme.colorScheme.expense,

            onClick = {

                onTypeSelected(

                    if (
                        selectedType ==
                        TransactionType.DEBIT
                    ) {

                        null

                    } else {

                        TransactionType.DEBIT
                    }
                )
            }
        )

        LedgeFilterChip(

            text =
                TransactionType
                    .CREDIT
                    .name,

            selected =
                selectedType ==
                        TransactionType.CREDIT,

            selectedColor =
                MaterialTheme.colorScheme.income,

            onClick = {

                onTypeSelected(

                    if (
                        selectedType ==
                        TransactionType.CREDIT
                    ) {

                        null

                    } else {

                        TransactionType.CREDIT
                    }
                )
            }
        )
    }

    FlowRow(

        horizontalArrangement =
            Arrangement.spacedBy(10.dp),

        verticalArrangement =
            Arrangement.spacedBy(10.dp)
    ) {

        currentCategories.forEach { category ->

            LedgeFilterChip(

                text =
                    category,

                selected =

                    category in
                            selectedCategories,

                onClick = {

                    onCategoryToggle(
                        category
                    )
                }
            )
        }
    }

    FlowRow(

        horizontalArrangement =
            Arrangement.spacedBy(10.dp),

        verticalArrangement =
            Arrangement.spacedBy(10.dp)
    ) {

        DashboardPeriod.entries
            .forEach { period ->

                LedgeFilterChip(

                    text =
                        period.label,

                    selected =

                        periodFilter.period ==
                                period,

                    onClick = {

                        if (
                            period ==
                            DashboardPeriod.CUSTOM
                        ) {

                            DatePickerHelper
                                .showDatePicker(
                                    context
                                ) { fromDate ->

                                    DatePickerHelper
                                        .showDatePicker(
                                            context
                                        ) { toDate ->

                                            onPeriodSelected(

                                                PeriodFilter(

                                                    period =
                                                        DashboardPeriod.CUSTOM,

                                                    from =
                                                        fromDate,

                                                    to =
                                                        toDate
                                                )
                                            )
                                        }
                                }

                        } else {

                            onPeriodSelected(

                                PeriodFilter(
                                    period
                                )
                            )
                        }
                    }
                )
            }
    }
}