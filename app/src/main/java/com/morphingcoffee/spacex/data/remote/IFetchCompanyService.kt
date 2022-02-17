package com.morphingcoffee.spacex.data.remote

import com.morphingcoffee.spacex.data.remote.model.CompanyDto
import retrofit2.Response
import retrofit2.http.GET

interface IFetchCompanyService {
    @GET("v4/company")
    suspend fun fetchCompany(): Response<CompanyDto>
}