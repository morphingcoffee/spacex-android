package com.morphingcoffee.spacex.data.models

import com.morphingcoffee.spacex.domain.models.Company
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CompanyDto(
    @Json(name = "name") val name: String?,
    @Json(name = "founder") val founder: String?,
    @Json(name = "founded") val founded: Int?,
    @Json(name = "employees") val employees: Int?,
    @Json(name = "valuation") val valuation: Int?,
)

fun toDomainModel(dto: CompanyDto): Company =
    Company(
        companyName = dto.name,
        founderName = dto.founder,
        foundedYear = dto.founded,
        numOfEmployees = dto.employees,
        valuationInUsd = dto.valuation,
    )