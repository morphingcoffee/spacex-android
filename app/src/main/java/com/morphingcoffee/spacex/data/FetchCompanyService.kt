package com.morphingcoffee.spacex.data

import com.morphingcoffee.spacex.data.models.CompanyDto
import retrofit2.Response
import retrofit2.http.GET

interface FetchCompanyService {
    @GET("v4/company")
    suspend fun getCompany(): Response<CompanyDto>
}