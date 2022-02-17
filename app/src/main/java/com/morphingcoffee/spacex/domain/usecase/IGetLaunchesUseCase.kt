package com.morphingcoffee.spacex.domain.usecase

import com.morphingcoffee.spacex.domain.model.Launch

interface IGetLaunchesUseCase {
    suspend fun execute(): List<Launch>?
}