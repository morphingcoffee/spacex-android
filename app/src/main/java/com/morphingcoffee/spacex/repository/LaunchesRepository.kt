package com.morphingcoffee.spacex.repository

import com.morphingcoffee.spacex.domain.interfaces.ILaunchesRepository
import com.morphingcoffee.spacex.domain.model.Launch

class LaunchesRepository : ILaunchesRepository {
    override suspend fun getLaunches(): List<Launch> {
        TODO("Not yet implemented")
    }
}