package app.initcn.ledge.data.entity

import androidx.compose.runtime.Immutable

@Immutable
data class CategoryTotal(

    val category: String,

    val total: Long
)