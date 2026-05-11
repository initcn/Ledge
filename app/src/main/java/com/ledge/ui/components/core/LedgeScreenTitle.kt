package com.ledge.ui.components.core

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier

@Composable
fun LedgeScreenTitle(

    title: String,

    modifier: Modifier =
        Modifier
) {

    Text(

        text = title,

        modifier = modifier,

        style =
            MaterialTheme.typography
                .headlineMedium
    )
}