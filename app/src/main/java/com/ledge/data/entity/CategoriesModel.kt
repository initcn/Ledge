package com.ledge.data.entity

data class CategoriesModel(

    val debit: List<String> = emptyList(),

    val credit: List<String> = emptyList(),

    val paymentInstrument: List<String> = emptyList()
)