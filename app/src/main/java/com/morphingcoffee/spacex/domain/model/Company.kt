package com.morphingcoffee.spacex.domain.model

data class Company(
    val companyName: String?,
    val founderName: String?,
    val foundedYear: Int?,
    val numOfEmployees: Int?,
    val valuationInUsd: Int?,
)