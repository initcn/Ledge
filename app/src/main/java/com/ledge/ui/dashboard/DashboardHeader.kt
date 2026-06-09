package com.ledge.ui.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.ledge.core.DashboardPeriod
import com.ledge.core.DatePickerHelper
import com.ledge.domain.PeriodFilter
import com.ledge.ui.components.core.LedgeScreenTitle

@Composable
fun DashboardHeader(
    periodFilter: PeriodFilter,
    onPeriodSelected: (PeriodFilter) -> Unit
) {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        LedgeScreenTitle(title = "Ledge", modifier = Modifier.weight(1f))

        Box {
            OutlinedButton(onClick = { expanded = true }) {
                Text(text = periodFilter.period.label)
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DashboardPeriod.entries.forEach { period ->
                    DropdownMenuItem(
                        text = { Text(text = period.label) },
                        onClick = {
                            expanded = false
                            if (period == DashboardPeriod.CUSTOM) {
                                DatePickerHelper.showDatePicker(context) { fromDate ->
                                    DatePickerHelper.showDatePicker(context) { toDate ->
                                        onPeriodSelected(
                                            PeriodFilter(
                                                period = period,
                                                from = fromDate,
                                                to = toDate
                                            )
                                        )
                                    }
                                }
                            } else {
                                onPeriodSelected(PeriodFilter(period))
                            }
                        }
                    )
                }
            }
        }
    }
}