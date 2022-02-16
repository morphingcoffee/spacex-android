package com.morphingcoffee.spacex.data

import com.morphingcoffee.spacex.data.models.LaunchDto
import retrofit2.Response
import retrofit2.http.GET

interface FetchLaunchesService {
    @GET("v4/launches")
    suspend fun fetchLaunches(): Response<List<LaunchDto>>
}