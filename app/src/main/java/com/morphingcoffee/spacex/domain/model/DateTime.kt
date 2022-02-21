package com.morphingcoffee.spacex.domain.model

/**
 * [DateTime] encapsulates a date & a time.
 *
 * @param month inclusively between 1 and 12
 * @param day inclusively between 1 and 31
 * @param hour inclusively between 0 and 23
 * @param minute inclusively between 0 and 59
 * @param second inclusively between 0 and 59
 */
data class DateTime(
    val year: Int,
    val month: Int,
    val day: Int,
    val hour: Int,
    val minute: Int,
    val second: Int
)