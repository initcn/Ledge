package com.ledge.domain

import com.ledge.core.DashboardPeriod
import com.ledge.core.DateUtils

object PeriodDateResolver {

    fun resolve(filter: PeriodFilter): Pair<Long, Long> {
        return when (filter.period) {
            DashboardPeriod.ALL -> Pair(0L, DateUtils.now())
            DashboardPeriod.DAY -> Pair(DateUtils.startOfDay(), DateUtils.now())
            DashboardPeriod.WEEK -> Pair(DateUtils.startOfWeek(), DateUtils.now())
            DashboardPeriod.MONTH -> Pair(DateUtils.startOfMonth(), DateUtils.now())
            DashboardPeriod.CUSTOM -> {
                val from = filter.from ?: 0L
                val to = filter.to ?: DateUtils.now()
                Pair(from, to)
            }
        }
    }
}