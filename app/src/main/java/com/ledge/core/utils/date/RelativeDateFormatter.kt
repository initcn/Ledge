package com.ledge.core.utils.date

import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object RelativeDateFormatter {

    private val zoneId =
        ZoneId.systemDefault()

    private val formatter =

        DateTimeFormatter.ofPattern(
            "dd MMM yyyy"
        )

    fun format(
        timestamp: Long
    ): String {

        val now =
            Instant.now()

        val instant =
            Instant.ofEpochMilli(
                timestamp
            )

        val duration =

            Duration.between(
                instant,
                now
            )

        return when {

            duration.toMinutes() < 1 -> {

                "Just now"
            }

            duration.toHours() < 1 -> {

                "${duration.toMinutes()} min ago"
            }

            duration.toDays() < 1 -> {

                "${duration.toHours()} hr ago"
            }

            duration.toDays() < 2 -> {

                "Yesterday"
            }

            else -> {

                instant

                    .atZone(zoneId)

                    .toLocalDate()

                    .format(formatter)
            }
        }
    }
}