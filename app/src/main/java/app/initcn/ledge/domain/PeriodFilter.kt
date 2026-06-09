package app.initcn.ledge.domain

import app.initcn.ledge.core.DashboardPeriod

data class PeriodFilter(
    val period: DashboardPeriod = DashboardPeriod.MONTH,
    val from: Long? = null,
    val to: Long? = null
)