package com.ledge.ui.add

import androidx.compose.foundation.clickable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
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
import com.ledge.core.model.TransactionType
import com.ledge.core.utils.date.DateFormatter
import com.ledge.core.utils.date.DatePickerHelper
import com.ledge.ui.add.components.TransactionTypeSelector
import com.ledge.ui.components.core.LedgeScreenTitle
import com.ledge.ui.components.input.LedgeDropdownField
import com.ledge.ui.theme.LedgeTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(

    viewModel: AddTransactionViewModel =
        hiltViewModel()

) {

    val context =
        LocalContext.current

    val focusManager =
        LocalFocusManager.current

    val uiState by viewModel
        .uiState
        .collectAsStateWithLifecycle()

    val snackBarHostState =
        remember {

            SnackbarHostState()
        }

    /*
    ---------------------------------------------------
    CATEGORY DATA
    ---------------------------------------------------
    */

    val categoryData =
        viewModel.categories

    val debitCategories =
        categoryData.debit

    val creditCategories =
        categoryData.credit

    val categories =

        if (
            uiState.type ==
            TransactionType.DEBIT
        ) {

            debitCategories

        } else {

            creditCategories
        }

    val paymentInstruments =
        categoryData.paymentInstrument

    /*
    ---------------------------------------------------
    SAFETY
    ---------------------------------------------------
    */

    if (

        debitCategories.isEmpty() ||

        creditCategories.isEmpty() ||

        paymentInstruments.isEmpty()

    ) {

        return
    }



    /*
    ---------------------------------------------------
    SAVE SUCCESS
    ---------------------------------------------------
    */

    LaunchedEffect(
        uiState.isSaved
    ) {

        if (uiState.isSaved) {

            snackBarHostState.showSnackbar(
                "Transaction Saved"
            )

            viewModel.resetSaveState()
        }
    }

    /*
    ---------------------------------------------------
    ERROR
    ---------------------------------------------------
    */

    LaunchedEffect(
        uiState.error
    ) {

        uiState.error?.let { error ->

            snackBarHostState.showSnackbar(
                error
            )

            viewModel.clearError()
        }
    }

    /*
    ---------------------------------------------------
    UI
    ---------------------------------------------------
    */

    Scaffold(

        containerColor =

            LedgeTheme.surfaces
                .background,

        snackbarHost = {

            SnackbarHost(

                hostState =
                    snackBarHostState
            )
        }
    ) { paddingValues ->

        Column(

            modifier = Modifier

                .fillMaxSize()

                .verticalScroll(
                    rememberScrollState()
                )

                .padding(paddingValues)

                .padding(16.dp),

            verticalArrangement =
                Arrangement.spacedBy(20.dp)
        ) {

            LedgeScreenTitle(
                title = "Add Transaction"
            )

            /*
            ---------------------------------------------------
            TYPE
            ---------------------------------------------------
            */

            TransactionTypeSelector(

                selectedType =
                    uiState.type,

                onTypeSelected =
                    viewModel::updateType
            )

            /*
            ---------------------------------------------------
            AMOUNT
            ---------------------------------------------------
            */

            OutlinedTextField(

                value =
                    uiState.amount,

                onValueChange =
                    viewModel::updateAmount,

                modifier = Modifier
                    .fillMaxWidth(),

                label = {

                    Text("Amount")
                },

                keyboardOptions = KeyboardOptions(

                    keyboardType =
                        KeyboardType.Number,

                    imeAction =
                        ImeAction.Next
                ),

                keyboardActions = KeyboardActions(

                    onNext = {

                        focusManager.moveFocus(
                            androidx.compose.ui.focus.FocusDirection.Down
                        )
                    }
                ),

                singleLine = true,

                isError =

                    uiState.amountError
                            != null,

                supportingText = {

                    uiState.amountError
                        ?.let { error ->

                            Text(error)
                        }
                }
            )

            /*
            ---------------------------------------------------
            DATE
            ---------------------------------------------------
            */

            Box(

                modifier = Modifier
                    .fillMaxWidth()
            ) {

                OutlinedTextField(

                    value =

                        DateFormatter.format(

                            uiState
                                .selectedDate
                        ),

                    onValueChange = {},

                    readOnly = true,

                    modifier = Modifier
                        .fillMaxWidth(),

                    label = {

                        Text("Date")
                    }
                )

                Box(

                    modifier = Modifier

                        .fillMaxWidth()

                        .height(56.dp)

                        .clickable(

                            interactionSource =

                                remember {

                                    MutableInteractionSource()
                                },

                            indication = null

                        ) {

                            DatePickerHelper
                                .showDatePicker(
                                    context
                                ) { date ->

                                    viewModel
                                        .updateDate(
                                            date
                                        )
                                }
                        }
                )
            }

            /*
            ---------------------------------------------------
            CATEGORY
            ---------------------------------------------------
            */

            LedgeDropdownField(

                label = "Category",

                value =
                    uiState.category,

                items =
                    categories,

                onItemSelected =
                    viewModel::updateCategory
            )

            /*
            ---------------------------------------------------
            PAYMENT INSTRUMENT
            ---------------------------------------------------
            */

            LedgeDropdownField(

                label =
                    "Payment Instrument",

                value =

                    uiState
                        .paymentInstrument,

                items =
                    paymentInstruments,

                onItemSelected =

                    viewModel
                    ::updatePaymentInstrument
            )

            /*
            ---------------------------------------------------
            NOTE
            ---------------------------------------------------
            */

            OutlinedTextField(

                value =
                    uiState.note,

                onValueChange =
                    viewModel::updateNote,

                modifier = Modifier
                    .fillMaxWidth(),

                label = {

                    Text(
                        "Note (Optional)"
                    )
                },

                keyboardOptions = KeyboardOptions(

                    imeAction =
                        ImeAction.Done
                ),

                keyboardActions = KeyboardActions(

                    onDone = {

                        focusManager.clearFocus()
                    }
                )
            )

            /*
            ---------------------------------------------------
            SAVE
            ---------------------------------------------------
            */

            Button(

                onClick = {

                    focusManager.clearFocus()

                    viewModel
                        .saveTransaction()
                },

                enabled =

                    uiState.amountError
                            == null &&

                            uiState.amount
                                .isNotBlank(),

                modifier = Modifier
                    .fillMaxWidth()
            ) {

                Text(
                    text =
                        "Save Transaction"
                )
            }
        }
    }
}