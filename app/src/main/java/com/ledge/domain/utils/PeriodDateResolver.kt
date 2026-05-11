package com.ledge.domain.utils

import com.ledge.core.model.DashboardPeriod
import com.ledge.core.model.PeriodFilter

import com.ledge.core.utils.date.DateUtils

object PeriodDateResolver {

    fun resolve(
        filter: PeriodFilter
    ): Pair<Long, Long> {

        return when (filter.period) {

            DashboardPeriod.ALL -> {

                Pair(
                    0L,
                    DateUtils.now()
                )
            }

            DashboardPeriod.DAY -> {

                Pair(
                    DateUtils.startOfDay(),
                    DateUtils.now()
                )
            }

            DashboardPeriod.WEEK -> {

                Pair(
                    DateUtils.startOfWeek(),
                    DateUtils.now()
                )
            }

            DashboardPeriod.MONTH -> {

                Pair(
                    DateUtils.startOfMonth(),
                    DateUtils.now()
                )
            }

            DashboardPeriod.CUSTOM -> {

                val from =
                    filter.from ?: 0L

                val to =
                    filter.to
                        ?: DateUtils.now()

                Pair(
                    from,
                    to
                )
            }
        }
    }
}