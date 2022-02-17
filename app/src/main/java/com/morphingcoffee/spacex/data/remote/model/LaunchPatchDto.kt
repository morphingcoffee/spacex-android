package com.morphingcoffee.spacex.data.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LaunchPatchDto(
    @Json(name = "small") val small: String?,
    @Json(name = "large") val large: String?
)