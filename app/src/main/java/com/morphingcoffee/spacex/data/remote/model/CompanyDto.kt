package com.morphingcoffee.spacex.data.remote.model

import com.morphingcoffee.spacex.domain.model.Company
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CompanyDto(
    @Json(name = "name") val name: String?,
    @Json(name = "founder") val founder: String?,
    @Json(name = "founded") val founded: Int?,
    @Json(name = "employees") val employees: Int?,
    @Json(name = "launch_sites") val launchSites: Int?,
    @Json(name = "valuation") val valuation: Long?,
)

// TODO decide whether this should move to a separate Mapper class (for testability?)
fun CompanyDto.toDomainModel(): Company =
    Company(
        companyName = name,
        founderName = founder,
        foundedYear = founded,
        numOfEmployees = employees,
        launchSites = launchSites,
        valuationInUsd = valuation,
    )