package com.ledge.data.model

data class DashboardTotals(

    val income: Long,

    val expense: Long,

    val borrowed: Long,

    val debtPaid: Long,

    val lent: Long,

    val lentRecovered: Long
)