package com.morphingcoffee.spacex.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LaunchLinksDto(
    @Json(name = "webcast") val youtubeURL: String?,
    @Json(name = "article") val articleURL: String?,
    @Json(name = "wikipedia") val wikiURL: String?
)