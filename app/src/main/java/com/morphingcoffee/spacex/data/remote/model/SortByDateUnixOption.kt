package com.morphingcoffee.spacex.data.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Requests sorting by "date_unix" field in a preferred direction.
 * [direction] must be 1 for Ascending sorting, and -1 for Descending sorting.
 */
@JsonClass(generateAdapter = true)
data class SortByDateUnixOption(@Json(name = "date_unix") val direction: Int)
