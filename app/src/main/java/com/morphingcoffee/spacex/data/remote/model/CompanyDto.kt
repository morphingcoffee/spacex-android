package com.morphingcoffee.spacex.data.remote.model

import com.morphingcoffee.spacex.data.local.model.CompanyEntity
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

fun CompanyDto.toDomainModel(): Company =
    Company(
        companyName = name,
        founderName = founder,
        foundedYear = founded,
        numOfEmployees = employees,
        launchSites = launchSites,
        valuationInUsd = valuation,
    )

fun CompanyDto.toEntity(): CompanyEntity =
    CompanyEntity(
        name = name,
        founder = founder,
        founded = founded,
        employees = employees,
        launchSites = launchSites,
        valuation = valuation,
    )