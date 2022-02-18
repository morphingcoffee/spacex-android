package com.morphingcoffee.spacex.repository

import com.morphingcoffee.spacex.data.remote.IFetchLaunchesService
import com.morphingcoffee.spacex.domain.interfaces.ILaunchesRepository
import com.morphingcoffee.spacex.domain.model.Launch

class LaunchesRepository(private val launchesService: IFetchLaunchesService) : ILaunchesRepository {
    override suspend fun getLaunches(): List<Launch> {
        TODO("Not yet implemented")
    }
}