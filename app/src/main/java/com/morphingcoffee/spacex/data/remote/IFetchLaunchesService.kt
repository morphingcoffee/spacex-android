package com.morphingcoffee.spacex.data.remote

import com.morphingcoffee.spacex.data.remote.model.LaunchesWithRocketsPaginationDto
import com.morphingcoffee.spacex.data.remote.model.LaunchesWithRocketsRequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface IFetchLaunchesService {
    @POST("v5/launches/query")
    suspend fun fetchLaunchesWithRockets(@Body body: LaunchesWithRocketsRequestBody = LaunchesWithRocketsRequestBody()): Response<LaunchesWithRocketsPaginationDto>
}