package com.ledge.core.utils.date

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

private val zoneId =
    ZoneId.systemDefault()

/*
---------------------------------------------------
LONG -> JAVA TIME
---------------------------------------------------
*/

fun Long.toInstant():
        Instant {

    return Instant
        .ofEpochMilli(this)
}

fun Long.toLocalDate():
        LocalDate {

    return toInstant()

        .atZone(zoneId)

        .toLocalDate()
}

fun Long.toLocalDateTime():
        LocalDateTime {

    return toInstant()

        .atZone(zoneId)

        .toLocalDateTime()
}

/*
---------------------------------------------------
JAVA TIME -> LONG
---------------------------------------------------
*/

fun LocalDate.toEpochMillis():
        Long {

    return atStartOfDay(zoneId)

        .toInstant()

        .toEpochMilli()
}

fun LocalDateTime.toEpochMillis():
        Long {

    return atZone(zoneId)

        .toInstant()

        .toEpochMilli()
}