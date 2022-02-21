package com.morphingcoffee.spacex.data.remote.model

import com.morphingcoffee.spacex.domain.model.DateTime
import java.time.LocalDateTime
import java.time.ZoneOffset

/** Wrapper for time since epoch **/
data class DateTimeDto(val unixDate: Long?)

fun DateTimeDto.toDomainModel(): DateTime? {
    val dt: LocalDateTime?
    var domainDt: DateTime? = null
    if (unixDate != null) {
        dt = LocalDateTime.ofEpochSecond(
            unixDate,
            0,
            ZoneOffset.UTC
        )
        domainDt = DateTime(
            year = dt.year,
            month = dt.monthValue,
            day = dt.dayOfMonth,
            hour = dt.hour,
            minute = dt.minute,
            second = dt.second
        )
    }
    return domainDt
}