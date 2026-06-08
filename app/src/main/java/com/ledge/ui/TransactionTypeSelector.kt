package com.ledge.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ledge.core.TransactionType
import com.ledge.ui.theme.LedgeTheme

@Composable
fun TransactionTypeSelector(

    selectedType: TransactionType,

    onTypeSelected:
        (TransactionType) -> Unit
) {

    Row(

        modifier = Modifier
            .fillMaxWidth()
            .background(

                LedgeTheme.surfaces
                    .surface,

                RoundedCornerShape(16.dp)
            )
            .padding(4.dp)
    ) {

        listOf(

            TransactionType.DEBIT,
            TransactionType.CREDIT

        ).forEach { type ->

            val selected =
                selectedType == type

            Box(

                modifier = Modifier
                    .weight(1f)
                    .background(

                        if (selected) {

                            LedgeTheme.surfaces
                                .surfaceHighest

                        } else {

                            LedgeTheme.surfaces
                                .surface
                        },

                        RoundedCornerShape(12.dp)
                    )
                    .clickable {

                        onTypeSelected(type)
                    }
                    .padding(vertical = 12.dp),

                contentAlignment =
                    Alignment.Center
            ) {

                Text(

                    text = type.name,

                    color =

                        if (selected) {

                            LedgeTheme.colors.primary

                        } else {

                            LedgeTheme.colors.onSurfaceVariant
                        }
                )
            }
        }
    }
}