package com.morphingcoffee.spacex.domain.usecase.impl

import com.morphingcoffee.spacex.domain.interfaces.ILaunchesRepository
import com.morphingcoffee.spacex.domain.model.Launch
import com.morphingcoffee.spacex.domain.usecase.IGetLaunchesUseCase

class GetLaunchesUseCase(private val launchesRepository: ILaunchesRepository) :
    IGetLaunchesUseCase {
    override suspend fun execute(): List<Launch>? {
        return launchesRepository.getLaunches()
    }
}