package com.ledge.ui.dashboard.components

import com.ledge.core.model.DashboardPeriod
import com.ledge.core.model.PeriodFilter

import com.ledge.core.utils.date.DateFormatter
import com.ledge.core.utils.date.DateUtils

fun buildDashboardPeriodText(

    periodFilter: PeriodFilter

): String? {

    return when (

        periodFilter.period

    ) {

        DashboardPeriod.ALL -> {

            null
        }

        DashboardPeriod.DAY -> {

            "Today • " +

                    DateFormatter.format(
                        System.currentTimeMillis()
                    )
        }

        DashboardPeriod.WEEK -> {

            val start =
                DateUtils.startOfWeek()

            val end =
                System.currentTimeMillis()

            "Week • " +

                    DateFormatter.format(
                        start
                    ) +

                    " → " +

                    DateFormatter.format(
                        end
                    )
        }

        DashboardPeriod.MONTH -> {

            val start =
                DateUtils.startOfMonth()

            val end =
                System.currentTimeMillis()

            "Month • " +

                    DateFormatter.format(
                        start
                    ) +

                    " → " +

                    DateFormatter.format(
                        end
                    )
        }

        DashboardPeriod.CUSTOM -> {

            val from =

                periodFilter
                    .from ?: 0L

            val to =

                periodFilter
                    .to ?: 0L

            "Custom • " +

                    DateFormatter.format(
                        from
                    ) +

                    " → " +

                    DateFormatter.format(
                        to
                    )
        }
    }
}