package com.morphingcoffee.spacex.domain.models

data class Company(
    val companyName: String?,
    val founderName: String?,
    val foundedYear: Int?,
    val numOfEmployees: Int?,
    val valuationInUsd: Int?,
)