package com.ledge.ui.components.transaction

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions

import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

import com.ledge.data.entity.TransactionEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTransactionSheet(

    transaction: TransactionEntity,
    onDismiss: () -> Unit,
    onSave: (TransactionEntity) -> Unit
) {

    val sheetState =
        rememberModalBottomSheetState()

    var amount by remember {
        mutableStateOf(
            transaction.amount.toString()
        )
    }

    var note by remember {
        mutableStateOf(transaction.note)
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),

            verticalArrangement =
                Arrangement.spacedBy(16.dp)
        ) {

            Text(text = "Edit Transaction")

            OutlinedTextField(

                value = amount,
                onValueChange = { amount = it },

                modifier = Modifier.fillMaxWidth(),

                label = { Text("Amount") },

                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )

            OutlinedTextField(

                value = note,
                onValueChange = { note = it },

                modifier = Modifier.fillMaxWidth(),

                label = { Text("Note") }
            )

            Button(

                onClick = {

                    val parsedAmount =
                        amount.trim()
                            .toBigDecimalOrNull()
                            ?.movePointRight(2)
                            ?.longValueExact()
                            ?: transaction.amount

                    val updated =
                        transaction.copy(
                            amount = parsedAmount,
                            note = note
                        )

                    onSave(updated)
                },

                modifier = Modifier.fillMaxWidth()
            ) {

                Text("Save Changes")
            }
        }
    }
}