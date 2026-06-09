package com.ledge.core

import android.app.DatePickerDialog
import android.content.Context
import java.util.Calendar

object DatePickerHelper {

    fun showDatePicker(

        context: Context,

        onDateSelected: (Long) -> Unit
    ) {

        val calendar = Calendar.getInstance()

        DatePickerDialog(

            context,

            { _, year, month, day ->

                val selectedDate = Calendar.getInstance()

                selectedDate.set(

                    year, month, day, 0, 0, 0
                )

                onDateSelected(
                    selectedDate.timeInMillis
                )
            },

            calendar.get(Calendar.YEAR),

            calendar.get(Calendar.MONTH),

            calendar.get(Calendar.DAY_OF_MONTH)

        ).show()
    }
}