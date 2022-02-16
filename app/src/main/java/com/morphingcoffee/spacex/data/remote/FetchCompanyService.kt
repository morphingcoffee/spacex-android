package com.morphingcoffee.spacex.data.remote

import com.morphingcoffee.spacex.data.model.CompanyDto
import retrofit2.Response
import retrofit2.http.GET

interface FetchCompanyService {
    @GET("v4/company")
    suspend fun fetchCompany(): Response<CompanyDto>
}