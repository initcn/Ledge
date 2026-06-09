package app.initcn.ledge.core

import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

object DateUtils {
    private val zoneId = ZoneId.systemDefault()


    // START OF DAY
    fun startOfDay(): Long {

        return LocalDate.now()

            .atStartOfDay(zoneId)

            .toInstant()

            .toEpochMilli()
    }

    // START OF WEEK
    fun startOfWeek(): Long {

        return LocalDate.now()

            .with(DayOfWeek.MONDAY)

            .atStartOfDay(zoneId)

            .toInstant()

            .toEpochMilli()
    }

    // START OF MONTH
    fun startOfMonth(): Long {

        return LocalDate.now()

            .withDayOfMonth(1)

            .atStartOfDay(zoneId)

            .toInstant()

            .toEpochMilli()
    }

    // END OF MONTH
    fun endOfMonth(): Long {

        return LocalDate.now()

            .withDayOfMonth(
                LocalDate.now().lengthOfMonth()
            )

            .atTime(LocalTime.MAX)

            .atZone(zoneId)

            .toInstant()

            .toEpochMilli()
    }


    // CURRENT MONTH
    fun currentMonth(): Int {

        return LocalDate.now().monthValue
    }


    // CURRENT YEAR
    fun currentYear(): Int {

        return LocalDate.now().year
    }

    // NOW
    fun now(): Long {

        return Instant.now().toEpochMilli()
    }


    // TODAY
    fun today(): LocalDate {

        return LocalDate.now()
    }
}