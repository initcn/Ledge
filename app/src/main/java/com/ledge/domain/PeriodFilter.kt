package com.ledge.domain

import com.ledge.core.DashboardPeriod

data class PeriodFilter(
    val period: DashboardPeriod = DashboardPeriod.MONTH,
    val from: Long? = null,
    val to: Long? = null
)