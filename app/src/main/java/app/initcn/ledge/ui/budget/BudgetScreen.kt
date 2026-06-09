package app.initcn.ledge.ui.budget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.initcn.ledge.core.LedgeTextFormatter
import app.initcn.ledge.data.repository.BudgetProgress
import app.initcn.ledge.ui.components.core.LedgeCard
import app.initcn.ledge.ui.components.core.LedgeScaffold
import app.initcn.ledge.ui.components.core.LedgeScreenTitle
import app.initcn.ledge.ui.components.input.LedgeTextField
import app.initcn.ledge.ui.theme.LedgeTheme
import app.initcn.ledge.ui.theme.expense
import app.initcn.ledge.ui.theme.income

@Composable
fun BudgetScreen(
    viewModel: BudgetViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val budgets by viewModel.budgets.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }

    val categoryData = viewModel.categories
    val debitCategories = categoryData.debit

    val budgetsByCategory = remember(budgets) {
        budgets.associateBy { it.category }
    }

    LaunchedEffect(key1 = uiState.isSaved) {
        if (uiState.isSaved) {
            snackBarHostState.showSnackbar("Budget Saved")
            viewModel.resetSaveState()
        }
    }

    LedgeScaffold(snackbarHostState = snackBarHostState) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    LedgeScreenTitle(title = "Budgets")
                    Text(
                        text = "Monthly category spending limits",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            items(
                items = debitCategories,
                key = { category -> category }
            ) { category ->
                val budget = budgetsByCategory[category]

                BudgetInputCard(
                    category = category,
                    amount = if (uiState.category == category) {
                        uiState.amount
                    } else {
                        budget?.let { (it.budgetAmount / 100.0).toString().removeSuffix(".0") }
                            ?: ""
                    },
                    amountError = if (uiState.category == category) {
                        uiState.amountError
                    } else {
                        null
                    },
                    progress = budget,
                    isSaving = uiState.isSaving && uiState.category == category,
                    onAmountChange = {
                        viewModel.updateCategory(category)
                        viewModel.updateAmount(it)
                    },
                    onSave = {
                        viewModel.updateCategory(category)
                        viewModel.saveBudget()
                    }
                )
            }
        }
    }
}

@Composable
private fun BudgetInputCard(
    category: String,
    amount: String,
    amountError: String?,
    progress: BudgetProgress?,
    isSaving: Boolean,
    onAmountChange: (String) -> Unit,
    onSave: () -> Unit
) {
    val progressValue = (progress?.progress ?: 0f).coerceIn(0f, 1f)
    val expenseColor = MaterialTheme.colorScheme.expense
    val incomeColor = MaterialTheme.colorScheme.income
    val warningColor = MaterialTheme.colorScheme.tertiary

    val progressColor = remember(
        progressValue,
        progress?.isOverBudget,
        expenseColor,
        incomeColor,
        warningColor
    ) {
        when {
            progress?.isOverBudget == true -> expenseColor
            progressValue >= 0.8f -> warningColor
            else -> incomeColor
        }
    }

    LedgeCard(
        modifier = Modifier.fillMaxWidth(),
        containerColor = LedgeTheme.surfaces.surfaceHigh
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = category,
                        style = MaterialTheme.typography.titleMedium
                    )

                    progress?.let {
                        Text(
                            text = "${LedgeTextFormatter.formatCurrency(it.spentAmount)} / ${
                                LedgeTextFormatter.formatCurrency(
                                    it.budgetAmount
                                )
                            }",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Button(
                    onClick = onSave,
                    enabled = !isSaving && amountError == null && amount.isNotBlank(),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(text = if (isSaving) "Saving..." else "Save")
                }
            }

            // CENTRALIZED FIELD - Eliminated massive ad-hoc configuration block
            LedgeTextField(
                value = amount,
                onValueChange = { value ->
                    if (value.all { it.isDigit() || it == '.' }) {
                        onAmountChange(value)
                    }
                },
                label = "Budget",
                isError = amountError != null,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal
                )
            )

            progress?.let {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    LinearProgressIndicator(
                        progress = { progressValue },
                        modifier = Modifier.fillMaxWidth(),
                        color = progressColor,
                        trackColor = LedgeTheme.surfaces.surfaceHighest
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = if (it.isOverBudget) "Over budget" else "Remaining",
                            style = MaterialTheme.typography.bodySmall,
                            color = progressColor
                        )

                        Text(
                            text = LedgeTextFormatter.formatCurrency(
                                if (it.isOverBudget) it.remainingAmount.times(-1) else it.remainingAmount
                            ),
                            style = MaterialTheme.typography.bodySmall,
                            color = progressColor
                        )
                    }
                }
            }
        }
    }
}