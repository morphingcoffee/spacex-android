package com.morphingcoffee.spacex.data.remote.model

import com.morphingcoffee.spacex.data.local.LaunchEntity
import com.morphingcoffee.spacex.domain.model.Launch
import com.morphingcoffee.spacex.domain.model.LaunchStatus
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LaunchWithRocketDto(
    @Json(name = "id") val id: String,
    @Json(name = "name") val name: String?,
    @Json(name = "success") val success: Boolean?,
    @Json(name = "date_utc") val dateUtc: String?,
    @Json(name = "links") val linksDto: LaunchLinksDto?,
    @Json(name = "rocket") val rocketDto: RocketDto?
)

fun LaunchWithRocketDto.toDomainModel(): Launch {
    val status = when (success) {
        true -> LaunchStatus.Successful
        false -> LaunchStatus.Failed
        else -> LaunchStatus.FutureLaunch
    }
    return Launch(
        id = id,
        name = name,
        rocket = rocketDto?.toDomainModel(),
        links = linksDto?.toDomainModel(),
        launchStatus = status,
        launchDateTime = dateUtc
    )
}

fun LaunchWithRocketDto.toEntity(): LaunchEntity {
    return LaunchEntity(
        uid = id,
        name = name,
        success = success,
        dateUtc = dateUtc,
        links = linksDto?.toEntity(),
        rocket = rocketDto?.toEntity()
    )
}