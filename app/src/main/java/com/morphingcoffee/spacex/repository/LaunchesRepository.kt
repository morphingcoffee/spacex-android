package com.morphingcoffee.spacex.repository

import com.morphingcoffee.spacex.domain.model.Launch

interface LaunchesRepository {
    suspend fun getLaunches(): List<Launch>
}