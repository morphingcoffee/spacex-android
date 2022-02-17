package com.morphingcoffee.spacex.domain.interfaces

import com.morphingcoffee.spacex.domain.model.Launch

interface ILaunchesRepository {
    suspend fun getLaunches(): List<Launch>
}