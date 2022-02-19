package com.morphingcoffee.spacex.data.remote

import com.morphingcoffee.spacex.data.remote.model.LaunchesPaginationDto
import com.morphingcoffee.spacex.data.remote.model.LaunchesRequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface IFetchLaunchesService {
    @POST("v5/launches/query")
    suspend fun fetchLaunches(@Body body: LaunchesRequestBody? = null): Response<LaunchesPaginationDto>
}