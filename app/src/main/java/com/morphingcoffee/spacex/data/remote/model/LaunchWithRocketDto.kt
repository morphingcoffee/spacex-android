package com.morphingcoffee.spacex.data.remote.model

import com.morphingcoffee.spacex.domain.model.Launch
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LaunchWithRocketDto(
    @Json(name = "name") val name: String?,
    @Json(name = "date_utc") val dateUtc: String?,
    @Json(name = "links") val linksDto: LaunchLinksDto?,
    @Json(name = "rocket") val rocketDto: RocketDto?
)

fun LaunchWithRocketDto.toDomainModel(): Launch =
    Launch(
        name = name,
        rocket = rocketDto?.toDomainModel(),
    )