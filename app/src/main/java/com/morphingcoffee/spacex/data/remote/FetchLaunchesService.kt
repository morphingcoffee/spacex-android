package com.morphingcoffee.spacex.data.remote

import com.morphingcoffee.spacex.data.model.LaunchDto
import retrofit2.Response
import retrofit2.http.GET

interface FetchLaunchesService {
    @GET("v4/launches")
    suspend fun fetchLaunches(): Response<List<LaunchDto>>
}