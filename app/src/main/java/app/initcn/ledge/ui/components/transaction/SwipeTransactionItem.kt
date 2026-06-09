package app.initcn.ledge.ui.components.transaction

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import app.initcn.ledge.core.CurrencyType
import app.initcn.ledge.core.toRowData
import app.initcn.ledge.data.entity.TransactionEntity
import app.initcn.ledge.ui.components.cards.TransactionCard
import app.initcn.ledge.ui.theme.LedgeTheme
import app.initcn.ledge.ui.theme.expense
import app.initcn.ledge.ui.theme.income

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeTransactionItem(

    transaction: TransactionEntity,

    onDelete: () -> Unit,

    onEdit: () -> Unit,

    currency: CurrencyType
) {

    val dismissState =

        rememberSwipeToDismissBoxState(

            positionalThreshold = {

                it * 0.4f
            }
        )

    LaunchedEffect(

        dismissState.currentValue

    ) {

        when (

            dismissState.currentValue

        ) {

            SwipeToDismissBoxValue
                .StartToEnd -> {

                onDelete()

                dismissState.snapTo(

                    SwipeToDismissBoxValue
                        .Settled
                )
            }

            SwipeToDismissBoxValue
                .EndToStart -> {

                onEdit()

                dismissState.snapTo(

                    SwipeToDismissBoxValue
                        .Settled
                )
            }

            SwipeToDismissBoxValue
                .Settled -> Unit
        }
    }

    SwipeToDismissBox(

        state = dismissState,

        enableDismissFromStartToEnd =
            true,

        enableDismissFromEndToStart =
            true,

        backgroundContent = {

            SwipeBackground(

                dismissValue =

                    dismissState
                        .dismissDirection
            )
        }

    ) {

        val rowData = remember(
            transaction,
            currency
        ) {

            transaction.toRowData(
                currency
            )
        }

        TransactionCard(
            transaction = rowData
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwipeBackground(

    dismissValue:
    SwipeToDismissBoxValue
) {

    val backgroundColor = when (

        dismissValue

    ) {

        SwipeToDismissBoxValue
            .StartToEnd -> {

            MaterialTheme
                .colorScheme
                .expense
        }

        SwipeToDismissBoxValue
            .EndToStart -> {

            MaterialTheme
                .colorScheme
                .income
        }

        SwipeToDismissBoxValue
            .Settled -> {

            LedgeTheme.surfaces
                .surfaceHigh
        }
    }

    val alignment = when (

        dismissValue

    ) {

        SwipeToDismissBoxValue
            .StartToEnd -> {

            Alignment.CenterStart
        }

        SwipeToDismissBoxValue
            .EndToStart -> {

            Alignment.CenterEnd
        }

        SwipeToDismissBoxValue
            .Settled -> {

            Alignment.Center
        }
    }

    val icon = when (

        dismissValue

    ) {

        SwipeToDismissBoxValue
            .StartToEnd -> {

            Icons.Outlined.Delete
        }

        SwipeToDismissBoxValue
            .EndToStart -> {

            Icons.Outlined.Edit
        }

        SwipeToDismissBoxValue
            .Settled -> {

            null
        }
    }

    Box(

        modifier = Modifier

            .fillMaxSize()

            .clip(MaterialTheme.shapes.large)

            .background(
                backgroundColor
            )

            .padding(
                horizontal = 28.dp
            ),

        contentAlignment =
            alignment

    ) {

        icon?.let {

            Icon(

                imageVector = it,

                contentDescription =
                    null,

                tint = Color.White
            )
        }
    }
}