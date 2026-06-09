package app.initcn.ledge.ui.add

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.initcn.ledge.core.DatePickerHelper
import app.initcn.ledge.core.LedgeTextFormatter
import app.initcn.ledge.core.TransactionType
import app.initcn.ledge.ui.TransactionTypeSelector
import app.initcn.ledge.ui.components.core.LedgeScaffold
import app.initcn.ledge.ui.components.core.LedgeScreenTitle
import app.initcn.ledge.ui.components.input.LedgeDropdownField
import app.initcn.ledge.ui.components.input.LedgeTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    paddingValues: PaddingValues,
    viewModel: AddTransactionViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }

    val categoryData = viewModel.categories
    val debitCategories = categoryData.debit
    val creditCategories = categoryData.credit

    val categories = if (uiState.type == TransactionType.DEBIT) {
        debitCategories
    } else {
        creditCategories
    }
    val paymentInstruments = categoryData.paymentInstrument

    if (debitCategories.isEmpty() ||
        creditCategories.isEmpty() || paymentInstruments.isEmpty()) {
        return
    }

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            snackBarHostState.showSnackbar("Transaction Saved")
            viewModel.resetSaveState()
        }
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            snackBarHostState.showSnackbar(error)
            viewModel.clearError()
        }
    }

    LedgeScaffold(snackbarHostState = snackBarHostState) { _ ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(
                top = paddingValues.calculateTopPadding() + 12.dp,
                bottom = paddingValues.calculateBottomPadding() + 88.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                LedgeScreenTitle(title = "Add Transaction")
            }

            item {
                TransactionTypeSelector(
                    selectedType = uiState.type,
                    onTypeSelected = viewModel::updateType
                )
            }

            item {
                LedgeTextField(
                    value = uiState.amount,
                    onValueChange = viewModel::updateAmount,
                    label = "Amount",
                    isError = uiState.amountError != null,
                    supportingText = uiState.amountError?.let { error -> { Text(error) } },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(androidx.compose.ui.focus.FocusDirection.Down) }
                    )
                )
            }

            item {
                Box(modifier = Modifier.fillMaxWidth()) {
                    LedgeTextField(
                        value = LedgeTextFormatter.formatAbsoluteDate(uiState.selectedDate),
                        onValueChange = {},
                        label = "Date",
                        readOnly = true
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                DatePickerHelper.showDatePicker(context) { date ->
                                    viewModel.updateDate(date)
                                }
                            }
                    )
                }
            }

            item {
                LedgeDropdownField(
                    label = "Category",
                    value = uiState.category,
                    items = categories,
                    onItemSelected = viewModel::updateCategory
                )
            }

            item {
                LedgeDropdownField(
                    label = "Payment Instrument",
                    value = uiState.paymentInstrument,
                    items = paymentInstruments,
                    onItemSelected = viewModel::updatePaymentInstrument
                )
            }

            item {
                LedgeTextField(
                    value = uiState.note,
                    onValueChange = viewModel::updateNote,
                    label = "Note (Optional)",
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                )
            }

            item {
                Button(
                    onClick = {
                        focusManager.clearFocus()
                        viewModel.saveTransaction()
                    },
                    enabled = uiState.amountError == null && uiState.amount.isNotBlank(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Save Transaction")
                }
            }
        }
    }
}