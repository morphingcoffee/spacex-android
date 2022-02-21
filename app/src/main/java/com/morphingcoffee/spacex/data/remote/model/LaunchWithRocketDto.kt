package com.morphingcoffee.spacex.data.remote.model

import com.morphingcoffee.spacex.data.local.model.LaunchEntity
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
    @Json(name = "date_local") val dateLocal: String?,
    @Json(name = "date_unix") val dateUnix: Long?,
    @Json(name = "links") val linksDto: LaunchLinksDto?,
    @Json(name = "rocket") val rocketDto: RocketDto?
)

fun LaunchWithRocketDto.toDomainModel(): Launch {
    val dateTime = DateTimeDto(unixDate = dateUnix).toDomainModel()
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
        launchDateTime = dateTime
    )
}

fun LaunchWithRocketDto.toEntity(): LaunchEntity {
    val dateTime = DateTimeDto(unixDate = dateUnix).toDomainModel()
    return LaunchEntity(
        uid = id,
        name = name,
        success = success,
        dateUtc = dateUtc,
        year = dateTime?.year,
        dateLocal = dateLocal,
        dateUnix = dateUnix,
        links = linksDto?.toEntity(),
        rocket = rocketDto?.toEntity()
    )
}