package com.morphingcoffee.spacex.data.remote.model

import com.squareup.moshi.JsonClass

/**
 * On server-side query endpoints accepts fields like:
 * "date_utc", "$or", "date_precision", "upcoming", "$text", etc.
 */
@JsonClass(generateAdapter = true)
open class RequestQuery()