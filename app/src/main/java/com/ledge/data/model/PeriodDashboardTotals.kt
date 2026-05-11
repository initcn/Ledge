package com.ledge.data.model

data class PeriodDashboardTotals(

    val income: Long,

    val expense: Long,

    val borrowed: Long,

    val debtPaid: Long,

    val lent: Long,

    val lentRecovered: Long
)