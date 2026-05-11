package com.ledge.ui.components.filter

import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable

import androidx.compose.ui.graphics.Color

import com.ledge.ui.theme.cardBackground

@Composable
fun LedgeFilterChip(

    text: String,

    selected: Boolean,

    selectedColor: Color =

        MaterialTheme
            .colorScheme
            .primary,

    onClick: () -> Unit
) {

    AssistChip(

        onClick = onClick,

        label = {

            Text(

                text = text,

                color =

                    if (selected) {

                        MaterialTheme
                            .colorScheme
                            .onPrimary

                    } else {

                        MaterialTheme
                            .colorScheme
                            .onSurface
                    }
            )
        },

        colors =

            AssistChipDefaults
                .assistChipColors(

                    containerColor =

                        if (selected) {

                            selectedColor

                        } else {

                            MaterialTheme
                                .colorScheme
                                .cardBackground
                        },

                    labelColor =

                        if (selected) {

                            MaterialTheme
                                .colorScheme
                                .onPrimary

                        } else {

                            MaterialTheme
                                .colorScheme
                                .onSurface
                        }
                )
    )
}