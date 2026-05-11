package com.ledge.core.model

data class PeriodFilter(

    val period: DashboardPeriod =
        DashboardPeriod.MONTH,

    val from: Long? = null,

    val to: Long? = null
)