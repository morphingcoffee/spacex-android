package com.morphingcoffee.spacex.domain.usecase.impl

import com.morphingcoffee.spacex.domain.interfaces.ILaunchesRepository
import com.morphingcoffee.spacex.domain.model.FilteringOption
import com.morphingcoffee.spacex.domain.model.SortingOption
import com.morphingcoffee.spacex.domain.usecase.IGetLaunchesUseCase
import com.morphingcoffee.spacex.domain.usecase.IGetLaunchesUseCase.Result

/**
 * Use case for getting a collection of [com.morphingcoffee.spacex.domain.model.Launch] from the
 * repository layer.
 */
class GetLaunchesUseCase(private val launchesRepository: ILaunchesRepository) :
    IGetLaunchesUseCase {
    override suspend fun execute(
        sortingOption: SortingOption,
        filterStatusOption: FilteringOption.ByLaunchStatus?,
        filterYearOption: FilteringOption.ByYear?
    ): Result {
        return try {
            return Result.Success(
                launchesRepository.getAllLaunchesWithCriteria(
                    sortingOption,
                    filterStatusOption,
                    filterYearOption
                )
            )
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}