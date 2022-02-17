package com.morphingcoffee.spacex.data.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LaunchDto(
    @Json(name = "name") val name: String?,
    @Json(name = "date_utc") val dateUtc: String?,
    @Json(name = "links") val linksDto: LaunchLinksDto?
)