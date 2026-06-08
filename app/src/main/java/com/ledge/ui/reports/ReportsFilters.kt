package com.ledge.ui.reports

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.ledge.core.DashboardPeriod
import com.ledge.core.TransactionType
import com.ledge.core.DatePickerHelper
import com.ledge.domain.PeriodFilter
import com.ledge.ui.components.core.LedgeCard
import com.ledge.ui.components.filter.LedgeFilterChip
import com.ledge.ui.theme.LedgeTheme
import com.ledge.ui.theme.expense
import com.ledge.ui.theme.income

@OptIn(ExperimentalLayoutApi::class)
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
    val context = LocalContext.current // 🔔 Correctly grabs the Android Context for the DatePickerDialog
    var isCategoryTrayExpanded by remember { mutableStateOf(false) }

    val categorySummaryText = remember(selectedCategories, currentCategories) {
        when {
            selectedCategories.isEmpty() -> "All Categories"
            selectedCategories.size == currentCategories.size -> "All Categories Selected"
            else -> "${selectedCategories.size} Selected"
        }
    }

    val arrowRotation by animateFloatAsState(
        targetValue = if (isCategoryTrayExpanded) 180f else 0f,
        label = "ArrowRotation"
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 1. TRANSACTION TYPE FILTERS
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            LedgeFilterChip(
                text = TransactionType.DEBIT.name,
                selected = selectedType == TransactionType.DEBIT,
                selectedColor = MaterialTheme.colorScheme.expense,
                onClick = { onTypeSelected(if (selectedType == TransactionType.DEBIT) null else TransactionType.DEBIT) }
            )

            LedgeFilterChip(
                text = TransactionType.CREDIT.name,
                selected = selectedType == TransactionType.CREDIT,
                selectedColor = MaterialTheme.colorScheme.income,
                onClick = { onTypeSelected(if (selectedType == TransactionType.CREDIT) null else TransactionType.CREDIT) }
            )
        }

        // 2. COLLAPSIBLE MULTI-SELECT CATEGORY TRAY
        LedgeCard(
            modifier = Modifier.fillMaxWidth(),
            containerColor = LedgeTheme.surfaces.surfaceHigh,
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = null,
                            modifier = Modifier.rotate(arrowRotation),
                            tint = if (selectedCategories.isNotEmpty()) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Column {
                            Text(
                                text = "Categories",
                                style = MaterialTheme.typography.titleSmall
                            )
                            Text(
                                text = categorySummaryText,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    LedgeFilterChip(
                        text = if (isCategoryTrayExpanded) "Hide" else "Expand",
                        selected = isCategoryTrayExpanded,
                        onClick = { isCategoryTrayExpanded = !isCategoryTrayExpanded }
                    )
                }

                AnimatedVisibility(visible = isCategoryTrayExpanded) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp)
                    ) {
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            currentCategories.forEach { category ->
                                LedgeFilterChip(
                                    text = category,
                                    selected = category in selectedCategories,
                                    onClick = { onCategoryToggle(category) }
                                )
                            }
                        }
                    }
                }
            }
        }

        // 3. PERIOD FILTERS
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            DashboardPeriod.entries.forEach { period ->
                LedgeFilterChip(
                    text = period.label,
                    selected = periodFilter.period == period,
                    onClick = {
                        if (period == DashboardPeriod.CUSTOM) {
                            DatePickerHelper.showDatePicker(context) { fromDate ->
                                DatePickerHelper.showDatePicker(context) { toDate ->
                                    onPeriodSelected(
                                        PeriodFilter(
                                            period = DashboardPeriod.CUSTOM,
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