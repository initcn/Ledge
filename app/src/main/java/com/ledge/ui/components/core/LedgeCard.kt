package com.ledge.ui.components.core

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme

import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

import com.ledge.ui.theme.cardBackgroundElevated

@Composable
fun LedgeCard(

    modifier: Modifier = Modifier,

    containerColor: Color =

        MaterialTheme
            .colorScheme
            .cardBackgroundElevated,

    shape: Shape =
        RoundedCornerShape(18.dp),

    elevation: Dp = 0.dp,

    contentPadding: PaddingValues =

        PaddingValues(

            horizontal = 16.dp,

            vertical = 16.dp
        ),

    content:
    @Composable ColumnScope.() -> Unit
) {

    Card(

        modifier = modifier,

        shape = shape,

        colors =

            CardDefaults.cardColors(

                containerColor =
                    containerColor
            ),

        elevation =

            CardDefaults.cardElevation(

                defaultElevation =
                    elevation
            )
    ) {

        Column(

            modifier = Modifier
                .padding(contentPadding),

            content = content
        )
    }
}