package com.ledge.core.utils.date

import java.time.format.DateTimeFormatter

object DateFormatter {

    private const val DEFAULT_PATTERN =
        "dd MMM yyyy"

    fun format(

        timestamp: Long,

        pattern: String =
            DEFAULT_PATTERN

    ): String {

        val formatter =

            DateTimeFormatter
                .ofPattern(pattern)

        return timestamp

            .toLocalDate()

            .format(formatter)
    }
}