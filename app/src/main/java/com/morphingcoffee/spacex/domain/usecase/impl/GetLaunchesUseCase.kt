package com.morphingcoffee.spacex.domain.usecase.impl

import com.morphingcoffee.spacex.domain.interfaces.ILaunchesRepository
import com.morphingcoffee.spacex.domain.usecase.IGetLaunchesUseCase
import com.morphingcoffee.spacex.domain.usecase.IGetLaunchesUseCase.Result

/**
 * Use case for getting a collection of [com.morphingcoffee.spacex.domain.model.Launch] from the
 * repository layer.
 */
class GetLaunchesUseCase(private val launchesRepository: ILaunchesRepository) :
    IGetLaunchesUseCase {
    override suspend fun execute(): Result {
        return try {
            return Result.Success(launchesRepository.getLaunches())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}