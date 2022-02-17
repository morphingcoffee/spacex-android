package com.morphingcoffee.spacex.data.remote

import com.morphingcoffee.spacex.data.remote.model.LaunchDto
import retrofit2.Response
import retrofit2.http.GET

interface IFetchLaunchesService {
    @GET("v5/launches/query")
    suspend fun fetchLaunches(): Response<List<LaunchDto>>
}